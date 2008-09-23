package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveResultFromTeacherInformationSheet extends FenixService {

    public void run(Teacher teacher, Integer resultId) throws DomainException {
	ResearchResult result = (ResearchResult) rootDomainObject.readResearchResultByOID(resultId);
	teacher.removeFromTeacherInformationSheet(result);
    }

}