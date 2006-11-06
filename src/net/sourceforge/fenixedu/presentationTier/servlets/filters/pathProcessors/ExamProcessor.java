package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.DegreeProcessor.DegreeContext;

public class ExamProcessor extends PathProcessor {

    public static final String PREFIX = "exames";
    
    public ExamProcessor(String forwardURI) {
        super(forwardURI);
    }

    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new ExamProcessorContext(parentContext);
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
            ExamProcessorContext ownContext = (ExamProcessorContext) context;
            doForward(context, ownContext.getParent().getDegree().getIdInternal());
            return true;
        }
    }

    public static class ExamProcessorContext extends ProcessingContext {

        public ExamProcessorContext(ProcessingContext parent) {
            super(parent);
        }

        @Override
        public DegreeContext getParent() {
            return (DegreeContext) super.getParent();
        }
        
    }
}
