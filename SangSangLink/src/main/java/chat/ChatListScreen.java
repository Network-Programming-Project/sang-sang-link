package chat;

import db.ChatRoomDB;
import db.UserDB;
import main.MainScreen;
import model.ChatRoom;
import model.User;
import session.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ChatListScreen extends JPanel {
    private JScrollPane scrollPane;
    private JPanel roomListPanel; // 실제 채팅방 목록이 들어갈 패널
    private JLabel roomListLabel;
    private User user;

    public ChatListScreen(User user) {
        this.user = user;
        // 패널 기본 설정
        setLayout(null);
        setVisible(false);
        setBackground(new Color(100, 175, 250));

        initialization();
    }

    public void initialization() {
        System.out.println("Initializing ChatListScreen");

        roomListLabel = new JLabel("채팅방 리스트", SwingConstants.CENTER);
        roomListLabel.setFont(new Font("Arial", Font.BOLD, 18));
        roomListLabel.setBounds(0,10, 350, 30);
        add(roomListLabel);
        // 채팅방 목록을 담을 패널 (스크롤 가능)
        roomListPanel = new JPanel();
        roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
        roomListPanel.setBackground(new Color(100, 175, 250)); // 배경

        // + 아이콘 버튼 (아바타 옆에 추가)
        JButton btnAdd = new JButton("+");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 16));
        btnAdd.setBounds(250, 350, 45, 45);
        btnAdd.setFocusPainted(false);
        btnAdd.setBackground(new Color(255, 255, 255));
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> {
            showChatAddScreen();
        });
        add(btnAdd);

        scrollPane = new JScrollPane(roomListPanel);
        scrollPane.setBounds(0, 60, 350, 380);
        scrollPane.setBorder(null); // 스크롤팬 테두리 제거
        add(scrollPane);

        // ChatRoomDB로부터 채팅방 목록 가져오기
        List<ChatRoom> chatRooms = ChatRoomDB.getChatRoomsByUserId(user.getId());
        System.out.println("ChatListScreen 채팅방 목록 가져오기"+chatRooms);

        // 채팅방 패널 생성 및 추가
        for (ChatRoom room : chatRooms) {
            JPanel roomItem = createRoomItem(room);
            roomListPanel.add(roomItem);
        }

        // layout 재정렬
        roomListPanel.revalidate();
        roomListPanel.repaint();
    }

    public JPanel createRoomItem(ChatRoom room) {
        JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new BorderLayout());
        roomPanel.setOpaque(true);
        roomPanel.setBackground(Color.WHITE); // 배경 흰색

        // 패널 사이즈 고정
        Dimension fixedSize = new Dimension(350, 50);
        roomPanel.setPreferredSize(fixedSize);
        roomPanel.setMaximumSize(fixedSize);
        roomPanel.setMinimumSize(fixedSize);

        // 채팅방 제목 라벨
        JLabel roomLabel = new JLabel(room.getTitle());
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roomLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        roomPanel.add(roomLabel, BorderLayout.CENTER);

        // 마우스 오버 시 약간 배경색 변경 (Hover 효과)
        roomPanel.addMouseListener(new MouseAdapter() {
            Color originalColor = roomPanel.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                roomPanel.setBackground(new Color(230, 230, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                roomPanel.setBackground(originalColor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // 채팅방 클릭 시 ChatScreen으로 전환
                showChatScreen(room, user);
            }
        });

        roomListPanel.revalidate();
        roomListPanel.repaint();

        scrollPane.revalidate();
        scrollPane.repaint();

        return roomPanel;
    }

    // 메인 스크린에 요청하는 콜백
    private void showChatScreen(ChatRoom room, User user) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame instanceof MainScreen mainScreen) {
            mainScreen.showChatScreen(room, user);
        }
    }

    private void showChatAddScreen(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame instanceof MainScreen mainScreen) {
            mainScreen.showChatAddScreen();
        }
    }
}



