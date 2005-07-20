/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

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

        ISuportePersistente suportePersistente = getDAOFactory();
        IPersistentProfessorship persistentProfessorship = suportePersistente
                .getIPersistentProfessorship();
        IPersistentTeacher persistentTeacher = suportePersistente.getIPersistentTeacher();
      
        if (executionYearID == null) {
            IPersistentExecutionYear persistentExecutionYear = suportePersistente
                    .getIPersistentExecutionYear();
            IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
            executionYearID = executionYear.getIdInternal();
        }

        List professorships = persistentProfessorship.readByTeacherAndExecutionYear(teacherID,
                executionYearID);
                               
        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        
        final List<IProfessorship> responsibleForsAux = teacher.responsibleFors();
        final List responsibleFors = new ArrayList();        
        for(IProfessorship professorship : responsibleForsAux){
            if(professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().getIdInternal().equals(executionYearID))
                responsibleFors.add(professorship);
        }                

        return getDetailedProfessorships(professorships, responsibleFors, suportePersistente);
    }
}