/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadGratuitySituationByStudentCurricularPlanByGratuityValues extends Service {

    public Object run(Integer studentCurricularPlanID, Integer gratuityValuesID)
            throws FenixServiceException{

        GratuitySituation gratuitySituation = null;

        StudentCurricularPlan studentCurricularPlan = rootDomainObject
                .readStudentCurricularPlanByOID(studentCurricularPlanID);
        GratuityValues gratuityValues = rootDomainObject.readGratuityValuesByOID(gratuityValuesID);
        gratuitySituation = studentCurricularPlan.getGratuitySituationByGratuityValues(gratuityValues);

        InfoGratuitySituation infoGratuitySituation = null;
        if (gratuitySituation != null) {
            infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
                    .newInfoFromDomain(gratuitySituation);
        }

        return infoGratuitySituation;
    }
}
