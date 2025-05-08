package com.cdwater.component;

import javax.swing.*;
import java.awt.*;

public class HelpDialog extends JDialog {
    public HelpDialog() {
        initDialog();
        showHelpDialog();
        setVisible(true);
    }

    private void initDialog() {
        setTitle("欢乐连连看 游戏说明");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(470, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setModal(true);
        //设置窗口图标
        ImageIcon icon = new ImageIcon("images/LLK.png");
        setIconImage(icon.getImage());
    }

    //绘制背景图片
    private class HelpPanel extends JEditorPane {
        @Override
        protected void paintComponent(Graphics g) {
            ImageIcon bgImage = new ImageIcon("images/bg_setting.png");
            g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);

            super.paintComponent(g);
        }
    }

    private void showHelpDialog() {
        HelpPanel helpPanel = new HelpPanel();
        helpPanel.setOpaque(false);
        helpPanel.setEditable(false);
        helpPanel.setContentType("text/html");
        //帮助文本
        String content = """
                <html>
                <head>
                    <style>
                        h1 { font-size: 18px; font-weight: bold; text-align: center; }
                        p { font-size: 14px; }
                    </style>
                </head>
                <body>
                    <h1>基本模式</h1>
                    <p style="text-align: justify;">基本模式是“欢乐连连看”游戏的基本模式，包含游戏的基本功能：开始游戏、暂停游戏、提示、重排、计时。</p>
                    <p>1、开始游戏</p>
                    <p>当第一次进入游戏或者完成一局游戏后，点击“开始游戏”可以生成游戏地图，进行连连看游戏。在游戏地图中用鼠标左键点击任意位置的两张图片，选中图片后，会在选择的图片四周显示红色的矩形框，并判断能否消去。</p>
                    <p>2、暂停游戏</p>
                    <p>当游戏开始后，可以点击“暂停”按钮，将游戏暂停。暂停游戏后，可以点击“继续游戏”按钮，继续游戏。</p>
                    <p>3、提示</p>
                    <p>当游戏开始后，可以点击“提示”按钮，将在游戏地图中用矩形框提示一对可以消除的图片，并显示连接的路径。</p>
                    <p>4、重排</p>
                    <p>当游戏开始后，可以点击“重排”按钮，将游戏地图中剩余的位置的图片重新排列。</p>
                </body>
                </html>
                """;
        helpPanel.setText(content);
        JScrollPane scrollPane = new JScrollPane(helpPanel);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}