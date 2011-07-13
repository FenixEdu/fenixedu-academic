package net.sourceforge.fenixedu.presentationTier.Action.teacher.teacher;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "teacher", path = "/viewStudentCurriculum", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "prepareViewStudentCurriculumChooseStudent", path = "df.page.prepareViewStudentCurriculumChooseStudent") })
public class ViewStudentCurriculumDispatchActionForTeacher extends net.sourceforge.fenixedu.presentationTier.Action.teacher.ViewStudentCurriculumDispatchAction {
}