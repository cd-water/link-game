package com.cdwater.component;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

import static com.cdwater.utils.LLKUtil.*;

public class SettingDialog extends JDialog {
    public SettingDialog() {
        initDialog();
        showSettingDialog();
        setVisible(true);
    }

    private void initDialog() {
        setTitle("欢乐连连看 游戏设置");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(470, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setModal(true);
        //设置窗口图标
        ImageIcon icon = new ImageIcon("images/LLK.png");
        setIconImage(icon.getImage());
    }

    //设置背景图片
    private class SettingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bgImage = new ImageIcon("images/bg_setting.png");
            g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void showSettingDialog() {
        SettingPanel settingPanel = new SettingPanel();

        JLabel sizeSetting = new JLabel("地图大小设置");
        JLabel mapRow = new JLabel("地图行数:");
        JLabel mapCol = new JLabel("地图列数:");
        JLabel imgTypes = new JLabel("图片种类数:");
        //创建数字格式化器
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        //输入区间设置
        numberFormat.setMinimumIntegerDigits(1);
        numberFormat.setMaximumIntegerDigits(2);
        //输入类型设置
        NumberFormatter rowFormatter = new NumberFormatter(numberFormat);
        rowFormatter.setValueClass(Integer.class);
        NumberFormatter colFormatter = new NumberFormatter(numberFormat);
        colFormatter.setValueClass(Integer.class);
        NumberFormatter typeFormatter = new NumberFormatter(numberFormat);
        typeFormatter.setValueClass(Integer.class);
        //输入框设置
        JFormattedTextField mapRowInput = new JFormattedTextField(rowFormatter);
        JFormattedTextField mapColInput = new JFormattedTextField(colFormatter);
        JFormattedTextField imgTypesInput = new JFormattedTextField(typeFormatter);
        mapRowInput.setValue(useRows);
        mapColInput.setValue(useCols);
        imgTypesInput.setValue(imgNums);

        //主题信息
        JLabel themeSetting = new JLabel("主题设置");
        String[] themes = {"水果忍者", "怪物猎人", "唱跳rap篮球"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);
        themeComboBox.setSelectedIndex(0);

        JLabel soundSetting = new JLabel("背景音设置");

        JButton confirm = new JButton("确定");
        JButton cancel = new JButton("取消");

        //添加监听事件
        confirm.addActionListener(e -> {
            int row = ((Number) mapRowInput.getValue()).intValue();
            int col = ((Number) mapColInput.getValue()).intValue();
            int nums = ((Number) imgTypesInput.getValue()).intValue();
            if (row > 10) {
                JOptionPane.showMessageDialog(settingPanel, "行数不允许超过10行", "提示信息",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (col > 16) {
                JOptionPane.showMessageDialog(settingPanel, "列数不允许超过16列", "提示信息",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (nums > 16) {
                JOptionPane.showMessageDialog(settingPanel, "图片种类数不允许超过16种", "提示信息",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if ((row * col) % 2 != 0) {
                JOptionPane.showMessageDialog(settingPanel, "图片总数必须为偶数", "提示信息",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            useRows = row;
            useCols = col;
            imgNums = nums;

            String selectedTheme = (String) themeComboBox.getSelectedItem();
            if ("水果忍者".equals(selectedTheme)) {
                currentBgImage = "images/bg_fruit.png";
            } else if ("怪物猎人".equals(selectedTheme)) {
                currentBgImage = "images/bg_mh.png";
            } else if ("唱跳rap篮球".equals(selectedTheme)) {
                currentBgImage = "images/bg_cxk.png";
            }
            loadImages();
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        settingPanel.setLayout(null);
        //设置坐标
        sizeSetting.setBounds(90, 100, 100, 20);
        mapRow.setBounds(50, 150, 60, 20);
        mapCol.setBounds(50, 200, 60, 20);
        imgTypes.setBounds(50, 250, 80, 20);
        mapRowInput.setBounds(120, 150, 50, 20);
        mapColInput.setBounds(120, 200, 50, 20);
        imgTypesInput.setBounds(140, 250, 50, 20);
        themeSetting.setBounds(280, 100, 100, 20);
        themeComboBox.setBounds(280, 130, 100, 30);
        soundSetting.setBounds(280, 200, 100, 20);
        confirm.setBounds(90, 350, 60, 30);
        cancel.setBounds(320, 350, 60, 30);

        //添加组件
        settingPanel.add(sizeSetting);
        settingPanel.add(mapRow);
        settingPanel.add(mapCol);
        settingPanel.add(imgTypes);
        settingPanel.add(mapRowInput);
        settingPanel.add(mapColInput);
        settingPanel.add(imgTypesInput);
        settingPanel.add(themeSetting);
        settingPanel.add(themeComboBox);
        settingPanel.add(soundSetting);
        settingPanel.add(confirm);
        settingPanel.add(cancel);
        this.add(settingPanel);
    }
}