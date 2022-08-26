package sakura.kooi.VirtualGraphicTablets.server.core.utils;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;

import java.awt.*;
import java.awt.image.*;

import static com.sun.jna.platform.win32.WinDef.HBITMAP;
import static com.sun.jna.platform.win32.WinDef.HDC;
import static com.sun.jna.platform.win32.WinGDI.*;
import static com.sun.jna.platform.win32.WinNT.HANDLE;

public class JnaUtils {
    private static final DirectColorModel SCREENSHOT_COLOR_MODEL = new DirectColorModel(24, 0x00FF0000, 0xFF00, 0xFF);
    private static final int[] SCREENSHOT_BAND_MASKS = {
            SCREENSHOT_COLOR_MODEL.getRedMask(),
            SCREENSHOT_COLOR_MODEL.getGreenMask(),
            SCREENSHOT_COLOR_MODEL.getBlueMask()
    };

    public static BufferedImage bitbltRegion(Rectangle capture) {
        int windowWidth = capture.width;
        int windowHeight = capture.height;

        HDC hdcTarget = User32.INSTANCE.GetDC(null);
        if (hdcTarget == null) {
            throw new Win32Exception(Native.getLastError());
        }

        Win32Exception we = null;
        HDC hdcTargetMem = null;
        HBITMAP hBitmap = null;
        HANDLE hOriginal = null;
        BufferedImage image = null;

        try {
            hdcTargetMem = GDI32.INSTANCE.CreateCompatibleDC(hdcTarget);
            if (hdcTargetMem == null) {
                throw new Win32Exception(Native.getLastError());
            }

            hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcTarget, windowWidth, windowHeight);
            if (hBitmap == null) {
                throw new Win32Exception(Native.getLastError());
            }

            hOriginal = GDI32.INSTANCE.SelectObject(hdcTargetMem, hBitmap);
            if (hOriginal == null) {
                throw new Win32Exception(Native.getLastError());
            }

            if (!GDI32.INSTANCE.BitBlt(hdcTargetMem, capture.x, capture.y, windowWidth - capture.x, windowHeight - capture.y, hdcTarget, 0, 0, GDI32.SRCCOPY)) {
                throw new Win32Exception(Native.getLastError());
            }

            BITMAPINFO bmi = new BITMAPINFO();
            bmi.bmiHeader.biWidth = windowWidth;
            bmi.bmiHeader.biHeight = -windowHeight;
            bmi.bmiHeader.biPlanes = 1;
            bmi.bmiHeader.biBitCount = 32;
            bmi.bmiHeader.biCompression = BI_RGB;

            Memory buffer = new Memory((long) windowWidth * windowHeight * 4);
            int resultOfDrawing = GDI32.INSTANCE.GetDIBits(hdcTarget, hBitmap, 0, windowHeight, buffer, bmi,
                    DIB_RGB_COLORS);
            if (resultOfDrawing == 0 || resultOfDrawing == WinError.ERROR_INVALID_PARAMETER) {
                throw new Win32Exception(Native.getLastError());
            }

            int bufferSize = windowWidth * windowHeight;
            DataBuffer dataBuffer = new DataBufferInt(buffer.getIntArray(0, bufferSize), bufferSize);
            WritableRaster raster = Raster.createPackedRaster(dataBuffer, windowWidth, windowHeight, windowWidth,
                    SCREENSHOT_BAND_MASKS, null);
            image = new BufferedImage(SCREENSHOT_COLOR_MODEL, raster, false, null);

        } catch (Win32Exception e) {
            we = e;
        } finally {
            if (hOriginal != null) {
                // per MSDN, set the display surface back when done drawing
                HANDLE result = GDI32.INSTANCE.SelectObject(hdcTargetMem, hOriginal);
                // failure modes are null or equal to HGDI_ERROR
                if (result == null || HGDI_ERROR.equals(result)) {
                    Win32Exception ex = new Win32Exception(Native.getLastError());
                    if (we != null) {
                        ex.addSuppressed(we);
                    }
                    we = ex;
                }
            }

            if (hBitmap != null) {
                if (!GDI32.INSTANCE.DeleteObject(hBitmap)) {
                    Win32Exception ex = new Win32Exception(Native.getLastError());
                    if (we != null) {
                        ex.addSuppressed(we);
                    }
                    we = ex;
                }
            }

            if (hdcTargetMem != null) {
                // get rid of the device context when done
                if (!GDI32.INSTANCE.DeleteDC(hdcTargetMem)) {
                    Win32Exception ex = new Win32Exception(Native.getLastError());
                    if (we != null) {
                        ex.addSuppressed(we);
                    }
                    we = ex;
                }
            }

            if (0 == User32.INSTANCE.ReleaseDC(null, hdcTarget)) {
                throw new IllegalStateException("Device context did not release properly.");
            }
        }

        if (we != null) {
            throw we;
        }
        return image;//.getSubimage(0, 0, capture.width, capture.height);
    }
}
