package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.util.PublicationArea;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AddResultToTeacherInformationSheet {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(Teacher teacher, Integer resultId, String publicationArea) throws Exception {

        ResearchResult result = RootDomainObject.getInstance().readResearchResultByOID(resultId);
        teacher.addToTeacherInformationSheet(result, PublicationArea.getEnum(publicationArea));
    }

}