import classmodel.MyUmlClassModelInteraction;
import collaboration.MyUmlCollaborationInteraction;
import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.format.UmlStandardPreCheck;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import statechart.MyUmlStateChartInteraction;

import java.util.HashSet;

public class MyUmlStandardPreCheck implements UmlStandardPreCheck {
    private MyUmlClassModelInteraction modelInteraction;
    private MyUmlStateChartInteraction chartInteraction;
    private MyUmlCollaborationInteraction collaborationInteraction;

    public MyUmlStandardPreCheck(MyUmlClassModelInteraction modelInteraction,
                                 MyUmlStateChartInteraction chartInteraction,
                                 MyUmlCollaborationInteraction collaborationInteraction) {
        this.modelInteraction = modelInteraction;
        this.chartInteraction = chartInteraction;
        this.collaborationInteraction = collaborationInteraction;
    }

    @Override
    public void checkForUml001() throws UmlRule001Exception {
        HashSet<AttributeClassInformation> information = modelInteraction.role1Check();
        if (!information.isEmpty()) {
            throw new UmlRule001Exception(information);
        }
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        HashSet<UmlClassOrInterface> information = modelInteraction.getCircularInheritance();
        if (!information.isEmpty()) {
            throw new UmlRule002Exception(information);
        }
    }

    @Override
    public void checkForUml003() throws UmlRule003Exception {
        HashSet<UmlClassOrInterface> information = modelInteraction.getDupGeneralization();
        if (!information.isEmpty()) {
            throw new UmlRule003Exception(information);
        }
    }

    @Override
    public void checkForUml004() throws UmlRule004Exception {
        HashSet<UmlClass> information = modelInteraction.getDupInterfaceOfClass();
        if (!information.isEmpty()) {
            throw new UmlRule004Exception(information);
        }
    }

    @Override
    public void checkForUml005() throws UmlRule005Exception {
        if (!modelInteraction.isAllNameExist()) {
            throw new UmlRule005Exception();
        }
    }

    @Override
    public void checkForUml006() throws UmlRule006Exception {
        if (!modelInteraction.checkInterfaceVisibility()) {
            throw new UmlRule006Exception();
        }
    }

    @Override
    public void checkForUml007() throws UmlRule007Exception {
        if (!collaborationInteraction.checkLifeline()) {
            throw new UmlRule007Exception();
        }
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        if (!chartInteraction.checkInitialState()) {
            throw new UmlRule008Exception();
        }
    }

}
