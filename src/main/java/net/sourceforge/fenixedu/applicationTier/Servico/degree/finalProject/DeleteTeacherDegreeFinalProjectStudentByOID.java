package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.credits.ReadDeleteTeacherDegreeFinalProjectStudentAuthorization;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteTeacherDegreeFinalProjectStudentByOID extends FenixService {

    protected void run(Integer teacherDegreeFinalProjectStudentID) throws FenixServiceException {
        final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
                rootDomainObject.readTeacherDegreeFinalProjectStudentByOID(teacherDegreeFinalProjectStudentID);
        if (teacherDegreeFinalProjectStudent == null) {
            throw new FenixServiceException("message.noTeacherDegreeFinalProjectStudent");
        }
        teacherDegreeFinalProjectStudent.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTeacherDegreeFinalProjectStudentByOID serviceInstance = new DeleteTeacherDegreeFinalProjectStudentByOID();

    @Service
    public static void runDeleteTeacherDegreeFinalProjectStudentByOID(Integer teacherDegreeFinalProjectStudentID) throws FenixServiceException  , NotAuthorizedException {
        ReadDeleteTeacherDegreeFinalProjectStudentAuthorization.instance.execute(teacherDegreeFinalProjectStudentID);
        serviceInstance.run(teacherDegreeFinalProjectStudentID);
    }

}