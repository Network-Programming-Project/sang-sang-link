package main;

import session.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfilePanel extends JPanel {
    private JLabel lblUserName;
    private JLabel lblConnectTime;
    private JLabel lblAvatar;

    public ProfilePanel(String connectTime) {
        setLayout(null);
        setBackground(new Color(100, 175, 250));  // 전체 배경 색상 설정

        JLabel lblTitle = new JLabel("나의 프로필");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBounds(20, 10, 150, 30);
        add(lblTitle);

        lblAvatar = new JLabel();
        lblAvatar.setIcon(new ImageIcon(getClass().getResource("/static/images/bugi.jpeg"))); // 아바타 이미지
        lblAvatar.setBounds(20, 50, 50, 50);
        lblAvatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(lblAvatar);

        // 아바타 클릭 이벤트 추가
        lblAvatar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeAvatar();
            }
        });

        lblUserName = new JLabel("사용자: " + Session.getUser().getUserName());
        lblUserName.setBounds(80, 60, 200, 30);
        add(lblUserName);

        lblConnectTime = new JLabel("접속시간: " + connectTime);
        lblConnectTime.setBounds(80, 90, 200, 30);
        add(lblConnectTime);
    }

    // 아바타 이미지를 변경하는 메서드
    private void changeAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("새 아바타 선택");
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                // 선택한 파일의 경로에서 이미지를 로드
                String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
                lblAvatar.setIcon(new ImageIcon(imagePath));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "이미지를 로드할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setUserName(String userName) {
        lblUserName.setText("사용자: " + userName);
    }

    public void setConnectTime(String connectTime) {
        lblConnectTime.setText("접속시간: " + connectTime);
    }
}
