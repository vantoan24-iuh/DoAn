package fit.iuh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ChatLieu")
public class ChatLieu implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long maChatLieu;

    @Column(name = "tenChatLieu", columnDefinition = "nvarchar(100)", nullable = false)
    private String chatLieu;


//    public ChatLieu() {
//    }
//
//    private String auto_ID(){
//        Dao_ChatLieu daoChatLieu = new Dao_ChatLieu();
//        String idPrefix = daoChatLieu.taoMaChatLieu();
//        return idPrefix;
//    }
//
    public ChatLieu(String chatLieu) {
        this.chatLieu = chatLieu;
    }
//
//    public ChatLieu(String maChatLieu, String chatLieu) {
//        this.maChatLieu = maChatLieu;
//        this.chatLieu = chatLieu;
//    }
//
//
//    public String getMaChatLieu() {
//        return maChatLieu;
//    }
//
//    public void setMaChatLieu(String maChatLieu) {
//        this.maChatLieu = maChatLieu;
//    }
//
//    public String getChatLieu() {
//        return chatLieu;
//    }
//
//    public void setChatLieu(String chatLieu) {
//        this.chatLieu = chatLieu;
//    }
}
