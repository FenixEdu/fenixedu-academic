/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadGratuitySituationByStudentCurricularPlanByGratuityValues {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static Object run(String studentCurricularPlanID, String gratuityValuesID) throws FenixServiceException {

        GratuitySituation gratuitySituation = null;

        StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanID);
        GratuityValues gratuityValues = FenixFramework.getDomainObject(gratuityValuesID);
        gratuitySituation = studentCurricularPlan.getGratuitySituationByGratuityValues(gratuityValues);

        InfoGratuitySituation infoGratuitySituation = null;
        if (gratuitySituation != null) {
            infoGratuitySituation =
                    InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree.newInfoFromDomain(gratuitySituation);
        }

        return infoGratuitySituation;
    }
}