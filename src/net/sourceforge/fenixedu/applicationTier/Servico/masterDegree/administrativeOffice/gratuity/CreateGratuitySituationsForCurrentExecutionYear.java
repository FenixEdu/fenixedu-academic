package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.transactions.IGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class CreateGratuitySituationsForCurrentExecutionYear implements IService {

    private Boolean firstYear;

    private Set<IGratuitySituation> gratuitySituationsToDelete;

    public void run(String year) throws ExcepcaoPersistencia {

        gratuitySituationsToDelete = new HashSet<IGratuitySituation>();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IExecutionYear executionYear = readExecutionYear(year, sp);

        // read master degree and specialization execution degrees
        Collection<IExecutionDegree> executionDegrees = executionYear
                .getExecutionDegreesByType(DegreeType.MASTER_DEGREE);

        for (IExecutionDegree executionDegree : executionDegrees) {

            IGratuityValues gratuityValues = executionDegree.getGratuityValues();

            if (gratuityValues == null) {
                continue;
            }

            this.firstYear = executionDegree.isFirstYear();

            List<IStudentCurricularPlan> studentCurricularPlans = executionDegree
                    .getDegreeCurricularPlan().getStudentCurricularPlans();
            for (IStudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {

                IGratuitySituation gratuitySituation = studentCurricularPlan
                        .getGratuitySituationByGratuityValues(gratuityValues);

                if (gratuitySituation == null) {
                    createGratuitySituation(gratuityValues, studentCurricularPlan);
                } else {
                    updateGratuitySituation(gratuitySituation);
                }
            }
        }

        for (IGratuitySituation gratuitySituationToDelete : this.gratuitySituationsToDelete) {
            sp.getIPersistentObject().deleteByOID(GratuitySituation.class,
                    gratuitySituationToDelete.getIdInternal());
        }
    }

    private IExecutionYear readExecutionYear(String year, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IExecutionYear executionYear;
        if (year == null || year.equals("")) {
            executionYear = sp.getIPersistentExecutionYear().readCurrentExecutionYear();
        } else {
            executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(year);
        }
        return executionYear;
    }

    /**
     * @param gratuitySituation
     */
    private void updateGratuitySituation(IGratuitySituation gratuitySituation) {

        // check if there isnt any specialization for a 2nd year
        if (gratuitySituation.getStudentCurricularPlan().getSpecialization().equals(
                Specialization.SPECIALIZATION)) {

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

    private void removeWrongGratuitySituation(IGratuitySituation gratuitySituation) {

        // find gratuity situation of first specialization year
        for (IGratuitySituation correctSituation : gratuitySituation.getStudentCurricularPlan()
                .getGratuitySituations()) {
            if (correctSituation.getGratuityValues().getExecutionDegree().isFirstYear()) {

                // transfer transactions from wrong to correct gratuity
                // situation
                for (IGratuityTransaction gratuityTransaction : gratuitySituation.getTransactionList()) {
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
    private void createGratuitySituation(IGratuityValues gratuityValues,
            IStudentCurricularPlan studentCurricularPlan) {

        if (studentCurricularPlan.getSpecialization().equals(Specialization.SPECIALIZATION)
                && !this.firstYear) {
            return;
        }

        DomainFactory.makeGratuitySituation(gratuityValues, studentCurricularPlan);
    }

}
