package chat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatScreen extends JFrame {
    private JPanel contentPane;
    private JTextField txtInput;
    private String userName;
    private JTextArea textArea;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private JLabel lblUserName;
    private JLabel lblConnectTime;

    public ChatScreen(String username, String ipAddr, String portNo) {
        // 기본 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 왼쪽 메뉴
        JPanel sideMenu = new JPanel();
        sideMenu.setBackground(Color.LIGHT_GRAY);
        sideMenu.setBounds(0, 0, 50, 500);
        sideMenu.setLayout(new GridLayout(2, 1, 0, 10));
        contentPane.add(sideMenu);

        // 프로필 아이콘
        JLabel lblProfile = new JLabel(new ImageIcon("/Users/jang-uk/Desktop/networkprogramming/images/profile.jpg"));
        lblProfile.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sideMenu.add(lblProfile);

        // 채팅 아이콘
        JLabel lblChat = new JLabel(new ImageIcon("/Users/jang-uk/Desktop/networkprogramming/images/chat.jpg"));
        lblChat.setHorizontalAlignment(SwingConstants.CENTER);
        lblChat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sideMenu.add(lblChat);

        // 프로필 화면
        JPanel profilePanel = new JPanel();
        profilePanel.setBounds(60, 0, 320, 500);
        profilePanel.setLayout(null);
        contentPane.add(profilePanel);

        JLabel lblTitle = new JLabel("나의 프로필");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBounds(20, 10, 150, 30);
        profilePanel.add(lblTitle);

        JLabel lblAvatar = new JLabel();
        lblAvatar.setIcon(new ImageIcon("/Users/jang-uk/Desktop/networkprogramming/images/avatar.jpg")); // 아바타 이미지
        lblAvatar.setBounds(20, 50, 50, 50);
        profilePanel.add(lblAvatar);

        lblUserName = new JLabel("사용자: " + username);
        lblUserName.setBounds(80, 60, 200, 30);
        profilePanel.add(lblUserName);

        lblConnectTime = new JLabel("접속시간: " + getCurrentTime());
        lblConnectTime.setBounds(80, 90, 200, 30);
        profilePanel.add(lblConnectTime);

        // 채팅 화면
        JPanel chatPanel = new JPanel();
        chatPanel.setBounds(60, 0, 320, 500);
        chatPanel.setLayout(null);
        chatPanel.setVisible(false); // 기본적으로 프로필 화면만 보이도록 설정
        contentPane.add(chatPanel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 300, 380);
        chatPanel.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        txtInput = new JTextField();
        txtInput.setBounds(10, 400, 230, 40);
        chatPanel.add(txtInput);
        txtInput.setColumns(10);

        JButton btnSend = new JButton("Send");
        btnSend.setBounds(250, 400, 60, 40);
        chatPanel.add(btnSend);

        // 메뉴 클릭 이벤트
        lblProfile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                profilePanel.setVisible(true);
                chatPanel.setVisible(false);
            }
        });

        lblChat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                profilePanel.setVisible(false);
                chatPanel.setVisible(true);
            }
        });

        // 통신 설정
        userName = username;
        try {
            socket = new Socket(ipAddr, Integer.parseInt(portNo));
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            SendMessage("/login " + userName);

            ListenNetwork net = new ListenNetwork();
            net.start();

            btnSend.addActionListener(e -> {
                String msg = "[" + userName + "] " + txtInput.getText();
                SendMessage(msg);
                txtInput.setText("");
            });

            txtInput.addActionListener(e -> {
                String msg = "[" + userName + "] " + txtInput.getText();
                SendMessage(msg);
                txtInput.setText("");
            });
        } catch (IOException e) {
            AppendText("Error connecting to server\n");
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return formatter.format(new Date());
    }

    // 서버에서 메시지 수신
    class ListenNetwork extends Thread {
        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    AppendText(msg + "\n");
                } catch (IOException e) {
                    AppendText("Connection lost\n");
                    break;
                }
            }
        }
    }

    public void AppendText(String msg) {
        textArea.append(msg);
        textArea.setCaretPosition(textArea.getText().length());
    }

    public void SendMessage(String msg) {
        try {
            dos.writeUTF(msg);
        } catch (IOException e) {
            AppendText("Error sending message\n");
        }
    }
}
