import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlOperation {
    private UmlOperation operation;
    private ArrayList<UmlParameter> parameters = new ArrayList<>();
    private HashMap<String, Integer> typeToNum = new HashMap<>();//type -> num of type
    private String returnType = null;
    private boolean isLegal = true;

    public MyUmlOperation(UmlOperation umlOperation) {
        this.operation = umlOperation;
    }

    public UmlOperation getUmlOperation() {
        return operation;
    }

    public HashMap<String, Integer> getTypeToNum() {
        return typeToNum;
    }

    public String getReturnType() {
        return returnType;
    }

    public void addParameter(UmlParameter parameter, HashMap<String, UmlElement> elements) {
        parameters.add(parameter);
        NameableType type = parameter.getType();
        Direction direction = parameter.getDirection();
        String name = "";
        if (type instanceof NamedType) {
            name = ((NamedType) type).getName();
            if (!(checkName(name) || name.equals("String")
                    || (direction == Direction.RETURN && name.equals("void")))) {
                isLegal = false;
            }
        } else if (type instanceof ReferenceType) {
            String id = ((ReferenceType) type).getReferenceId();             //////////
            name = elements.get(id).getName();
        }
        if (direction == Direction.RETURN) {
            returnType = name;
        } else {
            if (typeToNum.containsKey(name)) {
                typeToNum.put(name, typeToNum.get(name) + 1);
            } else {
                typeToNum.put(name, 1);
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

    public boolean checkParameter() {
        return isLegal;
    }

}
