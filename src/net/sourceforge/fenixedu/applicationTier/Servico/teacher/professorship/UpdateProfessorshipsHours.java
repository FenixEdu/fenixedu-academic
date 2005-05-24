/*
 * Created on Dec 18, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class UpdateProfessorshipsHours implements IService {

    public Boolean run(Integer teacherId, Integer executionYearId, final HashMap hours)
            throws FenixServiceException, ExcepcaoPersistencia {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
           
            Iterator entries = hours.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Entry) entries.next();

                String key = entry.getKey().toString();
                Integer executionCourseId = Integer.valueOf(key);
                String value = (String) entry.getValue();
                if (value != null) {
                    try {
                        Double ecHours = Double.valueOf(value);
                        IProfessorship professorship = professorshipDAO.readByTeacherAndExecutionCourse(teacherId, executionCourseId);
                        professorshipDAO.simpleLockWrite(professorship);
                        professorship.setHours(ecHours);
                    } catch (NumberFormatException e1) {
                        // ignored
                    }
                }
            }
          return Boolean.TRUE;
    }
}