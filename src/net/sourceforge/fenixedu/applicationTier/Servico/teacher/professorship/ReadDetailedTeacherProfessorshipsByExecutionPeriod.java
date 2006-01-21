/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsByExecutionPeriod extends
        ReadDetailedTeacherProfessorshipsAbstractService {

    public List run(Integer teacherOID, Integer executionPeriodOID) throws FenixServiceException,
            ExcepcaoPersistencia {
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        IPersistentProfessorship persistentProfessorship = persistentSupport
        .getIPersistentProfessorship();

        
        if (executionPeriodOID == null) {
            IPersistentExecutionPeriod executionPeriodDAO = persistentSupport
                    .getIPersistentExecutionPeriod();
            ExecutionPeriod executionPeriod = executionPeriod = executionPeriodDAO
                    .readActualExecutionPeriod();
            executionPeriodOID = executionPeriod.getIdInternal();
        }
        
        List professorships = persistentProfessorship.readByTeacherAndExecutionPeriod(teacherOID,
                executionPeriodOID);
                
        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherOID);
        
        final List<Professorship> responsibleForsAux = teacher.responsibleFors();
        final List responsibleFors = new ArrayList();        
        for(Professorship professorship : responsibleForsAux){
            if(professorship.getExecutionCourse().getExecutionPeriod().getIdInternal().equals(executionPeriodOID))
                responsibleFors.add(professorship);
        }                      

        List detailedProfessorships = getDetailedProfessorships(professorships, responsibleFors,
                persistentSupport);
        return detailedProfessorships;
    }
}