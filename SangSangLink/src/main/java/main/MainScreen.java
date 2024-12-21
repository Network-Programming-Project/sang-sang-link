package main;

import chat.ChatAddScreen;
import chat.ChatGraphicScreen;
import chat.ChatListScreen;
import chat.ChatScreen;
import main.*;
import model.ChatRoom;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class MainScreen extends JFrame {
    private JPanel contentPane;
    private SideMenu sideMenu;
    private JPanel centerPanel; // 가운데 패널
    private ProfilePanel profilePanel;
    private ChatListScreen chatListScreen;
    private ChatAddScreen chatAddScreen;
    private ChatScreen chatScreen;
    private ChatGraphicScreen chatGraphicScreen;
    private User user;

    public MainScreen(User user) {
        this.user = user;
        initialize();
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);

        contentPane = new JPanel(null);
        contentPane.setBackground(new Color(100, 175, 250));
        setContentPane(contentPane);

        // 사이드 메뉴
        sideMenu = new SideMenu();
        sideMenu.setBounds(0, 0, 50, 500);
        contentPane.add(sideMenu);

        // 가운데 패널
        centerPanel = new JPanel(null);
        centerPanel.setBounds(50, 0, 350, 500);
        contentPane.setBackground(new Color(100, 250, 100));
        contentPane.add(centerPanel);

        // 패널들 초기화
        profilePanel = new ProfilePanel(getCurrentTime());
        profilePanel.setBounds(0, 0, 350, 500);
        profilePanel.setVisible(true);
        centerPanel.add(profilePanel);

        chatListScreen = new ChatListScreen(user);
        chatListScreen.setBounds(0,0,350,500);
        centerPanel.add(chatListScreen);
        chatListScreen.setVisible(false);

        // 이벤트 연결
        sideMenu.setProfileClickListener(e -> {
            showPanel(profilePanel);
        });

        sideMenu.setChatClickListener(e -> {
            showPanel(chatListScreen);
        });

        sideMenu.setChatAddClickListener(e->{
//            showPanel(chatAddScreen);
            System.out.println("공지사항 Screen 추가예정");
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return formatter.format(new Date());
    }

    // centerPanel 내 특정 패널만 표시하는 메서드
    private void showPanel(JPanel panelToShow) {
        for (Component comp : centerPanel.getComponents()) {
            comp.setVisible(false);
        }
        panelToShow.setVisible(true);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    // 하위 컴포넌트에서 centerPanel 의 스위칭이 필요할 때
    public void showChatScreen(ChatRoom chatRoom, User user) {
        chatGraphicScreen = new ChatGraphicScreen(chatRoom, user);
        chatGraphicScreen.setBounds(0,0,350,500);
        chatGraphicScreen.setBackground(new Color(100, 175, 250));
        centerPanel.add(chatGraphicScreen);
        chatGraphicScreen.setVisible(false);

        showPanel(chatGraphicScreen);
    }

    public void showChatAddScreen(){
        chatAddScreen = new ChatAddScreen(user);
        chatAddScreen.setBounds(0,0,350,500);
        centerPanel.add(chatAddScreen);
        chatAddScreen.setVisible(false);
        showPanel(chatAddScreen);
    }

    public void addChatRoomChatListScreen(ChatRoom chatRoom) {
        chatListScreen.createRoomItem(chatRoom);
    }

    public void showChatListScreen(){
        chatListScreen.initialization();
        chatListScreen.revalidate();
        chatListScreen.repaint();
        showPanel(chatListScreen);
    }
}

