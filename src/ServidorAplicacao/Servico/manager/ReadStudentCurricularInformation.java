/*
 * Created on Feb 18, 2005
 *
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoEnrolment;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.equivalence.InfoEnrollmentGrade;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Luis Cruz
 * 
 */
public class ReadStudentCurricularInformation implements IService {

    public List run(final Integer studentNumber, final TipoCurso degreeType) throws ExcepcaoPersistencia {
        final List infoStudentCurricularPlans = new ArrayList();

        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();

        InfoStudent infoStudent = null;

        final List studentCurricularPlans = persistentStudentCurricularPlan
                .readByStudentNumberAndDegreeType(studentNumber, degreeType);
        for (final Iterator iterator = studentCurricularPlans.iterator(); iterator.hasNext();) {
            final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator
                    .next();
            final IStudent student = studentCurricularPlan.getStudent();

            if (infoStudent == null) {
                infoStudent = constructInfoStudent(student);
            }

            final InfoStudentCurricularPlan infoStudentCurricularPlan = constructInfoStudentCurricularPlan(studentCurricularPlan);
            infoStudentCurricularPlan.setInfoStudent(infoStudent);

            infoStudentCurricularPlans.add(infoStudentCurricularPlan);
        }

        return infoStudentCurricularPlans;
    }

    protected InfoStudent constructInfoStudent(final IStudent student) {
        final InfoStudent infoStudent = InfoStudent.newInfoFromDomain(student);
        final InfoPerson infoPerson = new InfoPerson();
        final IPessoa person = student.getPerson();

        infoStudent.setInfoPerson(infoPerson);
        infoPerson.setNome(person.getNome());
        infoPerson.setUsername(person.getUsername());
        infoPerson.setEmail(person.getEmail());
        infoStudent.setDegreeType(student.getDegreeType());

        return infoStudent;
    }

    protected InfoStudentCurricularPlan constructInfoStudentCurricularPlan(
            final IStudentCurricularPlan studentCurricularPlan) {
        final IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan
                .getDegreeCurricularPlan();
        final ICurso degree = degreeCurricularPlan.getDegree();
        final List enrollments = studentCurricularPlan.getEnrolments();

        final InfoStudentCurricularPlan infoStudentCurricularPlan = InfoStudentCurricularPlan
                .newInfoFromDomain(studentCurricularPlan);
        final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(degreeCurricularPlan);
        final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
        final List infoEnrollments = constructEnrollmentsList(enrollments);

        infoDegreeCurricularPlan.setInfoDegree(infoDegree);
        infoStudentCurricularPlan.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoStudentCurricularPlan.setInfoEnrolments(infoEnrollments);

        return infoStudentCurricularPlan;
    }

    protected List constructEnrollmentsList(final List enrollments) {
        return (List) CollectionUtils.collect(enrollments, new Transformer() {
            public Object transform(Object arg0) {
                final IEnrollment enrollment = (IEnrollment) arg0;
                final IExecutionPeriod executionPeriod = enrollment.getExecutionPeriod();
                final IExecutionYear executionYear = executionPeriod.getExecutionYear();
                final ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
                final IDegreeCurricularPlan degreeCurricularPlan = curricularCourse
                        .getDegreeCurricularPlan();
                final ICurso degree = degreeCurricularPlan.getDegree();
                final List enrollmentEvaluations = enrollment.getEvaluations();

                final InfoEnrollmentGrade infoEnrollmentGrade = new InfoEnrollmentGrade();
                final InfoEnrolment infoEnrolment = new InfoEnrolment();
                final InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
                final InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
                final InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
                final InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
                final InfoDegree infoDegree = new InfoDegree();

                infoEnrollmentGrade.setInfoEnrollment(infoEnrolment);
                infoEnrolment.setIdInternal(enrollment.getIdInternal());
                infoEnrolment.setInfoExecutionPeriod(infoExecutionPeriod);
                infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
                infoEnrolment.setInfoCurricularCourse(infoCurricularCourse);
                infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
                infoDegreeCurricularPlan.setInfoDegree(infoDegree);
                infoExecutionYear.setYear(executionYear.getYear());
                infoExecutionPeriod.setSemester(executionPeriod.getSemester());
                infoDegree.setSigla(degree.getSigla());
                infoCurricularCourse.setName(curricularCourse.getName());
                infoCurricularCourse.setCode(curricularCourse.getCode());

                Collections.sort(enrollmentEvaluations);
                final IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) enrollmentEvaluations
                        .get(enrollmentEvaluations.size() - 1);
                infoEnrollmentGrade.setGrade(enrolmentEvaluation.getGrade());

                return infoEnrollmentGrade;
            }
        });
    }

}
