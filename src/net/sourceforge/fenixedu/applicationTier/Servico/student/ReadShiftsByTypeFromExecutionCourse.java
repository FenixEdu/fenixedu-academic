package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Jo�o Mota
 * 
 */
public class ReadShiftsByTypeFromExecutionCourse extends FenixService {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static List run(InfoExecutionCourse infoExecutionCourse, ShiftType tipoAula) {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
        final Set<Shift> shifts = executionCourse.findShiftByType(tipoAula);

        return (List) CollectionUtils.collect(shifts, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                final Shift shift = (Shift) arg0;
                return InfoShift.newInfoFromDomain(shift);
            }
        });
    }

}