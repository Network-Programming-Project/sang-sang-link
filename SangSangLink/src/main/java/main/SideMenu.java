package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class SideMenu extends JPanel {
    private JLabel lblProfile;
    private JLabel lblChat;
    private JLabel lblChatAdd;

    public SideMenu() {
        initialization();
    }

    private void initialization() {
        setBackground(Color.DARK_GRAY);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 공통 설정
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 50, 0); // 상하 여백을 줄임

        // 프로필 아이콘
        lblProfile = createIconLabel("/static/images/profile.jpeg");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblProfile, gbc);

        // 채팅 아이콘
        lblChat = createIconLabel("/static/images/chat.jpeg");
        gbc.gridy = 1;
        add(lblChat, gbc);

        // 채팅 추가 아이콘
        lblChatAdd = createIconLabel("/static/images/chat.jpeg");
        gbc.gridy = 2;
        add(lblChatAdd, gbc);

        // 빈 공간 추가 (중간 점 아이콘처럼 보이게)
        JLabel lblDots = new JLabel("...");
        lblDots.setForeground(Color.LIGHT_GRAY);
        lblDots.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        add(lblDots, gbc);
    }

    // 공통 아이콘 생성 메서드
    private JLabel createIconLabel(String imagePath) {
        // 이미지 아이콘 생성
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));

        // 이미지 크기 조정 (원하는 크기로 설정)
        Image img = originalIcon.getImage();
        Image resizedImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // 예: 50x50 크기로 변경
        ImageIcon resizedIcon = new ImageIcon(resizedImg);

        // 이미지 아이콘을 JLabel에 설정
        JLabel label = new JLabel(resizedIcon);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }

    // 프로필 클릭 리스너 설정
    public void setProfileClickListener(Consumer<MouseEvent> onClick) {
        lblProfile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.accept(e);
            }
        });
    }

    // 채팅 클릭 리스너 설정
    public void setChatClickListener(Consumer<MouseEvent> onClick) {
        lblChat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.accept(e);
            }
        });
    }

    // 채팅 추가 클릭 리스너 설정
    public void setChatAddClickListener(Consumer<MouseEvent> onClick) {
        lblChatAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.accept(e);
            }
        });
    }
}
