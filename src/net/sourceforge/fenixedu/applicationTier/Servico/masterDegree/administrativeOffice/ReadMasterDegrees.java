/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadMasterDegrees implements IService {

    public List run(String executionYearString) throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = new ArrayList();
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Get the Actual Execution Year
            IExecutionYear executionYear = null;
            if (executionYearString != null) {
                executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(
                        executionYearString);
            } else {
                IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
                executionYear = executionYearDAO.readCurrentExecutionYear();
            }

            // Read the degrees
            result = sp.getIPersistentExecutionDegree().readMasterDegrees(executionYear.getYear());
            if (result == null || result.size() == 0) {
                throw new NonExistingServiceException();
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        List degrees = new ArrayList();
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner
                    .get((IExecutionDegree) iterator.next());
            degrees.add(infoExecutionDegree);
        }

        return degrees;

    }
}