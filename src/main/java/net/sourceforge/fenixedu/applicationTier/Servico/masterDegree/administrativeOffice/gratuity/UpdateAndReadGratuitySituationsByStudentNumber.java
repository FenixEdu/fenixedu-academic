package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class UpdateAndReadGratuitySituationsByStudentNumber {

    @Atomic
    public static List<InfoGratuitySituation> run(Integer studentNumber) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        List<InfoGratuitySituation> infoGratuitySituationsList = new ArrayList<InfoGratuitySituation>();

        for (Registration registration : Registration.readByNumberAndDegreeType(studentNumber, DegreeType.MASTER_DEGREE)) {
            Collection<StudentCurricularPlan> studentCurricularPlansList = registration.getStudentCurricularPlans();

            for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlansList) {

                Collection<GratuitySituation> gratuitySituations = studentCurricularPlan.getGratuitySituations();
                for (GratuitySituation gratuitySituation : gratuitySituations) {
                    gratuitySituation.updateValues();

                    infoGratuitySituationsList.add(InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
                            .newInfoFromDomain(gratuitySituation));
                }
            }

        }

        return infoGratuitySituationsList;
    }
}