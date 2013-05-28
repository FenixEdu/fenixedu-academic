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
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadGratuitySituationByStudentCurricularPlanByGratuityValues {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static Object run(Integer studentCurricularPlanID, Integer gratuityValuesID) throws FenixServiceException {

        GratuitySituation gratuitySituation = null;

        StudentCurricularPlan studentCurricularPlan = AbstractDomainObject.fromExternalId(studentCurricularPlanID);
        GratuityValues gratuityValues = AbstractDomainObject.fromExternalId(gratuityValuesID);
        gratuitySituation = studentCurricularPlan.getGratuitySituationByGratuityValues(gratuityValues);

        InfoGratuitySituation infoGratuitySituation = null;
        if (gratuitySituation != null) {
            infoGratuitySituation =
                    InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree.newInfoFromDomain(gratuitySituation);
        }

        return infoGratuitySituation;
    }
}