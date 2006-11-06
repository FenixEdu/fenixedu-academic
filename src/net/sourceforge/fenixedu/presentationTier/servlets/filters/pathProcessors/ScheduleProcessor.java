package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.DegreeProcessor.DegreeContext;

public class ScheduleProcessor extends PathProcessor {

    public static final String PREFIX = "horarios";
    
    public ScheduleProcessor(String forwardURI) {
        super(forwardURI);
    }

    public ScheduleProcessor add(SchoolClassProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new ScheduleContext(parentContext);
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
            ScheduleContext ownContext = (ScheduleContext) context;
            doForward(context, ownContext.getParent().getDegree().getIdInternal());
            return true;
        }
    }

    public static class ScheduleContext extends ProcessingContext {

        public ScheduleContext(ProcessingContext parent) {
            super(parent);
        }

        @Override
        public DegreeContext getParent() {
            return (DegreeContext) super.getParent();
        }
        
    }
}
