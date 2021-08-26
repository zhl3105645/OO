package classmodel;

import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlInterface {
    private UmlInterface umlInterface;
    private ArrayList<MyUmlInterface> fathers = new ArrayList<>();
    private HashMap<String, UmlAttribute> attributes = new HashMap<>();

    public MyUmlInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }

    public UmlInterface getUmlInterface() {
        return umlInterface;
    }

    public void addFather(MyUmlInterface father) {
        fathers.add(father);
    }

    public void addAttribute(UmlAttribute attribute) {
        if (!attributes.containsKey(attribute.getId())) {
            attributes.put(attribute.getId(), attribute);
        }
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

    public boolean isCircle() {
        HashMap<String, Boolean> visit = new HashMap<>();
        visit.put(umlInterface.getId(), true);
        for (MyUmlInterface father : fathers) {
            if (father.search1(umlInterface.getId(), visit)) {
                return true;
            }
        }
        return false;
    }

    public boolean search1(String id, HashMap<String, Boolean> visit) {
        visit.put(umlInterface.getId(), true);
        for (MyUmlInterface father : fathers) {
            if (father.getUmlInterface().getId().equals(id)) {
                return true;
            }
        }
        for (MyUmlInterface father : fathers) {
            if (visit.containsKey(father.umlInterface.getId())) {
                return false;
            } else if (father.search1(id, visit)) {
                return true;
            }
        }
        visit.remove(umlInterface.getId());
        return false;
    }

    public boolean isDupGeneralization() {
        HashMap<String, UmlInterface> visit = new HashMap<>();
        for (MyUmlInterface father : fathers) {
            ArrayList<UmlInterface> interfaces = father.getInterfaces();
            for (UmlInterface umlInterface : interfaces) {
                if (visit.containsKey(umlInterface.getId())) {
                    return true;
                }
                visit.put(umlInterface.getId(), umlInterface);
            }
        }
        return false;
    }

    public ArrayList<UmlInterface> getInterfaces() {
        ArrayList<UmlInterface> interfaces = new ArrayList<>();
        interfaces.add(umlInterface);
        for (MyUmlInterface father : fathers) {
            interfaces.addAll(father.getInterfaces());
        }
        return interfaces;
    }

    public boolean isDupRealization(HashMap<String, Boolean> visit) {
        if (visit.containsKey(umlInterface.getId())) {
            return true;
        } else {
            visit.put(umlInterface.getId(), true);
            for (MyUmlInterface father : fathers) {
                if (father.isDupRealization(visit)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean checkAttributeVisibility() {
        for (UmlAttribute attribute : attributes.values()) {
            if (attribute.getVisibility() != Visibility.PUBLIC) {
                return false;
            }
        }
        return true;
    }
}
