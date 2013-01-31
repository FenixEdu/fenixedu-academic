package net.sourceforge.fenixedu.presentationTier.Action.teacher.internationalRelatOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "internationalRelatOffice", path = "/viewStudentCurriculum", scope = "session", parameter = "method")
@Forwards(value = { @Forward(
		name = "prepareViewStudentCurriculumChooseStudent",
		path = "df.page.prepareViewStudentCurriculumChooseStudent") })
public class ViewStudentCurriculumDispatchActionForInternationalRelatOffice extends
		net.sourceforge.fenixedu.presentationTier.Action.teacher.ViewStudentCurriculumDispatchAction {
}