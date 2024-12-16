package main;

import session.Session;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private JLabel lblUserName;
    private JLabel lblConnectTime;

    public ProfilePanel(String connectTime) {
        setLayout(null);

        JLabel lblTitle = new JLabel("나의 프로필");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBounds(20, 10, 150, 30);
        add(lblTitle);

        JLabel lblAvatar = new JLabel();
        lblAvatar.setIcon(new ImageIcon(getClass().getResource("/static/images/bugi.jpeg"))); // 아바타 이미지
        lblAvatar.setBounds(20, 50, 50, 50);
        add(lblAvatar);

        lblUserName = new JLabel("사용자: " + Session.user.getUserName());
        lblUserName.setBounds(80, 60, 200, 30);
        add(lblUserName);

        lblConnectTime = new JLabel("접속시간: " + connectTime);
        lblConnectTime.setBounds(80, 90, 200, 30);
        add(lblConnectTime);
    }

    public void setUserName(String userName) {
        lblUserName.setText("사용자: " + userName);
    }

    public void setConnectTime(String connectTime) {
        lblConnectTime.setText("접속시간: " + connectTime);
    }
}
