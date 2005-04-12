/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 *  
 */
public class ReadExecutionDegreesByExecutionYearAndDegree implements IService {
    private static ReadExecutionDegreesByExecutionYearAndDegree service = new ReadExecutionDegreesByExecutionYearAndDegree();

    public ReadExecutionDegreesByExecutionYearAndDegree() {
    }

    public String getNome() {
        return "ReadExecutionDegreesByExecutionYearAndDegree";
    }

    public static ReadExecutionDegreesByExecutionYearAndDegree getService() {
        return service;
    }

    public Object run(IDegree curso, IExecutionYear year) throws FenixServiceException {
        List infoExecutionDegrees = new ArrayList();

        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            List executionDegrees = executionDegreeDAO.readByDegreeAndExecutionYear(curso, year);
            if (executionDegrees != null && !executionDegrees.isEmpty()) {
                for (int i = 0; i < executionDegrees.size(); i++) {
                    IExecutionDegree executionDegree = (IExecutionDegree) executionDegrees.get(i);
                    InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                    infoExecutionDegrees.add(infoExecutionDegree);
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegrees;
    }

}