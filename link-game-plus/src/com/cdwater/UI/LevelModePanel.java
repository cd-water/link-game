package com.cdwater.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.cdwater.utils.LLKUtil.levelTime;

public class LevelModePanel extends JPanel {
    private JButton easyBtn = new JButton("简单模式");
    private JButton medBtn = new JButton("中等模式");
    private JButton hardBtn = new JButton("困难模式");

    //绘制背景图片
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //设置背景图片
        ImageIcon bgImage = new ImageIcon("images/bg_level.png");
        g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    public LevelModePanel() {
        setLayout(null);
        //设置按钮样式
        Font btnFont = new Font("微软雅黑", Font.BOLD, 20);
        easyBtn.setContentAreaFilled(false);
        easyBtn.setOpaque(false);
        easyBtn.setForeground(Color.WHITE);
        easyBtn.setBorderPainted(false);
        easyBtn.setFocusPainted(false);
        easyBtn.setFont(btnFont);

        medBtn.setContentAreaFilled(false);
        medBtn.setOpaque(false);
        medBtn.setForeground(Color.WHITE);
        medBtn.setBorderPainted(false);
        medBtn.setFocusPainted(false);
        medBtn.setFont(btnFont);

        hardBtn.setContentAreaFilled(false);
        hardBtn.setOpaque(false);
        hardBtn.setForeground(Color.WHITE);
        hardBtn.setBorderPainted(false);
        hardBtn.setFocusPainted(false);
        hardBtn.setFont(btnFont);

        //设置按钮坐标
        easyBtn.setBounds(100, 320, 150, 80);
        medBtn.setBounds(310, 320, 150, 80);
        hardBtn.setBounds(520, 320, 150, 80);

        //添加按钮组件
        add(easyBtn);
        add(medBtn);
        add(hardBtn);

        //绑定监听事件
        easyBtn.addActionListener(e -> {
            levelTime = 300;
            Container parent = this.getParent();
            parent.remove(this);
            BasicModePanel basicModePanel = new BasicModePanel();
            parent.add(basicModePanel, BorderLayout.CENTER);
            parent.validate();
            parent.repaint();
        });
        medBtn.addActionListener(e -> {
            levelTime = 200;
            Container parent = this.getParent();
            parent.remove(this);
            BasicModePanel basicModePanel = new BasicModePanel();
            parent.add(basicModePanel, BorderLayout.CENTER);
            parent.validate();
            parent.repaint();
        });
        hardBtn.addActionListener(e -> {
            levelTime = 100;
            Container parent = this.getParent();
            parent.remove(this);
            BasicModePanel basicModePanel = new BasicModePanel();
            parent.add(basicModePanel, BorderLayout.CENTER);
            parent.validate();
            parent.repaint();
        });
    }
}