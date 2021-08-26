package classmodel;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlClass {
    private UmlClass umlClass;
    private MyUmlClass father = null;
    private HashMap<String, UmlAttribute> idToAttribute = new HashMap<>();
    private HashMap<String, ArrayList<UmlAttribute>> nameToAttribute = new HashMap<>();
    private HashMap<String, MyUmlOperation> idToOperation = new HashMap<>();
    private HashMap<String, ArrayList<MyUmlOperation>> nameToOperation = new HashMap<>();
    private HashMap<String, MyUmlClass> idToAssociation = new HashMap<>();
    private HashMap<String, MyUmlInterface> idToInterface = new HashMap<>();

    public MyUmlClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public UmlClass getUmlClass() {
        return umlClass;
    }

    public void setFather(MyUmlClass father) {
        this.father = father;
    }

    public void addAttribute(UmlAttribute attribute) {
        idToAttribute.put(attribute.getId(), attribute);
        if (nameToAttribute.containsKey(attribute.getName())) {
            nameToAttribute.get(attribute.getName()).add(attribute);
        } else {
            ArrayList<UmlAttribute> attributes = new ArrayList<>();
            attributes.add(attribute);
            nameToAttribute.put(attribute.getName(), attributes);
        }
    }

    public void addOperation(MyUmlOperation operation) {
        idToOperation.put(operation.getUmlOperation().getId(), operation);
        if (nameToOperation.containsKey(operation.getUmlOperation().getName())) {
            nameToOperation.get(operation.getUmlOperation().getName()).add(operation);
        } else {
            ArrayList<MyUmlOperation> operations = new ArrayList<>();
            operations.add(operation);
            nameToOperation.put(operation.getUmlOperation().getName(), operations);
        }
    }

    public void addAssociation(MyUmlClass myumlClass) {
        idToAssociation.put(myumlClass.getUmlClass().getId(), myumlClass);
    }

    public void addInterface(MyUmlInterface umlInterface) {
        idToInterface.put(umlInterface.getUmlInterface().getId(), umlInterface);
    }

    public int getOperationNum() {
        return idToOperation.size();
    }

    public int getAttributeNum() {
        if (father != null) {
            return idToAttribute.size() + father.getAttributeNum();
        } else {
            return idToAttribute.size();
        }
    }

    public Map<Visibility, Integer> getOperationVisibility(String operationName) {
        Map<Visibility, Integer> map = new HashMap<>();
        int publicNum = 0;
        int privateNum = 0;
        int protectNum = 0;
        int packageNum = 0;
        if (nameToOperation.containsKey(operationName)) {
            for (MyUmlOperation operation : nameToOperation.get(operationName)) {
                switch (operation.getUmlOperation().getVisibility()) {
                    case PUBLIC:
                        publicNum++;
                        break;
                    case PRIVATE:
                        privateNum++;
                        break;
                    case PROTECTED:
                        protectNum++;
                        break;
                    case PACKAGE:
                        packageNum++;
                        break;
                    default:
                }
            }
        }
        map.put(Visibility.PUBLIC, publicNum);
        map.put(Visibility.PRIVATE, privateNum);
        map.put(Visibility.PROTECTED, protectNum);
        map.put(Visibility.PACKAGE, packageNum);
        return map;
    }

    public ArrayList<UmlAttribute> getAllNameAttribute(String attributeName) {
        ArrayList<UmlAttribute> attributes = new ArrayList<>();
        if (nameToAttribute.get(attributeName) != null) {
            attributes.addAll(nameToAttribute.get(attributeName));
        }
        if (father != null && father.getAllNameAttribute(attributeName) != null) {
            attributes.addAll(father.getAllNameAttribute(attributeName));
        }
        return attributes;
    }

    public ArrayList<MyUmlOperation> getNameOperation(String operationName) {
        return nameToOperation.get(operationName);
    }

    public HashMap<String, String> getAssociateClass() {
        HashMap<String, String> idToName = new HashMap<>();
        for (String id : idToAssociation.keySet()) {
            idToName.put(id, idToAssociation.get(id).getUmlClass().getName());
        }
        if (father != null) {
            HashMap<String, String> idToName1 = father.getAssociateClass();
            for (String id : idToName1.keySet()) {
                idToName.put(id, idToName1.get(id));
            }
        }
        return idToName;
    }

    public String getTopClass() {
        if (father == null) {
            return umlClass.getName();
        } else {
            return father.getTopClass();
        }
    }

    public HashMap<String, String> getAllInterface(HashMap<String, Boolean> visit) {
        HashMap<String, String> idToName = new HashMap<>();
        for (String id : idToInterface.keySet()) {
            if (!visit.containsKey(id)) {
                visit.put(id, true);
                idToName.put(id, idToInterface.get(id).getUmlInterface().getName());
                ArrayList<MyUmlInterface> interfaces = idToInterface.get(id).getAllInterface(visit);
                for (MyUmlInterface myUmlInterface : interfaces) {
                    idToName.put(myUmlInterface.getUmlInterface().getId(),
                            myUmlInterface.getUmlInterface().getName());
                }
            }
        }
        if (father != null) {
            HashMap<String, String> idToName1 = father.getAllInterface(visit);
            for (String id : idToName1.keySet()) {
                idToName.put(id, idToName1.get(id));
            }
        }
        return idToName;
    }

    public List<AttributeClassInformation> getInformationNotHidden() {
        ArrayList<AttributeClassInformation> information = new ArrayList<>();
        for (UmlAttribute attribute : idToAttribute.values()) {
            if (attribute.getVisibility() != Visibility.PRIVATE) {
                AttributeClassInformation information1 = new
                        AttributeClassInformation(attribute.getName(), umlClass.getName());
                information.add(information1);
            }
        }
        if (father != null && father.getInformationNotHidden() != null) {
            List<AttributeClassInformation> information1 = father.getInformationNotHidden();
            information.addAll(information1);
        }
        return information;
    }

}
