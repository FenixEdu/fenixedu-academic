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

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoDegreeCurricularPlanAndExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionDegreesByExecutionYear implements IService {

    public List run(InfoExecutionYear infoExecutionYear) throws ExcepcaoPersistencia {

        System.out.println("infoExecutionYear: " + infoExecutionYear);

        final List infoExecutionDegreeList = new ArrayList();

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

        final List executionDegrees = readExecutionDegrees(infoExecutionYear, sp, executionDegreeDAO);

        for (final Iterator iterator = executionDegrees.iterator(); iterator.hasNext();) {
            final IExecutionDegree executionDegree = (IExecutionDegree) iterator.next();
            if (executionDegree != null) {
                final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoDegreeCurricularPlanAndExecutionYear
                        .newInfoFromDomain(executionDegree);
                System.out.print("infoExecutionDegree" + infoExecutionDegree.getIdInternal());
                System.out.print("infodcp" + infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());
                System.out.print("infodegree" + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal());
                infoExecutionDegreeList.add(infoExecutionDegree);
            }
        }

        return infoExecutionDegreeList;
    }

    private List readExecutionDegrees(final InfoExecutionYear infoExecutionYear,
            final ISuportePersistente sp, final IPersistentExecutionDegree executionDegreeDAO)
            throws ExcepcaoPersistencia {
        if (infoExecutionYear == null) {
            final IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            final IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
            return executionDegreeDAO.readByExecutionYear(executionYear.getYear());
        }
        return executionDegreeDAO.readByExecutionYear(infoExecutionYear.getYear());
    }

}