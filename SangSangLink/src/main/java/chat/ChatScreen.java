package chat;

import main.MainScreen;
import model.User;

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
    private String userName;
    private String ipAddr;
    private String portNo;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ChatScreen(User user) {

        this.userName = userName;
        this.ipAddr = ipAddr;
        // TODO 포트넘버 로직 추가 예정 고정으로 가야할듯
        this.portNo = "50001";

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
                sendMessage(msg);
                txtInput.setText("");
            }
        });

        // Enter 키로 메시지 전송
        txtInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = "[" + userName + "] " + txtInput.getText();
                sendMessage(msg);
                txtInput.setText("");
            }
        });

        try {
            socket = new Socket(ipAddr, Integer.parseInt(portNo));
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            sendMessage("/login " + userName);

            ListenNetwork net = new ListenNetwork();
            net.start();

            btnSend.addActionListener(e -> {
                String msg = "[" + userName + "] " + txtInput.getText();
                sendMessage(msg);
                txtInput.setText("");
            });

            txtInput.addActionListener(e -> {
                String msg = "[" + userName + "] " + txtInput.getText();
                sendMessage(msg);
                txtInput.setText("");
            });
        } catch (IOException e) {
            appendText("Error connecting to server\n");
        }
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
                    appendText(msg + "\n");
                } catch (IOException e) {
                    appendText("Connection lost\n");
                    break;
                }
            }
        }
    }
}
