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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ImportCustomizationOptions;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.ContentManagementLog;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

@Mapping(path = "/alternativeSite", module = "teacher", functionality = ManageExecutionCourseDA.class,
        formBean = "customOptionsForm")
public class AlternativeSiteDA extends ExecutionCourseBaseAction {

    @Input
    public ActionForward prepareCustomizationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        ExecutionCourseSite site = executionCourse.getSite();
        DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
        alternativeSiteForm.set("siteAddress", site.getAlternativeSite());
        alternativeSiteForm.set("mail", site.getMail());
        alternativeSiteForm.set("dynamicMailDistribution", site.getDynamicMailDistribution());
        alternativeSiteForm.set("initialStatement", site.getInitialStatement());
        alternativeSiteForm.set("introduction", site.getIntroduction());
        return forward(request, "/teacher/alternativeSiteManagement_bd.jsp");
    }

    public ActionForward editCustomizationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
        ExecutionCourse course = getExecutionCourse(request);
        String alternativeSite = (String) alternativeSiteForm.get("siteAddress");
        String mail = (String) alternativeSiteForm.get("mail");
        String initialStatement = (String) alternativeSiteForm.get("initialStatement");
        String introduction = (String) alternativeSiteForm.get("introduction");
        Boolean dynamicMailDistribution = (Boolean) alternativeSiteForm.get("dynamicMailDistribution");

        editOptions(course, alternativeSite, mail, dynamicMailDistribution, initialStatement, introduction);

        return prepareCustomizationOptions(mapping, form, request, response);
    }

    public ActionForward prepareImportCustomizationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        request.setAttribute("importContentBean", new ImportContentBean());
        request.setAttribute("executionCourse", executionCourse);
        return forward(request, "/teacher/importCustomizationOptions.jsp");
    }

    public ActionForward submitDataToImportCustomizationOptions(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final ExecutionCourse executionCourse = getExecutionCourse(request);
        final IViewState viewState = RenderUtils.getViewState();
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);
        request.setAttribute("executionCourse", executionCourse);
        return forward(request, "/teacher/importCustomizationOptions.jsp");
    }

    public ActionForward submitDataToImportCustomizationOptionsPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final ExecutionCourse executionCourse = getExecutionCourse(request);
        final IViewState viewState = RenderUtils.getViewState();
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        if (bean.getCurricularYear() == null || bean.getExecutionPeriod() == null || bean.getExecutionDegree() == null) {
            bean.setExecutionCourse(null);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("importContentBean", bean);
        request.setAttribute("executionCourse", executionCourse);
        return forward(request, "/teacher/importCustomizationOptions.jsp");
    }

    public ActionForward importCustomizationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixActionException {

        final IViewState viewState = RenderUtils.getViewState();
        ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);

        ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        ExecutionCourse executionCourseTo = getExecutionCourse(request);

        try {
            ImportCustomizationOptions.runImportCustomizationOptions(executionCourseTo.getExternalId(),
                    executionCourseTo.getSite(), executionCourseFrom.getSite());
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return prepareCustomizationOptions(mapping, form, request, response);
    }

    @Atomic
    private void editOptions(ExecutionCourse executionCourse, final String alternativeSite, final String mail,
            final Boolean dynamicMailDistribution, final String initialStatement, final String introduction) {
        final ExecutionCourseSite site = executionCourse.getSite();

        site.setAlternativeSite(alternativeSite);
        site.setMail(mail);
        site.setInitialStatement(initialStatement);
        site.setIntroduction(introduction);
        site.setDynamicMailDistribution(dynamicMailDistribution);

        ContentManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.content.customization.edited", executionCourse.getNome(),
                executionCourse.getDegreePresentationString());
    }

}
