package com.tele.view;

import com.gargoylesoftware.htmlunit.WebClient;
import com.google.common.collect.Lists;
import com.tele.model.ChargeRequest;
import com.tele.model.ChargeResponse;
import com.tele.model.Response;
import com.tele.service.RechargeServiceAccount;
import com.tele.utils.CheckUtil;
import com.tele.utils.DirNameUtil;
import com.tele.utils.StreamUtil;
import com.tele.utils.XLSUtil;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangleimin
 * @package com.tele.view
 * @date 16-9-29
 */
public class MainView implements ActionListener {

    private static final String RESULT_TEMPLATE = "共%s，已完成%s，还有%s";

    private RechargeServiceAccount rechargeService = new RechargeServiceAccount();

    JFrame frame = new JFrame("控台");// 框架布局
    JTabbedPane tabPane = new JTabbedPane();// 选项卡布局
    Container con = new Container();
    JLabel labelChoose = new JLabel("选择文件");
    JLabel labelSave = new JLabel("结果目录");
    JTextField txtPath = new JTextField();// TextField 源文件的路径
    JTextField txtSavePath = new JTextField();//  结果文件目录的路径
    JButton btnChoose = new JButton("...");// 选择
    JButton btnSave = new JButton("...");// 选择
    JFileChooser jfc = new JFileChooser();// 文件选择器
    JLabel labelResult = new JLabel("1111111111111", SwingConstants.CENTER);       // 进度
    JButton btnCommit = new JButton("确定");
    JTable table = null;
    Vector cellsVector = new Vector<>();
    Vector titleVector = new Vector<>();

    MainView() {
        jfc.setCurrentDirectory(new File("c://"));// 文件选择器的初始目录定为d盘
        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
        frame.setSize(600, 500);// 设定窗口大小
        frame.setContentPane(tabPane);// 设置布局
        frame.setResizable(false);
        labelChoose.setBounds(10, 10, 70, 20);
        labelSave.setBounds(10, 35, 70, 20);
        txtPath.setBounds(75, 10, 120, 20);
        txtSavePath.setBounds(75, 35, 120, 20);
        btnChoose.setBounds(210, 10, 50, 20);
        btnSave.setBounds(210, 35, 50, 20);
        btnCommit.setBounds(0, 70, frame.getWidth(), 30);
        labelResult.setBounds(0, 420, frame.getWidth(), 20);
        btnChoose.addActionListener(this); // 添加事件处理
        btnSave.addActionListener(this);    // 添加事件处理
        btnCommit.addActionListener(this); // 添加事件处理
        JComponent component = initTable();
        component.setBounds(0, 110, frame.getWidth(), 300);
        con.add(component);
        con.add(labelChoose);
        con.add(labelSave);
        con.add(txtPath);
        con.add(txtSavePath);
        con.add(btnChoose);
        con.add(btnSave);
        con.add(btnCommit);
        con.add(labelResult);
        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
        tabPane.add("面板", con);// 添加布局1
    }

    public JScrollPane initTable() {
        this.titleVector.add("分账序号");
        this.titleVector.add("卡号");
        this.titleVector.add("卡密");
        this.titleVector.add("充值结果");
        this.titleVector.add("充值卡状态");
        this.titleVector.add("充值卡余额");
        this.titleVector.add("充值卡有效期");
        table = new JTable(cellsVector, titleVector);
        return new JScrollPane(table);
    }

    /**
     * 时间监听的方法
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnChoose)) {// 判断触发方法的按钮是哪个
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);// 设定只能选择到文件夹
            int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
            if (state == 1) {
                return;
            } else {
                File f = jfc.getSelectedFile();// f为选择到的目录
                txtPath.setText(f.getAbsolutePath());
            }
        }
        if (e.getSource().equals(btnSave)) {// 判断触发方法的按钮是哪个
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 设定只能选择到文件夹
            int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
            if (state == 1) {
                return;
            } else {
                File f = jfc.getSelectedFile();// f为选择到的目录
                txtSavePath.setText(f.getAbsolutePath());
            }
        }
        if (e.getSource().equals(btnCommit)) {
            startProcess();
            if (!checkFile()) {
                JOptionPane.showMessageDialog(null, "请输入正确的文件路径", "错误", JOptionPane.ERROR_MESSAGE);
                finishProcess();
            }
            SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        Response<List<ChargeRequest>> readResp = rechargeService.readFile(txtPath.getText());
                        if (!readResp.isSuccess()) {
                            JOptionPane.showMessageDialog(null, readResp.getErrMsg(), "错误", JOptionPane.ERROR_MESSAGE);
                        } else {
                            List<ChargeRequest> chargeRequestList = readResp.getData();
                            WebClient webClient = rechargeService.createWebClient();
                            List<ChargeResponse> responseList = Lists.newArrayList();
                            for (int i = 1; i <= chargeRequestList.size(); i++) {
                                ChargeResponse chargeResp = rechargeService.rechargeOnce(webClient, chargeRequestList.get(i-1));
                                responseList.add(chargeResp);
                                labelResult.setText(String.format(RESULT_TEMPLATE, String.valueOf(chargeRequestList.size()), String.valueOf(i), String.valueOf(chargeRequestList.size() - i)));
                                Vector data = new Vector<>();
                                data.addElement(chargeResp.getAccountNo());
                                data.addElement(chargeResp.getCardNo());
                                data.addElement(chargeResp.getCardPwd());
                                data.addElement(chargeResp.getMessage());
                                if (chargeResp.getQueryResponse() != null) {
                                    data.addElement(chargeResp.getQueryResponse().getCardStatus());
                                    data.addElement(chargeResp.getQueryResponse().getCardValue());
                                    data.addElement(chargeResp.getQueryResponse().getExpirationTime());
                                } else {
                                    data.addElement("");
                                    data.addElement("");
                                    data.addElement("");
                                }
                                cellsVector.add(data);
                                table.updateUI();
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            XLSUtil xlsUtil = new XLSUtil();
                            String destFile = DirNameUtil.getDirName(txtSavePath.getText()) + FilenameUtils.getBaseName(txtPath.getText()) + "_result.xlsx";
                            xlsUtil.write(responseList, destFile);
                            StreamUtil.close(webClient);
                            JOptionPane.showMessageDialog(null, "完成！", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                        finishProcess();
                    }
                }
            );
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Response<List<ChargeRequest>> readResp = rechargeService.readFile(txtPath.getText());
                    if (!readResp.isSuccess()) {
                        JOptionPane.showMessageDialog(null, readResp.getErrMsg(), "错误", JOptionPane.ERROR_MESSAGE);
                    } else {
                        List<ChargeRequest> chargeRequestList = readResp.getData();
                        WebClient webClient = rechargeService.createWebClient();
                        List<ChargeResponse> responseList = Lists.newArrayList();
                        for (int i = 1; i <= chargeRequestList.size(); i++) {
                            ChargeResponse chargeResp = rechargeService.rechargeOnce(webClient, chargeRequestList.get(i-1));
                            responseList.add(chargeResp);
                            labelResult.setText(String.format(RESULT_TEMPLATE, String.valueOf(chargeRequestList.size()), String.valueOf(i), String.valueOf(chargeRequestList.size() - i)));
                            Vector data = new Vector<>();
                            data.addElement(chargeResp.getAccountNo());
                            data.addElement(chargeResp.getCardNo());
                            data.addElement(chargeResp.getCardPwd());
                            data.addElement(chargeResp.getMessage());
                            if (chargeResp.getQueryResponse() != null) {
                                data.addElement(chargeResp.getQueryResponse().getCardStatus());
                                data.addElement(chargeResp.getQueryResponse().getCardValue());
                                data.addElement(chargeResp.getQueryResponse().getExpirationTime());
                            } else {
                                data.addElement("");
                                data.addElement("");
                                data.addElement("");
                            }
                            cellsVector.add(data);
                            table.updateUI();
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                        XLSUtil xlsUtil = new XLSUtil();
                        String destFile = DirNameUtil.getDirName(txtSavePath.getText()) + FilenameUtils.getBaseName(txtPath.getText()) + "_result.xlsx";
                        xlsUtil.write(responseList, destFile);
                        StreamUtil.close(webClient);
                        JOptionPane.showMessageDialog(null, "完成！", "提示", JOptionPane.WARNING_MESSAGE);
                    }
                    finishProcess();
                }
            }).start();
        }
    }

    public boolean checkFile() {
        return DirNameUtil.isValidDir(txtSavePath.getText()) && DirNameUtil.isValidFile(txtPath.getText());
    }

    public void startProcess() {
        cellsVector.clear();
        table.updateUI();
        labelResult.setText("");
        labelResult.updateUI();
        btnChoose.setEnabled(false);
        btnCommit.setEnabled(false);
        txtPath.setEnabled(false);
        txtSavePath.setEnabled(false);
        btnSave.setEnabled(false);
    }

    public void finishProcess() {
        txtPath.setEnabled(true);
        txtSavePath.setEnabled(true);
        btnChoose.setEnabled(true);
        btnCommit.setEnabled(true);
        btnSave.setEnabled(true);
    }

    public static void main(String[] args) {
        if (CheckUtil.checkMacAddress()) {
            new MainView();
        } else {
            System.exit(1);
        }
    }
}
