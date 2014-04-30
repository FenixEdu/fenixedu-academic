package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteHoliday;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerPeopleApp;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ManagerPeopleApp.class, path = "manage-holidays", titleKey = "label.manage.holidays",
        accessGroup = "#managers")
@Mapping(module = "manager", path = "/manageHolidays")
@Forwards(@Forward(name = "showHolidays", path = "/manager/showHolidays.jsp"))
public class ManageHolidaysDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final Set<Holiday> holidays = rootDomainObject.getHolidaysSet();
        request.setAttribute("holidays", holidays);
        return mapping.findForward("showHolidays");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        executeFactoryMethod();
        return prepare(mapping, form, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        final String holidayIDString = request.getParameter("holidayID");
        if (holidayIDString != null && StringUtils.isNumeric(holidayIDString)) {
            final Holiday holiday = FenixFramework.getDomainObject(holidayIDString);

            DeleteHoliday.run(holiday);
        }
        return prepare(mapping, form, request, response);
    }

}