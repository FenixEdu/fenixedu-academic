package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;

public class RemoveResultFromTeacherInformationSheet extends FenixService {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static void run(Teacher teacher, Integer resultId) throws DomainException {
	ResearchResult result = (ResearchResult) rootDomainObject.readResearchResultByOID(resultId);
	teacher.removeFromTeacherInformationSheet(result);
    }

}