package net.sourceforge.fenixedu.applicationTier.Servico.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithFirstTimeEnrolment;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
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
        final IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(
                executionYearName);

        final Collection<InfoStudentCurricularPlanWithFirstTimeEnrolment> infoStudentCurricularPlans = new ArrayList();
        final Collection<IStudentCurricularPlan> studentCurricularPlans = new ArrayList();
        final Collection<IDegreeCurricularPlan> masterDegreeCurricularPlans = sp
                .getIPersistentDegreeCurricularPlan().readByDegreeTypeAndState(
                        DegreeType.MASTER_DEGREE, DegreeCurricularPlanState.ACTIVE);

        CollectionUtils.filter(masterDegreeCurricularPlans, new Predicate() {

            public boolean evaluate(Object arg0) {
                IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) arg0;
                for (IExecutionDegree executionDegree : degreeCurricularPlan
                        .getExecutionDegrees()) {
                    if (executionDegree.getExecutionYear().equals(executionYear)) {
                        return true;
                    }
                }
                return false;
            }

        });

        for (IDegreeCurricularPlan degreeCurricularPlan : masterDegreeCurricularPlans) {
            studentCurricularPlans.addAll(degreeCurricularPlan.getStudentCurricularPlans());
        }

        for (IStudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {

            if (!studentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.ACTIVE)
                    && !studentCurricularPlan.getCurrentState().equals(
                            StudentCurricularPlanState.SCHOOLPARTCONCLUDED)) {
                continue;
            }

            boolean firstTimeEnrolment = true;
            if (studentCurricularPlan.getSpecialization().equals(Specialization.MASTER_DEGREE)) {

                Collection<IStudentCurricularPlan> previousStudentCurricularPlans = sp
                        .getIStudentCurricularPlanPersistente().readAllByStudentNumberAndSpecialization(
                                studentCurricularPlan.getStudent().getNumber(),
                                DegreeType.MASTER_DEGREE, Specialization.MASTER_DEGREE);

                previousStudentCurricularPlans.remove(studentCurricularPlan);
                for (IStudentCurricularPlan previousStudentCurricularPlan : previousStudentCurricularPlans) {
                    if (previousStudentCurricularPlan.getDegreeCurricularPlan().getDegree().equals(
                            studentCurricularPlan.getDegreeCurricularPlan().getDegree())) {
                        firstTimeEnrolment = false;
                        break;
                    }
                }
            }
            
            if (firstTimeEnrolment) {

                IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan
                        .getDegreeCurricularPlan();
                List<IExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();

                Collections.sort(executionDegrees, new Comparator() {

                    public int compare(Object o1, Object o2) {
                        IExecutionDegree executionDegree1 = (IExecutionDegree) o1;
                        IExecutionDegree executionDegree2 = (IExecutionDegree) o2;

                        return executionDegree1.getExecutionYear().getYear().compareTo(
                                executionDegree2.getExecutionYear().getYear());
                    }

                });

                if (!executionDegrees.get(0).getExecutionYear().equals(executionYear)) {
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
