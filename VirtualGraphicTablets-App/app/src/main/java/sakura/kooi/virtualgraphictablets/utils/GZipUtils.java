package sakura.kooi.virtualgraphictablets.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtils {
    public static byte[] compress(byte[] data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos);
            gzipOutputStream.write(data);
            gzipOutputStream.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decompress(byte[] dataIn) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(dataIn);
            GZIPInputStream gzipInputStream = new GZIPInputStream(bais);

            int nRead;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[16384];

            while ((nRead = gzipInputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
