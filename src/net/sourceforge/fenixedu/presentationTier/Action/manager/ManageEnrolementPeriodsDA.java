package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ManageEnrolementPeriodsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	final IUserView userView = getUserView(request);
        final DynaActionForm actionForm = (DynaActionForm) form;

        setInfoExecutionPeriods(request, userView);

        final String executionPeriodIDString = (String) actionForm.get("executionPeriodID");
        if (isValidObjectID(executionPeriodIDString)) {
            setInfoEnrolmentPeriods(request, userView, executionPeriodIDString);
        }

        return mapping.findForward("showEnrolementPeriods");
    }

    public ActionForward changePeriodValues(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        final DynaActionForm actionForm = (DynaActionForm) form;

        final String enrolmentPeriodIDString = (String) actionForm.get("enrolmentPeriodID");
        final String startDateString = (String) actionForm.get("startDate");
        final String endDateString = (String) actionForm.get("endDate");
        
        final String startTimeString = (String) actionForm.get("startTime");
        final String endTimeString = (String) actionForm.get("endTime");


        final Object[] args = { Integer.valueOf(enrolmentPeriodIDString), getDate(startDateString, startTimeString),
        		getDate(endDateString, endTimeString)};
        ServiceManagerServiceFactory.executeService(userView, "ChangeEnrolmentPeriodValues", args);

        return prepare(mapping, form, request, response);
    }

    public ActionForward createPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        final DynaActionForm actionForm = (DynaActionForm) form;

        final String executionPeriodIDString = (String) actionForm.get("executionPeriodID");
        final String degreeTypeString = (String) actionForm.get("degreeType");
        final String enrolmentPeriodClassString = (String) actionForm.get("enrolmentPeriodClass");
        final String startDateString = (String) actionForm.get("startDate");
        final String endDateString = (String) actionForm.get("endDate");
        
        final String startTimeString = (String) actionForm.get("startTime");
        final String endTimeString = (String) actionForm.get("endTime");


        final DegreeType degreeType = degreeTypeString.length() == 0 ? null : DegreeType.valueOf(degreeTypeString);

        final Object[] args = { Integer.valueOf(executionPeriodIDString),
        		degreeType, enrolmentPeriodClassString,
        		getDate(startDateString, startTimeString),
        		getDate(endDateString, endTimeString)};
        ServiceManagerServiceFactory.executeService(userView, "CreateEnrolmentPeriods", args);

        return prepare(mapping, form, request, response);
    }

    private void setInfoEnrolmentPeriods(final HttpServletRequest request, final IUserView userView, 
            final String executionPeriodIDString) throws FenixFilterException, FenixServiceException {
        final Object[] args = { Integer.valueOf(executionPeriodIDString) };
        final List<InfoEnrolmentPeriod> infoEnrolmentPeriods = (List<InfoEnrolmentPeriod>) 
                ServiceManagerServiceFactory.executeService(userView, "ReadEnrolmentPeriods", args);
        sortInfoEnrolmentPeriods(infoEnrolmentPeriods);
        request.setAttribute("infoEnrolmentPeriods", infoEnrolmentPeriods);
    }

    private void setInfoExecutionPeriods(final HttpServletRequest request, final IUserView userView) 
            throws FenixFilterException, FenixServiceException {
        final List<InfoExecutionPeriod> infoExecutionPeriods = (List<InfoExecutionPeriod>) 
                ServiceManagerServiceFactory.executeService(userView, "ReadExecutionPeriods", null);
        sortInfoExecutionPeriods(infoExecutionPeriods);
        request.setAttribute("infoExecutionPeriods", infoExecutionPeriods);
    }

    private void sortInfoExecutionPeriods(final List<InfoExecutionPeriod> infoExecutionPeriods) {
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoExecutionYear.year"), true);
        comparatorChain.addComparator(new BeanComparator("semester"));
        Collections.sort(infoExecutionPeriods, comparatorChain);
    }

    private void sortInfoEnrolmentPeriods(final List<InfoEnrolmentPeriod> infoEnrolmentPeriods) {
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoDegreeCurricularPlan.infoDegree.tipoCurso"));
        comparatorChain.addComparator(new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));
        Collections.sort(infoEnrolmentPeriods, comparatorChain);
    }

    private boolean isValidObjectID(final String objectIDString) {
        return objectIDString != null && objectIDString.length() > 0 && StringUtils.isNumeric(objectIDString);
    }
    
    private Date getDate(String date, String time) throws ParseException {
	return DateFormatUtil.parse("yyyy/MM/ddHH:mm", date + time); 
    }

}