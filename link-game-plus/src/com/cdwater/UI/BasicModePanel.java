package com.cdwater.UI;

import com.cdwater.common.GameGraph;
import com.cdwater.component.GameTimer;
import com.cdwater.component.HelpDialog;
import com.cdwater.component.SettingDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.cdwater.utils.LLKUtil.*;

public class BasicModePanel extends JPanel implements MouseListener {
    private JButton pauseBtn = new JButton("暂停游戏");
    private JButton noticeBtn = new JButton("提示");
    private JButton disruptBtn = new JButton("重排");
    private JButton settingBtn = new JButton("设置");
    private JButton helpBtn = new JButton("帮助");
    private JButton againBtn = new JButton("重开");

    //游戏图结构
    private GameGraph gameGraph = new GameGraph();
    private int[] vexArray;//顶点数组
    private boolean[][] matrix;//邻接矩阵

    //开始绘制游戏地图
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //设置背景图片
        ImageIcon bgImage = new ImageIcon(currentBgImage);
        g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        //渲染游戏图片
        for (int i = 0; i < useRows; i++) {
            for (int j = 0; j < useCols; j++) {
                if (vexArray[i * useCols + j] != -1) {
                    g.drawImage(images.get(vexArray[i * useCols + j]), BEGIN_X + j * IMAGE_WIDTH, BEGIN_Y + i * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT, this);
                }
            }
        }
        //绘制地图边界
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);
        g.drawRect(BEGIN_X - 3, BEGIN_Y - 3, useCols * IMAGE_WIDTH + 4, useRows * IMAGE_HEIGHT + 4);
    }

    public BasicModePanel() {
        loadImages();//加载游戏图片
        vexArray = gameGraph.getVexArray();
        matrix = gameGraph.getMatrix();
        startGame();//开始游戏
        //添加监听
        this.addMouseListener(this);
        setLayout(null);
        //设置按钮样式
        pauseBtn.setFocusPainted(false);
        noticeBtn.setFocusPainted(false);
        disruptBtn.setFocusPainted(false);
        settingBtn.setFocusPainted(false);
        helpBtn.setFocusPainted(false);
        againBtn.setFocusPainted(false);
        //设置按钮坐标
        pauseBtn.setBounds(680, 100, 100, 50);
        noticeBtn.setBounds(680, 170, 100, 50);
        disruptBtn.setBounds(680, 240, 100, 50);
        settingBtn.setBounds(500, 520, 80, 40);
        helpBtn.setBounds(600, 520, 80, 40);
        againBtn.setBounds(700, 520, 80, 40);
        //添加按钮组件
        add(pauseBtn);
        add(noticeBtn);
        add(disruptBtn);
        add(settingBtn);
        add(helpBtn);
        add(againBtn);
        //绑定监听事件
        pauseBtn.addActionListener(e -> pauseGame());
        noticeBtn.addActionListener(e -> noticeGame());
        disruptBtn.addActionListener(e -> disruptGame());
        settingBtn.addActionListener(e -> jumpSetting());
        helpBtn.addActionListener(e -> jumpHelp());
        againBtn.addActionListener(e -> restartGame());
    }

    private void startGame() {
        //添加倒计时器
        gameTimer = new GameTimer(levelTime, this);
        gameTimer.getTimeLabel().setBounds(400, 520, 80, 30);
        add(gameTimer.getTimeLabel());
        gameTimer.getTimer().start();
        //添加进度条
        gameTimer.getProgressBar().setBounds(10, 520, 350, 30);
        add(gameTimer.getProgressBar());
        //打乱获取图片
        ArrayList<Integer> list = new ArrayList<>();
        int totalPairs = useRows * useCols / 2;//总对数
        for (int i = 0; i < totalPairs; i++) {
            int imgType = i % imgNums;
            list.add(imgType);
            list.add(imgType);
        }
        Collections.shuffle(list);//随机打乱
        //初始化顶点数组
        for (int i = 0; i < useRows; i++) {
            for (int j = 0; j < useCols; j++) {
                int index = i * useCols + j;
                vexArray[index] = list.get(index);
            }
        }
        //初始化邻接矩阵
        for (int i = 0; i < useRows; i++) {
            for (int j = 0; j < useCols; j++) {
                updateMatrix(i, j, matrix, vexArray);
            }
        }
        repaint();
    }

    public void restartGame() {
        //修改图结构顶点数和邻接矩阵大小
        vexArray = new int[useRows * useCols];
        matrix = new boolean[useRows * useCols][useRows * useCols];
        //重置倒计时器和进度条
        if (gameTimer != null) {
            gameTimer.getTimer().stop();
            remove(gameTimer.getTimeLabel());
            remove(gameTimer.getProgressBar());
        }
        gameTimer = new GameTimer(300, this);
        gameTimer.getTimeLabel().setBounds(400, 520, 80, 30);
        add(gameTimer.getTimeLabel());
        gameTimer.getProgressBar().setBounds(10, 520, 350, 30);
        add(gameTimer.getProgressBar());
        gameTimer.getTimer().start();
        //初始化回图默认数据
        Arrays.fill(vexArray, -1);
        for (boolean[] row : matrix) {
            Arrays.fill(row, false);
        }
        //打乱获取图片
        ArrayList<Integer> list = new ArrayList<>();
        int totalPairs = useRows * useCols / 2;//总对数
        for (int i = 0; i < totalPairs; i++) {
            int imgType = i % imgNums;
            list.add(imgType);
            list.add(imgType);
        }
        Collections.shuffle(list);//随机打乱
        //初始化顶点数组
        for (int i = 0; i < useRows; i++) {
            for (int j = 0; j < useCols; j++) {
                int index = i * useCols + j;
                vexArray[index] = list.get(index);
            }
        }
        //初始化邻接矩阵
        for (int i = 0; i < useRows; i++) {
            for (int j = 0; j < useCols; j++) {
                updateMatrix(i, j, matrix, vexArray);
            }
        }
        //重置游戏状态
        count = 0;
        isPause = false;
        isFirstSelected = true;
        pauseBtn.setText("暂停游戏");
        repaint();
    }

    private void pauseGame() {
        if (!isPause) {
            pauseBtn.setText("继续游戏");
            gameTimer.getTimer().stop();
        } else {
            pauseBtn.setText("暂停游戏");
            gameTimer.getTimer().restart();
        }
        isPause = !isPause;
    }

    private void noticeGame() {
        if (isPause) return;
        for (int i = 0; i < useCols * useRows; i++) {
            if (vexArray[i] == -1) continue;
            for (int j = 0; j < useCols * useRows && i != j; j++) {
                if (vexArray[i] == vexArray[j]) {
                    int row1 = i / useCols;
                    int col1 = i - row1 * useCols;
                    int row2 = j / useCols;
                    int col2 = j - row2 * useCols;
                    if (canConnect(row1, col1, row2, col2, matrix, vexArray)) {
                        drawSelected(row1, col1, this.getGraphics());
                        drawSelected(row2, col2, this.getGraphics());
                        drawLinkAndDeleteImage(row1, col1, row2, col2, this, vexArray, matrix);
                        return;
                    }
                }
            }
        }
    }

    private void disruptGame() {
        if (isPause) return;
        ArrayList<Integer> list = new ArrayList<>();
        //获取地图上剩余图片
        for (int i = 0; i < useRows; i++) {
            for (int j = 0; j < useCols; j++) {
                if (vexArray[i * useCols + j] != -1) {
                    list.add(vexArray[i * useCols + j]);
                    vexArray[i * useCols + j] = -1;
                }
            }
        }
        //打乱图片分布
        while (!list.isEmpty()) {
            int index = (int) (Math.random() * list.size());
            boolean flag = false;
            while (!flag) {
                int i = (int) (Math.random() * useRows);
                int j = (int) (Math.random() * useCols);
                if (vexArray[i * useCols + j] == -1) {
                    vexArray[i * useCols + j] = list.get(index);
                    list.remove(index);
                    flag = true;
                }
            }
        }
        //重新初始化邻接矩阵
        for (int i = 0; i < useRows; i++) {
            for (int j = 0; j < useCols; j++) {
                updateMatrix(i, j, matrix, vexArray);
            }
        }
        repaint();
    }

    private void jumpSetting() {
        new SettingDialog();
        restartGame();
    }

    private void jumpHelp() {
        new HelpDialog();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Graphics g = this.getGraphics();
        int x = e.getX() - BEGIN_X;
        int y = e.getY() - BEGIN_Y;
        int i = y / IMAGE_HEIGHT;
        int j = x / IMAGE_WIDTH;
        if (!isPause) {
            //如果为第一次点击
            if (isFirstSelected) {
                if (vexArray[i * useCols + j] != -1) {
                    //选中图片并绘制边框
                    isFirstSelected = false;
                    clickedRow = i;
                    clickedCol = j;
                    drawSelected(clickedRow, clickedCol, g);
                }
            } else {//如果为第二次点击
                if (vexArray[i * useCols + j] != -1) {
                    //点击的是同一种图片
                    if (vexArray[i * useCols + j] == vexArray[clickedRow * useCols + clickedCol]) {
                        //两次选同一位置的图片，则解除选中状态
                        if (i == clickedRow && j == clickedCol) {
                            clearSelected(clickedRow, clickedCol, this);
                            isFirstSelected = true;
                        }
                        //如果可以连通，则绘制连接线，然后消去选中图片并重置第一次选中标识
                        else if (canConnect(clickedRow, clickedCol, i, j, matrix, vexArray)) {
                            drawSelected(i, j, g);
                            //绘制连接线
                            drawLinkAndDeleteImage(clickedRow, clickedCol, i, j, this, vexArray, matrix);
                            isFirstSelected = true;
                        } else {//相同图片但是连接失败，把选中框给新选的
                            clearSelected(clickedRow, clickedCol, this);
                            clickedRow = i;
                            clickedCol = j;
                            drawSelected(clickedRow, clickedCol, g);
                        }
                    } else {//选的图片都不是同一种，则把选中框给新选的
                        clearSelected(clickedRow, clickedCol, this);
                        clickedRow = i;
                        clickedCol = j;
                        drawSelected(clickedRow, clickedCol, g);
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}