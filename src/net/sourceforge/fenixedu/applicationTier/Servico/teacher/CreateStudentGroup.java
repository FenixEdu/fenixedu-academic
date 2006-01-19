/*
 * Created on 8/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author ansr & scpo
 */

public class CreateStudentGroup implements IService {

  
    private List buildStudentList(List<String> studentUserNames, Grouping grouping)
            throws FenixServiceException, ExcepcaoPersistencia {
      
        List studentList = new ArrayList();
        for (final String studantUserName : studentUserNames) {
            Attends attend = grouping.getStudentAttend(studantUserName);
            Student student = attend.getAluno();
            studentList.add(student);
        }
        return studentList;
    }

    public boolean run(Integer executionCourseID, Integer groupNumber, Integer groupingID,
            Integer shiftID, List studentUserNames) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();
        
        final Grouping grouping = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class, groupingID);
        
        if (grouping == null)
            throw new FenixServiceException();
        
        Shift shift = (Shift) persistentShift.readByOID(Shift.class, shiftID);
        
        List studentList = buildStudentList(studentUserNames, grouping);
        
        grouping.createStudentGroup(shift, groupNumber, studentList);
                
        return true;
    }
}