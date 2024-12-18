package login;

import db.UserDB;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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
        lblTitle.setFont(new Font("Serif", Font.BOLD, 24));  // LoginScreen과 동일한 폰트
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(50, 20, 200, 40);
        contentPane.add(lblTitle);

        // 이메일 입력
        JLabel lblEmail = new JLabel("이메일");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        lblEmail.setBounds(40, 80, 80, 20);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        txtEmail.setBounds(120, 80, 140, 30);
        contentPane.add(txtEmail);

        // 비밀번호 입력
        JLabel lblPassword = new JLabel("비밀번호");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        lblPassword.setBounds(40, 130, 80, 20);
        contentPane.add(lblPassword);

        txtPassword = new JTextField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        txtPassword.setBounds(120, 130, 140, 30);
        contentPane.add(txtPassword);

        // 사용자 이름 입력
        JLabel lblUserName = new JLabel("이름");
        lblUserName.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        lblUserName.setBounds(40, 180, 80, 20);
        contentPane.add(lblUserName);

        txtUserName = new JTextField();
        txtUserName.setFont(new Font("Arial", Font.PLAIN, 14));  // LoginScreen과 동일한 폰트
        txtUserName.setBounds(120, 180, 140, 30);
        contentPane.add(txtUserName);

        // 회원가입 버튼
        JButton btnRegister = new JButton("회원가입");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 16));  // LoginScreen과 동일한 폰트
        btnRegister.setBounds(90, 250, 120, 40);
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
            String port = "5001";

            User newUser = User.builder()
                    .email(email)
                    .password(password)
                    .port(port)
                    .userName(userName)
                    .friends(new ArrayList<>())
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
}
