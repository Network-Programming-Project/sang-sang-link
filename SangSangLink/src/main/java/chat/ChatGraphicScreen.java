package chat;

import com.google.gson.Gson;
import db.ChatMessageDB;
import db.ChatRoomDB;
import model.ChatRoom;
import model.ChatRoomMessage;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatGraphicScreen extends JPanel {
    private JPanel messagePanel;
    private JTextField txtInput;
    private JButton btnSend;
    private JScrollPane scrollPane;
    private String ipAddr;
    private String portNo;
    private User user;
    private ChatRoom chatRoom;
    private Gson gson = new Gson();

    // 소켓
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;


    // TODO 채팅 창 나오면 이전에 있던 대화내용도 그려야함.
    public ChatGraphicScreen(ChatRoom chatRoom, User user) {

        this.user=user;
        this.ipAddr = "127.0.0.1";
        // TODO 포트넘버 로직 추가 예정 고정으로 가야할듯
        this.portNo = "50001";
        this.chatRoom=chatRoom;

        setLayout(null);
        setBounds(60, 0, 320, 500);
        setVisible(false); // 기본적으로 숨김 상태

        // 컴포넌트 초기화
        initializeComponents();
    }

    private void initializeComponents() {
        // 메시지 표시 패널
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));


        // 스크롤 패널
        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBounds(10, 10, 300, 380);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        // 입력 필드
        txtInput = new JTextField();
        txtInput.setBounds(10, 400, 230, 40);
        add(txtInput);

        // 전송 버튼
        btnSend = new JButton("보내기");
        btnSend.setBounds(250, 400, 60, 40);
        add(btnSend);


        // 전송 버튼 액션
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = txtInput.getText();
                sendMessage(user.getId(), chatRoom.getId(), msg);
                txtInput.setText("");
            }
        });

        // Enter 키로 메시지 전송
        txtInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = txtInput.getText();
                sendMessage(user.getId(), chatRoom.getId(), msg);
                txtInput.setText("");
                System.out.println("ChatScreen 메시지 전송 액션 리스너 유저 객체 확인: "+ user);
            }
        });

        // 채팅 기록 가져오기
        loadMessages();

        // 소켓 연결
        try {
            socket = new Socket(ipAddr, Integer.parseInt(portNo));
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            // 첫 메시지
            sendMessage(user.getId(), chatRoom.getId(), "입장");

            ListenNetwork net = new ListenNetwork();
            net.start();

        } catch (IOException e) {

        }
    }

    // JSON 메시지 생성
    private String createJsonMessage(Long userId, Long chatRoomId, String message) {
        JsonMessage jsonMessage = new JsonMessage(userId, chatRoomId, message);
        return gson.toJson(jsonMessage);
    }

    // 문자 전송
    private void sendMessage(Long userId, Long chatRoomId, String message) {
        try {
            dos.writeUTF(createJsonMessage(userId, chatRoomId, message));
        } catch (IOException e) {

        }
    }

    // 채팅 기록 가져오기
    private void loadMessages() {
        List<ChatRoomMessage> messages = ChatMessageDB.getMessages(chatRoom.getId());

        for (ChatRoomMessage message : messages) {
            addMessageBubble(message);
        }
    }

    private void addMessageBubble(ChatRoomMessage message) {
        SwingUtilities.invokeLater(() -> {
            JPanel bubble = new JPanel();
            bubble.setLayout(new BorderLayout());
            bubble.setBorder(new EmptyBorder(5, 10, 5, 10));

            JLabel messageLabel = new JLabel("<html><p style=\"width: 150px;\">" + message.getContent() + "</p></html>");
            messageLabel.setOpaque(true);
            messageLabel.setBackground(message.getUserId().equals(user.getId()) ? new Color(255, 255, 204) : Color.WHITE);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            messageLabel.setHorizontalAlignment(SwingConstants.LEFT);

            messageLabel.setPreferredSize(new Dimension(150, 50));
            messageLabel.setMaximumSize(new Dimension(150, 50));

            JLabel timeLabel = new JLabel("");
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            if (message.getUserId().equals(user.getId())) {
                bubble.add(messageLabel, BorderLayout.EAST);
                bubble.add(timeLabel, BorderLayout.WEST);
            } else {
                bubble.add(messageLabel, BorderLayout.WEST);
                bubble.add(timeLabel, BorderLayout.EAST);
            }

            bubble.setPreferredSize(new Dimension(scrollPane.getWidth()-20, 60));
            bubble.setMaximumSize(new Dimension(scrollPane.getWidth()-20, 60));

            messagePanel.add(bubble);
            messagePanel.add(Box.createVerticalStrut(10));

            revalidate();
            repaint();

            // 레이아웃 갱신 후 스크롤바를 하단으로 이동
            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        });
    }

    static class JsonMessage {
        private Long chatRoomId;
        private Long userId;
        private String message;

        public JsonMessage(Long userId, Long chatRoomId, String message) {
            this.chatRoomId = chatRoomId;
            this.userId = userId;
            this.message = message;
        }
    }

    class ListenNetwork extends Thread {
        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    System.out.println("수신된 메시지: " + msg);

                    // 수신한 메시지 JSON 파싱
                    JsonMessage received = gson.fromJson(msg, JsonMessage.class);

                    if(received.message.equals("/stop")){
                        break;
                    }
                    // ChatRoomMessage 생성
                    ChatRoomMessage chatRoomMessage = new ChatRoomMessage(
                            null,
                            received.message,
                            LocalDateTime.now(),
                            received.userId,
                            received.chatRoomId
                    );
                    ChatMessageDB.insert(chatRoomMessage);

                    // UI 업데이트는 EDT에서 처리
                    SwingUtilities.invokeLater(() -> addMessageBubble(chatRoomMessage));

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
