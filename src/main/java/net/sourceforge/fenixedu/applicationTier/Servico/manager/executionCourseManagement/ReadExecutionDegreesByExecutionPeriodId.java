package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadExecutionDegreesByExecutionPeriodId {

    @Service
    public static List<InfoExecutionDegree> run(Integer executionPeriodId) throws FenixServiceException {
        if (executionPeriodId == null) {
            throw new FenixServiceException("executionPeriodId.should.not.be.null");
        }
        ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);

        List<ExecutionDegree> executionDegrees =
                ExecutionDegree.getAllByExecutionYear(executionSemester.getExecutionYear().getYear());

        List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            infoExecutionDegreeList.add(infoExecutionDegree);
        }
        return infoExecutionDegreeList;
    }
}