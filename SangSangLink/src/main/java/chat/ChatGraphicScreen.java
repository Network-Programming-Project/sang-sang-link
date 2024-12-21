package chat;

import com.google.gson.Gson;
import db.ChatMessageDB;
import db.UserDB;
import model.ChatRoom;
import model.ChatRoomMessage;
import model.User;
import translation.TranslationResponse;
import translation.TranslationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatGraphicScreen extends JPanel {
    private JPanel messagePanel;
    private JTextField txtInput;
    private JButton btnSend;
    private JButton btnEmoticon;
    private JButton btnAttach;
    private JScrollPane scrollPane;
    private JLabel roomTitle;
    private String ipAddr;
    private String portNo;
    private User user;
    private ChatRoom chatRoom;
    private Gson gson = new Gson();

    // 이모티콘 패널
    private JDialog emoticonDialog;

    // 소켓
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private TranslationService translationService=TranslationService.getInstance();


    public ChatGraphicScreen(ChatRoom chatRoom, User user) {

        this.user=user;
        this.ipAddr = "127.0.0.1";
        // TODO 포트넘버 로직 추가 예정 고정으로 가야할듯
        this.portNo = "50001";
        this.chatRoom=chatRoom;

        setLayout(null);
        setVisible(false); // 기본적으로 숨김 상태

        // 컴포넌트 초기화
        initializeComponents();
    }

    private void initializeComponents() {
        // 메시지 표시 패널
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        roomTitle = new JLabel(chatRoom.getTitle()+"채팅방", SwingConstants.CENTER);
        roomTitle.setFont(new Font("Arial", Font.BOLD, 18));
        roomTitle.setBounds(0, 10, 350, 30);
        add(roomTitle);

        // 스크롤 패널
        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBounds(0, 50, 350, 370);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        btnEmoticon = new JButton("😊");
        btnEmoticon.setBounds(5, 420, 40, 40);
        add(btnEmoticon);

        btnAttach = new JButton("사진");
        btnAttach.setBounds(50, 420, 40, 40);
        add(btnAttach);

        // 입력 필드
        txtInput = new JTextField();
        txtInput.setBounds(100, 420, 190, 40);
        add(txtInput);

        // 전송 버튼
        btnSend = new JButton("보내기");
        btnSend.setBounds(290, 420, 60, 40);
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

        // 이모티콘 버튼 액션
        btnEmoticon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 이모티콘 다이얼로그 띄우기
                showEmoticonDialog();
            }
        });

        // 사진 버튼 액션
        btnAttach.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 사진 파일 선택
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif"));
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String imagePath = selectedFile.getAbsolutePath();
                    // 선택된 이미지를 전송 (파일 전송 로직 필요)
                    sendMessage(user.getId(), chatRoom.getId(), "사진: " + imagePath);
                }
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
        // 사용자 이름 가져오기
        String userName = UserDB.getUserById(message.getUserId()).getUserName();
        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 12)); // 사용자 이름 스타일 지정

        // 메시지 버블 생성
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

        // 텍스트 줄바꿈 기준
        int textWrapWidth = 150; // 텍스트 줄바꿈 기준
        int maxWidth = 220;      // 메시지 박스의 최대 너비
        final JLabel messageLabel = new JLabel("<html><p style=\"width: " + textWrapWidth + "px;\">" + message.getContent() + "</p></html>");
        messageLabel.setOpaque(true);
        messageLabel.setBackground(message.getUserId().equals(user.getId()) ? new Color(255, 255, 204) : Color.WHITE);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 메시지 길이에 따라 높이 계산
        FontMetrics metrics = messageLabel.getFontMetrics(messageLabel.getFont());
        int textWidth = metrics.stringWidth(message.getContent());
        int lineHeight = metrics.getHeight();
        int lineCount = (int) Math.ceil((double) textWidth / textWrapWidth); // 텍스트가 몇 줄인지 계산

        int bubbleHeight = lineHeight * lineCount + 12; // 줄 수에 따라 높이 조정

        // 메시지 박스 크기 계산 (비율 고정)
        double ratio = 0.7; // 텍스트 길이에 따른 메시지 박스 크기의 비율
        int bubbleWidth = Math.min(maxWidth, (int) (textWidth * ratio) + 50); // 비율로 크기 조정
        bubbleWidth = Math.max(20, bubbleWidth); // 최소 크기 100 유지

        System.out.println("bubbleWidth 길이: "+bubbleWidth);
        System.out.println("bubbleHeight 길이: "+bubbleHeight);

        // 메시지 레이블 크기 설정
        messageLabel.setPreferredSize(new Dimension(bubbleWidth, bubbleHeight));
        messageLabel.setMaximumSize(new Dimension(bubbleWidth, bubbleHeight));

        // 번역 아이콘 버튼 액션
        translateButton.addActionListener(e -> {
            String translatedText = null;
            try {
                translatedText = callTranslateAPI(message.getContent(), message.getLanguage());
                message.setContent(translatedText);
                message.determineLanguage();

                // 메시지 레이블 업데이트
                messageLabel.setText("<html><p style=\"width: " + textWrapWidth + "px;\">" + translatedText + "</p></html>");
                messageLabel.revalidate();
                messageLabel.repaint();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        // 메시지와 아이콘 배치
        if (message.getUserId().equals(user.getId())) {
            // 내가 쓴 메시지: 아이콘이 왼쪽, 메시지가 오른쪽
            bubble.add(Box.createHorizontalGlue()); // 오른쪽 기준으로 정렬
            bubble.add(translateButton);            // 왼쪽에 아이콘
            bubble.add(Box.createRigidArea(new Dimension(5, 0)));
            bubble.add(messageLabel);               // 오른쪽에 메시지
            bubble.add(Box.createRigidArea(new Dimension(5, 0)));
            bubble.add(userNameLabel);
        } else {
            // 상대방 메시지: 메시지가 왼쪽, 아이콘이 오른쪽
            bubble.add(userNameLabel);
            bubble.add(Box.createRigidArea(new Dimension(5, 0)));
            bubble.add(messageLabel);               // 왼쪽에 메시지
            bubble.add(Box.createRigidArea(new Dimension(5, 0)));
            bubble.add(translateButton);            // 오른쪽에 아이콘
            bubble.add(Box.createHorizontalGlue()); // 왼쪽 기준으로 정렬
        }

        // 메시지 패널에 추가
        messagePanel.add(bubble);
        messagePanel.add(Box.createVerticalStrut(5)); // 메시지 간 간격 추가

        revalidate();
        repaint();

        // 스크롤바를 가장 아래로 이동
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    // 번역 API 호출 메서드 (예시)
    private String callTranslateAPI(String text, String language) throws IOException {
        // 번역 API를 호출하는 로직 구현 (HTTP 요청 등)
        // 예시로 "번역된 텍스트" 반환
        TranslationResponse translate = translationService.translate(text, language);
        String translatedText=translate.getTranslations().getFirst().getText();
        return translatedText;
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
                    ChatRoomMessage chatRoomMessage = ChatRoomMessage.builder()
                            .content(received.message)
                            .chatRoomId(received.chatRoomId)
                            .userId(received.userId)
                            .sendAt(LocalDateTime.now())
                            .build()
                            ;
                    chatRoomMessage.determineLanguage();

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
    private void showEmoticonDialog() {
        // 이모티콘 선택을 위한 다이얼로그 생성
        emoticonDialog = new JDialog();
        emoticonDialog.setTitle("이모티콘 선택");
        emoticonDialog.setLayout(new GridLayout(3, 3));
        emoticonDialog.setSize(200, 200);
        emoticonDialog.setLocationRelativeTo(null);

        // 이모티콘 버튼들 생성
        String[] emoticons = { "😊", "😂", "😢", "😍", "😎", "😡", "😜", "😇", "😱" };
        for (String emoticon : emoticons) {
            JButton emoticonButton = new JButton(emoticon);
            emoticonButton.setFont(new Font("Arial", Font.PLAIN, 30));
            emoticonButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 이모티콘 클릭 시 텍스트 필드에 이모티콘 추가
                    txtInput.setText(txtInput.getText() + emoticon);
                    emoticonDialog.dispose(); // 다이얼로그 닫기
                }
            });
            emoticonDialog.add(emoticonButton);
        }
        emoticonDialog.setVisible(true);
    }
}
