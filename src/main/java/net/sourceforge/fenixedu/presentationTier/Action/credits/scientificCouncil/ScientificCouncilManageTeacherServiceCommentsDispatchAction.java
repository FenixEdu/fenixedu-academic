package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherServiceCommentsDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/teacherServiceComments",
        functionality = ScientificCouncilViewTeacherCreditsDA.class)
@Forwards(value = {
        @Forward(name = "editTeacherServiceComment", path = "/credits/degreeTeachingService/editTeacherServiceComment.jsp"),
        @Forward(name = "viewAnnualTeachingCredits", path = "/scientificCouncil/credits.do?method=viewAnnualTeachingCredits") })
public class ScientificCouncilManageTeacherServiceCommentsDispatchAction extends ManageTeacherServiceCommentsDispatchAction {
}
