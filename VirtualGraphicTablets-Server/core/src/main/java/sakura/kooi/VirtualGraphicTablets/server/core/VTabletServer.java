/*
 * Created by JFormDesigner on Tue Aug 23 20:39:31 CST 2022
 */

package sakura.kooi.VirtualGraphicTablets.server.core;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import sakura.kooi.VirtualGraphicTablets.server.bootstrap.logger.Logger;
import sakura.kooi.VirtualGraphicTablets.server.core.networking.GraphicServer;
import sakura.kooi.VirtualGraphicTablets.server.core.networking.UpstreamWorker;
import sakura.kooi.lib.swing.view.ColoredTextPane;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author SakuraKooi
 */
public class VTabletServer extends JFrame {
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        var panel1 = new JPanel();
        var panel4 = new JPanel();
        btnConnectUpstream = new JButton();
        btnDisconnectUpstream = new JButton();
        btnStartServer = new JButton();
        btnStopServer = new JButton();
        var label11 = new JLabel();
        var panel5 = new JPanel();
        var label13 = new JLabel();
        var label14 = new JLabel();
        numCanvaPosX = new JSpinner();
        numCanvaPosY = new JSpinner();
        var label15 = new JLabel();
        var label16 = new JLabel();
        numCanvaWidth = new JSpinner();
        numCanvaHeight = new JSpinner();
        var label5 = new JLabel();
        numFps = new JSpinner();
        var vSpacer2 = new Spacer();
        var panel6 = new JPanel();
        canvas = new JLabel();
        var panel2 = new JPanel();
        var label1 = new JLabel();
        lblUpstreamConnectStatus = new JLabel();
        var label3 = new JLabel();
        lblUpstreamVersion = new JLabel();
        scrollPane1 = new JScrollPane();
        txtLogs = new ColoredTextPane();
        var panel3 = new JPanel();
        var label4 = new JLabel();
        lblServerRunningStatus = new JLabel();
        var label6 = new JLabel();
        lblServerListenPort = new JLabel();
        var label8 = new JLabel();
        lblServerConnectStatus = new JLabel();

        //======== this ========
        setTitle("VirtualGraphicTablet Server"); //NON-NLS
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));

            //======== panel4 ========
            {
                panel4.setBorder(new CompoundBorder(
                    new TitledBorder("\u63a7\u5236\u53f0"), //NON-NLS
                    new EmptyBorder(5, 5, 5, 5)));
                panel4.setLayout(new GridLayoutManager(9, 1, new Insets(0, 0, 0, 0), -1, -1));

                //---- btnConnectUpstream ----
                btnConnectUpstream.setText("\u8fde\u63a5 VirtualTablet Server"); //NON-NLS
                panel4.add(btnConnectUpstream, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- btnDisconnectUpstream ----
                btnDisconnectUpstream.setText("\u65ad\u5f00 VirtualTablet Server"); //NON-NLS
                btnDisconnectUpstream.setEnabled(false);
                panel4.add(btnDisconnectUpstream, new GridConstraints(1, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- btnStartServer ----
                btnStartServer.setText("\u542f\u52a8 Graphic Server"); //NON-NLS
                panel4.add(btnStartServer, new GridConstraints(2, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- btnStopServer ----
                btnStopServer.setText("\u505c\u6b62 Graphic Server"); //NON-NLS
                btnStopServer.setEnabled(false);
                panel4.add(btnStopServer, new GridConstraints(3, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- label11 ----
                label11.setText(" "); //NON-NLS
                panel4.add(label11, new GridConstraints(4, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //======== panel5 ========
                {
                    panel5.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));

                    //---- label13 ----
                    label13.setText("Pos X"); //NON-NLS
                    panel5.add(label13, new GridConstraints(0, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- label14 ----
                    label14.setText("Pos Y"); //NON-NLS
                    panel5.add(label14, new GridConstraints(0, 1, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- numCanvaPosX ----
                    numCanvaPosX.setModel(new SpinnerNumberModel(0, 0, null, 1));
                    panel5.add(numCanvaPosX, new GridConstraints(1, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- numCanvaPosY ----
                    numCanvaPosY.setModel(new SpinnerNumberModel(0, 0, null, 1));
                    panel5.add(numCanvaPosY, new GridConstraints(1, 1, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- label15 ----
                    label15.setText("Width"); //NON-NLS
                    panel5.add(label15, new GridConstraints(2, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- label16 ----
                    label16.setText("Height"); //NON-NLS
                    panel5.add(label16, new GridConstraints(2, 1, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- numCanvaWidth ----
                    numCanvaWidth.setModel(new SpinnerNumberModel(0, 0, null, 1));
                    panel5.add(numCanvaWidth, new GridConstraints(3, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- numCanvaHeight ----
                    numCanvaHeight.setModel(new SpinnerNumberModel(0, 0, null, 1));
                    panel5.add(numCanvaHeight, new GridConstraints(3, 1, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));
                }
                panel4.add(panel5, new GridConstraints(5, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- label5 ----
                label5.setText("FPS"); //NON-NLS
                panel4.add(label5, new GridConstraints(6, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- numFps ----
                numFps.setModel(new SpinnerNumberModel(30, 1, 144, 1));
                panel4.add(numFps, new GridConstraints(7, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
                panel4.add(vSpacer2, new GridConstraints(8, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    null, null, null));
            }
            panel1.add(panel4, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null));

            //======== panel6 ========
            {
                panel6.setBorder(new EtchedBorder());
                panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
                panel6.add(canvas, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
            }
            panel1.add(panel6, new GridConstraints(0, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //======== panel2 ========
            {
                panel2.setBorder(new CompoundBorder(
                    new TitledBorder("Upstream"), //NON-NLS
                    new EmptyBorder(5, 5, 5, 5)));
                panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));

                //---- label1 ----
                label1.setText("\u8fde\u63a5\u72b6\u6001"); //NON-NLS
                panel2.add(label1, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- lblUpstreamConnectStatus ----
                lblUpstreamConnectStatus.setText("\u672a\u8fde\u63a5"); //NON-NLS
                panel2.add(lblUpstreamConnectStatus, new GridConstraints(0, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- label3 ----
                label3.setText("\u670d\u52a1\u7aef\u7248\u672c"); //NON-NLS
                panel2.add(label3, new GridConstraints(1, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- lblUpstreamVersion ----
                lblUpstreamVersion.setText("114514"); //NON-NLS
                panel2.add(lblUpstreamVersion, new GridConstraints(1, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
            }
            panel1.add(panel2, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(txtLogs);
            }
            panel1.add(scrollPane1, new GridConstraints(1, 1, 2, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //======== panel3 ========
            {
                panel3.setBorder(new CompoundBorder(
                    new TitledBorder("Server"), //NON-NLS
                    new EmptyBorder(5, 5, 5, 5)));
                panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));

                //---- label4 ----
                label4.setText("\u8fd0\u884c\u72b6\u6001"); //NON-NLS
                panel3.add(label4, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- lblServerRunningStatus ----
                lblServerRunningStatus.setText("\u672a\u542f\u52a8"); //NON-NLS
                panel3.add(lblServerRunningStatus, new GridConstraints(0, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- label6 ----
                label6.setText("\u76d1\u542c\u7aef\u53e3"); //NON-NLS
                panel3.add(label6, new GridConstraints(1, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- lblServerListenPort ----
                lblServerListenPort.setText("23372"); //NON-NLS
                panel3.add(lblServerListenPort, new GridConstraints(1, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- label8 ----
                label8.setText("\u8fde\u63a5\u72b6\u6001"); //NON-NLS
                panel3.add(label8, new GridConstraints(2, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- lblServerConnectStatus ----
                lblServerConnectStatus.setText("\u672a\u8fde\u63a5"); //NON-NLS
                panel3.add(lblServerConnectStatus, new GridConstraints(2, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
            }
            panel1.add(panel3, new GridConstraints(2, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JButton btnConnectUpstream;
    private JButton btnDisconnectUpstream;
    private JButton btnStartServer;
    private JButton btnStopServer;
    private JSpinner numCanvaPosX;
    private JSpinner numCanvaPosY;
    private JSpinner numCanvaWidth;
    private JSpinner numCanvaHeight;
    private JSpinner numFps;
    private JLabel canvas;
    private JLabel lblUpstreamConnectStatus;
    private JLabel lblUpstreamVersion;
    private JScrollPane scrollPane1;
    private ColoredTextPane txtLogs;
    private JLabel lblServerRunningStatus;
    private JLabel lblServerListenPort;
    private JLabel lblServerConnectStatus;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private int screenMaxWidth;
    private int screenMaxHeight;

    private UpstreamWorker upstreamWorker;
    private GraphicServer graphicServer;

    public VTabletServer() {
        initComponents();
        setPreferredSize(new Dimension(875, 580));
        setMinimumSize(new Dimension(720, 470));
        numCanvaPosX.setEditor(new JSpinner.NumberEditor(numCanvaPosX, "#"));
        numCanvaPosY.setEditor(new JSpinner.NumberEditor(numCanvaPosY, "#"));
        numCanvaWidth.setEditor(new JSpinner.NumberEditor(numCanvaWidth, "#"));
        numCanvaHeight.setEditor(new JSpinner.NumberEditor(numCanvaHeight, "#"));
        txtLogs.setEditable(false);
        Logger.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) { }
        }) {
            @Override
            public void println(String str) {
                txtLogs.appendANSI(str + "\n");
                txtLogs.setCaretPosition(txtLogs.getDocument().getLength());
            }

            @Override
            public void print(String str) {
                txtLogs.appendANSI(str);
                txtLogs.setCaretPosition(txtLogs.getDocument().getLength());
            }
        });
        btnConnectUpstream.addActionListener(e -> {
            upstreamWorker = new UpstreamWorker(this);
            upstreamWorker.start();
            btnDisconnectUpstream.setEnabled(true);
            btnConnectUpstream.setEnabled(false);
        });

        btnDisconnectUpstream.addActionListener(e -> {
            upstreamWorker.interrupt();
            upstreamWorker = null;
            btnDisconnectUpstream.setEnabled(false);
            btnConnectUpstream.setEnabled(true);
        });

        btnStartServer.addActionListener(e -> {
            graphicServer = new GraphicServer(this);
            graphicServer.start();
            btnStopServer.setEnabled(true);
            btnStartServer.setEnabled(false);
        });

        btnStopServer.addActionListener(e -> {
            graphicServer.close();
            graphicServer = null;
            btnStopServer.setEnabled(false);
            btnStartServer.setEnabled(true);
        });
    }

    public void onUpstreamConnected(String version, int width, int height) {
        lblUpstreamConnectStatus.setText("已连接");
        lblUpstreamVersion.setText(version);
        this.screenMaxWidth = width;
        this.screenMaxHeight = height;
        numCanvaPosX.setModel(new SpinnerNumberModel(0, 0, width, 1));
        numCanvaPosY.setModel(new SpinnerNumberModel(0, 0, height, 1));
        numCanvaWidth.setModel(new SpinnerNumberModel(0, 0, width, 1));
        numCanvaWidth.setValue(width);
        numCanvaHeight.setModel(new SpinnerNumberModel(0, 0, height, 1));
        numCanvaHeight.setValue(height);
    }

    public void onUpstreamDisconnected() {
        lblUpstreamConnectStatus.setText("未连接");
        lblUpstreamVersion.setText("");
    }

    public void onPacketReceived(Object packet) {
    }

    public void setGraphicServerRunning(boolean b) {
        lblServerRunningStatus.setText(b ? "运行中" : "未运行");
    }

    public void setGraphicServerConnected(boolean b) {
        lblServerConnectStatus.setText(b ? "已连接" : "未连接");
    }
}
