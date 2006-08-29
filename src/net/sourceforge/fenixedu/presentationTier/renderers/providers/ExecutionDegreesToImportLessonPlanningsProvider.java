package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.ImportLessonPlanningsBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionDegreesToImportLessonPlanningsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) source;
        ExecutionPeriod executionPeriod = bean.getExecutionPeriod();               
        if(executionPeriod != null) {            
            List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
            executionDegrees.addAll(executionPeriod.getExecutionYear().getExecutionDegrees());
            Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
            return executionDegrees;
        }
        return new ArrayList<ExecutionDegree>();   
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();  
    }
}
