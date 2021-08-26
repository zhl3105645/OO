package collaboration;

import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;

public class MyUmlLifeline {
    private UmlLifeline umlLifeline;
    private ArrayList<UmlMessage> incomingMessages = new ArrayList<>();
    private ArrayList<UmlMessage> sendMessage = new ArrayList<>();

    public MyUmlLifeline(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
    }

    public UmlLifeline getUmlLifeline() {
        return umlLifeline;
    }

    public void addIncomingMessages(UmlMessage umlMessage) {
        incomingMessages.add(umlMessage);
    }

    public void addSendMessage(UmlMessage umlMessage) {
        sendMessage.add(umlMessage);
    }

    public int getIncomingMessageNum() {
        return incomingMessages.size();
    }

    public int getSendMessageNum(MessageSort sort) {
        int num = 0;
        for (UmlMessage message : sendMessage) {
            if (message.getMessageSort() == sort) {
                num++;
            }
        }
        return num;
    }
}
