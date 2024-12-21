package login;

import db.UserDB;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RegisterScreen extends JFrame {

    private JTextField txtEmail;
    private JTextField txtPassword;
    private JTextField txtUserName;

    private JFrame loginScreen;

    public RegisterScreen(JFrame loginScreen) {
        this.loginScreen = loginScreen;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 300, 400);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(0, 155, 255));  // LoginScreen과 동일한 배경 색상
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("회원가입");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));  // LoginScreen과 동일한 폰트
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(50, 30, 200, 40);
        contentPane.add(lblTitle);

        // 이미지 추가 (SangSangLink 오른쪽)
        ImageIcon icon = new ImageIcon(getClass().getResource("/static/images/bugi.jpeg"));
        JLabel lblImage = new JLabel(new ImageIcon(getCircularImage(icon.getImage())));
        lblImage.setBounds(230, 25, 50, 50); // 크기 조정 및 위치 설정
        contentPane.add(lblImage);

        // 이메일 입력
        JLabel lblEmail = new JLabel("이메일");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        lblEmail.setBounds(40, 120, 80, 20);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        txtEmail.setBounds(120, 120, 140, 30);
        contentPane.add(txtEmail);

        // 비밀번호 입력
        JLabel lblPassword = new JLabel("비밀번호");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        lblPassword.setBounds(40, 170, 80, 20);
        contentPane.add(lblPassword);

        txtPassword = new JTextField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        txtPassword.setBounds(120, 170, 140, 30);
        contentPane.add(txtPassword);

        // 사용자 이름 입력
        JLabel lblUserName = new JLabel("이름");
        lblUserName.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        lblUserName.setBounds(40, 220, 80, 20);
        contentPane.add(lblUserName);

        txtUserName = new JTextField();
        txtUserName.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        txtUserName.setBounds(120, 220, 140, 30);
        contentPane.add(txtUserName);

        // 회원가입 버튼
        JButton btnRegister = new JButton("회원가입");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 16));  // LoginScreen과 동일한 폰트
        btnRegister.setBounds(90, 300, 120, 40);
        contentPane.add(btnRegister);

        // 회원가입 버튼 클릭 이벤트
        btnRegister.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();
            String userName = txtUserName.getText().trim();

            if (email.isEmpty() || password.isEmpty() || userName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "모든 필드를 입력해야 합니다.",
                        "회원가입 실패",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 포트 번호는 고정값 5001로 설정
            String port = "50001";

            User newUser = User.builder()
                    .email(email)
                    .password(password)
                    .port(port)
                    .userName(userName)
                    .friends(new ArrayList<>())
                    .chatRooms(new ArrayList<>())
                    .build();

            if (UserDB.register(newUser)) {
                JOptionPane.showMessageDialog(this,
                        "회원가입 성공!",
                        "회원가입",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose(); // RegisterScreen 닫기
                loginScreen.setVisible(true); // LoginScreen 다시 표시
            } else {
                JOptionPane.showMessageDialog(this,
                        "이메일이 이미 사용 중입니다.",
                        "회원가입 실패",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // 이미지 원형으로 만드는 메소드
    private Image getCircularImage(Image image) {
        int diameter = Math.min(image.getWidth(null), image.getHeight(null));  // 이미지 크기 중 작은 값을 반지름으로 사용
        BufferedImage bufferedImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();

        // 원형 모양으로 자르기 위한 Clip 설정
        g.setClip(new java.awt.geom.Ellipse2D.Double(0, 0, diameter, diameter));
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }

}
