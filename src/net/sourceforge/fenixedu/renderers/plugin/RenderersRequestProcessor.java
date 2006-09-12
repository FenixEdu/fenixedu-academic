package net.sourceforge.fenixedu.renderers.plugin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.renderers.components.state.ComponentLifeCycle;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.plugin.upload.CommonsFile;
import net.sourceforge.fenixedu.renderers.plugin.upload.RenderersRequestWrapper;
import net.sourceforge.fenixedu.renderers.plugin.upload.StrutsFile;
import net.sourceforge.fenixedu.renderers.plugin.upload.UploadedFile;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.apache.struts.upload.FormFile;

/**
 * The standard renderers request processor. This processor is responsible for handling any viewstate present
 * in the request. It will parse the request, retrieve all viewstates, and start the necessary lifecycle associated
 * with them before continuing with the standard struts processing.
 *  
 * <p>
 * If any exception is thrown during the processing of a viewstate it will be handled by struts like if the exceptions
 * occured in the destiny action. This default behaviour can be overriden by making the destiny action implement
 * the {@link net.sourceforge.fenixedu.renderers.plugin.ExceptionHandler} interface.
 *  
 * <p>
 * The processor ensures that the current request and context are available through
 * {@link #getCurrentRequest()} and {@link #getCurrentContext()} respectively during the entire request lifetime.
 * The processor also process multipart requests to allow any renderer to retrieve on uploaded file with
 * {@link #getUploadedFile(String)}.
 *  
 * <p>
 * This processor extends the tiles processor to easily integrate in an application that uses the tiles plugin.
 * 
 * @author cfgi
 */
public class RenderersRequestProcessor extends TilesRequestProcessor {

    private static ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<ServletContext>     currentContext = new ThreadLocal<ServletContext>();
    
    private static ThreadLocal<Map<String, UploadedFile>> fileItems = new ThreadLocal<Map<String, UploadedFile>>();
    
    public static HttpServletRequest getCurrentRequest() {
        return RenderersRequestProcessor.currentRequest.get();
    }

    public static ServletContext getCurrentContext() {
        return RenderersRequestProcessor.currentContext.get();
    }

    /**
     * @return the form file associated with the given field name or <code>null</code>
     *         if no file exists
     */
    public static UploadedFile getUploadedFile(String fieldName) {
        return RenderersRequestProcessor.fileItems.get().get(fieldName);
    }
    
    public static Collection<UploadedFile> getAllUploadedFiles() {
        return RenderersRequestProcessor.fileItems.get().values();
    }
    
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RenderersRequestProcessor.currentRequest.set(request);
        RenderersRequestProcessor.currentContext.set(getServletContext());
        
        try {
            super.process(request, response);
        }
        finally {
            RenderersRequestProcessor.currentRequest.set(null);
            RenderersRequestProcessor.currentContext.set(null);
            RenderersRequestProcessor.fileItems.set(null);
        }
    }

    protected HttpServletRequest parseMultipartRequest(HttpServletRequest request, ActionForm form) {
        Hashtable<String, UploadedFile> itemsMap = getNewFileItemsMap();

        if (form == null) {
            if (FileUpload.isMultipartContent(request)) {
                return createWrapperFromRequest(request, itemsMap);
            }
            else {
                return request;
            }
        }
        else {
            if (form.getMultipartRequestHandler() != null) {
                return createWrapperFromActionForm(request, itemsMap, form);
            }
            else {
                return request;
            }
        }
    }

    protected Hashtable<String, UploadedFile> getNewFileItemsMap() {
        Hashtable<String, UploadedFile> itemsMap = new Hashtable<String, UploadedFile>();
        RenderersRequestProcessor.fileItems.set(itemsMap);
        
        return itemsMap;
    }

    protected HttpServletRequest createWrapperFromRequest(HttpServletRequest request, Hashtable<String, UploadedFile> itemsMap) {
        RenderersRequestWrapper wrapper = new RenderersRequestWrapper(request);
        
        try {
            List fileItems = new FileUpload(new DefaultFileItemFactory()).parseRequest(request);
            
            for (Object object : fileItems) {
                FileItem item = (FileItem) object;
                
                if (item.isFormField()) {
                    wrapper.addParameter(item.getFieldName(), item.getString());
                }
                else {
                    UploadedFile uploadedFile = new CommonsFile(item);
                    
                    if (uploadedFile.getName() != null && uploadedFile.getName().length() > 0) {
                        itemsMap.put(item.getFieldName(), uploadedFile);
                    }
                    
                    wrapper.addParameter(item.getFieldName(), uploadedFile.getName());
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            return request;
        }
        
        return wrapper;
    }

    protected HttpServletRequest createWrapperFromActionForm(HttpServletRequest request, Hashtable<String, UploadedFile> itemsMap, ActionForm form) {
        RenderersRequestWrapper wrapper = new RenderersRequestWrapper(request);
        
        Hashtable<String, String[]> textElements = form.getMultipartRequestHandler().getTextElements();
        for (String name : textElements.keySet()) {
            String[] values = textElements.get(name);
            
            for (int i = 0; i < values.length; i++) {
                wrapper.addParameter(name, values[i]);
            }
        }
        
        Hashtable<String, FormFile> fileElements = form.getMultipartRequestHandler().getFileElements();
        for (String name : fileElements.keySet()) {
            UploadedFile uploadedFile = new StrutsFile(fileElements.get(name));
            
            if (uploadedFile.getName() != null && uploadedFile.getName().length() > 0) {
                itemsMap.put(name, uploadedFile);
            }
            
            wrapper.addParameter(name, uploadedFile.getName());
        }
        
        return wrapper;
    }

    @Override
    protected Action processActionCreate(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException {
        Action action = super.processActionCreate(request, response, mapping);
        
        if (action == null) {
            return new VoidAction();
        }
        
        return action;
    }

    @Override
    protected ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
        RenderersRequestProcessor.currentRequest.set(parseMultipartRequest(request, form));
        HttpServletRequest initialRequest = RenderersRequestProcessor.currentRequest.get();
        
        if (hasViewState(initialRequest)) {
            try {
                setViewStateProcessed(request);
                
                ActionForward forward = ComponentLifeCycle.execute(initialRequest);
                if (forward != null) {
                    return forward;
                }
    
                return super.processActionPerform(request, response, action, form, mapping);
            }
            catch (Exception e) {
            	System.out.println(SimpleDateFormat.getInstance().format(new Date()));
            	e.printStackTrace();
                if (action instanceof ExceptionHandler) {
                    	ExceptionHandler handler = (ExceptionHandler) action;
                    	
                    	IViewState viewState = RenderUtils.getViewState(); 
                    	ViewDestination destination = viewState.getInputDestination();
                    	ActionForward input = destination.getActionForward();
                    	
                    	ActionForward forward = handler.processException(request, mapping, input, e);
                    	if (forward != null) {
                    	    return forward;
                    	}
                    	else {
                    	    return processException(request, response, e, form, mapping);
                    	}
                }
                else {
                    return processException(request, response, e, form, mapping);
                }
            }
        }
        else {
            return super.processActionPerform(request, response, action, form, mapping);
        }
    
    }

    protected boolean hasViewState(HttpServletRequest request) {
        return viewStateNotProcessed(request) &&
        
                (request.getParameterValues(LifeCycleConstants.VIEWSTATE_PARAM_NAME) != null 
                || request.getParameterValues(LifeCycleConstants.VIEWSTATE_LIST_PARAM_NAME) != null);
    }

    protected boolean viewStateNotProcessed(HttpServletRequest request) {
        return request.getAttribute(LifeCycleConstants.PROCESSED_PARAM_NAME) == null;
    }

    protected void setViewStateProcessed(HttpServletRequest request) {
        request.setAttribute(LifeCycleConstants.PROCESSED_PARAM_NAME, true);
    }
}

class VoidAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return null;

    }

}