package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoveResultFromTeacherInformationSheet {

    @Checked("ResultPredicates.author")
    @Atomic
    public static void run(Teacher teacher, String resultId) throws DomainException {
        ResearchResult result = FenixFramework.getDomainObject(resultId);
        teacher.removeFromTeacherInformationSheet(result);
    }

}