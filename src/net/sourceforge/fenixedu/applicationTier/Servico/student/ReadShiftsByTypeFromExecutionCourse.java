package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Jo�o Mota
 * 
 */
public class ReadShiftsByTypeFromExecutionCourse extends Service {

    public List run(InfoExecutionCourse infoExecutionCourse, ShiftType tipoAula) {
    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
    	final Set<Shift> shifts = executionCourse.findShiftByType(tipoAula);

        return (List) CollectionUtils.collect(shifts, new Transformer() {
            public Object transform(Object arg0) {
                final Shift shift = (Shift) arg0;
                return InfoShift.newInfoFromDomain(shift);
            }});
    }

}