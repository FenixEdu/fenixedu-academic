/*
 * Created on Sep 8, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PasswordBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.EntryPhase;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author naat
 * @author zenida
 * 
 */
public class GeneratePasswordsForCandidaciesAction extends FenixDispatchAction {

    public ActionForward prepareChooseExecutionDegree(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(ExecutionDegree
		.getAllByExecutionYear(ExecutionYear.readCurrentExecutionYear()));
	Collections.sort(executionDegrees,
		ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);

	request.setAttribute("executionDegrees", executionDegrees);
	request.setAttribute("entryPhases", EntryPhase.getAll());

	return mapping.findForward("chooseExecutionDegree");
    }

    public ActionForward showCandidacies(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final DynaActionForm actionForm = (DynaActionForm) form;
	final Integer executionDegreeId = (Integer) actionForm.get("executionDegreeId");
	final EntryPhase entryPhase = new EntryPhase((Integer) actionForm.get("entryPhase"));
	final ExecutionDegree executionDegree = rootDomainObject
		.readExecutionDegreeByOID(executionDegreeId);
	final Set<StudentCandidacy> studentCandidacies = new HashSet<StudentCandidacy>(StudentCandidacy
		.readByExecutionDegreeAndExecutionYearAndEntryPhase(executionDegree, ExecutionYear
			.readCurrentExecutionYear(), entryPhase));

	request.setAttribute("studentCandidacies", studentCandidacies);

	return mapping.findForward("prepare");
    }

    public ActionForward generatePasswords(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final Object args[] = { Arrays.asList((Integer[]) ((DynaActionForm) form)
		.get("candidacyIdsToProcess")) };
	try {

	    final List<PasswordBean> passwordBeans = (List<PasswordBean>) ServiceManagerServiceFactory
		    .executeService(getUserView(request), "GeneratePasswordsForCandidacies", args);

	    Collections.sort(passwordBeans, new BeanComparator("person.username"));
	    request.setAttribute("passwordBeans", passwordBeans);

	    return mapping.findForward("success");

	} catch (FenixServiceException e) {
	    throw new FenixActionException();
	}

    }

}