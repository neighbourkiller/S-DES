package UI;

import ProcessKeys.BruteForceKeyCracker;
import ProcessKeys.CodeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class S_DESWindow extends JFrame {
    private JTextField inputField;  // 明文
    private JTextField inputKeys;  //密钥
    private JTextField displayField;//密文
    private JTextField displayTime;//显示暴力破解时间
    private String msg = new String();
    private int op = 0;//操作 1:加密 2：解密 3:暴力破解

    public S_DESWindow() {
        // 设置窗体标题
        setTitle("S-DES");
        // 设置窗体大小
        setSize(420, 300);
        setLayout(new GridLayout(4, 3));
        // 设置窗体关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 创建输入框
        inputField = new JTextField(10);
        inputKeys = new JTextField(10);
        displayField = new JTextField(15);

        displayTime = new JTextField(15);
        displayTime.setEditable(false);
        // 添加键盘输入监听器
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                op = 1;
                TestInput(inputField.getText(), inputKeys.getText());
            }
        });
        JButton btnok = new JButton("加密");
        JButton btnc = new JButton("清除");
        JButton btnUn = new JButton("解密");
        JButton btnBrute = new JButton("暴力破解");
        btnok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                op = 1;
                TestInput(inputField.getText(), inputKeys.getText());
            }
        });
        btnc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputField.setText("");
                inputKeys.setText("");
                displayField.setText("");
            }
        });
        btnUn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                op = 2;
                TestInput(displayField.getText(), inputKeys.getText());
            }
        });
        btnBrute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                op = 3;
                TestInput(inputField.getText(), displayField.getText());
            }
        });
        JLabel l1 = new JLabel("明文");
        JLabel l2 = new JLabel("密钥");
        JLabel l3 = new JLabel("密文");

        // 添加到窗体
        add(l1);
        add(inputField);
        add(btnok);
        add(l2);
        add(inputKeys);
        add(btnBrute);
        add(l3);
        add(displayField);
        add(btnUn);
        add(btnc);
        add(displayTime);

        setLocationRelativeTo(null);
        // 使窗体可见
        setVisible(true);
    }

/*    private Boolean testCode(String code, int op) {
        switch (op) {
            case 1:
                msg = "明文输入错误";
                break;
            case 2:
                msg = "密文输入错误";
                break;
            case 3:
                msg = "输入错误";
                break;
            default:
                break;
        }
        return code.length() != 8 || !code.matches("[01]+");
    }*/

    //只用于明文的检验
    private static String testCode(String code) {
        int asciiValue = code.charAt(0);
        // 检查字符是否在 ASCII 符号的范围内
        if (!(asciiValue >= 32 && asciiValue <= 126))
            return "false"; //不是符号，返回 false
        return "0"+Integer.toBinaryString(asciiValue);
    }

    private void TestInput(String Code, String keys) {
        if (op == 1) {
            if (Code.length() == 1) {//长度为1，
                if (testCode(Code).equals("false"))
                    JOptionPane.showMessageDialog(null, "明文输入错误", "错误", JOptionPane.ERROR_MESSAGE);
                else
                    displayField.setText(CodeUtils.Encode(testCode(Code), keys));
            } else if (Code.length() != 8 || !Code.matches("[01]+")) {
//                inputField.setText("");
                JOptionPane.showMessageDialog(null, "明文输入错误", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (keys.length() != 10 || !keys.matches("[01]+")) {
//                inputKeys.setText("");
                JOptionPane.showMessageDialog(null, "密钥输入错误", "错误", JOptionPane.ERROR_MESSAGE);
            } else displayField.setText(CodeUtils.Encode(Code, keys));
        } else if (op == 2) {
            if (Code.length() != 8 || !Code.matches("[01]+")) {
//                inputField.setText("");
                JOptionPane.showMessageDialog(null, "密文输入错误", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (keys.length() != 10 || !keys.matches("[01]+")) {
//                inputKeys.setText("");
                JOptionPane.showMessageDialog(null, "密钥输入错误", "错误", JOptionPane.ERROR_MESSAGE);
            } else inputField.setText(CodeUtils.Uncode(Code, keys));
        } else if (op == 3) {
            if (Code.length() != keys.length())
                JOptionPane.showMessageDialog(null, "明文密文长度不等", "错误", JOptionPane.ERROR_MESSAGE);
            else if (Code.length() != 8 || !Code.matches("[01]+")) {
                inputField.setText("");
                JOptionPane.showMessageDialog(null, "明文输入错误", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (!keys.matches("[01]+")) {
                inputField.setText("");
                JOptionPane.showMessageDialog(null, "密文输入错误", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                Map<String, Long> map = null;
                try {
                    map = BruteForceKeyCracker.bruteForce(Code, keys);
                    if (map == null) {
                        displayTime.setText("破解失败");
                    } else
                        for (Map.Entry<String, Long> entry : map.entrySet()) {
                            inputKeys.setText(entry.getKey());
                            displayTime.setText("暴力破解耗时:" + entry.getValue() + "ms");
                        }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public static void main(String[] args) {
        String str = "a";
        System.out.println(testCode(str));
    }
}
