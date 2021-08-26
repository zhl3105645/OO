package statechart;

import com.oocourse.uml3.models.elements.UmlElement;
import java.util.ArrayList;

public class MyUmlState {
    private UmlElement umlState;// state, initial, final
    private ArrayList<MyUmlState> toStates = new ArrayList<>();

    public MyUmlState(UmlElement state) {
        this.umlState = state;
    }

    public UmlElement getUmlState() {
        return umlState;
    }

    public void addToState(MyUmlState myUmlState) {
        toStates.add(myUmlState);
    }

    public ArrayList<MyUmlState> getToStates() {
        return toStates;
    }

    public boolean hasToState() {
        return toStates.size() != 0;
    }
}
