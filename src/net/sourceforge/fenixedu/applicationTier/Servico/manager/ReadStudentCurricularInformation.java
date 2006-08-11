/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.equivalence.InfoEnrollmentGrade;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz
 * 
 */
public class ReadStudentCurricularInformation extends Service {

    public List run(final Integer studentNumber, final DegreeType degreeType) throws ExcepcaoPersistencia {
        final List infoStudentCurricularPlans = new ArrayList();

        InfoStudent infoStudent = null;

        Registration student = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        if(student != null) {
        	final List<StudentCurricularPlan> studentCurricularPlans = student.getStudentCurricularPlans(); 
	        for (final Iterator iterator = studentCurricularPlans.iterator(); iterator.hasNext();) {
	            final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator
	                    .next();
	
	            if (infoStudent == null) {
	                infoStudent = constructInfoStudent(student);
	            }
	
	            final InfoStudentCurricularPlan infoStudentCurricularPlan = constructInfoStudentCurricularPlan(studentCurricularPlan);
	            infoStudentCurricularPlan.setInfoStudent(infoStudent);
	
	            infoStudentCurricularPlans.add(infoStudentCurricularPlan);
	        }
        }
        return infoStudentCurricularPlans;
    }

    protected InfoStudent constructInfoStudent(final Registration student) {
        final InfoStudent infoStudent = InfoStudent.newInfoFromDomain(student);
        final InfoPerson infoPerson = new InfoPerson();
        final Person person = student.getPerson();

        infoStudent.setInfoPerson(infoPerson);
        infoPerson.setNome(person.getNome());
        infoPerson.setUsername(person.getUsername());
        infoPerson.setEmail(person.getEmail());
        infoStudent.setDegreeType(student.getDegreeType());

        return infoStudent;
    }

    protected InfoStudentCurricularPlan constructInfoStudentCurricularPlan(
            final StudentCurricularPlan studentCurricularPlan) {
        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan
                .getDegreeCurricularPlan();
        final Degree degree = degreeCurricularPlan.getDegree();
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
                final Enrolment enrollment = (Enrolment) arg0;
                final ExecutionPeriod executionPeriod = enrollment.getExecutionPeriod();
                final ExecutionYear executionYear = executionPeriod.getExecutionYear();
                final CurricularCourse curricularCourse = enrollment.getCurricularCourse();
                final DegreeCurricularPlan degreeCurricularPlan = curricularCourse
                        .getDegreeCurricularPlan();
                final Degree degree = degreeCurricularPlan.getDegree();
                final List enrollmentEvaluations = enrollment.getEvaluations();

                final InfoEnrollmentGrade infoEnrollmentGrade = new InfoEnrollmentGrade();
                final InfoEnrolment infoEnrolment = new InfoEnrolment();
                final InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
                final InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
                final InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
                final InfoDegree infoDegree = new InfoDegree();

                infoEnrollmentGrade.setInfoEnrollment(infoEnrolment);
                infoEnrolment.setIdInternal(enrollment.getIdInternal());
                infoEnrolment.setInfoExecutionPeriod(infoExecutionPeriod);
                infoEnrolment.setInfoCurricularCourse(infoCurricularCourse);
                infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
                infoDegreeCurricularPlan.setInfoDegree(infoDegree);
                infoDegree.setSigla(degree.getSigla());
                infoCurricularCourse.setName(curricularCourse.getName());
                infoCurricularCourse.setCode(curricularCourse.getCode());

				
                if (!enrollmentEvaluations.isEmpty()) {
                	final EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) Collections.max(enrollmentEvaluations);
                	infoEnrollmentGrade.setGrade(enrolmentEvaluation.getGrade());
                } else {
                	infoEnrollmentGrade.setGrade("error.data.consistency.problem");
                }

                return infoEnrollmentGrade;
            }
        });
    }

}
