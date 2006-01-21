package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servico LerSalas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

public class ReadExecutionDegreesByExecutionYearId extends Service {

    public List run(Integer executionYearId) throws ExcepcaoPersistencia {

        List infoExecutionDegreeList = null;

        IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
        IPersistentExecutionDegree executionDegreeDAO = persistentSupport.getIPersistentExecutionDegree();

        ExecutionYear executionYear = null;
        if (executionYearId == null) {
            executionYear = persistentExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = (ExecutionYear) persistentExecutionYear.readByOID(ExecutionYear.class,
                    executionYearId);
        }

        List executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear.getYear());

        if (executionDegrees != null && executionDegrees.size() > 0) {
            Iterator iterator = executionDegrees.iterator();
            infoExecutionDegreeList = new ArrayList();

            while (iterator.hasNext()) {
                ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();

                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionDegree
                        .getExecutionYear());
                infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

                DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                        .newInfoFromDomain(degreeCurricularPlan);
                infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

                Degree degree = degreeCurricularPlan.getDegree();
                InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                infoDegreeCurricularPlan.setInfoDegree(infoDegree);

                infoExecutionDegreeList.add(infoExecutionDegree);
            }
        }

        return infoExecutionDegreeList;
    }

}