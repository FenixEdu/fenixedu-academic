package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Filtro.DeleteProfessorshipAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteProfessorship {

    protected Boolean run(String infoExecutionCourseCode, String teacherCode) throws FenixServiceException {

        Teacher teacher = FenixFramework.getDomainObject(teacherCode);
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(infoExecutionCourseCode);

        Professorship professorshipToDelete = null;
        if (teacher != null) {
            professorshipToDelete = teacher.getProfessorshipByExecutionCourse(executionCourse);
        }

        Collection shiftProfessorshipList = professorshipToDelete.getAssociatedShiftProfessorship();

        boolean hasCredits = false;

        if (!shiftProfessorshipList.isEmpty()) {
            hasCredits = CollectionUtils.exists(shiftProfessorshipList, new Predicate() {

                @Override
                public boolean evaluate(Object arg0) {
                    ShiftProfessorship shiftProfessorship = (ShiftProfessorship) arg0;
                    return shiftProfessorship.getPercentage() != null && shiftProfessorship.getPercentage() != 0;
                }
            });
        }

        if (!hasCredits) {
            professorshipToDelete.delete();
        } else {
            if (hasCredits) {
                throw new ExistingAssociatedCredits("error.remove.professorship");
            }
        }
        return Boolean.TRUE;
    }

    protected boolean canDeleteResponsibleFor() {
        return false;
    }

    public class ExistingAssociatedCredits extends FenixServiceException {
        private ExistingAssociatedCredits(String key) {
            super(key);
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteProfessorship serviceInstance = new DeleteProfessorship();

    @Atomic
    public static Boolean runDeleteProfessorship(String infoExecutionCourseCode, String teacherCode)
            throws FenixServiceException, NotAuthorizedException {
        DeleteProfessorshipAuthorizationFilter.instance.execute(infoExecutionCourseCode, teacherCode);
        return serviceInstance.run(infoExecutionCourseCode, teacherCode);
    }

}