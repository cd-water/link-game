package com.cdwater.common;

import static com.cdwater.utils.LLKUtil.useCols;
import static com.cdwater.utils.LLKUtil.useRows;

public class GameGraph {
    private int[] vexArray = new int[useRows * useCols];
    private boolean[][] matrix = new boolean[useRows * useCols][useRows * useCols];

    public GameGraph() {
        initGraph();
    }

    private void initGraph() {
        for (int i = 0; i < useRows * useCols; i++) {
            vexArray[i] = -1;
        }

        for (int i = 0; i < useRows * useCols; i++) {
            for (int j = 0; j < useRows * useCols; j++) {
                matrix[i][j] = false;
            }
        }
    }

    public boolean[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(boolean[][] matrix) {
        this.matrix = matrix;
    }

    public int[] getVexArray() {
        return vexArray;
    }

    public void setVexArray(int[] vexArray) {
        this.vexArray = vexArray;
    }
}