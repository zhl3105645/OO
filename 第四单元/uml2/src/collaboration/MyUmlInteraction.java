package collaboration;

import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlInteraction {
    private UmlInteraction umlInteraction;
    private HashMap<String, ArrayList<MyUmlLifeline>> nameToLifeline = new HashMap<>();
    private ArrayList<MyUmlLifeline> lifelines = new ArrayList<>();
    private ArrayList<UmlMessage> messages = new ArrayList<>();

    public MyUmlInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
    }

    public void addLifeline(MyUmlLifeline myUmlLifeline) {
        String name = myUmlLifeline.getUmlLifeline().getName();
        lifelines.add(myUmlLifeline);
        if (!nameToLifeline.containsKey(name)) {
            nameToLifeline.put(name, new ArrayList<MyUmlLifeline>());
        }
        nameToLifeline.get(name).add(myUmlLifeline);
    }

    public void addMessage(UmlMessage umlMessage) {
        messages.add(umlMessage);
    }

    public int getLifelineNum() {
        return lifelines.size();
    }

    public ArrayList<MyUmlLifeline> getLifelinesByName(String lifelineName) {
        ArrayList<MyUmlLifeline> myUmlLifelines = new ArrayList<>();
        if (nameToLifeline.containsKey(lifelineName)) {
            myUmlLifelines.addAll(nameToLifeline.get(lifelineName));
        }
        return myUmlLifelines;
    }
}
