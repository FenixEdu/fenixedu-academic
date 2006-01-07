package net.sourceforge.fenixedu.applicationTier.Servico.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithFirstTimeEnrolment;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListMasterDegreeStudents implements IService {

    public Collection run(String executionYearName) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(
                executionYearName);

        final Collection<InfoStudentCurricularPlanWithFirstTimeEnrolment> infoStudentCurricularPlans = new ArrayList();
        final Collection<StudentCurricularPlan> studentCurricularPlans = new ArrayList();
        final Collection<DegreeCurricularPlan> masterDegreeCurricularPlans = sp
                .getIPersistentDegreeCurricularPlan().readByDegreeTypeAndState(
                        DegreeType.MASTER_DEGREE, DegreeCurricularPlanState.ACTIVE);

        CollectionUtils.filter(masterDegreeCurricularPlans, new Predicate() {

            public boolean evaluate(Object arg0) {
                DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) arg0;
                for (ExecutionDegree executionDegree : degreeCurricularPlan
                        .getExecutionDegrees()) {
                    if (executionDegree.getExecutionYear().equals(executionYear)) {
                        return true;
                    }
                }
                return false;
            }

        });

        for (DegreeCurricularPlan degreeCurricularPlan : masterDegreeCurricularPlans) {
            studentCurricularPlans.addAll(degreeCurricularPlan.getStudentCurricularPlans());
        }

        for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {

            if (!studentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.ACTIVE)
                    && !studentCurricularPlan.getCurrentState().equals(
                            StudentCurricularPlanState.SCHOOLPARTCONCLUDED)) {
                continue;
            }

            boolean firstTimeEnrolment = true;
            if (studentCurricularPlan.getSpecialization().equals(Specialization.MASTER_DEGREE)) {

                Collection<StudentCurricularPlan> previousStudentCurricularPlans = sp
                        .getIStudentCurricularPlanPersistente().readAllByStudentNumberAndSpecialization(
                                studentCurricularPlan.getStudent().getNumber(),
                                DegreeType.MASTER_DEGREE, Specialization.MASTER_DEGREE);

                previousStudentCurricularPlans.remove(studentCurricularPlan);
                for (StudentCurricularPlan previousStudentCurricularPlan : previousStudentCurricularPlans) {
                    if (previousStudentCurricularPlan.getDegreeCurricularPlan().getDegree().equals(
                            studentCurricularPlan.getDegreeCurricularPlan().getDegree())) {
                        firstTimeEnrolment = false;
                        break;
                    }
                }
            }
            
            if (firstTimeEnrolment) {

                DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan
                        .getDegreeCurricularPlan();
                List<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();

                ExecutionDegree executionDegree = (ExecutionDegree) Collections.min(executionDegrees, new Comparator() {

                    public int compare(Object o1, Object o2) {
                        ExecutionDegree executionDegree1 = (ExecutionDegree) o1;
                        ExecutionDegree executionDegree2 = (ExecutionDegree) o2;

                        return executionDegree1.getExecutionYear().getYear().compareTo(
                                executionDegree2.getExecutionYear().getYear());
                    }

                });

                if (!executionDegree.getExecutionYear().equals(executionYear)) {
                    firstTimeEnrolment = false;
                }
            }

            InfoStudentCurricularPlanWithFirstTimeEnrolment infoStudentCurricularPlan = InfoStudentCurricularPlanWithFirstTimeEnrolment
                    .newInfoFromDomain(studentCurricularPlan);
            infoStudentCurricularPlan.setFirstTimeEnrolment(firstTimeEnrolment);
            infoStudentCurricularPlans.add(infoStudentCurricularPlan);
        }

        return infoStudentCurricularPlans;

    }

}
