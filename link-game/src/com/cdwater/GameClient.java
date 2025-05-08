package com.cdwater;

import com.cdwater.UI.BasicModePanel;
import com.cdwater.UI.StartMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameClient extends JFrame {
    private StartMenu startMenu;
    private BasicModePanel basicModePanel;

    public GameClient() {
        initUI();
        showStartMenu();
    }

    //设置UI界面
    private void initUI() {
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
    void showStartMenu() {
        startMenu = new StartMenu();
        add(startMenu, BorderLayout.CENTER);
        //选择模式(绑定监听事件)
        startMenu.basicModeBtn.addActionListener(EnterBasicMode());
        startMenu.relaxModeBtn.addActionListener(e -> System.out.println("开启休闲模式"));
        startMenu.levelModeBtn.addActionListener(e -> System.out.println("开启关卡模式"));
        //查看排行榜(绑定监听事件)
        startMenu.rankBtn.addActionListener(e -> System.out.println("查看排行榜"));
        //打开设置(绑定监听事件)
        startMenu.settingBtn.addActionListener(e -> System.out.println("打开设置"));
        //帮助菜单(绑定监听事件)
        startMenu.helpBtn.addActionListener(e -> System.out.println("帮助菜单"));
    }

    //进入游戏界面
    private ActionListener EnterBasicMode() {
        return e -> {
            basicModePanel = new BasicModePanel();
            startMenu.setVisible(false);
            add(basicModePanel, BorderLayout.CENTER);
            //暂停游戏和继续游戏(绑定监听事件)
            basicModePanel.pauseBtn.addActionListener(e1 -> {
                basicModePanel.pauseOrNot();
            });
            //请求提示(绑定监听事件)
            basicModePanel.noticeBtn.addActionListener(e2 -> {
                basicModePanel.noticeStep();
            });
            //重排游戏地图(绑定监听事件)
            basicModePanel.disruptBtn.addActionListener(e3 -> {
                basicModePanel.disruptMap();
            });
            //打开设置(绑定监听事件)
            startMenu.settingBtn.addActionListener(e4 -> System.out.println("打开设置"));
            //帮助菜单(绑定监听事件)
            startMenu.helpBtn.addActionListener(e5 -> System.out.println("帮助菜单"));
            //返回开始菜单(绑定监听事件)
            basicModePanel.backBtn.addActionListener(e6 -> {
                basicModePanel.setVisible(false);
                showStartMenu();
            });
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameClient::new);
    }
}

