package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class LessonPlanningsToSummariesManagementProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        SummariesManagementBean bean = (SummariesManagementBean) source;
        Shift shift = bean.getShift();
        if(shift != null) {
            ShiftType tipo = shift.getTipo();
            ExecutionCourse executionCourse = bean.getExecutionCourse();
            return executionCourse.getLessonPlanningsOrderedByOrder(tipo);   
        }               
        return new ArrayList<LessonPlanning>();
    }

    public Converter getConverter() {        
        return new DomainObjectKeyConverter();
    }

}
