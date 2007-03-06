/*
 * Created on 17/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author asnr and scpo
 * 
 */

public class PrepareEditStudentGroupMembers extends Service {

    public List run(Integer executionCourseID, Integer studentGroupID) throws FenixServiceException,
            ExcepcaoPersistencia {
        final StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupID);
        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        final List<Attends> groupingAttends = new ArrayList<Attends>();
        groupingAttends.addAll(studentGroup.getGrouping().getAttends());;

        final List<StudentGroup> studentsGroups = studentGroup.getGrouping().getStudentGroups();
        for (final StudentGroup studentGroupIter : studentsGroups) {
            for (final Attends attend : studentGroupIter.getAttends()) {
                groupingAttends.remove(attend);                
            }
        }
        final List<InfoStudent> infoStudents = new ArrayList<InfoStudent>();
        for (final Attends attend : groupingAttends) {
            infoStudents.add(InfoStudent.newInfoFromDomain(attend.getRegistration()));
        }
        return infoStudents;
    }
    
}
