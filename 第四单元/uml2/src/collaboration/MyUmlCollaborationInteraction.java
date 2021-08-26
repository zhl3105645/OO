package collaboration;

import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlCollaborationInteraction implements UmlCollaborationInteraction {
    private HashMap<String, MyUmlInteraction> interactions = new HashMap<>();//id ->
    private HashMap<String, MyUmlLifeline> lifelines = new HashMap<>();//id ->
    private HashMap<String, ArrayList<String>> nameToId = new HashMap<>();//name -> ids

    public MyUmlCollaborationInteraction(UmlElement... elements) {
        ArrayList<UmlElement> elements1 = new ArrayList<>();
        ArrayList<UmlElement> elements2 = new ArrayList<>();
        for (UmlElement e : elements) {
            if (e.getElementType() == ElementType.UML_INTERACTION) {
                MyUmlInteraction myUmlInteraction = new MyUmlInteraction((UmlInteraction) e);
                interactions.put(e.getId(), myUmlInteraction);
                if (!nameToId.containsKey(e.getName())) {
                    nameToId.put(e.getName(), new ArrayList<>());
                }
                nameToId.get(e.getName()).add(e.getId());
            }
            if (e.getElementType() == ElementType.UML_LIFELINE) {
                elements1.add(e);
            } else if (e.getElementType() == ElementType.UML_MESSAGE) {
                elements2.add(e);
            }
        }
        for (UmlElement e : elements1) {
            String parentId = e.getParentId();
            MyUmlLifeline myUmlLifeline = new MyUmlLifeline((UmlLifeline) e);
            lifelines.put(e.getId(), myUmlLifeline);
            if (interactions.containsKey(parentId)) {
                interactions.get(parentId).addLifeline(myUmlLifeline);
            }
        }
        for (UmlElement e : elements2) {
            String source = ((UmlMessage) e).getSource();
            String target = ((UmlMessage) e).getTarget();
            String parentId = e.getParentId();
            if (lifelines.containsKey(source)) {
                lifelines.get(source).addSendMessage((UmlMessage) e);
            }
            if (lifelines.containsKey(target)) {
                lifelines.get(target).addIncomingMessages((UmlMessage) e);
            }
            if (interactions.containsKey(parentId)) {
                interactions.get(parentId).addMessage((UmlMessage) e);
            }
        }
    }

    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!nameToId.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (nameToId.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        String interactionId = nameToId.get(interactionName).get(0);
        MyUmlInteraction myUmlInteraction = interactions.get(interactionId);
        return myUmlInteraction.getLifelineNum();
    }

    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!nameToId.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (nameToId.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        String interactionId = nameToId.get(interactionName).get(0);
        MyUmlInteraction myUmlInteraction = interactions.get(interactionId);
        ArrayList<MyUmlLifeline> myUmlLifelines = myUmlInteraction.getLifelinesByName(lifelineName);
        if (myUmlLifelines.isEmpty()) {
            throw new LifelineNotFoundException(interactionName, lifelineName);
        } else if (myUmlLifelines.size() > 1) {
            throw new LifelineDuplicatedException(interactionName, lifelineName);
        }
        MyUmlLifeline myUmlLifeline = myUmlLifelines.get(0);
        return myUmlLifeline.getIncomingMessageNum();
    }

    @Override
    public int getSentMessageCount(String interactionName, String lifelineName, MessageSort sort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!nameToId.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (nameToId.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        String interactionId = nameToId.get(interactionName).get(0);
        MyUmlInteraction myUmlInteraction = interactions.get(interactionId);
        ArrayList<MyUmlLifeline> myUmlLifelines = myUmlInteraction.getLifelinesByName(lifelineName);
        if (myUmlLifelines.isEmpty()) {
            throw new LifelineNotFoundException(interactionName, lifelineName);
        } else if (myUmlLifelines.size() > 1) {
            throw new LifelineDuplicatedException(interactionName, lifelineName);
        }
        MyUmlLifeline myUmlLifeline = myUmlLifelines.get(0);
        return myUmlLifeline.getSendMessageNum(sort);
    }
}
