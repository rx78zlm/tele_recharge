package com.tele.view;

import javax.swing.*;
import java.awt.*;

/**
 * @author zhangleimin
 * @package com.tele.view
 * @date 16-9-29
 */
public class SimpleTable {

    JFrame jf = new JFrame("简单表格");
    JTable table;
    Object[][] tableData =
            {
                    new Object[]{"李清照", 29, "女"},
                    new Object[]{"苏格拉底", 56, "男"},
                    new Object[]{"李白", 35, "男"},
                    new Object[]{"弄玉", 18, "女"},
                    new Object[]{"虎头", 2, "男"}
            };
    Object[] columnTitle = {"姓名" , "年龄" , "性别"};

    public void init() {
        table = new JTable(tableData , columnTitle);
        table.setLocation(0, 100);
        table.setSize(100, 100);
        JScrollPane pane = new JScrollPane(table);
        jf.add(pane);
        jf.pack();
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleTable().init();
    }
}
