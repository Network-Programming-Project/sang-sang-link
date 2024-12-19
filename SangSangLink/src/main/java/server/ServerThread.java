package server;

import com.google.gson.Gson;
import db.UserDB;
import model.ChatRoom;
import model.User;
import session.Session;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


class ServerThread extends Thread {
    // 스레드 정보
    private Long userId;
    private Long chatRoomId;

    // 소켓 정보
    Socket s;
    final DataInputStream is;
    final DataOutputStream os;

    boolean active;
    private final Gson gson = new Gson();

    public ServerThread(Socket s, DataInputStream is, DataOutputStream os) {
        this.is = is;
        this.os = os;
        this.s = s;
        this.active = true;
    }

    @Override
    public void run() {
        String message;
        while (true) {
            try {
                String receivedJson = is.readUTF(); // 클라이언트로부터 JSON 수신
                System.out.println("Received JSON: " + receivedJson);

                JsonMessage jsonMessage = gson.fromJson(receivedJson, JsonMessage.class);

                // 최초 접속 시 userId, chatRoomId 설정
                if (this.userId == null && this.chatRoomId == null) {
                    this.userId = jsonMessage.userId;
                    this.chatRoomId = jsonMessage.chatRoomId;

                    ServerManager.list.add(new ChatRoomThread(chatRoomId, this));
                    System.out.println("서버매니저 채팅방 map체크"+ServerManager.list);
                    os.writeUTF("채팅방 " + chatRoomId +"에 "+userId+"가 입장했습니다.");
                } else {
                    // 이후부터는 메시지 전송
                    sendMessageToChatRoom(jsonMessage.message);
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            this.is.close();
            this.os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 해당하는 스레드들을 가지고 메시지 전송
    private void sendMessageToChatRoom(String message) throws IOException {
        List<ChatRoomThread> list = ServerManager.list;

        for(ChatRoomThread chatRoomThread : list) {
            if(chatRoomId.equals(chatRoomThread.getChatRoomId())){
                chatRoomThread.getServerThread().os.writeUTF(message);
            }
        }
    }

    // JSON 메시지 클래스
    private static class JsonMessage {
        Long chatRoomId;
        Long userId;
        String message;

        public JsonMessage() {}
    }
}

