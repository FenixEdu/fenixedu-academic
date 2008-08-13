/*
 * Created on Dec 18, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class UpdateProfessorshipsHours extends Service {

    public Boolean run(Integer teacherId, Integer executionYearId, final HashMap hours)
            throws FenixServiceException{

        Iterator entries = hours.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Entry) entries.next();

            String key = entry.getKey().toString();
            Integer executionCourseId = Integer.valueOf(key);
            String value = (String) entry.getValue();
            if (value != null) {
                try {
                    Double ecHours = Double.valueOf(value);
                    Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
                    ExecutionCourse executionCourse = rootDomainObject
                            .readExecutionCourseByOID(executionCourseId);
                    Professorship professorship = teacher
                            .getProfessorshipByExecutionCourse(executionCourse);
                    professorship.setHours(ecHours);
                } catch (NumberFormatException e1) {
                    // ignored
                }
            }
        }
        return Boolean.TRUE;
    }
}