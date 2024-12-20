package server;

import chat.ChatGraphicScreen;
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

                // TODO ServerManager.list에 이미 userid, chatroomid를 동시에 만족하는 스레드가 있는지 체크 후 종료
                // 최초 접속 시 userId, chatRoomId 설정
                if (this.userId == null && this.chatRoomId == null) {
                    ServerManager.list.removeIf(chatRoomThread -> {
                        if (chatRoomThread.getUserId().equals(jsonMessage.userId) &&
                                chatRoomThread.getChatRoomId().equals(jsonMessage.chatRoomId)) {
                            // 기존 스레드 종료
                            // 클라이언트에 연결 종료 요청
                            chatRoomThread.getServerThread().stopThread(); // stopThread()는 종료 메서드
                            return true; // 제거 대상
                        }
                        return false; // 유지 대상
                    });

                    this.userId = jsonMessage.userId;
                    this.chatRoomId = jsonMessage.chatRoomId;

                    ServerManager.list.add(new ChatRoomThread(userId, chatRoomId, this));
                } else {
                    // 이후부터는 메시지 전송
                    sendMessageToChatRoom(receivedJson);
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
    // 소켓 통신 중단 처리
    public void stopThread() {
        try {
            // 클라이언트 소켓 스레드에 통신 중단 요청
            String stopMessage=createJsonMessage(userId, chatRoomId, "/stop");
            os.writeUTF(stopMessage);

            // 리소스 정리
            if (is != null) is.close();
            if (os != null) os.close();
            if (s != null) s.close();
            System.out.println("Thread stopped for userId: " + userId + ", chatRoomId: " + chatRoomId);
        } catch (IOException e) {
            System.err.println("Error while stopping thread: " + e.getMessage());
        }
    }

    // JSON 메시지 클래스
    private static class JsonMessage {
        Long chatRoomId;
        Long userId;
        String message;

        public JsonMessage(Long userId, Long chatRoomId, String message) {
            this.userId = userId;
            this.chatRoomId = chatRoomId;
            this.message = message;
        }
    }

    // JSON 메시지 생성
    private String createJsonMessage(Long userId, Long chatRoomId, String message) {
      JsonMessage jsonMessage = new JsonMessage(userId, chatRoomId, message);
        return gson.toJson(jsonMessage);
    }
}

