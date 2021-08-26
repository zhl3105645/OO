import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.OperationParamInformation;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlInteraction implements UmlInteraction {
    private HashMap<String, UmlElement> allElements = new HashMap<>();
    private HashMap<String, MyUmlClass> idToClass = new HashMap<>();
    private HashMap<String, MyUmlInterface> idToInterface = new HashMap<>();
    private HashMap<String, MyUmlOperation> idToOperation = new HashMap<>();
    private HashMap<String, MyUmlClass> nameToClass = new HashMap<>();
    private HashMap<String, Integer> nameToNum = new HashMap<>();
    private HashMap<String, String> endToReference = new HashMap<>();

    public MyUmlInteraction(UmlElement... elements) {
        initial1(elements);//initial the class,Interface,end
        initial2(elements);//initial the ass..,gen..,rel..,ope..,att..
        initial3(elements);//initial the par..
    }

    void initial1(UmlElement... elements) {
        for (UmlElement element : elements) {
            allElements.put(element.getId(), element);//get the all element
            if (element.getElementType() == ElementType.UML_CLASS) {
                MyUmlClass myUmlClass = new MyUmlClass((UmlClass) element);
                idToClass.put(element.getId(), myUmlClass);
                nameToClass.put(element.getName(), myUmlClass);
                if (nameToNum.containsKey(element.getName())) {
                    nameToNum.put(element.getName(), nameToNum.get(element.getName()) + 1);
                } else {
                    nameToNum.put(element.getName(), 1);
                }
            } else if (element.getElementType() == ElementType.UML_INTERFACE) {
                MyUmlInterface myUmlInterface = new MyUmlInterface((UmlInterface) element);
                idToInterface.put(element.getId(), myUmlInterface);
            } else if (element.getElementType() == ElementType.UML_ASSOCIATION_END) {
                String reference = ((UmlAssociationEnd) element).getReference();
                endToReference.put(element.getId(), reference);
            }
        }
    }

    void initial2(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_ASSOCIATION) {
                UmlAssociation umlAssociation = (UmlAssociation) element;
                if (idToClass.containsKey(endToReference.get(umlAssociation.getEnd1()))
                        && idToClass.containsKey(endToReference.get(umlAssociation.getEnd2()))) {
                    MyUmlClass class1 = idToClass.get(endToReference.get(umlAssociation.getEnd1()));
                    MyUmlClass class2 = idToClass.get(endToReference.get(umlAssociation.getEnd2()));
                    class1.addAssociation(class2);
                    class2.addAssociation(class1);
                }
            } else if (element.getElementType() == ElementType.UML_GENERALIZATION) {
                UmlGeneralization umlGeneralization = (UmlGeneralization) element;
                String source = umlGeneralization.getSource();
                String target = umlGeneralization.getTarget();
                if (idToClass.containsKey(source) && idToClass.containsKey(target)) {
                    idToClass.get(source).setFather(idToClass.get(target));
                } else if (idToInterface.containsKey(source)
                        && idToInterface.containsKey(target)) {
                    idToInterface.get(source).addFather(idToInterface.get(target));
                }
            } else if (element.getElementType() == ElementType.UML_INTERFACE_REALIZATION) {
                UmlInterfaceRealization umlInterfaceRealization = (UmlInterfaceRealization) element;
                String source = umlInterfaceRealization.getSource();
                String target = umlInterfaceRealization.getTarget();
                if (idToInterface.containsKey(target) && idToClass.containsKey(source)) {
                    idToClass.get(source).addInterface(idToInterface.get(target));
                }
            } else if (element.getElementType() == ElementType.UML_OPERATION) {
                UmlOperation umlOperation = (UmlOperation) element;
                MyUmlOperation myUmlOperation = new MyUmlOperation(umlOperation);
                idToOperation.put(umlOperation.getId(), myUmlOperation);
                if (idToClass.containsKey(umlOperation.getParentId())) {
                    idToClass.get(umlOperation.getParentId()).addOperation(myUmlOperation);
                }
            } else if (element.getElementType() == ElementType.UML_ATTRIBUTE) {
                UmlAttribute umlAttribute = (UmlAttribute) element;
                if (idToClass.containsKey(umlAttribute.getParentId())) {
                    idToClass.get(umlAttribute.getParentId()).addAttribute(umlAttribute);
                }
            }
        }
    }

    void initial3(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_PARAMETER) {
                UmlParameter parameter = (UmlParameter) element;
                if (idToOperation.containsKey(parameter.getParentId())) {
                    idToOperation.get(parameter.getParentId()).addParameter(parameter, allElements);
                }
            }
        }
    }

    boolean checkName(String name) {
        switch (name) {
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "char":
            case "boolean":
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getClassCount() {
        return idToClass.size();
    }

    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return nameToClass.get(className).getOperationNum();
        }
    }

    @Override
    public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return nameToClass.get(className).getAttributeNum();
        }
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className,
                                                                String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return nameToClass.get(className).getOperationVisibility(operationName);
        }
    }

    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            ArrayList<UmlAttribute> attributes
                    = nameToClass.get(className).getAllNameAttribute(attributeName);
            if (attributes.size() == 0) {
                throw new AttributeNotFoundException(className, attributeName);
            } else if (attributes.size() > 1) {
                throw new AttributeDuplicatedException(className, attributeName);
            } else {
                return attributes.get(0).getVisibility();
            }
        }
    }

    @Override
    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            ArrayList<UmlAttribute> attributes
                    = nameToClass.get(className).getAllNameAttribute(attributeName);
            if (attributes.size() == 0) {
                throw new AttributeNotFoundException(className, attributeName);
            } else if (attributes.size() > 1) {
                throw new AttributeDuplicatedException(className, attributeName);
            } else {
                NameableType type = attributes.get(0).getType();
                if (type instanceof NamedType) {
                    String name = ((NamedType) type).getName();
                    if (checkName(name) || name.equals("String")) {
                        return name;
                    } else {
                        throw new AttributeWrongTypeException(className, attributeName);
                    }
                } else if (type instanceof ReferenceType) {
                    String id = ((ReferenceType) type).getReferenceId();             //////////
                    return allElements.get(id).getName();
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public List<OperationParamInformation> getClassOperationParamType(
            String className, String operationName
    ) throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            List<OperationParamInformation> information = new ArrayList<>();
            ArrayList<MyUmlOperation> operations
                    = nameToClass.get(className).getNameOperation(operationName);
            ArrayList<String> returnTypes = new ArrayList<>();
            ArrayList<HashMap<String, Integer>> typeToNums = new ArrayList<>();
            for (MyUmlOperation operation : operations) {
                if (!operation.checkParameter()) {
                    throw new MethodWrongTypeException(className, operationName);
                }
                returnTypes.add(operation.getReturnType());
                typeToNums.add(operation.getTypeToNum());
                List<String> parametersType = new ArrayList<>();
                for (String key : operation.getTypeToNum().keySet()) {
                    int num = operation.getTypeToNum().get(key);
                    for (int i = 0; i < num; i++) {
                        parametersType.add(key);
                    }
                }
                OperationParamInformation information1 =
                        new OperationParamInformation(parametersType, operation.getReturnType());
                information.add(information1);
            }
            for (int i = 0; i < operations.size(); i++) {
                for (int j = i + 1; j < operations.size(); j++) {
                    if ((returnTypes.get(i) == null && returnTypes.get(j) == null)
                            || (returnTypes.get(i) != null
                            && returnTypes.get(i).equals(returnTypes.get(j)))) {
                        HashMap<String, Integer> map1 = typeToNums.get(i);
                        HashMap<String, Integer> map2 = typeToNums.get(j);
                        if (map1.size() == map2.size()) {
                            boolean flag = true;
                            for (String key : map1.keySet()) {
                                if (!(map2.containsKey(key)
                                        && map1.get(key).equals(map2.get(key)))) {
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                throw new MethodDuplicatedException(className, operationName);
                            }
                        }
                    }
                }
            }
            return information;
        }
    }

    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            HashMap<String, String> idToName = nameToClass.get(className).getAssociateClass();
            return new ArrayList<>(idToName.values());
        }
    }

    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return nameToClass.get(className).getTopClass();
        }
    }

    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            HashMap<String, Boolean> visit = new HashMap<>();//interface id -> boolean
            HashMap<String, String> idToName = nameToClass.get(className).getAllInterface(visit);
            return new ArrayList<>(idToName.values());
        }
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameToNum.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (nameToNum.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return nameToClass.get(className).getInformationNotHidden();
        }
    }
}
