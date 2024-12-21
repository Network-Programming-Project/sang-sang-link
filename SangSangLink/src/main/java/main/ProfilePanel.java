package main;

import session.Session;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProfilePanel extends JPanel {
    private JLabel lblUserName;
    private JLabel lblAvatar;
    private JLabel lblStatusMessage;
    private String statusMessage;
    private JList<String> friendList;
    private JScrollPane friendScrollPane;
    private DefaultListModel<String> friendListModel;

    public ProfilePanel(String currentTime) {
        setLayout(null);
        setBackground(new Color(100, 175, 250));  // 전체 배경 색상 설정

        // 제목
        JLabel lblTitle = new JLabel("나의 프로필");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBounds(20, 10, 150, 30);
        add(lblTitle);

        // 아바타 이미지
        lblAvatar = new JLabel();
        lblAvatar.setIcon(new ImageIcon(getClass().getResource("/static/images/bugi.jpeg"))); // 아바타 이미지
        lblAvatar.setBounds(20, 50, 50, 50);
        lblAvatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblAvatar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changeAvatar();
            }
        });
        add(lblAvatar);

        // 사용자 이름
        lblUserName = new JLabel("사용자: " + Session.getUser().getUserName());
        lblUserName.setBounds(80, 60, 200, 30);
        add(lblUserName);

        // 상태 메시지
        statusMessage = Session.getUser().getStatusMessage();
        lblStatusMessage = new JLabel("상태 메시지: " + statusMessage);
        lblStatusMessage.setBounds(80, 90, 200, 30);
        lblStatusMessage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblStatusMessage.setForeground(Color.BLUE);
        lblStatusMessage.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changeStatusMessage();
            }
        });
        add(lblStatusMessage);

        // 친구 목록 제목
        JLabel lblFriendsTitle = new JLabel("친구 목록");
        lblFriendsTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblFriendsTitle.setBounds(20, 130, 150, 20);
        add(lblFriendsTitle);

        // 친구 목록 데이터 모델 초기화
        friendListModel = new DefaultListModel<>();
        friendList = new JList<>(friendListModel);
        friendList.setFont(new Font("Arial", Font.PLAIN, 12));
        friendScrollPane = new JScrollPane(friendList);
        friendScrollPane.setBounds(20, 160, 310, 280);
        add(friendScrollPane);

        // 초기 친구 목록 로드
        updateFriendList();
    }

    // 친구 목록을 동적으로 업데이트
    private void updateFriendList() {
        List<User> friends = Session.getUser().getFriends();
        friendListModel.clear();
        for (User friend : friends) {
            friendListModel.addElement(friend.getUserName() + " - " + friend.getStatusMessage());
        }
    }

    // 아바타 이미지를 변경하는 메서드
    private void changeAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("새 아바타 선택");
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
                lblAvatar.setIcon(new ImageIcon(imagePath));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "이미지를 로드할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 상태 메시지를 변경하는 메서드
    private void changeStatusMessage() {
        String newStatusMessage = JOptionPane.showInputDialog(this, "새 상태 메시지를 입력하세요:", "상태 메시지 변경", JOptionPane.PLAIN_MESSAGE);
        if (newStatusMessage != null && !newStatusMessage.trim().isEmpty()) {
            statusMessage = newStatusMessage;
            lblStatusMessage.setText("상태 메시지: " + statusMessage);
            Session.getUser().setStatusMessage(statusMessage); // 세션 업데이트

            // 친구 목록 갱신
            updateFriendList();
        }
    }
}
