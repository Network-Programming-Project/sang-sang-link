package login;

import chat.ChatScreen;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginScreen extends JFrame {

	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtIPAddress;
	private JTextField txtPortNumber;
	private Clip clip;  // BGM을 제어할 Clip 객체 추가

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				LoginScreen frame = new LoginScreen();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public LoginScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(0, 155, 255));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		playBackgroundMusic("/Users/jang-uk/Desktop/networkprogramming/bgm/sangsangBGM.wav");  // 음악 파일 경로 지정

		// 제목
		JLabel lblTitle = new JLabel("SangSangLink");
		lblTitle.setFont(new Font("Serif", Font.BOLD, 24));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(50, 20, 200, 40);
		contentPane.add(lblTitle);

		// 이미지 추가 (SangSangLink 오른쪽)
		ImageIcon icon = new ImageIcon("/Users/jang-uk/Desktop/networkprogramming/images/sangsangbugi3.jpg"); // 이미지 경로
		JLabel lblImage = new JLabel(new ImageIcon(getCircularImage(icon.getImage())));
		lblImage.setBounds(230, 20, 50, 50); // 크기 조정 및 위치 설정
		contentPane.add(lblImage);

		// User Name 라벨
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setFont(new Font("Arial", Font.PLAIN, 14));
		lblUserName.setBounds(110, 85, 120, 20); // 폭 늘림
		contentPane.add(lblUserName);

		// User Name 입력
		txtUserName = new JTextField();
		txtUserName.setFont(new Font("Arial", Font.PLAIN, 14));
		txtUserName.setBounds(90, 105, 120, 30);
		contentPane.add(txtUserName);

		// IP Address 라벨
		JLabel lblIPAddress = new JLabel("IP Address");
		lblIPAddress.setFont(new Font("Arial", Font.PLAIN, 14));
		lblIPAddress.setBounds(110, 155, 120, 20);
		contentPane.add(lblIPAddress);

		// IP Address 입력
		txtIPAddress = new JTextField("127.0.0.1");
		txtIPAddress.setFont(new Font("Arial", Font.PLAIN, 14));
		txtIPAddress.setBounds(90, 175, 120, 30);
		contentPane.add(txtIPAddress);

		// Port Number 라벨
		JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPortNumber.setBounds(110, 225, 120, 20);
		contentPane.add(lblPortNumber);

		// Port Number 입력
		txtPortNumber = new JTextField("50000");
		txtPortNumber.setFont(new Font("Arial", Font.PLAIN, 14));
		txtPortNumber.setBounds(90, 245, 120, 30);
		contentPane.add(txtPortNumber);

		// 로그인 버튼
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
		btnLogin.setBounds(90, 300, 120, 40);
		contentPane.add(btnLogin);

		// 로그인 버튼 클릭 시 액션
		btnLogin.addActionListener(e -> {
			// BGM 멈추기
			stopBackgroundMusic();

			String username = txtUserName.getText().trim();
			String ipAddress = txtIPAddress.getText().trim();
			String portNumber = txtPortNumber.getText().trim();

			// 입력된 정보를 ChatScreen에 전달
			ChatScreen chatScreen = new ChatScreen(username, ipAddress, portNumber);
			chatScreen.setVisible(true); // ChatScreen 보이기

			setVisible(false); // LoginScreen 숨기기
			dispose(); // LoginScreen 종료
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

	// BGM 재생
	private void playBackgroundMusic(String musicFilePath) {
		try {
			File musicFile = new File(musicFilePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
			clip = AudioSystem.getClip(); // Clip 객체 초기화
			clip.open(audioStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);  // 무한 반복
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
