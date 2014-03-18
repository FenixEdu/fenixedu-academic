package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.ReadPersonInfoOfStudentsAction;


@Mapping(path = "/chooseStudentToVisualizeInformations", module = "masterDegreeAdministrativeOffice", formBean = "changePersonalInfoForm")
@Forwards(value = {
	@Forward(name = "Success", path = "/student/visualizePersonalStudentInfo.jsp")})
public class ReadPersonInfoOfStudentsAction_AM1 extends ReadPersonInfoOfStudentsAction {

}
