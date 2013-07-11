package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.util.PublicationArea;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddResultToTeacherInformationSheet {

    @Checked("ResultPredicates.author")
    @Atomic
    public static void run(Teacher teacher, String resultId, String publicationArea) throws Exception {

        ResearchResult result = FenixFramework.getDomainObject(resultId);
        teacher.addToTeacherInformationSheet(result, PublicationArea.getEnum(publicationArea));
    }

}