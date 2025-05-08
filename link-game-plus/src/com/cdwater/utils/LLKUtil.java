package com.cdwater.utils;

import com.cdwater.component.GameTimer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LLKUtil {
    public static int useRows = 10;
    public static int useCols = 16;
    public static int imgNums = 16;
    public static ArrayList<Image> images = new ArrayList<>();
    //背景图片
    public static String currentBgImage = "images/bg_fruit.png";
    //地图图片宽高
    public final static int IMAGE_WIDTH = 40;
    public final static int IMAGE_HEIGHT = 40;
    //游戏地图偏移
    public final static int BEGIN_X = 30;
    public final static int BEGIN_Y = 40;
    //状态管理
    public static boolean isPause = false;
    public static boolean isFirstSelected = true;
    public static int clickedRow;
    public static int clickedCol;
    //消除的图片数
    public static int count = 0;
    //存储连接判断经过顶点路径
    public static ArrayList<Integer> path = new ArrayList<>();
    //存储路径的拐点数
    public static int cornerNum;
    //倒计时器
    public static GameTimer gameTimer;
    //游戏难度时间
    public static int levelTime = 300;
    //标记是否为休闲模式
    public static boolean isRelax = false;

    //加载游戏地图所需图片
    public static void loadImages() {
        images.clear();
        for (int i = 0; i < imgNums; i++) {
            if (currentBgImage.equals("images/bg_fruit.png")) {
                images.add(Toolkit.getDefaultToolkit().getImage("images/" + (i + 1) + ".png"));
            } else if (currentBgImage.equals("images/bg_cxk.png")) {
                images.add(Toolkit.getDefaultToolkit().getImage("images/" + (i + 17) + ".png"));
            } else if (currentBgImage.equals("images/bg_mh.png")) {
                images.add(Toolkit.getDefaultToolkit().getImage("images/" + (i + 33) + ".png"));
            }
        }
    }

    //是否可消子判断
    public static boolean canConnect(int row1, int col1, int row2, int col2, boolean[][] matrix, int[] vexArray) {
        //刷新路径和拐点数
        path.clear();
        cornerNum = 0;

        //边界情况
        if ((row1 == 0 && row2 == 0) || (col1 == 0 && col2 == 0)
                || (row1 == useRows - 1 && row2 == useRows - 1)
                || (col1 == useCols - 1 && col2 == useCols - 1)) {
            return true;
        }

        int imgIndex1 = row1 * useCols + col1;
        int imgIndex2 = row2 * useCols + col2;
        path.add(imgIndex1);
        if (isCorner()) cornerNum++;
        if (findPath(imgIndex1, imgIndex2, matrix, vexArray)) {
            path.add(imgIndex2);
            if (isCorner()) cornerNum++;
            return true;
        }
        if (isCorner()) cornerNum--;
        path.removeLast();
        return false;
    }

    private static boolean isCorner() {
        int len = path.size();
        if (len > 2) {
            return (path.get(len - 1) + path.get(len - 3)) / 2 != path.get(len - 2);
        }
        return false;
    }

    private static boolean isExistPath(int imgIndex) {
        for (int vex : path) {
            if (vex == imgIndex) return true;
        }
        return false;
    }

    private static boolean findPath(int imgIndex1, int imgIndex2, boolean[][] matrix, int[] vexArray) {
        for (int i = 0; i < useRows * useCols; i++) {
            if (matrix[imgIndex1][i] && !isExistPath(i)) {
                path.add(i);
                if (isCorner()) cornerNum++;
                //拐点数大于2，查找失败
                if (cornerNum > 2) {
                    //回溯
                    if (isCorner()) cornerNum--;
                    path.removeLast();
                    continue;
                }
                //未达到目标顶点imgIndex2时，继续搜索
                if (i != imgIndex2) {
                    //两图路径之间有顶点不为空，则路径不通
                    if (vexArray[i] != -1) {
                        //回溯
                        if (isCorner()) cornerNum--;
                        path.removeLast();
                        continue;
                    }
                    if (findPath(i, imgIndex2, matrix, vexArray)) {
                        return true;
                    }
                } else {
                    return true;
                }
                //回溯
                if (isCorner()) cornerNum--;
                path.removeLast();
            }
        }
        return false;
    }

    //绘制选中框
    public static void drawSelected(int row, int col, Graphics g) {
        //点击地图外不绘制
        if (row < 0 || row >= 10 || col < 0 || col >= 16) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(222, 22, 58));
        g.drawRect(BEGIN_X + col * IMAGE_WIDTH + 2, BEGIN_Y + row * IMAGE_HEIGHT + 2, IMAGE_WIDTH - 5, IMAGE_HEIGHT - 5);
    }

    //清除选中框
    public static void clearSelected(int row, int col, JPanel panel) {
        panel.repaint(BEGIN_X + col * IMAGE_WIDTH, BEGIN_Y + row * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    //画线消子
    public static void drawLinkAndDeleteImage(int row1, int col1, int row2, int col2, JPanel panel, int[] vexArray, boolean[][] matrix) {
        Graphics g = panel.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(98, 213, 21));

        //起点与终点的坐标
        int xBegin = col1 * IMAGE_WIDTH + BEGIN_X + IMAGE_WIDTH / 2;
        int yBegin = row1 * IMAGE_HEIGHT + BEGIN_Y + IMAGE_HEIGHT / 2;
        int xEnd = col2 * IMAGE_WIDTH + BEGIN_X + IMAGE_WIDTH / 2;
        int yEnd = row2 * IMAGE_HEIGHT + BEGIN_Y + IMAGE_HEIGHT / 2;

        //边界情况
        if (row1 == 0 && row2 == 0) {
            g2.drawLine(xBegin, yBegin, xBegin, yBegin - IMAGE_HEIGHT);
            g2.drawLine(xBegin, yBegin - IMAGE_HEIGHT, xEnd, yEnd - IMAGE_HEIGHT);
            g2.drawLine(xEnd, yEnd - IMAGE_HEIGHT, xEnd, yEnd);
        } else if (col1 == 0 && col2 == 0) {
            g2.drawLine(xBegin, yBegin, xBegin - IMAGE_WIDTH, yBegin);
            g2.drawLine(xBegin - IMAGE_WIDTH, yBegin, xEnd - IMAGE_WIDTH, yEnd);
            g2.drawLine(xEnd - IMAGE_WIDTH, yEnd, xEnd, yEnd);
        } else if (row1 == useRows - 1 && row2 == useRows - 1) {
            g2.drawLine(xBegin, yBegin, xBegin, yBegin + IMAGE_HEIGHT);
            g2.drawLine(xBegin, yBegin + IMAGE_HEIGHT, xEnd, yEnd + IMAGE_HEIGHT);
            g2.drawLine(xEnd, yEnd + IMAGE_HEIGHT, xEnd, yEnd);
        } else if (col1 == useCols - 1 && col2 == useCols - 1) {
            g2.drawLine(xBegin, yBegin, xBegin + IMAGE_WIDTH, yBegin);
            g2.drawLine(xBegin + IMAGE_WIDTH, yBegin, xEnd + IMAGE_WIDTH, yEnd);
            g2.drawLine(xEnd + IMAGE_WIDTH, yEnd, xEnd, yEnd);
        } else {
            for (int i = 0; i < path.size() - 1; i++) {
                int r1 = path.get(i) / useCols;
                int c1 = path.get(i) - r1 * useCols;
                int r2 = path.get(i + 1) / useCols;
                int c2 = path.get(i + 1) - r2 * useCols;
                int x1 = c1 * IMAGE_WIDTH + BEGIN_X + IMAGE_WIDTH / 2;
                int y1 = r1 * IMAGE_HEIGHT + BEGIN_Y + IMAGE_HEIGHT / 2;
                int x2 = c2 * IMAGE_WIDTH + BEGIN_X + IMAGE_WIDTH / 2;
                int y2 = r2 * IMAGE_HEIGHT + BEGIN_Y + IMAGE_HEIGHT / 2;
                g2.drawLine(x1, y1, x2, y2);
            }
        }
        //消除图片数+2
        count += 2;
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //擦除连线
        panel.repaint();
        //消除图片对
        vexArray[row1 * useCols + col1] = -1;
        vexArray[row2 * useCols + col2] = -1;
        //修改连通状态
        updateMatrix(row1, col1, matrix, vexArray);
        updateMatrix(row2, col2, matrix, vexArray);
        isWin(panel);
    }

    //更新邻接矩阵（连通状态）
    public static void updateMatrix(int i, int j, boolean[][] matrix, int[] vexArray) {
        int imgIndex1 = i * useCols + j;
        //判断左边
        if (j > 0) {
            int imgIndex2 = imgIndex1 - 1;
            int imgId1 = vexArray[imgIndex1];
            int imgId2 = vexArray[imgIndex2];
            if (imgId1 == imgId2 || imgId1 == -1 || imgId2 == -1) {
                matrix[imgIndex1][imgIndex2] = true;
                matrix[imgIndex2][imgIndex1] = true;
            }
        }
        //判断右边
        if (j < useCols - 1) {
            int imgIndex2 = imgIndex1 + 1;
            int imgId1 = vexArray[imgIndex1];
            int imgId2 = vexArray[imgIndex2];
            if (imgId1 == imgId2 || imgId1 == -1 || imgId2 == -1) {
                matrix[imgIndex1][imgIndex2] = true;
                matrix[imgIndex2][imgIndex1] = true;
            }
        }
        //判断上边
        if (i > 0) {
            int imgIndex2 = imgIndex1 - useCols;
            int imgId1 = vexArray[imgIndex1];
            int imgId2 = vexArray[imgIndex2];
            if (imgId1 == imgId2 || imgId1 == -1 || imgId2 == -1) {
                matrix[imgIndex1][imgIndex2] = true;
                matrix[imgIndex2][imgIndex1] = true;
            }
        }
        //判断下边
        if (i < useRows - 1) {
            int imgIndex2 = imgIndex1 + useCols;
            int imgId1 = vexArray[imgIndex1];
            int imgId2 = vexArray[imgIndex2];
            if (imgId1 == imgId2 || imgId1 == -1 || imgId2 == -1) {
                matrix[imgIndex1][imgIndex2] = true;
                matrix[imgIndex2][imgIndex1] = true;
            }
        }
    }

    //win判断
    public static void isWin(JPanel panel) {
        if (count == useRows * useCols) {
            if (!isRelax) {
                gameTimer.getTimer().stop();
            }
            JOptionPane.showMessageDialog(panel, "win！win！win！", "提示信息",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}