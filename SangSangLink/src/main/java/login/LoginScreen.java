package login;

import db.UserDB;
import main.MainScreen;
import model.User;
import session.Session;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class LoginScreen extends JFrame {

	private JPanel contentPane;
	private JTextField txtEmail;
	private JTextField txtPassword;
	private JTextField txtPortNumber;
	private Clip clip;  // BGM을 제어할 Clip 객체 추가

	public static void main(String[] args) {
		// 여러 개의 LoginScreen을 동시에 실행하기 위해 각각을 별도의 스레드로 실행
		for (int i = 0; i < 2; i++) {  // 예시로 3번의 인스턴스를 실행
			SwingUtilities.invokeLater(() -> {
				try {
					LoginScreen frame = new LoginScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	public LoginScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(0, 155, 255));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		playBackgroundMusic("/static/bgm/sangsangBGM.wav");  // 음악 파일 경로 지정

		// 제목
		JLabel lblTitle = new JLabel("SangSangLink");
		lblTitle.setFont(new Font("Serif", Font.BOLD, 24));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(50, 20, 200, 40);
		contentPane.add(lblTitle);

		// 이미지 추가 (SangSangLink 오른쪽)
		ImageIcon icon = new ImageIcon(getClass().getResource("/static/images/bugi.jpeg"));
		JLabel lblImage = new JLabel(new ImageIcon(getCircularImage(icon.getImage())));
		lblImage.setBounds(230, 20, 50, 50); // 크기 조정 및 위치 설정
		contentPane.add(lblImage);

		// User Name 라벨
		JLabel lblEmailName = new JLabel("이메일");
		lblEmailName.setFont(new Font("Arial", Font.PLAIN, 14));
		lblEmailName.setBounds(110, 85, 120, 20); // 폭 늘림
		contentPane.add(lblEmailName);

		// 사용자 이메일 입력
		txtEmail = new JTextField("hansung.ac.kr");
		txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
		txtEmail.setBounds(90, 105, 120, 30);
		contentPane.add(txtEmail);

		// 비밀번호 라벨
		JLabel lblPassword = new JLabel("비밀번호");
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPassword.setBounds(110, 155, 120, 20);
		contentPane.add(lblPassword);

		// 비밀번호 입력
		txtPassword = new JTextField("1234");
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		txtPassword.setBounds(90, 175, 120, 30);
		contentPane.add(txtPassword);

		// 회원가입 버튼
		JButton btnRegister = new JButton("회원가입");
		btnRegister.setFont(new Font("Arial", Font.BOLD, 16));
		btnRegister.setBounds(90, 250, 120, 40);
		contentPane.add(btnRegister);

		// 회원가입 버튼 클릭 시 액션
		btnRegister.addActionListener(e -> {
			setVisible(false); // LoginScreen 숨기기
			RegisterScreen registerScreen = new RegisterScreen(this); // 현재 창을 전달
			registerScreen.setVisible(true); // RegisterScreen 표시
		});


		// 로그인 버튼
		JButton btnLogin = new JButton("로그인");
		btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
		btnLogin.setBounds(90, 300, 120, 40);
		contentPane.add(btnLogin);

		// 로그인 버튼 클릭 시 액션
		btnLogin.addActionListener(e -> {
			// BGM 멈추기
			stopBackgroundMusic();

			String email = txtEmail.getText().trim();
			String password = txtPassword.getText().trim();
			System.out.println("이메일"+email);
			System.out.println("비밀번호"+password);
			// 로그인
			User user=UserDB.login(email, password);

			if(user!=null){
				// 세션 저장
				Session.setUser(user);
				MainScreen mainScreen = new MainScreen();
				mainScreen.setVisible(true); // ChatScreen 보이기

				setVisible(false); // LoginScreen 숨기기
				dispose(); // LoginScreen 종료
			}else{
				// 로그인 실패
				JOptionPane.showMessageDialog(LoginScreen.this,
						"이메일 또는 비밀번호가 올바르지 않습니다.",
						"로그인 실패",
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

	// BGM 멈추기
	private void stopBackgroundMusic() {
		if (clip != null) {
			clip.stop(); // 음악 정지
		}
	}

	private void playBackgroundMusic(String resourcePath) {
		try {
			// 클래스패스에서 리소스 로드
			URL resourceUrl = getClass().getResource(resourcePath);
			System.out.println("Resource URL: " + resourceUrl); // 디버깅 출력
			if (resourceUrl == null) {
				throw new IllegalArgumentException("Resource not found: " + resourcePath);
			}

			AudioInputStream audioStream = AudioSystem.getAudioInputStream(resourceUrl);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY); // 무한 반복
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
