package net.sourceforge.fenixedu.applicationTier.Servico.research.teacher;


import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class RemoveResultFromTeacherInformationSheet {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(Teacher teacher, Integer resultId) throws DomainException {
        ResearchResult result = AbstractDomainObject.fromExternalId(resultId);
        teacher.removeFromTeacherInformationSheet(result);
    }

}