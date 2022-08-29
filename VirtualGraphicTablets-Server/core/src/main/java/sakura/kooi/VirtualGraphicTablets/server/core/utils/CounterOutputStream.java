package sakura.kooi.VirtualGraphicTablets.server.core.utils;

import sakura.kooi.VirtualGraphicTablets.server.core.network.TrafficCounter;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CounterOutputStream extends FilterOutputStream {
    public CounterOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        TrafficCounter.getCounterTrafficDown().addAndGet(1);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        out.write(b, off, len);
        TrafficCounter.getCounterTrafficDown().addAndGet(len);
    }
}
