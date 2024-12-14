package main;

import chat.ChatScreen;
import main.*;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainScreen extends JFrame {
    private JPanel contentPane;
    private ProfilePanel profilePanel;
    private ChatScreen chatScreen;
    private JLabel lblUserName;
    private JLabel lblConnectTime;

    public MainScreen(User user) {
        initialize(user);
    }

    private void initialize(User user) {
        // 기본 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 사이드 메뉴 추가
        SideMenu sideMenu = new SideMenu();
        sideMenu.setBounds(0, 0, 50, 500);
        contentPane.add(sideMenu);

        // 프로필 패널 초기화
        profilePanel = new ProfilePanel(user.getUserName(), getCurrentTime());
        profilePanel.setBounds(60, 0, 320, 500);
        profilePanel.setVisible(true);
        contentPane.add(profilePanel);

        // 채팅 화면 초기화
        chatScreen = new ChatScreen(user);
        chatScreen.setBounds(60, 0, 320, 500);
        chatScreen.setVisible(false);
        contentPane.add(chatScreen);

        // 메뉴 클릭 이벤트 연결
        sideMenu.setProfileClickListener(e -> {
            profilePanel.setVisible(true);
            chatScreen.setVisible(false);
        });

        sideMenu.setChatClickListener(e -> {
            profilePanel.setVisible(false);
            chatScreen.setVisible(true);
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return formatter.format(new Date());
    }

    public static void main(String[] args) {

    }
}

