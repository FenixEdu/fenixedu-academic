package net.sourceforge.fenixedu.presentationTier.Action.commons.student.actionMappings;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentCurriculum", module = "coordinator")
@Forwards(value = { @Forward(name = "ShowStudentCurriculum", path = "/student/displayStudentCurriculum.jsp"),
        @Forward(name = "NotAuthorized", path = "/student/notAuthorized.jsp") })
public class CurriculumDispatchAction_AM1 extends CurriculumDispatchAction {

}
