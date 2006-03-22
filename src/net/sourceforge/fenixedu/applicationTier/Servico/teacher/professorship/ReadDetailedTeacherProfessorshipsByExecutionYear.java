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
            
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        
        ExecutionYear executionYear = null;
        if (executionYearID == null) {
            IPersistentExecutionYear persistentExecutionYear = persistentSupport
                    .getIPersistentExecutionYear();
            executionYear = persistentExecutionYear.readCurrentExecutionYear();
                    
        } else{
            executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        }

        List professorships = teacher.getProfessorships(executionYear);
        
        final List<Professorship> responsibleForsAux = teacher.responsibleFors();
        final List responsibleFors = new ArrayList();        
        for(Professorship professorship : responsibleForsAux){
            if(professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().getIdInternal().equals(executionYearID))
                responsibleFors.add(professorship);
        }                

        return getDetailedProfessorships(professorships, responsibleFors, persistentSupport);
    }
}