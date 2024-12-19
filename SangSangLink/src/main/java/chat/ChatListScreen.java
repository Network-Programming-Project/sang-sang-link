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
    private User user;

    public ChatListScreen(User user) {
        this.user = user;
        // 패널 기본 설정
        setLayout(null);
        setBounds(60, 0, 320, 500);
        setVisible(false);

        initialization();
    }

    private void initialization() {
        // 채팅방 목록을 담을 패널 (스크롤 가능)
        roomListPanel = new JPanel();
        roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
        roomListPanel.setBackground(Color.WHITE); // 배경 화이트

        scrollPane = new JScrollPane(roomListPanel);
        scrollPane.setBounds(10, 10, 300, 380);
        scrollPane.setBorder(null); // 스크롤팬 테두리 제거
        add(scrollPane);

        // ChatRoomDB로부터 채팅방 목록 가져오기
        List<ChatRoom> chatRooms = ChatRoomDB.getChatRoomsByUserId(Session.getUser().getId());
        System.out.println("chatRoomList 채팅방 목록 가져오기 사용자 id"+Session.getUser().getId());

        System.out.println("chatRoomList 개인 사용자 채팅방"+chatRooms);
        System.out.println("chatRoomList 모든 사용자 정보 가져오기: "+ UserDB.users);

        // 채팅방 패널 생성 및 추가
        for (ChatRoom room : chatRooms) {
            JPanel roomItem = createRoomItem(room);
            roomListPanel.add(roomItem);
        }

        // layout 재정렬
        roomListPanel.revalidate();
        roomListPanel.repaint();
    }

    private JPanel createRoomItem(ChatRoom room) {
        JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new BorderLayout());
        roomPanel.setOpaque(true);
        roomPanel.setBackground(Color.WHITE); // 배경 흰색

        // 패널 사이즈 고정
        Dimension fixedSize = new Dimension(280, 50);
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

        // 구분선 대신 공간 주기 위해 빈 패널을 아래에 추가할 수도 있음
        // roomListPanel에 setBoxLayout을 썼으니 각 패널 사이 간격 부여 가능
        return roomPanel;
    }

    // 메인 스크린에 요청하는 콜백
    private void showChatScreen(ChatRoom room, User user) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame instanceof MainScreen mainScreen) {
            mainScreen.showChatScreen(room, user);
        }
    }

}



