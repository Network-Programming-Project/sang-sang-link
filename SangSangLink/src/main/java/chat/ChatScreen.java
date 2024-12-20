package chat;

import com.google.gson.Gson;
import model.ChatRoom;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.net.Socket;

public class ChatScreen extends JPanel {
    private JTextArea textArea;
    private JTextField txtInput;
    private JButton btnSend, btnEmoticon, btnAttach;
    private String ipAddr;
    private String portNo;
    private User user;

    // 소켓
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    // 채팅방 정보 객체
    private ChatRoom chatRoom;

    // 이모티콘 패널
    private JDialog emoticonDialog;

    // json 파싱 객체
    private Gson gson = new Gson();

    public ChatScreen(ChatRoom chatRoom, User user) {
        this.user = user;
        this.ipAddr = "127.0.0.1";
        this.portNo = "50001";
        this.chatRoom = chatRoom;

        setLayout(null);
        setBounds(60, 0, 320, 500);
        setVisible(false); // 기본적으로 숨김 상태

        initializeComponents();
    }

    private void initializeComponents() {
        // 스크롤 패널
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 300, 380);
        add(scrollPane);

        // 텍스트 영역
        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        // 입력 필드
        txtInput = new JTextField();
        txtInput.setBounds(10, 400, 230, 40);
        add(txtInput);
        txtInput.setColumns(10);

        // 전송 버튼
        btnSend = new JButton("보내기");
        btnSend.setBounds(250, 400, 60, 40);
        add(btnSend);

        // 이모티콘 버튼
        btnEmoticon = new JButton("😊");
        btnEmoticon.setBounds(10, 450, 50, 40);
        add(btnEmoticon);

        // 파일 첨부 버튼
        btnAttach = new JButton("사진");
        btnAttach.setBounds(70, 450, 60, 40);
        add(btnAttach);

        // 전송 버튼 액션
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = txtInput.getText();
                sendMessage(createJsonMessage(user.getId(), chatRoom.getId(), msg));
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
                    sendMessage(createJsonMessage(user.getId(), chatRoom.getId(), "사진: " + imagePath));
                }
            }
        });

        try {
            socket = new Socket(ipAddr, Integer.parseInt(portNo));
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            // 첫 메시지
            sendMessage(createJsonMessage(user.getId(), chatRoom.getId(), "입장"));

            ListenNetwork net = new ListenNetwork();
            net.start();

        } catch (IOException e) {
            appendText("Error connecting to server\n");
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

    private String createJsonMessage(Long userId, Long chatRoomId, String message) {
        JsonMessage jsonMessage = new JsonMessage(userId, chatRoomId, message);
        return gson.toJson(jsonMessage);
    }

    public void sendMessage(String msg) {
        try {
            dos.writeUTF(msg);
        } catch (IOException e) {
            appendText("Error sending message\n");
        }
    }

    public void appendText(String msg) {
        textArea.append(msg);
        textArea.setCaretPosition(textArea.getText().length());
    }

    class ListenNetwork extends Thread {
        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    appendText(msg + "\n");
                } catch (IOException e) {
                    appendText("Connection lost\n");
                    break;
                }
            }
        }
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
}
