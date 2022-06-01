/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoExecutionCourseEditor;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.service.services.commons.ReadExecutionPeriods;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.InsertExecutionCourseAtExecutionPeriod;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = AcademicAdminExecutionsApp.class, path = "insert-execution-course",
        titleKey = "label.manager.executionCourseManagement.insert.executionCourse",
        accessGroup = "academic(MANAGE_EXECUTION_COURSES)")
@Mapping(module = "academicAdministration", path = "/insertExecutionCourse", formBean = "insertExecutionCourseForm")
@Forwards({
        @Forward(name = "firstPage", path = "/academicAdministration/executionCourseManagement/welcomeScreen.jsp"),
        @Forward(name = "insertExecutionCourse",
                path = "/academicAdministration/executionCourseManagement/insertExecutionCourse.jsp") })
public class InsertExecutionCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return prepareInsertExecutionCourse(mapping, form, request, response);
    }

    @EntryPoint
    public ActionForward prepareInsertExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        List infoExecutionPeriods = null;
        infoExecutionPeriods = ReadExecutionPeriods.run();

        if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
            // exclude closed execution periods
            infoExecutionPeriods = (List) CollectionUtils.select(infoExecutionPeriods, new Predicate() {
                @Override
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

            List<LabelValueBean> executionPeriodLabels = new ArrayList<LabelValueBean>();
            CollectionUtils.collect(infoExecutionPeriods, new Transformer() {
                @Override
                public Object transform(Object arg0) {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

                    LabelValueBean executionPeriod =
                            new LabelValueBean(infoExecutionPeriod.getName() + " - "
                                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod.getExternalId()
                                    .toString());
                    return executionPeriod;
                }
            }, executionPeriodLabels);

            request.setAttribute(PresentationConstants.LIST_EXECUTION_PERIODS, executionPeriodLabels);

            List<LabelValueBean> entryPhases = new ArrayList<LabelValueBean>();
            for (EntryPhase entryPhase : EntryPhase.values()) {
                LabelValueBean labelValueBean = new LabelValueBean(entryPhase.getLocalizedName(), entryPhase.getName());
                entryPhases.add(labelValueBean);
            }
            request.setAttribute("entryPhases", entryPhases);

        }

        return mapping.findForward("insertExecutionCourse");
    }

    public ActionForward insertExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        InfoExecutionCourseEditor infoExecutionCourse = null;
        try {
            infoExecutionCourse = fillInfoExecutionCourse(form, request);
            checkInfoExecutionCourse(infoExecutionCourse);
        } catch (DomainException ex) {
            //ugly hack to simulate a form validator and its error messages
            addActionMessageLiteral("error", request, ex.getKey());
            return prepareInsertExecutionCourse(mapping, form, request, response);
        }

        try {
            InsertExecutionCourseAtExecutionPeriod.run(infoExecutionCourse);
            addActionMessage("success", request, "message.manager.executionCourseManagement.insert.success",
                    infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(), infoExecutionCourse.getInfoExecutionPeriod()
                            .getExecutionPeriod().getName(), infoExecutionCourse.getInfoExecutionPeriod().getExecutionPeriod()
                            .getExecutionYear().getYear());
        } catch (DomainException ex) {
            addActionMessage("error", request, ex.getMessage(), ex.getArgs());
            return prepareInsertExecutionCourse(mapping, form, request, response);
        } catch (FenixServiceException ex) {
            addActionMessage("error", request, ex.getMessage());
        }
        return mapping.findForward("firstPage");
    }

    private InfoExecutionCourseEditor fillInfoExecutionCourse(ActionForm form, HttpServletRequest request) {

        DynaActionForm dynaForm = (DynaValidatorForm) form;
        InfoExecutionCourseEditor infoExecutionCourse = new InfoExecutionCourseEditor();

        String name = (String) dynaForm.get("name");
        infoExecutionCourse.setNome(name);

        String nameEn = (String) dynaForm.get("nameEn");
        infoExecutionCourse.setNameEn(nameEn);

        LocalizedString nameI18 = new LocalizedString(LocaleUtils.PT, name).with(LocaleUtils.EN, nameEn);
        infoExecutionCourse.setNameI18(nameI18);

        String code = (String) dynaForm.get("code");
        infoExecutionCourse.setSigla(code);

        String executionPeriodId = (String) dynaForm.get("executionPeriodId");
        InfoExecutionPeriod infoExecutionPeriod = null;
        if (!StringUtils.isEmpty(executionPeriodId) && StringUtils.isNumeric(executionPeriodId)) {
            infoExecutionPeriod = new InfoExecutionPeriod((ExecutionSemester) FenixFramework.getDomainObject(executionPeriodId));
        }

        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        String comment = "";
        if ((String) dynaForm.get("comment") != null) {
            comment = (String) dynaForm.get("comment");
        }
        infoExecutionCourse.setComment(comment);

        String entryPhaseString = dynaForm.getString("entryPhase");
        EntryPhase entryPhase = null;
        if (entryPhaseString != null && entryPhaseString.length() > 0) {
            entryPhase = EntryPhase.valueOf(entryPhaseString);
        }
        infoExecutionCourse.setEntryPhase(entryPhase);

        return infoExecutionCourse;
    }

    private void checkInfoExecutionCourse(InfoExecutionCourseEditor infoExecutionCourse) {
        StringBuilder errors = new StringBuilder();
        if (infoExecutionCourse.getInfoExecutionPeriod() == null) {
            errors.append(errorStringBuilder("property.executionPeriod"));
        }
        if (StringUtils.isEmpty(infoExecutionCourse.getNome())) {
            errors.append(errorStringBuilder("label.name"));
        }
        if (StringUtils.isEmpty(infoExecutionCourse.getNameEn())) {
            errors.append(errorStringBuilder("label.name"));
        }
        if (StringUtils.isEmpty(infoExecutionCourse.getSigla())) {
            errors.append(errorStringBuilder("label.code"));
        }
        if (errors.length() > 0) {
            //ugly hack to simulate a form validator and its error messages
            throw new DomainException(errors.toString());
        }
    }

    private String errorStringBuilder(String property) {
        return BundleUtil.getString(Bundle.MANAGER, "errors.required", BundleUtil.getString(Bundle.APPLICATION, property)) + " ";
    }
}
