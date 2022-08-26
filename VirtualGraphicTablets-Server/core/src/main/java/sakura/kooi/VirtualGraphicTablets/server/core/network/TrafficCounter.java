package sakura.kooi.VirtualGraphicTablets.server.core.network;

import lombok.Getter;
import sakura.kooi.VirtualGraphicTablets.server.core.VTabletServer;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class TrafficCounter extends Thread {
    @Getter
    private static AtomicInteger counterFrame = new AtomicInteger();
    @Getter
    private static AtomicInteger counterTrafficUp = new AtomicInteger();
    @Getter
    private static AtomicInteger counterTrafficDown = new AtomicInteger();

    private VTabletServer parent;
    public TrafficCounter(VTabletServer parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        DecimalFormat numberFormat = new DecimalFormat();
        numberFormat.setMaximumFractionDigits(2);
        while (!isInterrupted()) {
            parent.lblCurrentFrame.setText(String.valueOf(counterFrame.getAndSet(0)));
            parent.lblTrafficUp.setText(numberFormat.format(counterTrafficUp.getAndSet(0) / 1024.0f));
            parent.lblTrafficDown.setText(numberFormat.format(counterTrafficDown.getAndSet(0) / 1024.0f));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
