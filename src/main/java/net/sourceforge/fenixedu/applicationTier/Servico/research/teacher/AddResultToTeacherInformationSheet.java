package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import net.sourceforge.fenixedu.util.PublicationArea;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddResultToTeacherInformationSheet {

    @Atomic
    public static void run(Teacher teacher, String resultId, String publicationArea) throws Exception {
        check(ResultPredicates.author);

        ResearchResult result = FenixFramework.getDomainObject(resultId);
        teacher.addToTeacherInformationSheet(result, PublicationArea.getEnum(publicationArea));
    }

}