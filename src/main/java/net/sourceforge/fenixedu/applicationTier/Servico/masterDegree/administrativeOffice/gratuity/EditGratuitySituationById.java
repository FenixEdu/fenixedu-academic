package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithAll;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditGratuitySituationById {

    @Atomic
    public static Object run(InfoGratuitySituation infoGratuitySituation) throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        if (infoGratuitySituation == null) {
            throw new FenixServiceException();
        }

        StudentCurricularPlan studentCurricularPlan =
                FenixFramework.getDomainObject(infoGratuitySituation.getInfoStudentCurricularPlan()
                        .getExternalId());

        GratuityValues gratuityValues =
                FenixFramework.getDomainObject(infoGratuitySituation.getInfoGratuityValues().getExternalId());

        final GratuitySituation gratuitySituation = studentCurricularPlan.getGratuitySituationByGratuityValues(gratuityValues);

        if (gratuitySituation == null) {
            throw new NonExistingServiceException("Gratuity Situation not exist yet.");
        }

        // set employee who made register
        final Person person = Person.readPersonByUsername(infoGratuitySituation.getInfoEmployee().getPerson().getUsername());
        if (person != null) {
            gratuitySituation.setEmployee(person.getEmployee());
        }

        gratuitySituation.setWhen(Calendar.getInstance().getTime());
        gratuitySituation.setExemptionDescription(infoGratuitySituation.getExemptionDescription());
        gratuitySituation.setExemptionPercentage(infoGratuitySituation.getExemptionPercentage());
        gratuitySituation.setExemptionValue(infoGratuitySituation.getExemptionValue());
        gratuitySituation.setExemptionType(infoGratuitySituation.getExemptionType());

        gratuitySituation.updateValues();

        return InfoGratuitySituationWithAll.newInfoFromDomain(gratuitySituation);
    }

}