package net.sourceforge.fenixedu.applicationTier.Servico.manager.teachersManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério 5/Dez/2003
 *  
 */
public class ReadInfoTeacherByTeacherNumber extends Service {

    public InfoTeacher run(Integer teacherNumber) throws FenixServiceException, ExcepcaoPersistencia {

        InfoTeacher infoTeacher = null;
       
        if (teacherNumber == null) {
            throw new FenixServiceException("error.readInfoTeacherByTeacherNumber.nullTeacherNumber");
        }

        final Teacher teacher = Teacher.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new NonExistingServiceException("error.readInfoTeacherByTeacherNumber.noTeacher");
        }
        
        if (teacher.getProfessorshipsCount() == 0) {
            throw new NonExistingServiceException("error.readInfoTeacherByTeacherNumber.noProfessorshipsOrNoResp");
        }
        final List<InfoProfessorship> infoProfessorShips = new ArrayList(teacher.getProfessorshipsCount());
        for (final Professorship professorship : teacher.getProfessorshipsSet()) {
            infoProfessorShips.add(InfoProfessorship.newInfoFromDomain(professorship));
        }
        
        final List<InfoProfessorship> infoResponsibleFors = new ArrayList<InfoProfessorship>();
        for(final InfoProfessorship infoProfessorship : infoProfessorShips){
            if(infoProfessorship.getResponsibleFor()) {
        	infoResponsibleFors.add(infoProfessorship);
            }
        }

        infoTeacher = InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher);
        infoTeacher.setResponsibleForExecutionCourses(infoResponsibleFors);
        infoTeacher.setProfessorShipsExecutionCourses(infoProfessorShips);
        return infoTeacher;
    }
}