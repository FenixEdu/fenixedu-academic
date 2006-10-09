package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class CreateGratuitySituationsForCurrentExecutionYear extends Service {

    private Boolean firstYear;

    private Set<GratuitySituation> gratuitySituationsToDelete;

    public void run(String year) throws ExcepcaoPersistencia {

        gratuitySituationsToDelete = new HashSet<GratuitySituation>();

        ExecutionYear executionYear = readExecutionYear(year);

        // read master degree and persistentSupportecialization execution degrees
        Collection<ExecutionDegree> executionDegrees = executionYear
                .getExecutionDegreesByType(DegreeType.MASTER_DEGREE);

        for (ExecutionDegree executionDegree : executionDegrees) {

            GratuityValues gratuityValues = executionDegree.getGratuityValues();

            if (gratuityValues == null) {
                continue;
            }

            this.firstYear = executionDegree.isFirstYear();

            List<StudentCurricularPlan> studentCurricularPlans = executionDegree
                    .getDegreeCurricularPlan().getStudentCurricularPlans();
            for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {

                GratuitySituation gratuitySituation = studentCurricularPlan
                        .getGratuitySituationByGratuityValues(gratuityValues);
                
                if(year.equals("2002/2003") && gratuitySituation.getTransactionListCount() == 0){
                    gratuitySituation.removeEmployee();
                    gratuitySituation.removeGratuityValues();
                    gratuitySituation.removeStudentCurricularPlan();
                    this.gratuitySituationsToDelete.add(gratuitySituation);
                    continue;
                }

                if (gratuitySituation == null) {
                    createGratuitySituation(gratuityValues, studentCurricularPlan);
                } else {
                    updateGratuitySituation(gratuitySituation);
                }
            }
        }

        for (GratuitySituation gratuitySituationToDelete : this.gratuitySituationsToDelete) {
            gratuitySituationToDelete.delete();
        }
    }

    private ExecutionYear readExecutionYear(String year)
            throws ExcepcaoPersistencia {
        
        final ExecutionYear executionYear;
        if (year == null || year.equals("")) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = ExecutionYear.readExecutionYearByName(year);
        }
        return executionYear;
    }

    /**
     * @param gratuitySituation
     */
    private void updateGratuitySituation(GratuitySituation gratuitySituation) {

        // check if there isnt any persistentSupportecialization for a 2nd year
        if (gratuitySituation.getStudentCurricularPlan().getSpecialization().equals(
                Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {

            if (this.firstYear == null) {
                this.firstYear = gratuitySituation.getGratuityValues().getExecutionDegree()
                        .isFirstYear();
            }

            if (!this.firstYear) {
                // SHIT!!!!!!!!!!!!
                removeWrongGratuitySituation(gratuitySituation);
                return;
            }
        }

        gratuitySituation.updateValues();

    }

    private void removeWrongGratuitySituation(GratuitySituation gratuitySituation) {

        // find gratuity situation of first persistentSupportecialization year
        for (GratuitySituation correctSituation : gratuitySituation.getStudentCurricularPlan()
                .getGratuitySituations()) {
            if (correctSituation.getGratuityValues().getExecutionDegree().isFirstYear()) {

                // transfer transactions from wrong to correct gratuity
                // situation
                for (GratuityTransaction gratuityTransaction : gratuitySituation.getTransactionList()) {
                    correctSituation.addTransactionList(gratuityTransaction);
                }

                break;
            }
        }

        gratuitySituation.removeEmployee();
        gratuitySituation.removeGratuityValues();
        gratuitySituation.removeStudentCurricularPlan();
        this.gratuitySituationsToDelete.add(gratuitySituation);
    }

    /**
     * @param gratuityValues
     * @param studentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    private void createGratuitySituation(GratuityValues gratuityValues,
            StudentCurricularPlan studentCurricularPlan) {

        if (studentCurricularPlan.getSpecialization().equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)
                && !this.firstYear) {
            return;
        }

        new GratuitySituation(gratuityValues, studentCurricularPlan);
    }

}
