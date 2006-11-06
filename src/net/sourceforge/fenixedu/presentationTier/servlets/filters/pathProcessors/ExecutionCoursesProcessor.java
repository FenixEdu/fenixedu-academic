package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.DegreeProcessor.DegreeContext;

public class ExecutionCoursesProcessor extends PathProcessor {

    public static final String PREFIX = "disciplinas";
    
    private final String forwardURI;
    
    public ExecutionCoursesProcessor(String forwardURI) {
        super();
        
        this.forwardURI = forwardURI;
    }

    public ExecutionCoursesProcessor add(ExecutionCourseProcessor processor) {
        addChild(processor);
        return this;
    }
    
    public ExecutionCoursesProcessor add(DegreeCurricularPlanProcessor processor) {
        addChild(processor);
        return this;
    }

    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new ExecutionCoursesContext((DegreeContext) parentContext);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();
        return current.equalsIgnoreCase(PREFIX);
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider)
            throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            ExecutionCoursesContext ownContext = (ExecutionCoursesContext) context;
            return doForward(ownContext, this.forwardURI, ownContext.getDegree().getIdInternal());
        }
    }

    public static class ExecutionCoursesContext extends ProcessingContext implements DegreeCurricularPlanContext {

        public ExecutionCoursesContext(DegreeContext parent) {
            super(parent);
        }

        @Override
        public DegreeContext getParent() {
            return (DegreeContext) super.getParent();
        }
        
        public Degree getDegree() {
            return getParent().getDegree();
        }
        
        public DegreeCurricularPlan getDegreeCurricularPlan() {
            return getDegree().getMostRecentDegreeCurricularPlan();
        }
        
    }
}
