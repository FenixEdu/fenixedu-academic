package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.util.PublicationArea;

public class AddResultToTeacherInformationSheet extends Service {

    public void run(Teacher teacher, Integer resultId, String publicationArea) throws Exception {
        
        ResearchResult result = (ResearchResult) rootDomainObject.readResearchResultByOID(resultId);
        teacher.addToTeacherInformationSheet(result, PublicationArea.getEnum(publicationArea));
    }
    
}
