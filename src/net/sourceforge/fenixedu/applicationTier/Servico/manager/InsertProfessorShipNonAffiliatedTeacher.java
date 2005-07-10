package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class InsertProfessorShipNonAffiliatedTeacher implements IService {

    public InsertProfessorShipNonAffiliatedTeacher() {
    }

    public void run(Integer nonAffiliatedTeacherID, Integer executionCourseID) throws Exception {

        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);

        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        IPersistentNonAffiliatedTeacher persistentNonAffiliatedTeacher = sp
                .getIPersistentNonAffiliatedTeacher();
        INonAffiliatedTeacher nonAffiliatedTeacher = (INonAffiliatedTeacher) persistentNonAffiliatedTeacher
                .readByOID(NonAffiliatedTeacher.class, nonAffiliatedTeacherID);

        if (nonAffiliatedTeacher == null) {
            throw new NonExistingServiceException("message.non.existing.nonAffiliatedTeacher", null);
        }

        if (executionCourse.getNonAffiliatedTeachers().contains(nonAffiliatedTeacher)
                || nonAffiliatedTeacher.getExecutionCourses().contains(executionCourse)) {
            throw new ExistingServiceException("label",null);
        }
        
        persistentExecutionCourse.simpleLockWrite(executionCourse);
        persistentNonAffiliatedTeacher.simpleLockWrite(nonAffiliatedTeacher);
        
        List nonAffiliatedTeachers = executionCourse.getNonAffiliatedTeachers();
        nonAffiliatedTeachers.add(nonAffiliatedTeacher);
        
        List executionCourses = nonAffiliatedTeacher.getExecutionCourses();
        executionCourses.add(executionCourse);
    }

}
