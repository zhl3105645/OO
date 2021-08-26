package statechart;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartInteraction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyUmlStateChartInteraction implements UmlStateChartInteraction {
    private HashMap<String, String> idToParent = new HashMap<>();// id -> parentId
    private HashMap<String, MyUmlStateMachine> stateMachines = new HashMap<>();
    private HashMap<String, UmlRegion> regions = new HashMap<>();
    private HashMap<String, MyUmlState> states = new HashMap<>();
    private HashMap<String, UmlTransition> transitions = new HashMap<>();
    private HashMap<String, ArrayList<String>> nameToId = new HashMap<>();// name -> id

    public MyUmlStateChartInteraction(UmlElement... elements) {
        ArrayList<UmlElement> elements0 = new ArrayList<>();
        ArrayList<UmlElement> elements1 = new ArrayList<>();
        ArrayList<UmlElement> elements2 = new ArrayList<>();
        ArrayList<UmlElement> elements3 = new ArrayList<>();
        for (UmlElement e : elements) {
            idToParent.put(e.getId(), e.getParentId());
            if (e.getElementType() == ElementType.UML_STATE_MACHINE) {
                MyUmlStateMachine myUmlStateMachine = new MyUmlStateMachine((UmlStateMachine) e);
                stateMachines.put(e.getId(), myUmlStateMachine);
                if (!nameToId.containsKey(e.getName())) {
                    nameToId.put(e.getName(), new ArrayList<>());
                }
                nameToId.get(e.getName()).add(e.getId());
            }
            if (e.getElementType() == ElementType.UML_REGION) {
                elements0.add(e);
            } else if (e.getElementType() == ElementType.UML_STATE
                    || e.getElementType() == ElementType.UML_PSEUDOSTATE
                    || e.getElementType() == ElementType.UML_FINAL_STATE) {
                elements1.add(e);
            } else if (e.getElementType() == ElementType.UML_TRANSITION) {
                elements2.add(e);
            } else if (e.getElementType() == ElementType.UML_EVENT) {
                elements3.add(e);
            }
        }
        for (UmlElement e : elements0) {
            regions.put(e.getId(), (UmlRegion) e);
        }
        for (UmlElement e : elements1) {
            String regionId = e.getParentId();
            String stateMachineId = idToParent.get(regionId);
            MyUmlState myUmlState = new MyUmlState(e.getId(), e.getName());
            states.put(e.getId(), myUmlState);
            if (stateMachines.containsKey(stateMachineId)) {
                stateMachines.get(stateMachineId).addState(myUmlState);
            }
        }
        for (UmlElement e : elements2) {
            String regionId = e.getParentId();
            String stateMachineId = idToParent.get(regionId);
            String target = ((UmlTransition) e).getTarget();
            String source = ((UmlTransition) e).getSource();
            if (states.containsKey(source)) {
                states.get(source).addToState(states.get(target));
            }
            if (stateMachines.containsKey(stateMachineId)) {
                stateMachines.get(stateMachineId).addTransition((UmlTransition) e);
                transitions.put(e.getId(), (UmlTransition) e);
            }
        }
        for (UmlElement e : elements3) {
            String transitionId = e.getParentId();
            String regionId = idToParent.get(transitionId);
            String stateMachineId = idToParent.get(regionId);
            stateMachines.get(stateMachineId)
                    .addTriggers(transitions.get(transitionId), (UmlEvent) e);
        }
    }

    @Override
    public int getStateCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!nameToId.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (nameToId.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        String stateMachineId = nameToId.get(stateMachineName).get(0);
        MyUmlStateMachine myUmlStateMachine = stateMachines.get(stateMachineId);
        return myUmlStateMachine.getStateNum();
    }

    @Override
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException,
            TransitionNotFoundException {
        if (!nameToId.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (nameToId.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        } else {
            ArrayList<MyUmlState> states = stateMachines.get(nameToId.get(stateMachineName).get(0))
                    .getStatesByName(sourceStateName);
            ArrayList<MyUmlState> states1 =
                    stateMachines.get(nameToId.get(stateMachineName).get(0))
                            .getStatesByName(targetStateName);
            if (states.isEmpty()) {
                throw new StateNotFoundException(stateMachineName, sourceStateName);
            } else if (states.size() > 1) {
                throw new StateDuplicatedException(stateMachineName, sourceStateName);
            } else if (states1.isEmpty()) {
                throw new StateNotFoundException(stateMachineName, targetStateName);
            } else if (states1.size() > 1) {
                throw new StateDuplicatedException(stateMachineName, targetStateName);
            } else if (!stateMachines.get(nameToId.get(stateMachineName).get(0))
                    .getTransition(sourceStateName, targetStateName)) {
                throw new TransitionNotFoundException(stateMachineName,
                        sourceStateName, targetStateName);
            } else {
                return stateMachines.get(nameToId.get(stateMachineName).get(0))
                        .getTransitionTriggers(sourceStateName, targetStateName);
            }
        }
    }

    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        if (!nameToId.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (nameToId.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        String stateMachineId = nameToId.get(stateMachineName).get(0);
        MyUmlStateMachine myUmlStateMachine = stateMachines.get(stateMachineId);
        ArrayList<MyUmlState> myUmlStates = myUmlStateMachine.getStatesByName(stateName);
        if (myUmlStates.isEmpty()) {
            throw new StateNotFoundException(stateMachineName, stateName);
        } else if (myUmlStates.size() > 1) {
            throw new StateDuplicatedException(stateMachineName, stateName);
        }
        MyUmlState myUmlState = myUmlStates.get(0);
        HashMap<String, ArrayList<MyUmlState>> states = myUmlStateMachine.getStates();
        LinkedList<MyUmlState> queue = new LinkedList<>();
        queue.addLast(myUmlState);
        int num = 0;
        HashMap<MyUmlState, Boolean> visited = new HashMap<>();
        while (!queue.isEmpty()) {
            ArrayList<MyUmlState> toStates;
            MyUmlState state = queue.removeFirst();
            if (state.hasToState()) {
                toStates = state.getToStates();
                for (MyUmlState e : toStates) {
                    if (states.containsKey(e.getName())) {
                        if (visited.containsKey(e)) {
                            continue;
                        }
                        visited.put(e, true);
                        queue.addLast(e);
                        num++;
                    }
                }
            }
        }
        return num;
    }
}
