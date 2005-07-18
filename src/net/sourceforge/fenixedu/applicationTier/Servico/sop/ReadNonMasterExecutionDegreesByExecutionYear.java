package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadNonMasterExecutionDegreesByExecutionYear implements IService {

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

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        String yearToSearch;
        if (infoExecutionYear.getYear() == null || infoExecutionYear.getYear().length() == 0) {
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear = executionYearDAO.readCurrentExecutionYear();
            yearToSearch = executionYear.getYear();
        } else {
            yearToSearch = infoExecutionYear.getYear();
        }

        IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
        List executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(yearToSearch,
                DegreeType.DEGREE);

        Iterator iterator = executionDegrees.iterator();
        infoExecutionDegreeList = new ArrayList();

        while (iterator.hasNext()) {
            IExecutionDegree executionDegree = (IExecutionDegree) iterator.next();
            infoExecutionDegreeList.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
        }

        return infoExecutionDegreeList;
    }

}
