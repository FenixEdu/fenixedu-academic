/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsByExecutionYear extends
        ReadDetailedTeacherProfessorshipsAbstractService {
    /**
     * @author jpvl
     */
    public class NotFoundExecutionYear extends FenixServiceException {

    }

    public List run(Integer teacherID, Integer executionYearID) throws FenixServiceException,
            ExcepcaoPersistencia {
        IPersistentProfessorship persistentProfessorship = persistentSupport
                .getIPersistentProfessorship();
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
      
        if (executionYearID == null) {
            IPersistentExecutionYear persistentExecutionYear = persistentSupport
                    .getIPersistentExecutionYear();
            ExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
            executionYearID = executionYear.getIdInternal();
        }

        List professorships = persistentProfessorship.readByTeacherAndExecutionYear(teacherID,
                executionYearID);
                               
        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        
        final List<Professorship> responsibleForsAux = teacher.responsibleFors();
        final List responsibleFors = new ArrayList();        
        for(Professorship professorship : responsibleForsAux){
            if(professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().getIdInternal().equals(executionYearID))
                responsibleFors.add(professorship);
        }                

        return getDetailedProfessorships(professorships, responsibleFors, persistentSupport);
    }
}