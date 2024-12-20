package server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomThread {
    private Long userId;

    private Long chatRoomId;

    private ServerThread serverThread;
}
