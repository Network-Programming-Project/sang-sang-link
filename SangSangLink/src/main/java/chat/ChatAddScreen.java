package chat;

import db.ChatRoomDB;
import model.ChatRoom;
import model.User;
import session.Session;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChatAddScreen extends JPanel {
    private JLabel titleLabel;
    private JTextField roomNameField;
    private JList<String> friendList;
    private JScrollPane friendScrollPane;
    private JButton createButton; // 채팅방 생성 버튼(추후 로직 연결 가능)

    public ChatAddScreen() {
        setLayout(null);
        setBackground(new Color(100, 175, 250));

        // 상단 제목 라벨
        titleLabel = new JLabel("채팅방 만들기", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(0, 10, 350, 30);
        add(titleLabel);

        // 채팅방 제목 입력 필드
        roomNameField = new JTextField();
        roomNameField.setBounds(10, 50, 330, 30);
        add(roomNameField);

        // 초대할 친구 리스트
        User user = Session.getUser();
        List<User> friends = user.getFriends();
        List<String> friendsNames=new ArrayList<>();

        for(User friend : friends) {
            friendsNames.add(friend.getUserName());
        }

        friendList = new JList<>(friendsNames.toArray(new String[0]));
        friendScrollPane = new JScrollPane(friendList);
        friendScrollPane.setBounds(10, 90, 330, 300);
        add(friendScrollPane);

        // 채팅방 생성 버튼 (아직 로직은 없음)
        createButton = new JButton("생성");
        createButton.setBounds(10, 400, 330, 40);
        // 추후 addActionListener를 추가하여 생성 로직 연동 가능
        add(createButton);

        // TODO 채팅방 추가할 때 ChatListScreen 다시 그리기 추가
        // TODO 다시 그릴 때 initialization 을 static 으로 수정 후 구현 try
        createButton.addActionListener(e -> {
            ChatRoom chatRoom=ChatRoom.builder()
                    .users(null)
                    .title(roomNameField.getText())
                    .build()
                    ;

            ChatRoomDB.insert(chatRoom);
            System.out.println(chatRoom+ "채팅방이 생성되었습니다.");
        });
    }

    public String getRoomName() {
        return roomNameField.getText().trim();
    }

    public List<String> getSelectedFriends() {
        return friendList.getSelectedValuesList();
    }


}

