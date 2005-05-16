/*
 * Created on 16/May/2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class UpdateNonAffiliatedTeachersProfessorship implements IService {

    public void run(List nonAffiliatedTeachersIds, Integer executionCourseId)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseId);

        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        IPersistentNonAffiliatedTeacher persistentNonAffiliatedTeacher = sp
                .getIPersistentNonAffiliatedTeacher();
        List nonAffiliatedTeachers = executionCourse.getNonAffiliatedTeachers();
        persistentExecutionCourse.simpleLockWrite(executionCourse);
        List nonAffiliatedTeachersToRemove = new ArrayList(nonAffiliatedTeachersIds.size());
        for (Iterator iter = executionCourse.getNonAffiliatedTeachers().iterator(); iter.hasNext();) {
            INonAffiliatedTeacher nonAffiliatedTeacher = (INonAffiliatedTeacher) iter.next();
            persistentNonAffiliatedTeacher.simpleLockWrite(nonAffiliatedTeacher);

            if (!nonAffiliatedTeachersIds.contains(nonAffiliatedTeacher.getIdInternal())) {
                List execucionCourses = nonAffiliatedTeacher.getExecutionCourses();
                execucionCourses.remove(executionCourse);
                nonAffiliatedTeachersToRemove.add(nonAffiliatedTeacher);
            }
        }
        nonAffiliatedTeachers.removeAll(nonAffiliatedTeachersToRemove);
    }

}
