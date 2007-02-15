package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

public class DepartmentsProcessor extends PathProcessor {

    public static final String PREFIX1 = "departments"; 
    public static final String PREFIX2 = "departamentos";

    public DepartmentsProcessor(String forwardURI) {
        super(forwardURI);
    }

    public DepartmentsProcessor add(DepartmentProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new DepartmentsContext(parentContext);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();
        
        return current.equalsIgnoreCase(PREFIX1) || current.equalsIgnoreCase(PREFIX2);
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            return doForward(context);
        }
    }

    public static class DepartmentsContext extends ProcessingContext {

        public DepartmentsContext(ProcessingContext parent) {
            super(parent);
        }
        
    }
}
