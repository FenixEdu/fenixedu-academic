package net.sourceforge.fenixedu.presentationTier.Action.commons.student.teacher;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "teacher", path = "/viewCurriculumGraph", scope = "request", parameter = "method")
public class ViewCurriculumGraphDispatchActionForTeacher extends
        net.sourceforge.fenixedu.presentationTier.Action.commons.student.ViewCurriculumGraphDispatchAction {
}