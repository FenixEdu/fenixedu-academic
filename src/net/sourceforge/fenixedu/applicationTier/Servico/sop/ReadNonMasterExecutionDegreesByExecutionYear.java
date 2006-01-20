package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadNonMasterExecutionDegreesByExecutionYear extends Service {

    /**
     * @param infoExecutionYear
     *            if this parameter is null it returns the current execution
     *            year
     * @return
     * @throws FenixServiceException
     * @throws ExcepcaoPersistencia
     */
    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException,
            ExcepcaoPersistencia {

        List infoExecutionDegreeList = null;

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        String yearToSearch;
        if (infoExecutionYear.getYear() == null || infoExecutionYear.getYear().length() == 0) {
            IPersistentExecutionYear executionYearDAO = persistentSupport.getIPersistentExecutionYear();
            ExecutionYear executionYear = executionYearDAO.readCurrentExecutionYear();
            yearToSearch = executionYear.getYear();
        } else {
            yearToSearch = infoExecutionYear.getYear();
        }

        IPersistentExecutionDegree executionDegreeDAO = persistentSupport.getIPersistentExecutionDegree();
        List executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(yearToSearch,
                DegreeType.DEGREE);

        Iterator iterator = executionDegrees.iterator();
        infoExecutionDegreeList = new ArrayList();

        while (iterator.hasNext()) {
            ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
            infoExecutionDegreeList.add(InfoExecutionDegreeWithInfoDegreeCurricularPlan.newInfoFromDomain(executionDegree));
        }

        return infoExecutionDegreeList;
    }

}
