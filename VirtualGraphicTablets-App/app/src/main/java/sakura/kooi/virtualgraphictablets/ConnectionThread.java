package sakura.kooi.virtualgraphictablets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;

public class ConnectionThread extends Thread {
    private TabletActivity parent;
    private String server;
    private int port;

    private Socket client;
    private DataInputStream dis;
    private DataOutputStream dos;

    public PacketWriter packetWriter;

    public ConnectionThread(TabletActivity parent, String server, int port) {
        this.parent = parent;
        this.server = server;
        this.port = port;
    }

    @Override
    public void run() {
        client = new Socket();
        try {
            client.connect(new InetSocketAddress(InetAddress.getByName(server), port), 5000);
        } catch (IOException e) {
            parent.onNetworkError(e);
            return;
        }
        parent.onClientConnected();

        boolean error = false;
        try {
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
            packetWriter = new PacketWriter(client, dos);
            packetWriter.start();
            while (!isInterrupted()) {
                int size = dis.readInt();
                byte[] data = new byte[size];
                dis.readFully(data);
                Vgt.PacketContainer container = Vgt.PacketContainer.parseFrom(data);
                Object packet;
                switch (container.getPacketId()) {
                    case 2: {
                        packet = Vgt.S02PacketServerInfo.parseFrom(container.getPayload().toByteArray());
                        break;
                    }
                    case 3: {
                        packet = Vgt.S03PacketScreen.parseFrom(container.getPayload().toByteArray());
                        break;
                    }
                    default: {
                        Log.e("VGT-Connection", "Unknown packet " + container.getPacketId() + " received from client");
                        continue;
                    }
                }

                if (packet != null) {
                    parent.onPacketReceived(packet);
                }
            }
        } catch (IOException e) {
            if (!(e instanceof EOFException)) {
                Log.e("VGT-Connection", "Invalid packet received from client", e);
                parent.onNetworkError(e);
                error = true;
            }
        }

        if (packetWriter != null) {
            packetWriter.interrupt();
            packetWriter = null;
        }

        if (client != null) {
            try {
                client.close();
            } catch (IOException ignored) {
            }
            client = null;
        }

        parent.onDisconnected(error);
    }
}