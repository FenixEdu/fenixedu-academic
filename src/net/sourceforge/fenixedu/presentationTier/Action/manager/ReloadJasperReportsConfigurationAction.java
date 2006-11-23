package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ReloadJasperReportsConfigurationAction extends Action {
    private static final Logger logger = Logger.getLogger(ReloadJasperReportsConfigurationAction.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

	ReportsUtils.getReportsMap().clear(); 
	
	ReportsUtils.getProperties().clear();
	PropertiesManager.loadProperties(ReportsUtils.getProperties(), ReportsUtils.getReportsPropertiesFile());
	
        logger.info("reloaded jasper reports configuration");
        return null;
    }
    
}
