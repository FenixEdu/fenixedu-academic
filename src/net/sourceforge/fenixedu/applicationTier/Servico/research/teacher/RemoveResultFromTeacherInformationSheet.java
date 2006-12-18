package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveResultFromTeacherInformationSheet extends Service {
    
    public void run(Teacher teacher, Integer resultId) throws ExcepcaoPersistencia, DomainException {
        ResearchResult result = (ResearchResult) rootDomainObject.readResearchResultByOID(resultId);        
        teacher.removeFromTeacherInformationSheet(result);
    }
    
}