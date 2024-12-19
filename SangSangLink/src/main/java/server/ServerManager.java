package server;

import session.Session;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerManager {
    static List<ChatRoomThread> list=new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket ssocket = new ServerSocket(50001);     // 1. 서버 소켓 생성

        Socket s;

        while (true) {
            System.out.println("서버 매니저 while 시작");
            s = ssocket.accept();
            System.out.println("클라이언트와 소켓 연결 성공");

            // 아까는 여기서 Session.user 부분이 null이여서 오류가 발생
            DataInputStream is = new DataInputStream(s.getInputStream());
            DataOutputStream os = new DataOutputStream(s.getOutputStream());

            ServerThread thread = new ServerThread(s, is, os);
            thread.start();
        }
    }
}
