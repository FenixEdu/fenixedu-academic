/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class ReadExecutionCourseTeachers implements IService {

    /**
     * Executes the service. Returns the current collection of infoTeachers.
     */

    public List run(Integer executionCourseId) throws FenixServiceException {

        List professorShips = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            professorShips = executionCourse.getProfessorships();
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (professorShips == null || professorShips.isEmpty())
            return null;

        List infoTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        ITeacher teacher = null;

        while (iter.hasNext()) {
            teacher = ((IProfessorship) iter.next()).getTeacher();
            infoTeachers.add(Cloner.copyITeacher2InfoTeacher(teacher));
        }

        return infoTeachers;
    }
}