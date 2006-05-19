package net.sourceforge.fenixedu.renderers.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ReloadConfigurationAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ConfigurationReader reader = new ConfigurationReader();
        
        reader.readAll(getServlet().getServletContext());
        
        return mapping.findForward("success");
    }
}
