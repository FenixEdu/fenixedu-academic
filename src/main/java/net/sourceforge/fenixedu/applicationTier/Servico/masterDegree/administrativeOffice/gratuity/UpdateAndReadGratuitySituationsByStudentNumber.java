package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class UpdateAndReadGratuitySituationsByStudentNumber {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static List<InfoGratuitySituation> run(Integer studentNumber) {

        List<InfoGratuitySituation> infoGratuitySituationsList = new ArrayList<InfoGratuitySituation>();

        for (Registration registration : Registration.readByNumberAndDegreeType(studentNumber, DegreeType.MASTER_DEGREE)) {
            List<StudentCurricularPlan> studentCurricularPlansList = registration.getStudentCurricularPlans();

            for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlansList) {

                List<GratuitySituation> gratuitySituations = studentCurricularPlan.getGratuitySituations();
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