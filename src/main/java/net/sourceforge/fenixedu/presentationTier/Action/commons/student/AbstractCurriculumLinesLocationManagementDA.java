package net.sourceforge.fenixedu.presentationTier.Action.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.student.curriculumLines.MoveCurriculumLines;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResultMessage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

abstract public class AbstractCurriculumLinesLocationManagementDA extends FenixDispatchAction {

    private ActionForward prepare(final ActionMapping mapping, final HttpServletRequest request, final boolean isWithRules) {
        request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
        request.setAttribute("withRules", isWithRules);
        return mapping.findForward("showCurriculum");
    }

    public ActionForward prepareWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepare(mapping, request, false);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return prepare(mapping, request, true);
    }

    public ActionForward chooseNewDestination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final List<CurriculumLine> selectedCurriculumLines = getSelectedCurriculumLines(request);

        if (selectedCurriculumLines.isEmpty()) {
            addActionMessage(request, "label.student.moveCurriculumLines.curriculumLines.selection.required");
            return prepare(mapping, request, isWithRules(request));
        }

        final boolean withRules = isWithRules(request);
        final MoveCurriculumLinesBean bean = MoveCurriculumLinesBean.buildFrom(selectedCurriculumLines, withRules);
        bean.setStudentCurricularPlan(getStudentCurricularPlan(request));
        bean.withRules(withRules);
        request.setAttribute("moveCurriculumLinesBean", bean);

        return mapping.findForward("chooseNewLocation");
    }

    protected boolean isWithRules(final HttpServletRequest request) {
        return Boolean.valueOf((String) getFromRequest(request, "withRules")).booleanValue();
    }

    public ActionForward moveCurriculumLines(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final MoveCurriculumLinesBean moveCurriculumLinesBean = getRenderedObject("move-curriculum-lines-bean");

        if (!RenderUtils.getViewState("move-curriculum-lines-bean-entries").isValid()) {
            request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);
            return mapping.findForward("chooseNewLocation");
        }

        try {
            MoveCurriculumLines.run(moveCurriculumLinesBean);
        } catch (final EnrollmentDomainException e) {
            addRuleResultMessagesToActionMessages(request, e.getFalseResult());
            request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);
            return mapping.findForward("chooseNewLocation");

        } catch (final IllegalDataAccessException e) {
            addActionMessage(request, "error.NotAuthorized");
            request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);
            return mapping.findForward("chooseNewLocation");

        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("moveCurriculumLinesBean", moveCurriculumLinesBean);
            return mapping.findForward("chooseNewLocation");
        }

        request.setAttribute("studentCurricularPlan", moveCurriculumLinesBean.getStudentCurricularPlan());
        request.setAttribute("withRules", moveCurriculumLinesBean.isWithRules());
        return mapping.findForward("showCurriculum");
    }

    private void addRuleResultMessagesToActionMessages(HttpServletRequest request, RuleResult... falseRuleResults) {
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
            result.add((CurriculumLine) AbstractDomainObject.fromExternalId(curriculumLineIdString));
        }

        return result;
    }

    protected StudentCurricularPlan getStudentCurricularPlan(HttpServletRequest request) {
        return getDomainObject(request, "scpID");
    }

}
