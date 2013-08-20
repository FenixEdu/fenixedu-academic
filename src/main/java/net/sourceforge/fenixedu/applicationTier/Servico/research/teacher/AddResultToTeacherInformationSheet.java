package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.util.PublicationArea;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class AddResultToTeacherInformationSheet {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(Teacher teacher, String resultId, String publicationArea) throws Exception {

        ResearchResult result = AbstractDomainObject.fromExternalId(resultId);
        teacher.addToTeacherInformationSheet(result, PublicationArea.getEnum(publicationArea));
    }

}