package statechart;

import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlStateMachine {
    private UmlStateMachine umlStateMachine;
    private HashMap<String, ArrayList<MyUmlState>> nameToState = new HashMap<>();
    private ArrayList<MyUmlState> states = new ArrayList<>();
    private ArrayList<UmlTransition> transitions = new ArrayList<>();
    private HashMap<UmlTransition, UmlEvent> triggers = new HashMap<>();

    public MyUmlStateMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
    }

    public void addState(MyUmlState myUmlState) {
        states.add(myUmlState);
        if (!nameToState.containsKey(myUmlState.getName())) {
            nameToState.put(myUmlState.getName(), new ArrayList<>());
        }
        nameToState.get(myUmlState.getName()).add(myUmlState);
    }

    public void addTransition(UmlTransition transition) {
        transitions.add(transition);
    }

    public void addTriggers(UmlTransition umlTransition, UmlEvent umlEvent) {
        triggers.put(umlTransition, umlEvent);
    }

    public int getStateNum() {
        return states.size();
    }

    public int getTransitionNum() {
        return transitions.size();
    }

    public ArrayList<MyUmlState> getStatesByName(String stateName) {
        ArrayList<MyUmlState> states = new ArrayList<>();
        if (nameToState.containsKey(stateName)) {
            states.addAll(nameToState.get(stateName));
        }
        return states;
    }

    public HashMap<String, ArrayList<MyUmlState>> getStates() {
        return nameToState;
    }

    public boolean getTransition(String sourceName, String targetName) {
        for (UmlTransition e : transitions) {
            if (e.getSource().equals(nameToState.get(sourceName).get(0).getId())
                    && e.getTarget().equals(nameToState.get(targetName).get(0).getId())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getTransitionTriggers(String sourceName, String targetName) {
        ArrayList<String> eventName = new ArrayList<>();
        for (UmlTransition e : transitions) {
            if (e.getSource().equals(nameToState.get(sourceName).get(0).getId())
                    && e.getTarget().equals(nameToState.get(targetName).get(0).getId())) {
                if (triggers.containsKey(e)) {
                    eventName.add(triggers.get(e).getName());
                }
            }
        }
        return eventName;
    }

}
