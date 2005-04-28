package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

public class ReadNonMasterExecutionDegreesByExecutionYear implements IServico {

    private static ReadNonMasterExecutionDegreesByExecutionYear service = new ReadNonMasterExecutionDegreesByExecutionYear();

    /**
     * The singleton access method of this class.
     */
    public static ReadNonMasterExecutionDegreesByExecutionYear getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private ReadNonMasterExecutionDegreesByExecutionYear() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadNonMasterExecutionDegreesByExecutionYear";
    }

    /**
     * @param infoExecutionYear
     *            if this parameter is null it returns the current execution
     *            year
     * @return @throws
     *         FenixServiceException
     */
    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        List infoExecutionDegreeList = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionYear executionYear;
            if (infoExecutionYear == null) {
                IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
                executionYear = executionYearDAO.readCurrentExecutionYear();
            } else {
                executionYear = InfoExecutionYear.newDomainFromInfo(infoExecutionYear);
            }

            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
            List executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(executionYear,
                    new TipoCurso(1));

            Iterator iterator = executionDegrees.iterator();
            infoExecutionDegreeList = new ArrayList();

            while (iterator.hasNext()) {
                IExecutionDegree executionDegree = (IExecutionDegree) iterator.next();
                infoExecutionDegreeList.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegreeList;
    }
}