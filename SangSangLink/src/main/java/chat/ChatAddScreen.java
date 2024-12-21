package chat;

import db.ChatRoomDB;
import model.ChatRoom;
import model.User;
import session.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ChatAddScreen extends JPanel {
    private JLabel titleLabel, titleSetLabel, friendListLabel;
    private JTextField roomNameField;
    private JPanel friendsListPanel;
    private JScrollPane friendScrollPane;
    private JButton createButton; // 채팅방 생성 버튼(추후 로직 연결 가능)
    private List<User> checkFriends = new ArrayList<>();

    public ChatAddScreen(User user) {
        setLayout(null);
        setBackground(new Color(100, 175, 250));

        // 현재 사용자(나 자신)를 checkFriends에 추가
        checkFriends.add(user);

        // 상단 제목 라벨
        titleLabel = new JLabel("채팅방 만들기", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(0, 10, 350, 30);
        add(titleLabel);

        titleSetLabel = new JLabel("채팅방 제목", SwingConstants.LEFT);
        titleSetLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleSetLabel.setBounds(15, 45, 350, 30);
        add(titleSetLabel);

        // 채팅방 제목 입력 필드
        roomNameField = new JTextField();
        roomNameField.setBounds(10, 75, 330, 30);
        add(roomNameField);

        friendListLabel = new JLabel("친구 목록", SwingConstants.LEFT);
        friendListLabel.setFont(new Font("Arial", Font.BOLD, 14));
        friendListLabel.setBounds(15, 110, 350, 30);
        add(friendListLabel);

        // 친구 목록을 담을 패널 (스크롤 가능)
        friendsListPanel = new JPanel();
        friendsListPanel.setLayout(new BoxLayout(friendsListPanel, BoxLayout.Y_AXIS));
        friendsListPanel.setBackground(new Color(100, 175, 250)); // 배경

        friendScrollPane = new JScrollPane(friendsListPanel);
        friendScrollPane.setBounds(10, 145, 330, 250);
        friendScrollPane.setBorder(null);
        add(friendScrollPane);

        for(User friend: user.getFriends()) {
            JPanel friendItem = createFriendItem(friend);
            friendsListPanel.add(friendItem);
        }


        // 채팅방 생성 버튼
        createButton = new JButton("생성");
        createButton.setBounds(10, 400, 330, 40);
        createButton.addActionListener(e -> {
            // 채팅방 생성 로직
            if (checkFriends.isEmpty()) {
                JOptionPane.showMessageDialog(this, "친구를 선택해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ChatRoom chatRoom = ChatRoom.builder()
                    .users(new ArrayList<>(checkFriends)) // 선택된 친구들로 채팅방 생성
                    .title(roomNameField.getText())
                    .build();

            ChatRoomDB.insert(chatRoom);
            System.out.println(chatRoom + " 채팅방이 생성되었습니다.");
        });
        add(createButton);
    }

    private JPanel createFriendItem(User friend) {
        JPanel friendPanel = new JPanel();
        friendPanel.setLayout(new BorderLayout());
        friendPanel.setOpaque(true);
        friendPanel.setBackground(Color.WHITE); // 배경 흰색

        // 패널 사이즈 고정
        Dimension fixedSize = new Dimension(330, 50);
        friendPanel.setPreferredSize(fixedSize);
        friendPanel.setMaximumSize(fixedSize);
        friendPanel.setMinimumSize(fixedSize);

        // 채팅방 제목 라벨
        JLabel friendLabel = new JLabel(friend.getUserName());
        friendLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        friendLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        friendPanel.add(friendLabel, BorderLayout.CENTER);

        // 마우스 오버 시 약간 배경색 변경 (Hover 효과)
        friendLabel.addMouseListener(new MouseAdapter() {
            Color originalColor = friendLabel.getBackground();

            @Override
            public void mouseClicked(MouseEvent e) {
                if (friendPanel.getBackground().equals(Color.LIGHT_GRAY)) {
                    // 선택 해제
                    friendPanel.setBackground(Color.WHITE);
                    checkFriends.remove(friend); // 선택 해제 시 리스트에서 제거
                } else {
                    // 선택
                    friendPanel.setBackground(Color.LIGHT_GRAY);
                    checkFriends.add(friend); // 선택 시 리스트에 추가
                }
            }
        });

        // 구분선 대신 공간 주기 위해 빈 패널을 아래에 추가할 수도 있음
        // roomListPanel에 setBoxLayout을 썼으니 각 패널 사이 간격 부여 가능
        return friendPanel;
    }

    public String getRoomName() {
        return roomNameField.getText().trim();
    }

}

