package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoveResultFromTeacherInformationSheet {

    @Atomic
    public static void run(Teacher teacher, String resultId) throws DomainException {
        check(ResultPredicates.author);
        ResearchResult result = FenixFramework.getDomainObject(resultId);
        teacher.removeFromTeacherInformationSheet(result);
    }

}