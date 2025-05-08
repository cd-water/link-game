package com.cdwater;

import com.cdwater.UI.*;
import com.cdwater.component.HelpDialog;
import com.cdwater.component.RankDialog;
import com.cdwater.component.SettingDialog;

import javax.swing.*;
import java.awt.*;

import static com.cdwater.utils.LLKUtil.isRelax;

public class GameClient extends JFrame {
    public GameClient() {
        initUI();
        showStartMenu();
    }

    //设置UI界面
    private void initUI() {
        //设置窗口信息
        setTitle("欢乐连连看");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        //设置窗口图标
        ImageIcon icon = new ImageIcon("images/LLK.png");
        setIconImage(icon.getImage());
    }

    //打开开始菜单
    public void showStartMenu() {
        StartMenu startMenu = new StartMenu();
        add(startMenu, BorderLayout.CENTER);

        //选择模式(绑定监听事件)
        startMenu.basicModeBtn.addActionListener(e -> {
            BasicModePanel basicModePanel = new BasicModePanel();
            startMenu.setVisible(false);
            add(basicModePanel, BorderLayout.CENTER);
        });
        startMenu.relaxModeBtn.addActionListener(e -> {
            //启动休闲模式
            isRelax = true;
            RelaxModePanel relaxModePanel = new RelaxModePanel();
            startMenu.setVisible(false);
            add(relaxModePanel, BorderLayout.CENTER);
        });
        startMenu.levelModeBtn.addActionListener(e -> {
            LevelModePanel levelModePanel = new LevelModePanel();
            startMenu.setVisible(false);
            add(levelModePanel, BorderLayout.CENTER);
        });
        //查看排行榜(绑定监听事件)
        startMenu.rankBtn.addActionListener(e -> {
            RankDialog rankDialog = new RankDialog();
            startMenu.setVisible(false);
            add(rankDialog, BorderLayout.CENTER);
        });
        //打开设置(绑定监听事件)
        startMenu.settingBtn.addActionListener(e -> new SettingDialog());
        //帮助菜单(绑定监听事件)
        startMenu.helpBtn.addActionListener(e -> new HelpDialog());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameClient::new);
    }
}