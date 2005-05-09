package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class ReadStudentMarksByCurricularCourse implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentMarksByCurricularCourse() {
    }

    public List run(Integer curricularCourseID, Integer studentNumber, String executionYear)
            throws FenixServiceException {
        List enrolmentEvaluations = null;
        InfoTeacher infoTeacher = null;
        List infoSiteEnrolmentEvaluations = new ArrayList();
        IEnrolment enrolment = new Enrolment();
        IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            // read curricularCourse by ID
            ICurricularCourse curricularCourse = new CurricularCourse();
            curricularCourse.setIdInternal(curricularCourseID);
            curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOID(
                    CurricularCourse.class, curricularCourseID, false);

            final ICurricularCourse curricularCourseTemp = curricularCourse;

            // IStudentCurricularPlan studentCurricularPlan =
            // sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(
            // studentNumber,
            // DegreeType.MESTRADO_OBJ);
            //
            // if (studentCurricularPlan == null)
            // {
            //
            // throw new ExistingServiceException();
            // }

            // get student curricular Plan
            // in case student has school part concluded his curricular plan is
            // not in active state

            List studentCurricularPlans = sp.getIStudentCurricularPlanPersistente()
                    .readByStudentNumberAndDegreeType(studentNumber, DegreeType.MASTER_DEGREE);

            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) CollectionUtils
                    .find(studentCurricularPlans, new Predicate() {
                        public boolean evaluate(Object object) {
                            IStudentCurricularPlan studentCurricularPlanElem = (IStudentCurricularPlan) object;
                            if (studentCurricularPlanElem.getDegreeCurricularPlan().equals(
                                    curricularCourseTemp.getDegreeCurricularPlan())) {
                                return true;
                            }
                            return false;
                        }
                    });
            if (studentCurricularPlan == null) {

                studentCurricularPlan = (IStudentCurricularPlan) CollectionUtils.find(
                        studentCurricularPlans, new Predicate() {
                            public boolean evaluate(Object object) {
                                IStudentCurricularPlan studentCurricularPlanElem = (IStudentCurricularPlan) object;
                                if (studentCurricularPlanElem.getDegreeCurricularPlan().getDegree()
                                        .equals(
                                                curricularCourseTemp.getDegreeCurricularPlan()
                                                        .getDegree())) {
                                    return true;
                                }
                                return false;
                            }
                        });

                if (studentCurricularPlan == null) {
                    throw new ExistingServiceException();
                }

            }
            // }
            if (executionYear != null) {
                enrolment = sp.getIPersistentEnrolment()
                        .readEnrolmentByStudentCurricularPlanAndCurricularCourse(studentCurricularPlan,
                                curricularCourse, executionYear);
            } else {
                // TODO: Não se sabe se este comportamento está correcto!
                List enrollments = sp.getIPersistentEnrolment()
                        .readByStudentCurricularPlanAndCurricularCourse(studentCurricularPlan,
                                curricularCourse);

                if (enrollments.isEmpty()) {
                    throw new ExistingServiceException();
                }
                enrolment = (IEnrolment) enrollments.get(0);
            }

            if (enrolment != null) {
                // ListIterator iter1 = enrolments.listIterator();
                // while (iter1.hasNext()) {
                // enrolment = (IEnrolment) iter1.next();

                EnrolmentEvaluationState enrolmentEvaluationState = new EnrolmentEvaluationState(
                        EnrolmentEvaluationState.FINAL);
                enrolmentEvaluations = persistentEnrolmentEvaluation
                        .readEnrolmentEvaluationByEnrolmentEvaluationState(enrolment,
                                enrolmentEvaluationState);
                // enrolmentEvaluations = enrolment.getEvaluations();

                List infoTeachers = new ArrayList();
                if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
                    IPerson person = ((IEnrolmentEvaluation) enrolmentEvaluations.get(0))
                            .getPersonResponsibleForGrade();
                    ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
                    infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                    infoTeachers.add(infoTeacher);
                }

                List infoEnrolmentEvaluations = new ArrayList();
                if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
                    ListIterator iter = enrolmentEvaluations.listIterator();
                    while (iter.hasNext()) {
                        enrolmentEvaluation = (IEnrolmentEvaluation) iter.next();
                        InfoEnrolmentEvaluation infoEnrolmentEvaluation = Cloner
                                .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
                        InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                                .newInfoFromDomain(enrolmentEvaluation.getEnrolment());
                        infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);

                        if (enrolmentEvaluation != null) {
                            if (enrolmentEvaluation.getEmployee() != null) {
                                IPerson person2 = (IPerson) sp.getIPessoaPersistente().readByOID(
                                        Person.class,
                                        enrolmentEvaluation.getEmployee().getPerson().getIdInternal(),
                                        false);
                                infoEnrolmentEvaluation.setInfoEmployee(Cloner
                                        .copyIPerson2InfoPerson(person2));
                            }

                        }
                        infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
                    }

                }
                InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
                infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
                infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
                infoSiteEnrolmentEvaluations.add(infoSiteEnrolmentEvaluation);

            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        return infoSiteEnrolmentEvaluations;
    }
}