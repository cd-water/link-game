package com.cdwater.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class BasicModePanel extends JPanel implements MouseListener {
    //按钮组件
    public JButton pauseBtn = new JButton("暂停游戏");
    public JButton noticeBtn = new JButton("提示");
    public JButton disruptBtn = new JButton("重排");
    public JButton settingBtn = new JButton("设置");
    public JButton helpBtn = new JButton("帮助");
    public JButton backBtn = new JButton("返回");
    //游戏地图行列
    private final int ROWS = 10;
    private final int COLS = 16;
    //地图图片宽高
    private final int IMAGE_WIDTH = 40;
    private final int IMAGE_HEIGHT = 40;
    //游戏地图偏移
    private final int BEGIN_X = 30;
    private final int BEGIN_Y = 40;
    //连接消除方式
    private int linkMethod;
    private final int ZERO_CORNER = 0;
    private final int ONE_CORNER = 1;
    private final int TWO_CORNER = 2;
    //是否点击了暂停游戏
    private boolean isPause = false;
    //是否可以点击图片
    private boolean canClick = true;
    //是否被选中图片
    private boolean isSelected = false;
    //已经消除的图片个数
    private int count;
    //记录判断能否连接时，连接路线上的拐点信息
    private TurnPoint turnPointOne, turnPointTwo;
    //存储渲染所需图片
    private Image[] images;
    //存储地图信息(渲染图片ID，-1代表为空)
    private final int[][] gameMap = new int[ROWS][COLS];
    //记录首次选中图片的id以及行和列
    private int clickId, clickRow, clickCol;

    public BasicModePanel() {
        setLayout(null);
        count = 0;
        createMap();
        //添加监听
        this.addMouseListener(this);

        //设置按钮样式
        pauseBtn.setFocusPainted(false);
        noticeBtn.setFocusPainted(false);
        disruptBtn.setFocusPainted(false);
        settingBtn.setFocusPainted(false);
        helpBtn.setFocusPainted(false);
        backBtn.setFocusPainted(false);

        //设置按钮坐标
        pauseBtn.setBounds(680, 100, 100, 50);
        noticeBtn.setBounds(680, 170, 100, 50);
        disruptBtn.setBounds(680, 240, 100, 50);
        settingBtn.setBounds(500, 520, 80, 40);
        helpBtn.setBounds(600, 520, 80, 40);
        backBtn.setBounds(700, 520, 80, 40);

        //添加按钮组件
        add(pauseBtn);
        add(noticeBtn);
        add(disruptBtn);
        add(settingBtn);
        add(helpBtn);
        add(backBtn);

        loadImages();//读取图片信息
        repaint();//第一次画
    }

    public static class TurnPoint {
        public int x;
        public int y;

        public TurnPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    //创建游戏地图
    private void createMap() {
        ArrayList<Integer> list = new ArrayList<>();
        //添加rows*cols个数据到list
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                list.add(j);
            }
        }
        //把list数据随机打乱添加到gameMap
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int index = (int) (Math.random() * list.size());
                gameMap[i][j] = list.get(index);
                list.remove(index);
            }
        }
    }

    //加载游戏地图所需图片
    private void loadImages() {
        images = new Image[16];
        for (int i = 0; i < 16; i++) {
            images[i] = Toolkit.getDefaultToolkit().getImage("images/" + (i + 1) + ".png");
        }
    }

    //开始绘制游戏地图
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //设置背景图片
        ImageIcon bg_image = new ImageIcon("images/bg_basic.png");
        g.drawImage(bg_image.getImage(), 0, 0, getWidth(), getHeight(), this);
        //渲染游戏图片
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (gameMap[i][j] != -1) {
                    g.drawImage(images[gameMap[i][j]], BEGIN_X + j * IMAGE_WIDTH, BEGIN_Y + i * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT, this);
                }
            }
        }
    }

    //判断能否直连
    private boolean zeroCornerLink(int selectedOneX, int selectedOneY, int selectedTwoX, int selectedTwoY) {
        //位于同一行
        if (selectedOneY == selectedTwoY) {
            //保证横坐标选图1小于选图2
            if (selectedOneX > selectedTwoX) {
                int temp = selectedOneX;
                selectedOneX = selectedTwoX;
                selectedTwoX = temp;
            }
            //判断两图之间是否存在图片
            for (int i = selectedOneX + 1; i < selectedTwoX; i++) {
                //如果两图之间不为空则无法连接
                if (gameMap[selectedOneY][i] != -1) {
                    return false;
                }
            }
            //标记连接方式
            linkMethod = ZERO_CORNER;
            return true;
        }
        //位于同一列
        if (selectedOneX == selectedTwoX) {
            //保证纵坐标选图1小于选图2
            if (selectedOneY > selectedTwoY) {
                int temp = selectedOneY;
                selectedOneY = selectedTwoY;
                selectedTwoY = temp;
            }
            //判断两图之间是否存在图片
            for (int i = selectedOneY + 1; i < selectedTwoY; i++) {
                //如果两图之间不为空则无法连接
                if (gameMap[i][selectedOneX] != -1) {
                    return false;
                }
            }
            //标记连接方式
            linkMethod = ZERO_CORNER;
            return true;
        }
        return false;
    }

    //一个拐点的情况下连接
    private boolean oneCornerLink(int selectedOneX, int selectedOneY, int selectedTwoX, int selectedTwoY) {
        //可能拐点情况1
        if (gameMap[selectedOneY][selectedTwoX] == -1 && zeroCornerLink(selectedTwoX, selectedOneY, selectedOneX, selectedOneY) && zeroCornerLink(selectedTwoX, selectedOneY, selectedTwoX, selectedTwoY)) {
            linkMethod = ONE_CORNER;
            //记录拐点信息
            turnPointOne = new TurnPoint(selectedTwoX, selectedOneY);
            return true;
        }
        //可能拐点情况2
        if (gameMap[selectedTwoY][selectedOneX] == -1 && zeroCornerLink(selectedOneX, selectedTwoY, selectedOneX, selectedOneY) && zeroCornerLink(selectedOneX, selectedTwoY, selectedTwoX, selectedTwoY)) {
            linkMethod = ONE_CORNER;
            //记录拐点信息
            turnPointOne = new TurnPoint(selectedOneX, selectedTwoY);
            return true;
        }
        return false;
    }

    //两个拐点的情况连接
    private boolean twoCornerLink(int selectedOneX, int selectedOneY, int selectedTwoX, int selectedTwoY) {
        //向上搜索路径
        for (int i = selectedOneY - 1; i >= -1; i--) {
            if (i > -1) {
                if (gameMap[i][selectedOneX] == -1) {
                    if (oneCornerLink(selectedOneX, i, selectedTwoX, selectedTwoY)) {
                        linkMethod = TWO_CORNER;
                        //记录拐点信息
                        turnPointOne = new TurnPoint(selectedOneX, i);
                        turnPointTwo = new TurnPoint(selectedTwoX, i);
                        return true;
                    }
                } else {
                    break;
                }
            } else if (zeroCornerLink(selectedTwoX, i, selectedTwoX, selectedTwoY)) {
                linkMethod = TWO_CORNER;
                //记录拐点信息
                turnPointOne = new TurnPoint(selectedOneX, i);
                turnPointTwo = new TurnPoint(selectedTwoX, i);
                return true;
            }
        }
        //向下搜索路径
        for (int i = selectedOneY + 1; i <= ROWS; i++) {
            if (i < ROWS) {
                if (gameMap[i][selectedOneX] == -1) {
                    if (oneCornerLink(selectedOneX, i, selectedTwoX, selectedTwoY)) {
                        linkMethod = TWO_CORNER;
                        //记录拐点信息
                        turnPointOne = new TurnPoint(selectedOneX, i);
                        turnPointTwo = new TurnPoint(selectedTwoX, i);
                        return true;
                    }
                } else {
                    break;
                }
            } else if (zeroCornerLink(selectedTwoX, i, selectedTwoX, selectedTwoY)) {
                linkMethod = TWO_CORNER;
                //记录拐点信息
                turnPointOne = new TurnPoint(selectedOneX, i);
                turnPointTwo = new TurnPoint(selectedTwoX, i);
                return true;
            }
        }
        //向左搜索路径
        for (int i = selectedOneX - 1; i >= -1; i--) {
            if (i > -1) {
                if (gameMap[selectedOneY][i] == -1) {
                    if (oneCornerLink(i, selectedOneY, selectedTwoX, selectedTwoY)) {
                        linkMethod = TWO_CORNER;
                        //记录拐点信息
                        turnPointOne = new TurnPoint(i, selectedOneY);
                        turnPointTwo = new TurnPoint(i, selectedTwoY);
                        return true;
                    }
                } else {
                    break;
                }
            } else if (zeroCornerLink(i, selectedTwoY, selectedTwoX, selectedTwoY)) {
                linkMethod = TWO_CORNER;
                //记录拐点信息
                turnPointOne = new TurnPoint(i, selectedOneY);
                turnPointTwo = new TurnPoint(i, selectedTwoY);
                return true;
            }
        }
        //向右搜索路径
        for (int i = selectedOneX + 1; i <= COLS; i++) {
            if (i < COLS) {
                if (gameMap[selectedOneY][i] == -1) {
                    if (oneCornerLink(i, selectedOneY, selectedTwoX, selectedTwoY)) {
                        linkMethod = TWO_CORNER;
                        //记录拐点信息
                        turnPointOne = new TurnPoint(i, selectedOneY);
                        turnPointTwo = new TurnPoint(i, selectedTwoY);
                        return true;
                    }
                } else {
                    break;
                }
            } else if (zeroCornerLink(i, selectedTwoY, selectedTwoX, selectedTwoY)) {
                linkMethod = TWO_CORNER;
                //记录拐点信息
                turnPointOne = new TurnPoint(i, selectedOneY);
                turnPointTwo = new TurnPoint(i, selectedTwoY);
                return true;
            }
        }
        return false;
    }

    //绘制选中框
    private void drawSelected(int x, int y, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(222, 22, 58));
        g.drawRect(x + 2, y + 2, IMAGE_WIDTH - 5, IMAGE_HEIGHT - 5);
    }

    //清除选中框
    public void clearSelected(int x, int y) {
        repaint(BEGIN_X + y * IMAGE_WIDTH, BEGIN_Y + x * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    //绘制连接线
    private void drawLinkAndDeleteImage(int pointOneRow, int pointOneCol, int pointTwoRow, int pointTwoCol) {
        Graphics g = this.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(98, 213, 21));

        //连线起点终点均为图片中心
        Point p1 = new Point(pointOneCol * IMAGE_WIDTH + BEGIN_X + IMAGE_WIDTH / 2, pointOneRow * IMAGE_HEIGHT + BEGIN_Y + IMAGE_HEIGHT / 2);
        Point p2 = new Point(pointTwoCol * IMAGE_HEIGHT + BEGIN_X + IMAGE_WIDTH / 2, pointTwoRow * IMAGE_HEIGHT + BEGIN_Y + IMAGE_HEIGHT / 2);

        //根据不同的消除方式绘制连线
        if (linkMethod == ZERO_CORNER) {
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        } else if (linkMethod == ONE_CORNER) {
            //将拐点信息转换成像素坐标
            Point cornerPoint = new Point(turnPointOne.x * IMAGE_WIDTH + BEGIN_X + IMAGE_WIDTH / 2, turnPointOne.y * IMAGE_HEIGHT + BEGIN_Y + IMAGE_HEIGHT / 2);
            g.drawLine(p1.x, p1.y, cornerPoint.x, cornerPoint.y);
            g.drawLine(p2.x, p2.y, cornerPoint.x, cornerPoint.y);
        } else if (linkMethod == TWO_CORNER) {
            //将拐点信息转换成像素坐标
            Point cornerPointOne = new Point(turnPointOne.x * IMAGE_WIDTH + BEGIN_X + IMAGE_WIDTH / 2, turnPointOne.y * IMAGE_HEIGHT + BEGIN_Y + IMAGE_HEIGHT / 2);
            Point cornerPointTwo = new Point(turnPointTwo.x * IMAGE_WIDTH + BEGIN_X + IMAGE_WIDTH / 2, turnPointTwo.y * IMAGE_HEIGHT + BEGIN_Y + IMAGE_HEIGHT / 2);
            //保证(x1,y1)与拐点1在同一列或者同一行
            if (p1.x != cornerPointOne.x && p1.y != cornerPointOne.y) {
                Point temp;
                temp = cornerPointOne;
                cornerPointOne = cornerPointTwo;
                cornerPointTwo = temp;
            }
            g.drawLine(p1.x, p1.y, cornerPointOne.x, cornerPointOne.y);
            g.drawLine(p2.x, p2.y, cornerPointTwo.x, cornerPointTwo.y);
            g.drawLine(cornerPointOne.x, cornerPointOne.y, cornerPointTwo.x, cornerPointTwo.y);
        }
        //消去的方块数目+2
        count += 2;
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //擦除连线
        repaint();
        //消除图片位置标空
        gameMap[pointOneRow][pointOneCol] = -1;
        gameMap[pointTwoRow][pointTwoCol] = -1;
        //判断游戏是否结束
        isWin();
    }

    //继续游戏/暂停游戏切换
    public void pauseOrNot() {
        if (!isPause) {
            pauseBtn.setText("继续游戏");
        } else {
            pauseBtn.setText("暂停游戏");
        }
        isPause = !isPause;
        canClick = !canClick;
    }

    //提示一步
    public void noticeStep() {
        //暂停游戏状态无法提示
        if (!canClick) {
            return;
        }
        //如果之前玩家选中了一个方块，清空该选中框
        if (isSelected) {
            clearSelected(clickRow, clickCol);
            isSelected = false;
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (gameMap[i][j] != -1) {
                    for (int p = i; p < ROWS; p++) {
                        for (int q = 0; q < COLS; q++) {
                            //如果图案不相等
                            if (gameMap[p][q] != gameMap[i][j] || (p == i && q == j)) {
                                continue;
                            }
                            if (zeroCornerLink(q, p, j, i) || oneCornerLink(q, p, j, i) || twoCornerLink(q, p, j, i)) {
                                drawSelected(j * IMAGE_WIDTH + BEGIN_X, i * IMAGE_HEIGHT + BEGIN_Y, this.getGraphics());
                                drawSelected(q * IMAGE_WIDTH + BEGIN_X, p * IMAGE_HEIGHT + BEGIN_Y, this.getGraphics());
                                drawLinkAndDeleteImage(p, q, i, j);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    //重排游戏地图
    public void disruptMap() {
        //暂停游戏状态无法重排
        if (!canClick) {
            return;
        }
        ArrayList<Integer> list = new ArrayList<>();
        //获取地图上剩余图片
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (gameMap[i][j] != -1) list.add(gameMap[i][j]);
                gameMap[i][j] = -1;
            }
        }
        //打乱随机分布
        while (!list.isEmpty()) {
            int index = (int) (Math.random() * list.size());
            boolean flag = false;
            while (!flag) {
                int i = (int) (Math.random() * ROWS);
                int j = (int) (Math.random() * COLS);
                if (gameMap[i][j] == -1) {
                    gameMap[i][j] = list.get(index);
                    list.remove(index);
                    flag = true;
                }
            }
        }
        repaint();
    }

    //判断胜利条件并作后续处理
    private void isWin() {
        if (count == ROWS * COLS) {
            String[] options = {"再来一局", "返回菜单"};
            int choice = JOptionPane.showOptionDialog(this, "win！win！win！", "过关",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            //再来一局
            if (choice == 0 || choice == JOptionPane.CLOSED_OPTION) {
                count = 0;
                createMap();
                clickId = -1;
                clickRow = -1;
                clickCol = -1;
                repaint();
            } else if (choice == 1) {
                backBtn.doClick();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    //鼠标点击事件
    @Override
    public void mousePressed(MouseEvent e) {
        Graphics g = this.getGraphics();
        //计算点击图片所在行列
        int x = e.getX() - BEGIN_X;//点击位置x-偏移量x
        int y = e.getY() - BEGIN_Y;//点击位置y-偏移量y
        int i = y / IMAGE_HEIGHT;//对应数组行数
        int j = x / IMAGE_WIDTH;//对应数组列数
        if (canClick) {
            //如果是第一次点击
            if (!isSelected) {
                if (gameMap[i][j] != -1) {
                    //选中图片并绘制边框
                    clickId = gameMap[i][j];
                    isSelected = true;
                    clickRow = i;
                    clickCol = j;
                    drawSelected(j * IMAGE_WIDTH + BEGIN_X, i * IMAGE_HEIGHT + BEGIN_Y, g);
                }
            } else {//如果是第二次点击
                if (gameMap[i][j] != -1) {
                    //点击的是同一种图片
                    if (gameMap[i][j] == clickId) {
                        //两次选同一位置的图片解除选中状态
                        if (i == clickRow && j == clickCol) {
                            clearSelected(clickRow, clickCol);
                            isSelected = false;
                        }
                        //如果可以连通，则绘制连接线，然后消去选中图片并重置第一次选中标识
                        else if (zeroCornerLink(clickCol, clickRow, j, i) || oneCornerLink(clickCol, clickRow, j, i) || twoCornerLink(clickCol, clickRow, j, i)) {
                            drawSelected(j * IMAGE_WIDTH + BEGIN_X, i * IMAGE_HEIGHT + BEGIN_Y, g);
                            //绘制连接线
                            drawLinkAndDeleteImage(clickRow, clickCol, i, j);
                            isSelected = false;
                        } else {//相同图片但连接失败，把选定框给新选的
                            clearSelected(clickRow, clickCol);
                            clickId = gameMap[i][j];
                            clickRow = i;
                            clickCol = j;
                            drawSelected(j * IMAGE_WIDTH + BEGIN_X, i * IMAGE_HEIGHT + BEGIN_Y, g);
                        }
                    } else {//选的图片都不是同一种，把选定框给新选的
                        clearSelected(clickRow, clickCol);
                        clickId = gameMap[i][j];
                        clickRow = i;
                        clickCol = j;
                        drawSelected(j * IMAGE_WIDTH + BEGIN_X, i * IMAGE_HEIGHT + BEGIN_Y, g);
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
