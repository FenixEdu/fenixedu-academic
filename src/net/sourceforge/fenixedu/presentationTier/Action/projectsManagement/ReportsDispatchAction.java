/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoReport;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.projectsManagement.RubricType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Susana Fernandes
 */
public class ReportsDispatchAction extends FenixDispatchAction {
    protected final int numberOfSpanElements = 20;

    public ActionForward showHelp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixServiceException, FenixFilterException {
	final IUserView userView = UserView.getUser();
	final String helpPage = request.getParameter("helpPage");
	final String costCenter = request.getParameter("costCenter");
	getCostCenterName(request, costCenter);
	final RubricType rubricType = RubricType.getRubricType(helpPage);
	if (rubricType != null) {
	    List infoRubricList = (List) ServiceUtils.executeService("ReadRubric", new Object[] { userView.getUtilizador(),
		    costCenter, rubricType });
	    request.setAttribute("infoRubricList", infoRubricList);
	} else if (!helpPage.equals("listHelp"))
	    return mapping.findForward("index");

	request.setAttribute("helpPage", helpPage);
	return mapping.findForward("helpPage");
    }

    protected void getSpans(HttpServletRequest request, InfoReport infoReport) {
	if (infoReport != null) {
	    Boolean lastSpan = new Boolean(true);
	    int lines = infoReport.getLines().size();
	    if (lines > numberOfSpanElements) {
		Integer span = getCodeFromRequest(request, "span");
		if (span == null)
		    span = new Integer(0);
		int pagesNumber = (int) Math.ceil((double) lines / numberOfSpanElements);
		int startSpan = span.intValue() * numberOfSpanElements;
		int length = numberOfSpanElements;
		if (span.intValue() == (pagesNumber - 1)) {
		    if (lines % numberOfSpanElements != 0)
			length = lines % numberOfSpanElements;
		} else {
		    lastSpan = new Boolean(false);
		}

		request.setAttribute("span", span);
		request.setAttribute("startSpan", new Integer(startSpan));
		request.setAttribute("length", new Integer(length));
		request.setAttribute("numberOfSpanElements", new Integer(numberOfSpanElements));
		request.setAttribute("spanNumber", new Integer(pagesNumber));
	    }
	    request.setAttribute("lastSpan", lastSpan);
	}
    }

    protected Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
	Integer code = null;
	try {
	    Object objectCode = request.getAttribute(codeString);
	    if (objectCode != null) {
		if (objectCode instanceof String)
		    code = new Integer((String) objectCode);
		else if (objectCode instanceof Integer)
		    code = (Integer) objectCode;
	    } else {
		String thisCodeString = request.getParameter(codeString);
		if (thisCodeString != null)
		    code = new Integer(thisCodeString);
	    }
	} catch (NumberFormatException e) {
	    return null;
	}
	return code;
    }

    protected void getCostCenterName(HttpServletRequest request, String costCenter) throws FenixServiceException,
	    FenixFilterException {
	if (costCenter != null && !costCenter.equals("")) {
	    final IUserView userView = UserView.getUser();
	    request.setAttribute("infoCostCenter", ServiceUtils.executeService("ReadCostCenter", new Object[] {
		    userView.getUtilizador(), costCenter }));
	}
    }

}