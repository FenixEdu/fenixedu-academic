package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

public abstract class PathProcessor {

    private String forwardURI;
    private List<PathProcessor> children;
    
    public PathProcessor() {
        super();
        
        this.children = new ArrayList<PathProcessor>();
    }

    public PathProcessor(String forwardURI) {
        this();
        
        this.forwardURI = forwardURI;
    }
    
    protected void addChild(PathProcessor processor) {
        this.children.add(processor);
    }
    
    public abstract ProcessingContext getProcessingContext(ProcessingContext parentContext);
    
    public boolean process(ProcessingContext parentContext, PathElementsProvider provider) throws IOException, ServletException {
        ProcessingContext context = getProcessingContext(parentContext);

        if (! accepts(context, provider)) {
            return false;
        }
        
        context.accept();
        
        if (forward(context, provider)) {
            return true;
        }
        
        return processChildren(context, provider);
    }

    protected abstract boolean accepts(ProcessingContext context, PathElementsProvider provider);

    protected abstract boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException;
    
    protected boolean processChildren(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
        if (!provider.hasNext()) {
            return false;
        }
        
        provider.next();
        
        for (PathProcessor child : this.children) {
            if (child.process(context, provider)) {
                return true;
            }
            
            if (context.isChildAccepted()) {
                return false;
            }
        }
        
        return false;
    }
    
    protected boolean doForward(ProcessingContext context, Object ... args) throws IOException, ServletException {
       return doForward(context, this.forwardURI, args);
    }

    protected static boolean doForward(ProcessingContext context, String uriFormat, Object ... args) throws IOException, ServletException {
        String path = context.getContextPath() + String.format(uriFormat, args);
        context.getResponse().sendRedirect(path);

        return true;
    }
}
