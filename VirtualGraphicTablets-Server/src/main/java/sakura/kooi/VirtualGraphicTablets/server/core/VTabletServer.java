/*
 * Created by JFormDesigner on Tue Aug 23 20:39:31 CST 2022
 */

package sakura.kooi.VirtualGraphicTablets.server.core;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.sun.jna.Platform;
import lombok.CustomLog;
import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;
import sakura.kooi.VirtualGraphicTablets.server.bootstrap.logger.Logger;
import sakura.kooi.VirtualGraphicTablets.server.core.network.GraphicServer;
import sakura.kooi.VirtualGraphicTablets.server.core.network.TrafficCounter;
import sakura.kooi.VirtualGraphicTablets.server.core.utils.HIDWrapper;
import sakura.kooi.VirtualGraphicTablets.server.core.utils.RobotUtils;
import sakura.kooi.lib.swing.view.ColoredTextPane;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.io.PrintStream;

import static sakura.kooi.VirtualGraphicTablets.server.core.utils.HIDWrapper.HOVER_EXIT;

@CustomLog
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
        var label32 = new JLabel();
        numResizeFactor = new JSpinner();
        var vSpacer2 = new Spacer();
        canvasContainer = new JPanel();
        canvas = new JLabel();
        var vSpacer1 = new Spacer();
        var panel2 = new JPanel();
        var label1 = new JLabel();
        lblUpstreamConnectStatus = new JLabel();
        var panel3 = new JPanel();
        var label4 = new JLabel();
        lblServerRunningStatus = new JLabel();
        var label6 = new JLabel();
        lblServerListenPort = new JLabel();
        var label8 = new JLabel();
        lblServerConnectStatus = new JLabel();
        scrollPane1 = new JScrollPane();
        txtLogs = new ColoredTextPane();
        panel9 = new JPanel();
        var label2 = new JLabel();
        lblCurrentFrame = new JLabel();
        var label17 = new JLabel();
        var label7 = new JLabel();
        var label12 = new JLabel();
        lblTrafficUp = new JLabel();
        var label18 = new JLabel();
        var label10 = new JLabel();
        var label9 = new JLabel();
        lblTrafficDown = new JLabel();
        var label19 = new JLabel();
        var label20 = new JLabel();
        var label21 = new JLabel();
        lblEncodingDelay = new JLabel();
        var label22 = new JLabel();
        var label23 = new JLabel();
        var label24 = new JLabel();
        lblDecodingDelay = new JLabel();
        var label25 = new JLabel();
        var label26 = new JLabel();
        var label27 = new JLabel();
        lblDisplayDelay = new JLabel();
        var label28 = new JLabel();
        var label29 = new JLabel();
        var label30 = new JLabel();
        lblRenderQueue = new JLabel();
        var label31 = new JLabel();
        var hSpacer1 = new Spacer();

        //======== this ========
        setTitle("VirtualGraphicTablet Server");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel1.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), 4, 4));

            //======== panel4 ========
            {
                panel4.setBorder(new CompoundBorder(
                    new TitledBorder("\u63a7\u5236\u53f0"),
                    new EmptyBorder(5, 5, 5, 5)));
                panel4.setLayout(new GridLayoutManager(11, 1, new Insets(0, 0, 0, 0), 4, 4));

                //---- btnConnectUpstream ----
                btnConnectUpstream.setText("\u8fde\u63a5 VirtualTablet Server");
                panel4.add(btnConnectUpstream, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- btnDisconnectUpstream ----
                btnDisconnectUpstream.setText("\u65ad\u5f00 VirtualTablet Server");
                btnDisconnectUpstream.setEnabled(false);
                panel4.add(btnDisconnectUpstream, new GridConstraints(1, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- btnStartServer ----
                btnStartServer.setText("\u542f\u52a8 Graphic Server");
                panel4.add(btnStartServer, new GridConstraints(2, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- btnStopServer ----
                btnStopServer.setText("\u505c\u6b62 Graphic Server");
                btnStopServer.setEnabled(false);
                panel4.add(btnStopServer, new GridConstraints(3, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- label11 ----
                label11.setText(" ");
                panel4.add(label11, new GridConstraints(4, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //======== panel5 ========
                {
                    panel5.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));

                    //---- label13 ----
                    label13.setText("Pos X");
                    panel5.add(label13, new GridConstraints(0, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- label14 ----
                    label14.setText("Pos Y");
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
                    label15.setText("Width");
                    panel5.add(label15, new GridConstraints(2, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- label16 ----
                    label16.setText("Height");
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
                label5.setText("FPS");
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

                //---- label32 ----
                label32.setText("Resize Factor");
                panel4.add(label32, new GridConstraints(8, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- numResizeFactor ----
                numResizeFactor.setModel(new SpinnerNumberModel(100, 1, 100, 1));
                panel4.add(numResizeFactor, new GridConstraints(9, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
                panel4.add(vSpacer2, new GridConstraints(10, 0, 1, 1,
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

            //======== canvasContainer ========
            {
                canvasContainer.setBorder(new EtchedBorder());
                canvasContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

                //---- canvas ----
                canvas.setHorizontalAlignment(SwingConstants.CENTER);
                canvasContainer.add(canvas, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
            }
            panel1.add(canvasContainer, new GridConstraints(0, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
            panel1.add(vSpacer1, new GridConstraints(0, 2, 2, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null));

            //======== panel2 ========
            {
                panel2.setBorder(new CompoundBorder(
                    new TitledBorder("Driver"),
                    new EmptyBorder(5, 5, 5, 5)));
                panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 4, 4));

                //---- label1 ----
                label1.setText("\u8fde\u63a5\u72b6\u6001");
                panel2.add(label1, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- lblUpstreamConnectStatus ----
                lblUpstreamConnectStatus.setText("\u672a\u8fde\u63a5");
                panel2.add(lblUpstreamConnectStatus, new GridConstraints(0, 1, 1, 1,
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

            //======== panel3 ========
            {
                panel3.setBorder(new CompoundBorder(
                    new TitledBorder("Server"),
                    new EmptyBorder(5, 5, 5, 5)));
                panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), 4, 4));

                //---- label4 ----
                label4.setText("\u8fd0\u884c\u72b6\u6001");
                panel3.add(label4, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- lblServerRunningStatus ----
                lblServerRunningStatus.setText("\u672a\u542f\u52a8");
                panel3.add(lblServerRunningStatus, new GridConstraints(0, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- label6 ----
                label6.setText("\u76d1\u542c\u7aef\u53e3");
                panel3.add(label6, new GridConstraints(1, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- lblServerListenPort ----
                lblServerListenPort.setText("23372");
                panel3.add(lblServerListenPort, new GridConstraints(1, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- label8 ----
                label8.setText("\u8fde\u63a5\u72b6\u6001");
                panel3.add(label8, new GridConstraints(2, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- lblServerConnectStatus ----
                lblServerConnectStatus.setText("\u672a\u8fde\u63a5");
                panel3.add(lblServerConnectStatus, new GridConstraints(2, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
            }
            panel1.add(panel3, new GridConstraints(2, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null));

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(txtLogs);
            }
            panel1.add(scrollPane1, new GridConstraints(1, 1, 2, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //======== panel9 ========
            {
                panel9.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));

                //---- label2 ----
                label2.setText("\u5f53\u524d\u5e27\u7387");
                panel9.add(label2);

                //---- lblCurrentFrame ----
                lblCurrentFrame.setText("60");
                lblCurrentFrame.setMinimumSize(new Dimension(20, 17));
                lblCurrentFrame.setPreferredSize(new Dimension(20, 17));
                lblCurrentFrame.setHorizontalTextPosition(SwingConstants.CENTER);
                lblCurrentFrame.setHorizontalAlignment(SwingConstants.CENTER);
                panel9.add(lblCurrentFrame);

                //---- label17 ----
                label17.setText(" FPS");
                panel9.add(label17);

                //---- label7 ----
                label7.setText(" | ");
                panel9.add(label7);

                //---- label12 ----
                label12.setText("\u4e0a\u884c\u6d41\u91cf");
                panel9.add(label12);

                //---- lblTrafficUp ----
                lblTrafficUp.setText("1000.00");
                lblTrafficUp.setHorizontalAlignment(SwingConstants.CENTER);
                lblTrafficUp.setMinimumSize(new Dimension(46, 17));
                lblTrafficUp.setPreferredSize(new Dimension(46, 17));
                panel9.add(lblTrafficUp);

                //---- label18 ----
                label18.setText(" KB/s");
                panel9.add(label18);

                //---- label10 ----
                label10.setText(" | ");
                panel9.add(label10);

                //---- label9 ----
                label9.setText("\u4e0b\u884c\u6d41\u91cf");
                panel9.add(label9);

                //---- lblTrafficDown ----
                lblTrafficDown.setText("1000.00");
                lblTrafficDown.setHorizontalAlignment(SwingConstants.CENTER);
                lblTrafficDown.setMinimumSize(new Dimension(46, 17));
                lblTrafficDown.setPreferredSize(new Dimension(46, 17));
                panel9.add(lblTrafficDown);

                //---- label19 ----
                label19.setText(" KB/s");
                panel9.add(label19);

                //---- label20 ----
                label20.setText(" | ");
                panel9.add(label20);

                //---- label21 ----
                label21.setText("\u7f16\u7801\u5ef6\u8fdf");
                panel9.add(label21);

                //---- lblEncodingDelay ----
                lblEncodingDelay.setText("1000");
                lblEncodingDelay.setHorizontalAlignment(SwingConstants.CENTER);
                lblEncodingDelay.setMinimumSize(new Dimension(30, 17));
                lblEncodingDelay.setPreferredSize(new Dimension(30, 17));
                panel9.add(lblEncodingDelay);

                //---- label22 ----
                label22.setText("ms");
                panel9.add(label22);

                //---- label23 ----
                label23.setText(" | ");
                panel9.add(label23);

                //---- label24 ----
                label24.setText("\u89e3\u7801\u5ef6\u8fdf");
                panel9.add(label24);

                //---- lblDecodingDelay ----
                lblDecodingDelay.setText("1000");
                lblDecodingDelay.setHorizontalAlignment(SwingConstants.CENTER);
                lblDecodingDelay.setMinimumSize(new Dimension(30, 17));
                lblDecodingDelay.setPreferredSize(new Dimension(30, 17));
                panel9.add(lblDecodingDelay);

                //---- label25 ----
                label25.setText("ms");
                panel9.add(label25);

                //---- label26 ----
                label26.setText(" | ");
                panel9.add(label26);

                //---- label27 ----
                label27.setText("\u663e\u793a\u5ef6\u8fdf");
                panel9.add(label27);

                //---- lblDisplayDelay ----
                lblDisplayDelay.setText("10000");
                lblDisplayDelay.setHorizontalAlignment(SwingConstants.CENTER);
                lblDisplayDelay.setMinimumSize(new Dimension(36, 17));
                lblDisplayDelay.setPreferredSize(new Dimension(36, 17));
                panel9.add(lblDisplayDelay);

                //---- label28 ----
                label28.setText("ms");
                panel9.add(label28);

                //---- label29 ----
                label29.setText(" | ");
                panel9.add(label29);

                //---- label30 ----
                label30.setText("\u6e32\u67d3\u961f\u5217");
                panel9.add(label30);

                //---- lblRenderQueue ----
                lblRenderQueue.setText("100");
                lblRenderQueue.setHorizontalAlignment(SwingConstants.CENTER);
                lblRenderQueue.setMinimumSize(new Dimension(22, 17));
                lblRenderQueue.setPreferredSize(new Dimension(22, 17));
                panel9.add(lblRenderQueue);

                //---- label31 ----
                label31.setText("f");
                panel9.add(label31);
            }
            panel1.add(panel9, new GridConstraints(3, 0, 1, 2,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
            panel1.add(hSpacer1, new GridConstraints(4, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
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
    public JSpinner numCanvaPosX;
    public JSpinner numCanvaPosY;
    public JSpinner numCanvaWidth;
    public JSpinner numCanvaHeight;
    public JSpinner numFps;
    public JSpinner numResizeFactor;
    public JPanel canvasContainer;
    public JLabel canvas;
    private JLabel lblUpstreamConnectStatus;
    private JLabel lblServerRunningStatus;
    private JLabel lblServerListenPort;
    private JLabel lblServerConnectStatus;
    private JScrollPane scrollPane1;
    private ColoredTextPane txtLogs;
    private JPanel panel9;
    public JLabel lblCurrentFrame;
    public JLabel lblTrafficUp;
    public JLabel lblTrafficDown;
    public JLabel lblEncodingDelay;
    private JLabel lblDecodingDelay;
    private JLabel lblDisplayDelay;
    private JLabel lblRenderQueue;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private int screenMaxWidth;
    private int screenMaxHeight;

    public int tabletWidth;
    public int tabletHeight;

    public long clientDecodeTime = 0;

    private HIDWrapper hidWrapper = new HIDWrapper();
    private GraphicServer graphicServer;
    private TrafficCounter trafficCounter;

    public VTabletServer() {
        initComponents();
        setPreferredSize(new Dimension(875, 580));
        setMinimumSize(new Dimension(720, 470));
        numCanvaPosX.setEditor(new JSpinner.NumberEditor(numCanvaPosX, "#"));
        numCanvaPosY.setEditor(new JSpinner.NumberEditor(numCanvaPosY, "#"));
        numCanvaWidth.setEditor(new JSpinner.NumberEditor(numCanvaWidth, "#"));
        numCanvaHeight.setEditor(new JSpinner.NumberEditor(numCanvaHeight, "#"));
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        numCanvaPosX.setModel(new SpinnerNumberModel(0, 0, size.getWidth(), 1));
        numCanvaPosY.setModel(new SpinnerNumberModel(0, 0, size.getHeight(), 1));
        numCanvaWidth.setModel(new SpinnerNumberModel(1, 1, size.getWidth(), 1));
        numCanvaHeight.setModel(new SpinnerNumberModel(1, 1, size.getHeight(), 1));

        numCanvaPosX.setValue(0);
        numCanvaPosY.setValue(0);
        numCanvaWidth.setValue((int) size.getWidth());
        numCanvaHeight.setValue((int) size.getHeight());

        numCanvaPosX.addChangeListener(e -> {
            if ((int)numCanvaPosX.getValue() + (int)numCanvaWidth.getValue() > screenMaxWidth) {
                numCanvaWidth.setValue(screenMaxWidth - (int)numCanvaPosX.getValue());
            }
        });
        numCanvaPosY.addChangeListener(e -> {
            if ((int)numCanvaPosY.getValue() + (int)numCanvaHeight.getValue() > screenMaxHeight) {
                numCanvaHeight.setValue(screenMaxHeight - (int)numCanvaPosY.getValue());
            }
        });

        this.screenMaxWidth = (int) size.getWidth();
        this.screenMaxHeight = (int) size.getHeight();
        txtLogs.setEditable(false);
        Logger.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
            }
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
            if (!Platform.isWindows()) {
                log.e("Windows system is required");
            }
            if (Platform.is64Bit()) {
                log.e("Virtual input driver only supports 32bit Java VM! You're using 64bit Java version");
            }
            try {
                hidWrapper.open();
            } catch (Exception | UnsatisfiedLinkError ex) {
                log.e("Open Virtual HID device failed", ex);
                return;
            }
            onUpstreamConnected();
            btnDisconnectUpstream.setEnabled(true);
            btnConnectUpstream.setEnabled(false);
        });

        btnDisconnectUpstream.addActionListener(e -> {
            hidWrapper.close();
            onUpstreamDisconnected();
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

        trafficCounter = new TrafficCounter(this);
        trafficCounter.start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                trafficCounter.interrupt();
            }
        });
    }

    public void onUpstreamConnected() {
        lblUpstreamConnectStatus.setText("已连接");
        log.s("Successfully opened HID driver");
    }

    public void onUpstreamDisconnected() {
        lblUpstreamConnectStatus.setText("未连接");
        log.i("HID driver handle closed");
    }

    public void onPacketReceived(Object pkt) {
        if (pkt instanceof Vgt.C01PacketHandshake) {
            Vgt.C01PacketHandshake packet = (Vgt.C01PacketHandshake) pkt;
            tabletWidth = packet.getScreenWidth();
            tabletHeight = packet.getScreenHeight();
            log.s("Client handshake with canvas size {}x{}", tabletWidth, tabletHeight);

            Vgt.S02PacketServerInfo resp = Vgt.S02PacketServerInfo.newBuilder()
                    .setDummy(114514)
                    .build();
            Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder()
                    .setPacketId(2)
                    .setPayload(resp.toByteString()).build();

            graphicServer.packetWriter.getSendQueue().add(container);
            graphicServer.startScreenWorker();
        } else if (pkt instanceof Vgt.C04PacketHover) {
            Vgt.C04PacketHover packet = (Vgt.C04PacketHover) pkt;

            createPenEvent((int) numCanvaPosX.getValue() + packet.getPosX(),
                    (int) numCanvaPosY.getValue() + packet.getPosY(),
                    0f, false);
        } else if (pkt instanceof Vgt.C05PacketTouch) {
            Vgt.C05PacketTouch packet = (Vgt.C05PacketTouch) pkt;
            createPenEvent((int) numCanvaPosX.getValue() + packet.getPosX(),
                        (int) numCanvaPosY.getValue() + packet.getPosY(),
                        packet.getPressure(), true);
        } else if (pkt instanceof Vgt.C06PacketExit) {
            Vgt.C06PacketExit packet = (Vgt.C06PacketExit) pkt;
            hidWrapper.sendDigitizer((byte) HOVER_EXIT, 0, 0, 0);
        } else if (pkt instanceof Vgt.C07PacketTriggerHotkey) {
            Vgt.C07PacketTriggerHotkey packet = (Vgt.C07PacketTriggerHotkey) pkt;
            switch (packet.getKey()) {
                case TOOL_BRUSH:
                    RobotUtils.keyPress(KeyEvent.VK_B);
                    break;
                case TOOL_ERASER:
                    RobotUtils.keyPress(KeyEvent.VK_E);
                    break;
                case TOOL_SLICE:
                    RobotUtils.keyPress(KeyEvent.VK_I);
                    break;
                case TOOL_HAND:
                    RobotUtils.keyPress(KeyEvent.VK_H);
                    break;
                case ACTION_UNDO:
                    RobotUtils.action(robot -> {
                        robot.keyPress(KeyEvent.VK_ALT);
                        robot.keyPress(KeyEvent.VK_CONTROL);
                        robot.keyPress(KeyEvent.VK_Z);
                        robot.keyRelease(KeyEvent.VK_Z);
                        robot.keyRelease(KeyEvent.VK_CONTROL);
                        robot.keyRelease(KeyEvent.VK_ALT);
                    });
                    break;
                case ACTION_REDO:
                    RobotUtils.action(robot -> {
                        robot.keyPress(KeyEvent.VK_SHIFT);
                        robot.keyPress(KeyEvent.VK_CONTROL);
                        robot.keyPress(KeyEvent.VK_Z);
                        robot.keyRelease(KeyEvent.VK_Z);
                        robot.keyRelease(KeyEvent.VK_CONTROL);
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                    });
                    break;
                case ACTION_ZOOM_IN:
                    RobotUtils.action(robot -> {
                        robot.keyPress(KeyEvent.VK_CONTROL);
                        robot.keyPress(KeyEvent.VK_EQUALS);
                        robot.keyRelease(KeyEvent.VK_EQUALS);
                        robot.keyRelease(KeyEvent.VK_CONTROL);
                    });
                    break;
                case ACTION_ZOOM_OUT:
                    RobotUtils.action(robot -> {
                        robot.keyPress(KeyEvent.VK_CONTROL);
                        robot.keyPress(KeyEvent.VK_MINUS);
                        robot.keyRelease(KeyEvent.VK_MINUS);
                        robot.keyRelease(KeyEvent.VK_CONTROL);
                    });
                    break;
                case UNRECOGNIZED:
                    log.w("Unknown custom hotkey triggered");
                    break;
            }
        } else if (pkt instanceof Vgt.C08PacketDecodePerformanceReport) {
            Vgt.C08PacketDecodePerformanceReport packet = (Vgt.C08PacketDecodePerformanceReport) pkt;
            long renderDelay = System.currentTimeMillis() - packet.getTimestamp();
            clientDecodeTime = packet.getDecodeTook();
            lblDecodingDelay.setText(String.valueOf(clientDecodeTime));
            lblDisplayDelay.setText(String.valueOf(renderDelay));
            lblRenderQueue.setText(String.valueOf(packet.getRenderQueueSize()));
        }
    }

    public void createPenEvent(int realX, int realY, float pressureV, boolean pressed) {
        int flag = HIDWrapper.IN_RANGE;
        if (pressed) {
            flag |= HIDWrapper.TIPSWITCH;
        }

        float percentX = realX / (float) screenMaxWidth;
        float percentY = realY / (float) screenMaxHeight;
        hidWrapper.sendDigitizer((byte) flag, Math.abs(percentX), Math.abs(percentY), pressureV);
    }

    public void setGraphicServerRunning(boolean b) {
        lblServerRunningStatus.setText(b ? "运行中" : "未运行");
    }

    public void setGraphicServerConnected(boolean b) {
        lblServerConnectStatus.setText(b ? "已连接" : "未连接");
    }

    public void removePreview() {
        canvas.setIcon(null);
    }
}
