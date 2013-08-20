/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author asnr and scpo
 * 
 */
public class DeleteStudentGroup {

    protected Boolean run(String executionCourseCode, String studentGroupCode) throws FenixServiceException {
        StudentGroup deletedStudentGroup = AbstractDomainObject.fromExternalId(studentGroupCode);

        if (deletedStudentGroup == null) {
            throw new ExistingServiceException();
        }

        deletedStudentGroup.delete();

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteStudentGroup serviceInstance = new DeleteStudentGroup();

    @Service
    public static Boolean runDeleteStudentGroup(String executionCourseCode, String studentGroupCode)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, studentGroupCode);
    }
}