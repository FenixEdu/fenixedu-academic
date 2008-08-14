package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageHolidaysDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final Set<Holiday> holidays = rootDomainObject.getHolidaysSet();
	request.setAttribute("holidays", holidays);
	return mapping.findForward("showHolidays");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixFilterException, FenixServiceException {
	executeFactoryMethod(request);
	return prepare(mapping, form, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixFilterException, FenixServiceException {
	final String holidayIDString = request.getParameter("holidayID");
	if (holidayIDString != null && StringUtils.isNumeric(holidayIDString)) {
	    final Integer holidayID = Integer.valueOf(holidayIDString);
	    final Holiday holiday = rootDomainObject.readHolidayByOID(holidayID);
	    final Object[] args = { holiday };
	    executeService(request, "DeleteHoliday", args);
	}
	return prepare(mapping, form, request, response);
    }

}
