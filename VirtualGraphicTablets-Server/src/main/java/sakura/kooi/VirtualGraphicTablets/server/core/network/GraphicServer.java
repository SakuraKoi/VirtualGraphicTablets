package sakura.kooi.VirtualGraphicTablets.server.core.network;

import lombok.CustomLog;
import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;
import sakura.kooi.VirtualGraphicTablets.server.core.VTabletServer;
import sakura.kooi.VirtualGraphicTablets.server.core.utils.CounterOutputStream;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

@CustomLog
public class GraphicServer extends Thread {
    private VTabletServer parent;

    private ServerSocket server;

    private Socket client;
    private DataInputStream dis;
    private DataOutputStream dos;

    public PacketWriter packetWriter;
    private ScreenWorker screenWorker;

    public GraphicServer(VTabletServer parent) {
        this.parent = parent;
    }

    public void close() {
        interrupt();
        if (client != null) {
            try {
                client.close();
            } catch (IOException ignored) { }
        }
        try {
            server.close();
        } catch (IOException ignored) { }
    }

    public void startScreenWorker() {
        if (screenWorker != null)
            screenWorker.start();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 23372), 1);
        } catch (IOException e) {
            log.e("Failed start graphic server.", e);
            return;
        }
        log.s("Graphic server is listening on 0.0.0.0:23372");
        parent.setGraphicServerRunning(true);
        while (!isInterrupted()) {
            try {
                client = server.accept();
            } catch (IOException e) {
                if (!e.getMessage().contains("Interrupted"))
                    log.e("Failed accept new client", e);
                continue;
            }
            InetAddress clientAddr = client.getInetAddress();
            parent.setGraphicServerConnected(true);
            log.i("Client {} connected", clientAddr.getHostAddress());
            try {
                try {
                    Inflater inflater = new Inflater();
                    Deflater deflater = new Deflater();
                    deflater.setLevel(1);
                    dis = new DataInputStream(new InflaterInputStream(
                                    new BufferedInputStream(client.getInputStream()), inflater, 2048));
                    dos = new DataOutputStream(new DeflaterOutputStream(new CounterOutputStream(
                            new BufferedOutputStream(client.getOutputStream())), deflater, 65536, true));
                    packetWriter = new PacketWriter(parent, client, dos);
                    packetWriter.start();
                    screenWorker = new ScreenWorker(parent, packetWriter);
                    while (!isInterrupted()) {
                        int size = dis.readInt();
                        byte[] data = dis.readNBytes(size);
                        Vgt.PacketContainer container = Vgt.PacketContainer.parseFrom(data);
                        Object packet;
                        // TODO use a packet registry
                        switch (container.getPacketId()) {
                            case 1: {
                                packet = Vgt.C01PacketHandshake.parseFrom(container.getPayload());
                                break;
                            }
                            case 4: {
                                packet = Vgt.C04PacketHover.parseFrom(container.getPayload());
                                break;
                            }
                            case 5: {
                                packet = Vgt.C05PacketTouch.parseFrom(container.getPayload());
                                break;
                            }
                            case 6: {
                                packet = Vgt.C06PacketExit.parseFrom(container.getPayload());
                                break;
                            }
                            case 7: {
                                packet = Vgt.C07PacketTriggerHotkey.parseFrom(container.getPayload());
                                break;
                            }
                            case 8: {
                                packet = Vgt.C08PacketDecodePerformanceReport.parseFrom(container.getPayload());
                                break;
                            }
                            default: {
                                log.e("Unknown packet {} received from client", container.getPacketId());
                                continue;
                            }
                        }

                        if (packet != null) {
                            parent.onPacketReceived(packet);
                        }
                    }
                } catch (IOException e) {
                    if (e instanceof EOFException) {
                    } else if (!e.getMessage().contains("Socket closed") && !e.getMessage().contains("Connection reset")) {
                        log.e("Invalid packet received from client", e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (packetWriter != null) {
                packetWriter.interrupt();
                packetWriter = null;
            }
            if (screenWorker != null) {
                screenWorker.interrupt();
                screenWorker = null;
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ignored) {
                }
                client = null;
            }

            parent.setGraphicServerConnected(false);
            log.i("Client {} disconnected", clientAddr.getHostAddress());
        }
        try {
            server.close();
        } catch (IOException ignored) {
        }
        parent.setGraphicServerRunning(false);
        log.i("Graphic server stopped");
    }
}
