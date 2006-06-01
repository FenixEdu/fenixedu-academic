package net.sourceforge.fenixedu.renderers.plugin;

import java.io.IOException;
import java.util.Collection;
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

public class RenderersRequestProcessor extends TilesRequestProcessor {

    private static ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<ServletContext>     currentContext = new ThreadLocal<ServletContext>();
    
    private static ThreadLocal<Map<String, FileItem>> fileItems = new ThreadLocal<Map<String, FileItem>>();
    
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpServletRequest newRequest = parseMultipartRequest(request);
        
        currentRequest.set(newRequest);
        currentContext.set(getServletContext());
        
        super.process(request, response);
    }

    private HttpServletRequest parseMultipartRequest(HttpServletRequest request) {
        Hashtable<String, FileItem> itemsMap = getNewFileItemsMap();

        if (FileUpload.isMultipartContent(request)) {
            RenderersRequestWrapper wrapper = new RenderersRequestWrapper(request);
            
            try {
                List fileItems = new FileUpload(new DefaultFileItemFactory()).parseRequest(request);
                
                for (Object object : fileItems) {
                    FileItem item = (FileItem) object;
                    
                    if (! item.isFormField()) {
                        itemsMap.put(item.getFieldName(), item);
                    }
                    else {
                        wrapper.addParameter(item.getFieldName(), item.getString());
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
                return request;
            }
            
            return wrapper;
        }
        else {
            return request;
        }
    }

    private Hashtable<String, FileItem> getNewFileItemsMap() {
        Hashtable<String, FileItem> itemsMap = new Hashtable<String, FileItem>();
        this.fileItems.set(itemsMap);
        
        return itemsMap;
    }

    public static HttpServletRequest getCurrentRequest() {
        return currentRequest.get();
    }

    public static ServletContext getCurrentContext() {
        return currentContext.get();
    }

    public static FileItem getFileItem(String fieldName) {
        return fileItems.get().get(fieldName);
    }
    
    public static Collection<FileItem> getAllFileItems() {
        return fileItems.get().values();
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
        HttpServletRequest initialRequest = this.currentRequest.get();
        
        try {
            if (hasViewState(initialRequest)) {
     
                setViewStateProcessed(request);
                
                ActionForward forward = ComponentLifeCycle.execute(initialRequest);
                if (forward != null) {
                    return forward;
                }
            }

            // TODO: exception thrown here may cause unexpected behaviour
            return super.processActionPerform(request, response, action, form, mapping);
        }
        catch (Exception e) {
            if (action instanceof ExceptionHandler) {
                	ExceptionHandler handler = (ExceptionHandler) action;
                	
                	// TODO: ensure that a view state is present when an exception occurs
                	IViewState viewState = RenderUtils.getViewState(); 
                	ViewDestination destination = viewState.getInputDestination();
                	ActionForward input = destination.getActionForward();
                	
                	ActionForward forward = handler.processException(request, input, e);
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

    private boolean hasViewState(HttpServletRequest request) {
        return viewStateNotProcessed(request) &&
        
                (request.getParameterValues(LifeCycleConstants.VIEWSTATE_PARAM_NAME) != null 
                || request.getParameterValues(LifeCycleConstants.VIEWSTATE_LIST_PARAM_NAME) != null);
    }

    private boolean viewStateNotProcessed(HttpServletRequest request) {
        return request.getAttribute(LifeCycleConstants.PROCESSED_PARAM_NAME) == null;
    }

    private void setViewStateProcessed(HttpServletRequest request) {
        request.setAttribute(LifeCycleConstants.PROCESSED_PARAM_NAME, true);
    }
}

class VoidAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return null;

    }

}