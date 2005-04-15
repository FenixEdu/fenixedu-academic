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
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
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

    public List run(String executionYearString) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Get the Actual Execution Year
        final IExecutionYear executionYear;
        if (executionYearString != null) {
            executionYear = sp.getIPersistentExecutionYear()
                    .readExecutionYearByName(executionYearString);
        } else {
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
            executionYear = executionYearDAO.readCurrentExecutionYear();
        }

        // Read the degrees
        final List result = sp.getIPersistentExecutionDegree().readMasterDegrees(executionYear.getYear());
        if (result == null || result.size() == 0) {
            throw new NonExistingServiceException();
        }

        final List degrees = new ArrayList(result.size());
        for (final Iterator iterator = result.iterator(); iterator.hasNext(); ) {
            final IExecutionDegree executionDegree = (IExecutionDegree) iterator.next();
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                    .newInfoFromDomain(executionDegree);

            final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

            final IDegree degree = degreeCurricularPlan.getDegree();
            final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
            infoDegreeCurricularPlan.setInfoDegree(infoDegree);

            degrees.add(infoExecutionDegree);
        }

        return degrees;
    }
}