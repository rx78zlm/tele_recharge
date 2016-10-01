package com.tele.view;

import com.gargoylesoftware.htmlunit.WebClient;
import com.google.common.collect.Lists;
import com.tele.model.ChargeRequest;
import com.tele.model.ChargeResponse;
import com.tele.model.Response;
import com.tele.service.RechargeServiceAccount;
import com.tele.utils.DirNameUtil;
import com.tele.utils.StreamUtil;
import com.tele.utils.XLSUtil;
import org.apache.commons.io.FilenameUtils;
import sun.tools.jar.resources.jar_fr;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
public class TableView implements ActionListener {

    JFrame frame = new JFrame("控台");// 框架布局
    JTabbedPane tabPane = new JTabbedPane();// 选项卡布局
    Container con = new Container();
    JButton btnCommit = new JButton("确定");
    JButton btnClear = new JButton("清除");
    JTable table;
    Vector CellsVector = new Vector();
    Vector TitleVector = new Vector();

    TableView() {
        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
        frame.setSize(500, 500);// 设定窗口大小
        frame.setContentPane(tabPane);// 设置布局
        frame.setResizable(false);
        btnCommit.setBounds(0, 0, frame.getWidth(), 30);
        btnClear.setBounds(0, 30, frame.getWidth(), 30);
        btnCommit.addActionListener(this); // 添加事件处理
        btnClear.addActionListener(this);
        con.add(btnCommit);
        con.add(btnClear);
        JComponent component = initTable();
        component.setBounds(0, 70, frame.getWidth(), 200);
        con.add(component);
        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
        tabPane.add("面板", con);// 添加布局1
    }

    public JScrollPane initTable() {
        Object[][] tableData =
                {
                };
        Object[] columnTitle = {"姓名" , "年龄" , "性别"};
        this.TitleVector.add("姓名");
        this.TitleVector.add("年龄");
        this.TitleVector.add("性别");
        table = new JTable(CellsVector, TitleVector);
//        DefaultTableModel tableModel = new DefaultTableModel(tableData , columnTitle);
//        table = new JTable(tableModel);
        return new JScrollPane(table);
    }

    /**
     * 时间监听的方法
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnCommit)) {
            Vector data = new Vector();
            data.addElement("jack");
            data.addElement("15");
            data.addElement(new Date().toString());
            CellsVector.add(data);
            table.updateUI();
        }
        if (e.getSource().equals(btnClear)) {
            CellsVector.clear();
            table.updateUI();
        }
    }

    public static void main(String[] args) {
        new TableView();
    }
}
