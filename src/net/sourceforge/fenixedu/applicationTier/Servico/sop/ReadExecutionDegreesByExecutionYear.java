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
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionDegreesByExecutionYear implements IService {

    public ReadExecutionDegreesByExecutionYear() {
    }

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        List infoExecutionDegreeList = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            List executionDegrees = null;
            if (infoExecutionYear == null) {
                IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
                IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
                executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear.getYear());
            } else {
                executionDegrees = executionDegreeDAO.readByExecutionYear(infoExecutionYear.getYear());
            }

            Iterator iterator = executionDegrees.iterator();
            infoExecutionDegreeList = new ArrayList();

            while (iterator.hasNext()) {
                IExecutionDegree executionDegree = (IExecutionDegree) iterator.next();
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYear
                        .newInfoFromDomain(executionDegree);
                if (executionDegree.getCurricularPlan() == null) {
                } else {
                    infoExecutionDegree.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                            .newInfoFromDomain(executionDegree.getCurricularPlan()));
                    if (executionDegree.getCurricularPlan().getDegree() == null) {
                    } else
                        infoExecutionDegree.getInfoDegreeCurricularPlan().setInfoDegree(
                                InfoDegree.newInfoFromDomain(executionDegree.getCurricularPlan()
                                        .getDegree()));
                }
                infoExecutionDegreeList.add(infoExecutionDegree);
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegreeList;
    }

}