package net.sourceforge.fenixedu.applicationTier.Servico.manager.teachersManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 5/Dez/2003
 *  
 */
public class ReadInfoTeacherByTeacherNumber implements IService {

    public InfoTeacher run(Integer teacherNumber) throws FenixServiceException {

        InfoTeacher infoTeacher = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();

            if (teacherNumber == null) {
                throw new FenixServiceException("nullTeacherNumber");
            }

            ITeacher teacher = teacherDAO.readByNumber(teacherNumber);
            if (teacher == null) {
                throw new NonExistingServiceException("noTeacher");
            }

            List professorShips = persistentProfessorship.readByTeacher(teacher);

            List responsibleFors = persistentResponsibleFor.readByTeacher(teacher);
            if ((professorShips == null || professorShips.size() == 0)
                    && (responsibleFors == null || responsibleFors.size() == 0)) {
                throw new NonExistingServiceException("noPSnorRF");
            }

            List infoProfessorShips = new ArrayList();
            CollectionUtils.collect(professorShips, new Transformer() {
                public Object transform(Object input) {
                    IProfessorship professorship = (IProfessorship) input;
                    return Cloner.copyIProfessorship2InfoProfessorship(professorship);
                }
            }, infoProfessorShips);

            List infoResponsibleFors = new ArrayList();
            CollectionUtils.collect(responsibleFors, new Transformer() {
                public Object transform(Object input) {
                    IResponsibleFor responsibleFor = (IResponsibleFor) input;
                    return Cloner.copyIResponsibleFor2InfoResponsibleFor(responsibleFor);
                }
            }, infoResponsibleFors);

            infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            infoTeacher.setResponsibleForExecutionCourses(infoResponsibleFors);
            infoTeacher.setProfessorShipsExecutionCourses(infoProfessorShips);
        } catch (ExcepcaoPersistencia ex) {
            throw new RuntimeException(ex);
        }
        return infoTeacher;
    }
}