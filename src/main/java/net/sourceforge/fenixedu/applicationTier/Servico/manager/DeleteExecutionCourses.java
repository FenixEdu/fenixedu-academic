/*
 * Created on 30/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author jdnf, mrsp and Luis Cruz
 * 
 */
public class DeleteExecutionCourses {

    //ist150958: @Checked removed, however, service is used elsewhere in Manager
    // called from AssociateExecutionCourseToCurricularCourseDA
    //      (Admin -> Gest Estr Ensino -> Estr Cursos Antiga -> ...(curriculares)... -> Escolher disc exec)
    // called from ReadExecutionCourseAction
    //      (not present in any funcionality)
    @Atomic
    public static List<String> run(final List<String> executionCourseIDs) throws FenixServiceException {
        final List<String> undeletedExecutionCoursesCodes = new ArrayList<String>();

        for (final String executionCourseID : executionCourseIDs) {
            final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

            if (!executionCourse.canBeDeleted()) {
                undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
            } else {
                executionCourse.delete();
            }
        }
        return undeletedExecutionCoursesCodes;
    }

}