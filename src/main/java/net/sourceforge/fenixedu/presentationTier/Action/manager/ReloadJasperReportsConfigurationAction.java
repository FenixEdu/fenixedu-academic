package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/reloadJasperReports", scope = "session")
public class ReloadJasperReportsConfigurationAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(ReloadJasperReportsConfigurationAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ReportsUtils.getReportsMap().clear();

        ReportsUtils.getProperties().clear();
        ReportsUtils.loadProperties(ReportsUtils.getProperties(), ReportsUtils.getReportsPropertiesFile());

        if (LogLevel.INFO) {
            logger.info("reloaded jasper reports configuration");
        }
        return null;
    }

}
