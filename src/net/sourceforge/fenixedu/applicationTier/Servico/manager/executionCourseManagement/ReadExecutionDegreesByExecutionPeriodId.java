package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadExecutionDegreesByExecutionPeriodId extends Service {

    public List run(Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {

        List infoExecutionDegreeList = null;

        IPersistentExecutionDegree executionDegreeDAO = persistentSupport.getIPersistentExecutionDegree();

        if (executionPeriodId == null) {
            throw new FenixServiceException("nullId");
        }

        ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, executionPeriodId);

        List executionDegrees = executionDegreeDAO.readByExecutionYear(executionPeriod
                .getExecutionYear().getYear());

        Iterator iterator = executionDegrees.iterator();
        infoExecutionDegreeList = new ArrayList();

        while (iterator.hasNext()) {
            final ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);

            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

            final Degree degree = degreeCurricularPlan.getDegree();
            final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
            infoDegreeCurricularPlan.setInfoDegree(infoDegree);

            infoExecutionDegreeList.add(infoExecutionDegree);
        }

        return infoExecutionDegreeList;
    }
}