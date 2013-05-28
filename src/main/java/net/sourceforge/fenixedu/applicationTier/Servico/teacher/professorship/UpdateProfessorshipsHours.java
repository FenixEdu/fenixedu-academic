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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author jpvl
 */
public class UpdateProfessorshipsHours {

    @Checked("RolePredicates.CREDITS_MANAGER_PREDICATE")
    @Service
    public static Boolean run(Integer teacherId, Integer executionYearId, final HashMap hours) throws FenixServiceException {

        Iterator entries = hours.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Entry) entries.next();

            String key = entry.getKey().toString();
            Integer executionCourseId = Integer.valueOf(key);
            String value = (String) entry.getValue();
            if (value != null) {
                try {
                    Double ecHours = Double.valueOf(value);
                    Teacher teacher = AbstractDomainObject.fromExternalId(teacherId);
                    ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
                    Professorship professorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
                    professorship.setHours(ecHours);
                } catch (NumberFormatException e1) {
                    // ignored
                }
            }
        }
        return Boolean.TRUE;
    }
}