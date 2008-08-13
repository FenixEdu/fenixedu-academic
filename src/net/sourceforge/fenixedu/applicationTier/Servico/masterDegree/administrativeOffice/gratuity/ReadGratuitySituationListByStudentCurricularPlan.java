package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadGratuitySituationListByStudentCurricularPlan extends Service {

    public List run(Integer studentCurricularPlanID) throws FenixServiceException{
        List infoGratuitySituations = new ArrayList();
        List gratuitySituations = null;
        GratuitySituation gratuitySituation = null;

        StudentCurricularPlan studentCurricularPlan = rootDomainObject
                .readStudentCurricularPlanByOID(studentCurricularPlanID);

        gratuitySituations = studentCurricularPlan.getGratuitySituations();

        InfoGratuitySituation infoGratuitySituation = null;
        for (Iterator iter = gratuitySituations.iterator(); iter.hasNext();) {
            gratuitySituation = (GratuitySituation) iter.next();

            if (gratuitySituation != null) {
                infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
                        .newInfoFromDomain(gratuitySituation);

                infoGratuitySituations.add(infoGratuitySituation);
            }
        }

        return infoGratuitySituations;
    }
}
