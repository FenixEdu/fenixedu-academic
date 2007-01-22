package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is the case for all the path processors. A path processor is responsible
 * for interpreting one or more elements of the requested path and redirecting the 
 * user to a new page or provide a certain context to subprocessors, that is, processors
 * that will process the remaining of the requested path.
 * 
 * @author cfgi
 */
public abstract class PathProcessor {

    private String forwardURI;
    private List<PathProcessor> children;
    
    public PathProcessor() {
        super();
        
        this.children = new ArrayList<PathProcessor>();
    }

    /**
     * Creates a new processor that has an URI to forward to.
     * 
     * @param forwardURI
     *            the URI were the user will be forwarded by default when
     *            {@link #doForward(ProcessingContext, Object[])} is called
     */
    public PathProcessor(String forwardURI) {
        this();
        
        this.forwardURI = forwardURI;
    }
    
    public String getForwardURI() {
        return this.forwardURI;
    }

    /**
     * Generic method to add a child processor to the processor chain.
     * 
     * @param processor
     *            the child processor
     */
    protected void addChild(PathProcessor processor) {
        this.children.add(processor);
    }
    
    public abstract ProcessingContext getProcessingContext(ProcessingContext parentContext);
    
    /**
     * Processing a path consists of the following steps.
     * <ol>
     * <li>creating a context from the parent context</li>
     * <li>check if this processor accepts the current path element</li>
     * <li>check if this processor knows were to forward the user</li>
     * <li>allow all children processors to process the remaining of the path</li>
     * </ol>
     * 
     * @param parentContext
     *            the context of the parent processor
     * @param provider
     *            the path elements provider
     * 
     * @return <code>true</code> if this processor or any of it's children
     *         processed the path successfully, that is, if the user was
     *         forwarded to the desired page
     * 
     * @throws IOException
     *             if the redirection of the user failed
     * @throws ServletException
     *             if the redirection of the user failed
     */
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

    /**
     * Checks if the processor accepts to process the path from the point given
     * by the provider. If the processor accepts this provider then no other
     * sibling processor will be called for this path. This means that if the
     * processor accepts the path but in the ends fails to redirect the user
     * then an error page will be shown to the user as we could not find the
     * page he was looking for.
     * 
     * @param context
     *            the current context
     * @param provider
     *            the path element provider
     * 
     * @return <code>true</code> if this processor accepts to process the path
     *         from the point given by the provider
     */
    protected abstract boolean accepts(ProcessingContext context, PathElementsProvider provider);

    /**
     * After the processor accepted the provider then it checks if it can
     * forward the user to the desired page. This normally happens when
     * {@link PathElementsProvider#hasNext()} is <code>false</code>, that is,
     * when there are no more elements in the path but a processor may know in
     * advance the desired page without prapgating the processing the the
     * children processor.
     * 
     * @param context
     *            the current context
     * @param provider
     *            the path element provider
     *            
     * @return <code>true</code> if the user was redirected
     * 
     * @throws IOException
     *             if the redirection of the user failed
     * @throws ServletException
     *             if the redirection of the user failed
     */
    protected abstract boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException;
    
    /**
     * When the processor accepts the provider but does not redirect the user to
     * the final page then all the children processor will be allowed to process
     * the path starting from the next element given by the provider.
     * 
     * @param context
     *            the current context
     * @param provider
     *            the path element provider
     * 
     * @return <code>true</code> if the user was redirecte
     * 
     * @throws IOException
     *             if the redirection of the user failed
     * @throws ServletException
     *             if the redirection of the user failed
     */
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

    /**
     * This method is a convenience method for
     * {@link #doForward(ProcessingContext, String, Object[])} where the URI
     * used is the one given in {@link #PathProcessor(String)}.
     */
    protected boolean doForward(ProcessingContext context, Object ... args) throws IOException, ServletException {
       return doForward(context, this.forwardURI, args);
    }

    /**
     * Redirects the user to the given URI after formating the URI with the
     * given args. This allow you to have an URI like
     * <code>"/path.do?id=%s"</code> and provide the value for the id
     * parameter when redirecting.
     * 
     * @param context
     *            the current context
     * @param uriFormat
     *            the URI to where the user is redirected after being formated
     * @param args
     *            the arguents used when formating the URI
     * @return <code>true</code>
     * 
     * @throws IOException
     *             if the redirection of the user failed
     * @throws ServletException
     *             if the redirection of the user failed
     */
    protected static boolean doForward(ProcessingContext context, String uriFormat, Object ... args) throws IOException, ServletException {
        // use client redirect
        // String path = context.getContextPath() + String.format(uriFormat, args);
        // context.getResponse().sendRedirect(path);

        // try internal forward unless impossible
        dispatch(context.getRequest(), context.getResponse(), String.format(uriFormat, args));
        return true;
    }
    
    protected static void dispatch(HttpServletRequest request, HttpServletResponse response, String path) throws IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);

        if (dispatcher == null) {
            response.sendRedirect(request.getContextPath() + path);
        }
        else {
            dispatcher.forward(request, response);
        }
    }
}
