/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author lmac1
 * @author Fernanda Quitério 17/Dez/2003
 */
@Mapping(module = "manager", path = "/readExecutionPeriods", input = "mainPageInput.do", scope = "request")
@Forwards(value = { @Forward(name = "readExecutionPeriods", path = "/manager/readExecutionPeriods_bd.jsp", tileProperties = @Tile(navLocal = "/manager/executionCourseManagement/mainMenu.jsp")) })
public class ReadExecutionPeriodsAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	List infoExecutionPeriods = ReadExecutionPeriods.run();

	if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {

	    // Collections.sort(infoExecutionPeriods, new
	    // ExecutionPeriodComparator());
	    ComparatorChain comparator = new ComparatorChain();
	    comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
	    comparator.addComparator(new BeanComparator("name"), true);
	    Collections.sort(infoExecutionPeriods, comparator);

	    if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
		request.setAttribute(PresentationConstants.LIST_EXECUTION_PERIODS, infoExecutionPeriods);
	    }

	}

	return mapping.findForward("readExecutionPeriods");

    }
}