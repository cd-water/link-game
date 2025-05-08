package com.cdwater.component;

import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;

public class GameTimer {
    private final JLabel timeLabel = new JLabel();
    private Timer timer;
    private JProgressBar progressBar;
    private int canLoseTime;

    public GameTimer(int canLoseTime, JPanel panel) {
        this.canLoseTime = canLoseTime;
        int minutes = canLoseTime / 60;
        int seconds = canLoseTime % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
        initTimer(panel);

        progressBar = new JProgressBar(0, canLoseTime);
        progressBar.setValue(canLoseTime);
    }

    private void initTimer(JPanel panel) {
        timeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        timeLabel.setForeground(Color.RED);
        timer = new Timer(1000, e -> {
            canLoseTime--;
            progressBar.setValue(canLoseTime);
            int minutes = canLoseTime / 60;
            int seconds = canLoseTime % 60;
            timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
            if (canLoseTime <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(panel, "lose！lose！lose！", "提示信息",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public int getCanLoseTime() {
        return canLoseTime;
    }

    public void setCanLoseTime(int canLoseTime) {
        this.canLoseTime = canLoseTime;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}