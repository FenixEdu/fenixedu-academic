package net.sourceforge.fenixedu.applicationTier.Servico.manager.teachersManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 5/Dez/2003
 *  
 */
public class ReadInfoTeacherByTeacherNumber implements IService {

    public InfoTeacher run(Integer teacherNumber) throws FenixServiceException, ExcepcaoPersistencia {

        InfoTeacher infoTeacher = null;
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
       
        if (teacherNumber == null) {
            throw new FenixServiceException("nullTeacherNumber");
        }

        Teacher teacher = teacherDAO.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new NonExistingServiceException("noTeacher");
        }

        List professorShips = persistentProfessorship.readByTeacher(teacher.getIdInternal());

        List responsibleFors = teacher.responsibleFors();
        if ((professorShips == null || professorShips.size() == 0)
                && (responsibleFors == null || responsibleFors.size() == 0)) {
            throw new NonExistingServiceException("noPSnorRF");
        }

        List<InfoProfessorship> infoProfessorShips = new ArrayList();
        CollectionUtils.collect(professorShips, new Transformer() {
            public Object transform(Object input) {
                Professorship professorship = (Professorship) input;
                return InfoProfessorshipWithAll.newInfoFromDomain(professorship);
            }
        }, infoProfessorShips);
        
        List infoResponsibleFors = new ArrayList();
        for(InfoProfessorship infoProfessorship : infoProfessorShips){
            if(infoProfessorship.getResponsibleFor())
                infoResponsibleFors.add(infoProfessorship);
        }

        infoTeacher = InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher);
        infoTeacher.setResponsibleForExecutionCourses(infoResponsibleFors);
        infoTeacher.setProfessorShipsExecutionCourses(infoProfessorShips);
        return infoTeacher;
    }
}