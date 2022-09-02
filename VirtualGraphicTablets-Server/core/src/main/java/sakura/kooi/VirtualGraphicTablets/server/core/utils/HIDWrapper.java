package sakura.kooi.VirtualGraphicTablets.server.core.utils;


import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;

public class HIDWrapper {
    public static final int IN_RANGE = 32;
    public static final int TIPSWITCH = 1;
    public static final int HOVER_EXIT = 128;
    private VMultiClient INSTANCE = null;
    public interface VMultiClient extends Library {
        Pointer VTDCreate();

        int VTDGetStatus(Pointer pObject);

        void VTDDispose(Pointer pObject);

        void VTDUpdateDigitizerAbs_Full(Pointer pObject, byte status, WinDef.USHORT x, WinDef.USHORT y, byte pressure, byte XTilt, byte YTilt);

        void VTDGetLogs(char[] str, int len);
    }

    private Pointer pDevice = null;
    private static boolean shutdownHookInstalled = false;

    public void open() {
        if (INSTANCE == null) {
            INSTANCE = Native.load("vmulticlient", VMultiClient.class);
        }

        if (pDevice != null)
            throw new IllegalStateException("Close HID device first");

        pDevice = INSTANCE.VTDCreate();
        if (INSTANCE.VTDGetStatus(pDevice) != 1) {
            INSTANCE.VTDDispose(pDevice);
            pDevice = null;
            char[] out = new char[4096];
            INSTANCE.VTDGetLogs(out, 4096);
            throw new LastErrorException(new String(out));
        }

        if (!shutdownHookInstalled) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (pDevice != null) {
                    INSTANCE.VTDDispose(pDevice);
                    pDevice = null;
                }
            }));
            shutdownHookInstalled = true;
        }

    }

    public void close() {
        if (pDevice != null) {
            INSTANCE.VTDDispose(pDevice);
            pDevice = null;
        }
    }

    public void sendDigitizer(byte flag, float percentX, float percentY, float pressureV) {
        if (pDevice != null) {
            byte pressure = (byte) (pressureV * Byte.MAX_VALUE);
            WinDef.USHORT x = new WinDef.USHORT((long) (percentX * Short.MAX_VALUE));
            WinDef.USHORT y = new WinDef.USHORT((long) (percentY * Short.MAX_VALUE));
            INSTANCE.VTDUpdateDigitizerAbs_Full(pDevice, flag, x, y, pressure, (byte) 0, (byte) 0);
        }
    }
}
