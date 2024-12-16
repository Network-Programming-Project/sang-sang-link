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

    public SideMenu(){
        initialization();
    }

    private void initialization(){
        setBackground(Color.LIGHT_GRAY);
        setBounds(0, 0, 50, 500);
        setLayout(new GridLayout(2, 1, 0, 10));

        // 프로필 아이콘
        lblProfile = new JLabel(new ImageIcon(getClass().getResource("/static/images/profile.jpeg")));
        lblProfile.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(lblProfile);

        // 채팅 아이콘
        lblChat = new JLabel(new ImageIcon(getClass().getResource("/static/images/chat.jpeg")));
        lblChat.setHorizontalAlignment(SwingConstants.CENTER);
        lblChat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(lblChat);

        // 채팅 추가 아이콘
        // TODO 아이콘 수정 및 메뉴 배치 수정 필요
        lblChatAdd = new JLabel(new ImageIcon(getClass().getResource("/static/images/chat.jpeg")));
        lblChatAdd.setHorizontalAlignment(SwingConstants.CENTER);
        lblChatAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(lblChatAdd);
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

    // 채팅 추가 리스너 설정
    public void setChatAddClickListener(Consumer<MouseEvent> onClick) {
        lblChatAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.accept(e);
            }
        });
    }
}
