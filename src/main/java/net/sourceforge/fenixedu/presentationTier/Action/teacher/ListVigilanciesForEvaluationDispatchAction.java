/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.ChangeConvokeActive;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.ChangeConvokeStatus;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.AttendingStatus;
import net.sourceforge.fenixedu.domain.vigilancy.OwnCourseVigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "teacher", path = "/evaluation/vigilancy/vigilantsForEvaluation", functionality = ManageExecutionCourseDA.class)
public class ListVigilanciesForEvaluationDispatchAction extends ExecutionCourseBaseAction {

    private ActionForward doForward(HttpServletRequest request, String path) {
        request.setAttribute("teacher$actual$page", path);
        return new ActionForward("/evaluation/evaluationFrame.jsp");
    }

    private void setState(HttpServletRequest request) {
        String executionCourseID = request.getParameter("executionCourseID");
        request.setAttribute("executionCourseID", executionCourseID);
        String writtenEvaluationID = request.getParameter("evaluationOID");
        WrittenEvaluation evaluation = (WrittenEvaluation) FenixFramework.getDomainObject(writtenEvaluationID);

        ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(Vigilancy.COMPARATOR_BY_VIGILANT_CATEGORY);
        comparator.addComparator(Vigilancy.COMPARATOR_BY_VIGILANT_USERNAME);

        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>(evaluation.getOthersVigilancies());
        Collections.sort(vigilancies, comparator);
        List<Vigilancy> ownVigilancies = new ArrayList<Vigilancy>(evaluation.getTeachersVigilancies());
        Collections.sort(ownVigilancies, comparator);

        request.setAttribute("evaluation", evaluation);
        request.setAttribute("ownVigilancies", ownVigilancies);
        request.setAttribute("vigilancies", vigilancies);
    }

    public ActionForward viewVigilants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setState(request);
        request.setAttribute("unconvokeRequest", new VariantBean());
        return doForward(request, "/teacher/evaluation/vigilancies/viewVigilancies.jsp");
    }

    public ActionForward editReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setState(request);
        return doForward(request, "/teacher/evaluation/vigilancies/editVigilanciesReport.jsp");
    }

    public ActionForward changeConvokeStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        String vigilancyID = request.getParameter("oid");
        Vigilancy vigilancy = (Vigilancy) FenixFramework.getDomainObject(vigilancyID);
        String participationType = request.getParameter("participationType");
        AttendingStatus status = AttendingStatus.valueOf(participationType);
        try {

            ChangeConvokeStatus.run(vigilancy, status);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        return editReport(mapping, form, request, response);
    }

    public ActionForward changeActiveConvoke(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        String vigilancyID = request.getParameter("oid");
        Vigilancy vigilancy = (Vigilancy) FenixFramework.getDomainObject(vigilancyID);
        if (vigilancy instanceof OwnCourseVigilancy) {
            String bool = request.getParameter("bool");
            Boolean active = Boolean.valueOf(bool);
            try {

                ChangeConvokeActive.run(vigilancy, active, getLoggedPerson(request));
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }
        return viewVigilants(mapping, form, request, response);
    }

    public ActionForward requestUnconvokes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        IViewState viewState = RenderUtils.getViewState("variantBean");
        WrittenEvaluation evaluation = (WrittenEvaluation) FenixFramework.getDomainObject(request.getParameter("evaluationOID"));
        if (viewState != null) {
            List<Vigilancy> vigilancies = evaluation.getActiveOtherVigilancies();
            VariantBean bean = (VariantBean) viewState.getMetaObject().getObject();
            Integer numberOfVigilantsToUnconvoke = bean.getInteger();
            if (numberOfVigilantsToUnconvoke > vigilancies.size()) {
                addActionMessage(request, "error.there.are.not.so.many.vigilants");
                return viewVigilants(mapping, form, request, response);
            }

            Collections.sort(vigilancies, new ReverseComparator(Vigilancy.COMPARATOR_BY_VIGILANT_SORT_CRITERIA));

            for (Vigilancy vigilancy : vigilancies.subList(0, numberOfVigilantsToUnconvoke)) {
                try {

                    ChangeConvokeActive.run(vigilancy, Boolean.FALSE, getLoggedPerson(request));
                } catch (DomainException e) {
                    addActionMessage(request, e.getMessage());
                }

            }
        }

        RenderUtils.invalidateViewState("variantBean");
        return viewVigilants(mapping, form, request, response);
    }

}