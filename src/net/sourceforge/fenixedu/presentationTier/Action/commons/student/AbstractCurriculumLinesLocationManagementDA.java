package net.sourceforge.fenixedu.presentationTier.Action.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResultMessage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AbstractCurriculumLinesLocationManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));

	return mapping.findForward("showCurriculum");
    }

    public ActionForward chooseNewDestination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final List<CurriculumLine> selectedCurriculumLines = getSelectedCurriculumLines(request);

	if (selectedCurriculumLines.isEmpty()) {
	    addActionMessage(request, "label.student.moveCurriculumLines.curriculumLines.selection.required");
	    return prepare(mapping, form, request, response);
	}

	final MoveCurriculumLinesBean moveCurriculumLinesBean = MoveCurriculumLinesBean.buildFrom(selectedCurriculumLines);
	moveCurriculumLinesBean.setStudentCurricularPlan(getStudentCurricularPlan(request));

	request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);

	return mapping.findForward("chooseNewLocation");
    }

    public ActionForward moveCurriculumLines(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final MoveCurriculumLinesBean moveCurriculumLinesBean = (MoveCurriculumLinesBean) getRenderedObject("move-curriculum-lines-bean");

	try {
	    executeService("MoveCurriculumLines", moveCurriculumLinesBean);
	} catch (EnrollmentDomainException ex) {
	    addRuleResultMessagesToActionMessages(request, ex.getFalseRuleResults());

	    request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);

	    return mapping.findForward("chooseNewLocation");

	} catch (DomainException ex) {

	    addActionMessage(request, ex.getMessage(), ex.getArgs());

	    request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);

	    return mapping.findForward("chooseNewLocation");
	}

	request.setAttribute("studentCurricularPlan", moveCurriculumLinesBean.getStudentCurricularPlan());
	return mapping.findForward("showCurriculum");

    }

    private void addRuleResultMessagesToActionMessages(HttpServletRequest request, List<RuleResult> falseRuleResults) {
	for (final RuleResult ruleResult : falseRuleResults) {
	    for (final RuleResultMessage message : ruleResult.getMessages()) {
		if (message.isToTranslate()) {
		    addActionMessage(request, message.getMessage(), message.getArgs());
		} else {
		    addActionMessageLiteral(request, message.getMessage());
		}
	    }
	}

    }

    private List<CurriculumLine> getSelectedCurriculumLines(HttpServletRequest request) {
	final String[] selectedCurriculumLineIds = request.getParameterValues("selectedCurriculumLineIds");
	if (selectedCurriculumLineIds == null) {
	    return Collections.emptyList();
	}

	final List<CurriculumLine> result = new ArrayList<CurriculumLine>();
	for (final String curriculumLineIdString : selectedCurriculumLineIds) {
	    result.add((CurriculumLine) rootDomainObject.readCurriculumModuleByOID(Integer.valueOf(curriculumLineIdString)));
	}

	return result;

    }

    protected StudentCurricularPlan getStudentCurricularPlan(HttpServletRequest request) {
	return rootDomainObject.readStudentCurricularPlanByOID(getRequestParameterAsInteger(request, "scpID"));
    }

}
