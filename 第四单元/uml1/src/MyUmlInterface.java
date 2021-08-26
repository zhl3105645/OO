import com.oocourse.uml1.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlInterface {
    private UmlInterface umlInterface;
    private ArrayList<MyUmlInterface> fathers = new ArrayList<>();

    public MyUmlInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }

    public UmlInterface getUmlInterface() {
        return umlInterface;
    }

    public void addFather(MyUmlInterface father) {
        fathers.add(father);
    }

    public ArrayList<MyUmlInterface> getAllInterface(HashMap<String, Boolean> visit) {
        ArrayList<MyUmlInterface> interfaces = new ArrayList<>();
        for (MyUmlInterface father : fathers) {
            if (!visit.containsKey(father.getUmlInterface().getId())) {
                visit.put(father.getUmlInterface().getId(), true);
                interfaces.add(father);
                ArrayList<MyUmlInterface> interfaces1 = father.getAllInterface(visit);
                if (!interfaces1.isEmpty()) {
                    interfaces.addAll(interfaces1);
                }
            }
        }
        return interfaces;
    }

}
