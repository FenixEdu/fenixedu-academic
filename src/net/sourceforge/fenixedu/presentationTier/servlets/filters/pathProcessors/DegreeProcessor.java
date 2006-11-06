package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class DegreeProcessor extends PathProcessor {

    public DegreeProcessor(String forwardURI) {
        super(forwardURI);
    }

    public DegreeProcessor add(ExecutionCoursesProcessor processor) {
        addChild(processor);
        return this;
    }

    public DegreeProcessor add(ScheduleProcessor processor) {
        addChild(processor);
        return this;
    }
    
    public DegreeProcessor add(ExamProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parent) {
        return new DegreeContext(parent);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();
        Degree degree = Degree.readBySigla(current);
        
        if (degree == null) {
            return false;
        }
        else {
            DegreeContext ownContext = (DegreeContext) context;
            ownContext.setDegree(degree);
            
            return true;
        }
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
        if (! provider.hasNext()) {
            DegreeContext ownContext = (DegreeContext) context;
            return doForward(context, 
                    ExecutionPeriod.readActualExecutionPeriod().getIdInternal(),
                    ownContext.getDegree().getIdInternal());
        }
        else {
            return false;
        }
    }

    public static class DegreeContext extends ProcessingContext {
        
        public DegreeContext(ProcessingContext parent) {
            super(parent);
        }

        private Degree degree;

        public Degree getDegree() {
            return this.degree;
        }

        public void setDegree(Degree degree) {
            this.degree = degree;
        }
        
    }

}
