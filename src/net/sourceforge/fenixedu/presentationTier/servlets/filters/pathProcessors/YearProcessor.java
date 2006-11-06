package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class YearProcessor extends PathProcessor {

    public YearProcessor(String forwardURI) {
        super(forwardURI);
    }

    public YearProcessor add(SemesterProcessor processor) {
        addChild(processor);
        return this;
    }
    
    public YearProcessor add(SectionProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new YearContext(parentContext);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();
        
        YearContext ownContext = (YearContext) context;
        ownContext.setYear(current);
        
        return ownContext.getExecutionYear() != null;
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider)
            throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            YearContext ownContext = (YearContext) context;
            ExecutionCourse executionCourse = ownContext.getExecutionCourse();
            
            if (executionCourse == null) {
                return false;
            }
            else {
                return doForward(context, executionCourse.getIdInternal());
            }
        }
    }

    public static class YearContext extends AbstractExecutionCourseContext {
        
        private String year;
        
        public YearContext(ProcessingContext parent) {
            super(parent);
        }

        public String getYear() {
            return this.year;
        }

        public void setYear(String year) {
            this.year = year;
        }
        
        public ExecutionYear getExecutionYear() {
            String year = getYear();
            if (year == null) {
                return null;
            }
            
            if (! year.matches("\\p{Digit}{4}-\\p{Digit}{4}")) {
                return null;
            }
            
            return ExecutionYear.readExecutionYearByName(year.replace('-', '/'));
        }

        public CurricularCourse getCurricularCourse() {
            return ((ExecutionCourseContext) getParent()).getCurricularCourse();
        }
        
        public List<ExecutionCourse> getExecutionCourses() {
            SortedSet<ExecutionCourse> result = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
            
            ExecutionYear year = getExecutionYear();
            for (ExecutionCourse executionCourse : ((ExecutionCourseContext) getParent()).getExecutionCourses()) {
                if (executionCourse.getExecutionPeriod().getExecutionYear().equals(year)) {
                    result.add(executionCourse);
                }
            }
            
            return new ArrayList<ExecutionCourse>(result);
        }
        
    }
    
}
