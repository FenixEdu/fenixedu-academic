/*
 * Created on 14/Mar/2003
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadMasterDegrees implements IService {

    public List run(String executionYearString) throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = new ArrayList();
        try {
            sp = SuportePersistenteOJB.getInstance();

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
                    .get((ICursoExecucao) iterator.next());
            degrees.add(infoExecutionDegree);
        }

        return degrees;

    }
}