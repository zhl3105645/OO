package statechart;

import java.util.ArrayList;

public class MyUmlState {
    private ArrayList<MyUmlState> toStates = new ArrayList<>();
    private String id;
    private String name;

    public MyUmlState(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void addToState(MyUmlState myUmlState) {
        toStates.add(myUmlState);
    }

    public ArrayList<MyUmlState> getToStates() {
        return toStates;
    }

    public boolean hasToState() {
        if (toStates.size() == 0) {
            return false;
        }
        return true;
    }
}
