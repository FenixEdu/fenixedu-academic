package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Fernanda Quitério 17/Dez/2003
 * 
 */
public class InsertExecutionCourseDispatchAction extends FenixDispatchAction {
    public ActionForward prepareInsertExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = SessionUtils.getUserView(request);

	List infoExecutionPeriods = null;
	try {
	    infoExecutionPeriods = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);
	} catch (FenixServiceException ex) {
	    throw new FenixActionException();
	}

	if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
	    // exclude closed execution periods
	    infoExecutionPeriods = (List) CollectionUtils.select(infoExecutionPeriods, new Predicate() {
		public boolean evaluate(Object input) {
		    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;
		    if (!infoExecutionPeriod.getState().equals(PeriodState.CLOSED)) {
			return true;
		    }
		    return false;
		}
	    });

	    ComparatorChain comparator = new ComparatorChain();
	    comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
	    comparator.addComparator(new BeanComparator("name"), true);
	    Collections.sort(infoExecutionPeriods, comparator);

	    List executionPeriodLabels = new ArrayList();
	    CollectionUtils.collect(infoExecutionPeriods, new Transformer() {
		public Object transform(Object arg0) {
		    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

		    LabelValueBean executionPeriod = new LabelValueBean(infoExecutionPeriod.getName() + " - "
			    + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod.getIdInternal()
			    .toString());
		    return executionPeriod;
		}
	    }, executionPeriodLabels);

	    request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, executionPeriodLabels);

	    List<LabelValueBean> entryPhases = new ArrayList<LabelValueBean>();
	    for (EntryPhase entryPhase : EntryPhase.getAll()) {
		LabelValueBean labelValueBean = new LabelValueBean(entryPhase.toString(), entryPhase.getEntryPhase().toString());
		entryPhases.add(labelValueBean);
	    }
	    request.setAttribute("entryPhases", entryPhases);

	}

	return mapping.findForward("insertExecutionCourse");
    }

    public ActionForward insertExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = SessionUtils.getUserView(request);

	InfoExecutionCourseEditor infoExecutionCourse = fillInfoExecutionCourse(form, request);

	Object args[] = { infoExecutionCourse };
	try {
	    ServiceUtils.executeService(userView, "InsertExecutionCourseAtExecutionPeriod", args);

	} catch (ExistingServiceException ex) {
	    throw new ExistingActionException(ex.getMessage(), ex);

	} catch (NonExistingServiceException exception) {
	    throw new NonExistingActionException(exception.getMessage(), mapping.getInputForward());

	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	return mapping.findForward("firstPage");
    }

    private InfoExecutionCourseEditor fillInfoExecutionCourse(ActionForm form, HttpServletRequest request) {

	DynaActionForm dynaForm = (DynaValidatorForm) form;

	InfoExecutionCourseEditor infoExecutionCourse = new InfoExecutionCourseEditor();

	String name = (String) dynaForm.get("name");
	infoExecutionCourse.setNome(name);

	String code = (String) dynaForm.get("code");
	infoExecutionCourse.setSigla(code);

	InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod(rootDomainObject
		.readExecutionSemesterByOID(new Integer((String) dynaForm.get("executionPeriodId"))));
	infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

	String comment = new String("");
	if ((String) dynaForm.get("comment") != null) {
	    comment = (String) dynaForm.get("comment");
	}
	infoExecutionCourse.setComment(comment);

	String entryPhaseString = dynaForm.getString("entryPhase");
	EntryPhase entryPhase = null;
	if (entryPhaseString != null && entryPhaseString.length() > 0) {
	    entryPhase = EntryPhase.valueOf(Integer.valueOf(entryPhaseString));
	}
	infoExecutionCourse.setEntryPhase(entryPhase);

	return infoExecutionCourse;
    }
}