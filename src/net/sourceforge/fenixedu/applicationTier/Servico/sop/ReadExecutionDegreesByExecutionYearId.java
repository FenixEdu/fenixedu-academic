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

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionDegreesByExecutionYearId implements IService {

    public List run(Integer executionYearId) throws ExcepcaoPersistencia {

        List infoExecutionDegreeList = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

        IExecutionYear executionYear = null;
        if (executionYearId == null) {
            executionYear = persistentExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = (IExecutionYear) persistentExecutionYear.readByOID(ExecutionYear.class,
                    executionYearId);
        }

        List executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear.getYear());

        if (executionDegrees != null && executionDegrees.size() > 0) {
            Iterator iterator = executionDegrees.iterator();
            infoExecutionDegreeList = new ArrayList();

            while (iterator.hasNext()) {
                IExecutionDegree executionDegree = (IExecutionDegree) iterator.next();

                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionDegree
                        .getExecutionYear());
                infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

                IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                        .newInfoFromDomain(degreeCurricularPlan);
                infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

                IDegree degree = degreeCurricularPlan.getDegree();
                InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                infoDegreeCurricularPlan.setInfoDegree(infoDegree);

                infoExecutionDegreeList.add(infoExecutionDegree);
            }
        }

        return infoExecutionDegreeList;
    }

}