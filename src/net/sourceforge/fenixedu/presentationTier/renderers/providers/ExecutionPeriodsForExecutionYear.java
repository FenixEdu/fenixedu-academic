package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution.TeacherServiceBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.collections.comparators.ReverseComparator;

public class ExecutionPeriodsForExecutionYear implements DataProvider {

    public Object provide(Object source, Object currentValue) {

    	TeacherServiceBean bean = (TeacherServiceBean) source;
    	ExecutionYear executionYear = bean.getExecutionYear();
    	List<ExecutionPeriod> periods = new ArrayList<ExecutionPeriod>();
    	if(executionYear!=null) {
    		periods.addAll(executionYear.getExecutionPeriods());
    	}
       
        return periods;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
