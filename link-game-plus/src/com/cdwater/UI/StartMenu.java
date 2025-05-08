package com.cdwater.UI;

import javax.swing.*;
import java.awt.*;

public class StartMenu extends JPanel {
    //按钮组件
    public JButton basicModeBtn = new JButton("基本模式");
    public JButton relaxModeBtn = new JButton("休闲模式");
    public JButton levelModeBtn = new JButton("关卡模式");
    public JButton rankBtn = new JButton("排行榜");
    public JButton settingBtn = new JButton("设置");
    public JButton helpBtn = new JButton("帮助");

    public StartMenu() {
        setLayout(null);
        //设置按钮样式
        Font btnFont = new Font("微软雅黑", Font.BOLD, 16);
        basicModeBtn.setContentAreaFilled(false);
        basicModeBtn.setOpaque(false);
        basicModeBtn.setForeground(Color.BLACK);
        basicModeBtn.setBorderPainted(false);
        basicModeBtn.setFocusPainted(false);
        basicModeBtn.setFont(btnFont);

        relaxModeBtn.setContentAreaFilled(false);
        relaxModeBtn.setOpaque(false);
        relaxModeBtn.setForeground(Color.BLACK);
        relaxModeBtn.setBorderPainted(false);
        relaxModeBtn.setFocusPainted(false);
        relaxModeBtn.setFont(btnFont);

        levelModeBtn.setContentAreaFilled(false);
        levelModeBtn.setOpaque(false);
        levelModeBtn.setForeground(Color.BLACK);
        levelModeBtn.setBorderPainted(false);
        levelModeBtn.setFocusPainted(false);
        levelModeBtn.setFont(btnFont);

        rankBtn.setFocusPainted(false);
        settingBtn.setFocusPainted(false);
        helpBtn.setFocusPainted(false);
        //设置按钮坐标
        basicModeBtn.setBounds(0, 200, 150, 80);
        relaxModeBtn.setBounds(0, 300, 150, 80);
        levelModeBtn.setBounds(0, 400, 150, 80);
        rankBtn.setBounds(500, 520, 80, 40);
        settingBtn.setBounds(600, 520, 80, 40);
        helpBtn.setBounds(700, 520, 80, 40);
        //添加按钮组件
        add(basicModeBtn);
        add(relaxModeBtn);
        add(levelModeBtn);
        add(rankBtn);
        add(settingBtn);
        add(helpBtn);
    }

    //设置背景图片
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon bgImage = new ImageIcon("images/bg_start.png");
        g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}