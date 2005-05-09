package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.StudentCurricularPlanState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateStudentCurricularPlan implements IService {

public void run(final Integer studentNumber, final DegreeType degreeType,
            final StudentCurricularPlanState studentCurricularPlanState,
            final Integer degreeCurricularPlanId, final Date startDate) throws ExcepcaoPersistencia,
            FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        final IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSupport
                .getIPersistentDegreeCurricularPlan();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();

        final IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                degreeType);
        if (student == null) {
            throw new NonExistingServiceException("exception.student.does.not.exist");
        }

        final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

        // TODO : student can only have one active student curricular plan at a time.
        final List studentCurricularPlans = student.getStudentCurricularPlans();
            for (final Iterator iterator = studentCurricularPlans.iterator(); iterator.hasNext(); ) {
                final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
                final IDegreeCurricularPlan associatedDegreeCurricularCourse = studentCurricularPlan.getDegreeCurricularPlan();
                if (associatedDegreeCurricularCourse.getIdInternal().equals(degreeCurricularPlan.getIdInternal())
                        && studentCurricularPlan.getCurrentState().equals(studentCurricularPlanState)) {
                    throw new ExistingServiceException("student.curricular.plan.already.exists");
                }
        }

        final IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
        persistentStudentCurricularPlan.simpleLockWrite(studentCurricularPlan);
        studentCurricularPlan.setCurrentState(studentCurricularPlanState);
        studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);
        studentCurricularPlan.setEnrolments(new ArrayList(0));
        studentCurricularPlan.setNotNeedToEnrollCurricularCourses(new ArrayList(0));
        studentCurricularPlan.setStartDate(startDate);
        studentCurricularPlan.setStudent(student);
        studentCurricularPlan.setWhen(new Date());
    }}
