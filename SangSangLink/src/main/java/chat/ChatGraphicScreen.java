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
        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.X_AXIS));
        bubble.setBorder(new EmptyBorder(5, 10, 5, 10));

        // 번역 아이콘 버튼
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/static/images/translate.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton translateButton = new JButton(scaledIcon);
        translateButton.setPreferredSize(new Dimension(20, 20));
        translateButton.setBorder(BorderFactory.createEmptyBorder());
        translateButton.setContentAreaFilled(false);

        translateButton.addActionListener(e -> {
            String translatedText = callTranslateAPI(message.getContent());
            JOptionPane.showMessageDialog(null, "Translated Text:\n" + translatedText);
        });

        // 텍스트 줄바꿈 기준
        int textWrapWidth = 150; // 텍스트 줄바꿈 기준을 더 작게 설정
        int maxWidth = 220;      // 메시지 박스의 최대 너비
        JLabel messageLabel = new JLabel("<html><p style=\"width: " + textWrapWidth + "px;\">" + message.getContent() + "</p></html>");
        messageLabel.setOpaque(true);
        messageLabel.setBackground(message.getUserId().equals(user.getId()) ? new Color(255, 255, 204) : Color.WHITE);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 메시지 길이에 따라 높이 계산
        FontMetrics metrics = messageLabel.getFontMetrics(messageLabel.getFont());
        int textWidth = metrics.stringWidth(message.getContent());
        int lineHeight = metrics.getHeight();
        int lineCount = (int) Math.ceil((double) textWidth / textWrapWidth); // 텍스트가 몇 줄인지 계산
        int bubbleHeight = lineHeight * lineCount + 10; // 줄 수에 따라 높이 조정

        // 메시지 박스 크기 계산 (비율 고정)
        double ratio = 0.7; // 텍스트 길이에 따른 메시지 박스 크기의 비율
        int bubbleWidth = Math.min(maxWidth, (int) (textWidth * ratio) + 50); // 비율로 크기 조정
        bubbleWidth = Math.max(20, bubbleWidth); // 최소 크기 100 유지

        // 메시지 레이블 크기 설정
        messageLabel.setPreferredSize(new Dimension(bubbleWidth, bubbleHeight));
        messageLabel.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        // 메시지와 아이콘 배치
        if (message.getUserId().equals(user.getId())) {
            // 내가 쓴 메시지: 아이콘이 왼쪽, 메시지가 오른쪽
            bubble.add(Box.createHorizontalGlue()); // 오른쪽 기준으로 정렬
            bubble.add(translateButton);            // 왼쪽에 아이콘
            bubble.add(Box.createRigidArea(new Dimension(5, 0)));
            bubble.add(messageLabel);               // 오른쪽에 메시지
        } else {
            // 상대방 메시지: 메시지가 왼쪽, 아이콘이 오른쪽
            bubble.add(messageLabel);               // 왼쪽에 메시지
            bubble.add(Box.createRigidArea(new Dimension(5, 0)));
            bubble.add(translateButton);            // 오른쪽에 아이콘
            bubble.add(Box.createHorizontalGlue()); // 왼쪽 기준으로 정렬
        }

        bubble.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagePanel.add(bubble);
        messagePanel.add(Box.createVerticalStrut(10));

        // 레이아웃 갱신
        revalidate();
        repaint();

        // 스크롤바를 가장 아래로 이동
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    // 번역 API 호출 메서드 (예시)
    private String callTranslateAPI(String text) {
        // 번역 API를 호출하는 로직 구현 (HTTP 요청 등)
        // 예시로 "번역된 텍스트" 반환
        return "번역된 텍스트";
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
                    System.out.println("chatRoomIncrement"+ ChatMessageDB.autoIncrement);
                    ChatMessageDB.insert(chatRoomMessage);
                    System.out.println("ChatScreen 클라이언트 insert 후"+chatRoomMessage);

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
