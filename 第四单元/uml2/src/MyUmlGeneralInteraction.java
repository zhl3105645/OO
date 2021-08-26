import classmodel.MyUmlClassModelInteraction;
import collaboration.MyUmlCollaborationInteraction;
import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.OperationParamInformation;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import statechart.MyUmlStateChartInteraction;

import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {

    private MyUmlClassModelInteraction modelInteraction;
    private MyUmlCollaborationInteraction collaborationInteraction;
    private MyUmlStateChartInteraction stateChartInteraction;

    public MyUmlGeneralInteraction(UmlElement... elements) {
        modelInteraction = new MyUmlClassModelInteraction(elements);
        collaborationInteraction = new MyUmlCollaborationInteraction(elements);
        stateChartInteraction = new MyUmlStateChartInteraction(elements);
    }

    @Override
    public int getClassCount() {
        return modelInteraction.getClassCount();
    }

    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return modelInteraction.getClassOperationCount(className);
    }

    @Override
    public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return modelInteraction.getClassAttributeCount(className);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className,
                                                                String operationName) throws
            ClassNotFoundException, ClassDuplicatedException {
        return modelInteraction.getClassOperationVisibility(className, operationName);
    }

    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName) throws
            ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return modelInteraction.getClassAttributeVisibility(className, attributeName);
    }

    @Override
    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        return modelInteraction.getClassAttributeType(className, attributeName);
    }

    @Override
    public List<OperationParamInformation> getClassOperationParamType(
            String className, String operationName
    ) throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return modelInteraction.getClassOperationParamType(className, operationName);
    }

    @Override
    public List<String> getClassAssociatedClassList(String className) throws
            ClassNotFoundException, ClassDuplicatedException {
        return modelInteraction.getClassAssociatedClassList(className);
    }

    @Override
    public List<String> getImplementInterfaceList(String className) throws
            ClassNotFoundException, ClassDuplicatedException {
        return modelInteraction.getImplementInterfaceList(className);
    }

    @Override
    public String getTopParentClass(String className) throws
            ClassNotFoundException, ClassDuplicatedException {
        return modelInteraction.getTopParentClass(className);
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className) throws
            ClassNotFoundException, ClassDuplicatedException {
        return modelInteraction.getInformationNotHidden(className);
    }

    @Override
    public int getStateCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateChartInteraction.getStateCount(stateMachineName);
    }

    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return stateChartInteraction.getSubsequentStateCount(stateMachineName, stateName);
    }

    @Override
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateMachineDuplicatedException, TransitionNotFoundException,
            StateDuplicatedException, StateMachineNotFoundException, StateNotFoundException {
        return stateChartInteraction.getTransitionTrigger(stateMachineName,
                sourceStateName, targetStateName);
    }

    @Override
    public int getParticipantCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        return collaborationInteraction.getParticipantCount(interactionName);
    }

    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return collaborationInteraction.getIncomingMessageCount(interactionName, lifelineName);
    }

    @Override
    public int getSentMessageCount(String interactionName, String lifelineName, MessageSort sort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return collaborationInteraction.getSentMessageCount(interactionName, lifelineName, sort);
    }
}
