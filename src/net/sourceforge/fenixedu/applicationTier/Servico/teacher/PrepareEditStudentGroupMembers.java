/*
 * Created on 17/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */

public class PrepareEditStudentGroupMembers implements IService {

    public List run(Integer executionCourseID, Integer studentGroupID) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IStudentGroup studentGroup = (IStudentGroup) persistentSupport
                .getIPersistentStudentGroup().readByOID(StudentGroup.class, studentGroupID);
        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        final List<IAttends> groupingAttends = new ArrayList<IAttends>();
        groupingAttends.addAll(studentGroup.getGrouping().getAttends());;

        final List<IStudentGroup> studentsGroups = studentGroup.getGrouping().getStudentGroups();
        for (final IStudentGroup studentGroupIter : studentsGroups) {
            for (final IAttends attend : studentGroupIter.getAttends()) {
                groupingAttends.remove(attend);                
            }
        }
        final List<InfoStudent> infoStudents = new ArrayList();
        for (final IAttends attend : groupingAttends) {
            infoStudents.add(InfoStudent.newInfoFromDomain(attend.getAluno()));
        }
        return infoStudents;
    }
}
