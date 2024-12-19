package chat;

import com.google.gson.Gson;
import db.UserDB;
import main.MainScreen;
import model.ChatRoom;
import model.User;
import session.Session;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatScreen extends JPanel {
    private JTextArea textArea;
    private JTextField txtInput;
    private JButton btnSend;
    private String ipAddr;
    private String portNo;
    private User user;

    // 소켓
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    // 채팅방 정보 객체
    private ChatRoom chatRoom;

    // json 파싱 객체
    private Gson gson = new Gson();

    // TODO 채팅 창 나오면 이전에 있던 대화내용도 그려야함.
    public ChatScreen(ChatRoom chatRoom, User user) {

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
        // 스크롤 패널
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 300, 380);
        add(scrollPane);

        // 텍스트 영역
        // TODO 채팅방으로 꾸미기
        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        // 입력 필드
        txtInput = new JTextField();
        txtInput.setBounds(10, 400, 230, 40);
        add(txtInput);
        txtInput.setColumns(10);

        // 전송 버튼
        // TODO 전송 버튼 디자인
        btnSend = new JButton("보내기");
        btnSend.setBounds(250, 400, 60, 40);
        add(btnSend);

        // 전송 버튼 액션
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = txtInput.getText();
                sendMessage(createJsonMessage(user.getId(), chatRoom.getId(), msg));
                txtInput.setText("");
            }
        });

        // Enter 키로 메시지 전송
        txtInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = txtInput.getText();
                sendMessage(createJsonMessage(user.getId(), chatRoom.getId(), msg));
                txtInput.setText("");
                System.out.println("ChatScreen 메시지 전송 액션 리스너 유저 객체 확인: "+ user);
            }
        });

        try {
            socket = new Socket(ipAddr, Integer.parseInt(portNo));
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            // JSON 형식으로 채팅방 ID를 서버에 전송
            // 첫 메시지
            sendMessage(createJsonMessage(user.getId(), chatRoom.getId(), "입장"));
            System.out.println("Sent JSON: " + createJsonMessage(user.getId(), chatRoom.getId(), "입장"));

            ListenNetwork net = new ListenNetwork();
            net.start();

        } catch (IOException e) {
            appendText("Error connecting to server\n");
        }
    }

    // JSON 메시지 생성
    private String createJsonMessage(Long userId, Long chatRoomId, String message) {
        JsonMessage jsonMessage = new JsonMessage(userId, chatRoomId, message);
        return gson.toJson(jsonMessage);
    }

    // 변경 부분 없음
    public void sendMessage(String msg) {
        try {
            dos.writeUTF(msg);
        } catch (IOException e) {
            appendText("Error sending message\n");
        }
    }

    // 변경 부분 없음
    public void appendText(String msg) {
        textArea.append(msg);
        textArea.setCaretPosition(textArea.getText().length());
    }

    // 서버로부터 메시지를 받을 때 호출되는 메소드
    public void receiveMessage(String message) {
        textArea.append("Server: " + message + "\n");
    }

    // 서버에서 메시지 수신
    class ListenNetwork extends Thread {
        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    System.out.println(user.getUserName()+"메시지 받음"+msg);
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
