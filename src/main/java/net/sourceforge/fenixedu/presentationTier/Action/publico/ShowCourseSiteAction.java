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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ExecutionCourseSiteComponentService;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadCurriculumByCurricularCourseCode;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteFirstPage;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author T�nia Pous�o Create on 20/Nov/2003
 */
@Mapping(module = "publico", path = "/showCourseSite", input = "showCurricularCourseSite", attribute = "chooseContextDegreeForm",
        formBean = "chooseContextDegreeForm", scope = "request", validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "showExecutionCourseSite", path = "showExecutionCourseSite"),
        @Forward(name = "showCurricularCourseSiteEnglish", path = "showCurricularCourseSiteEnglish"),
        @Forward(name = "showCurricularCourseSite", path = "showCurricularCourseSite") })
public class ShowCourseSiteAction extends FenixContextDispatchAction {

    public ActionForward showCurricularCourseSite(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        // Integer executionPeriodOId = getFromRequest("executionPeriodOID",
        // request);

        DynaActionForm indexForm = (DynaActionForm) actionForm;
        String degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        String index = getFromRequest("index", request);
        request.setAttribute("index", index);

        String executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);

        String degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        String curricularCourseId = getFromRequest("curricularCourseID", request);
        request.setAttribute("curricularCourseID", curricularCourseId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        Integer curricularYear = (Integer) indexForm.get("curYear");

        indexForm.set("indice", indexForm.get("indice"));
        indexForm.set("curYear", curricularYear);

        InfoCurriculum infoCurriculum = null;
        try {
            infoCurriculum = ReadCurriculumByCurricularCourseCode.run(curricularCourseId);
        } catch (NonExistingServiceException e) {
            errors.add("chosenCurricularCourse", new ActionError("error.coordinator.chosenCurricularCourse"));
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourse")) {
                errors.add("nullCode", new ActionError("error.coordinator.noCurricularCourse"));
            } else {
                throw new FenixActionException(e);
            }
        }
        if (infoCurriculum == null) {
            errors.add("noCurriculum", new ActionError("error.coordinator.noCurriculum"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        // order list of execution courses by name
        if (infoCurriculum.getInfoCurricularCourse() != null
                && infoCurriculum.getInfoCurricularCourse().getInfoAssociatedExecutionCourses() != null) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("infoExecutionPeriod.infoExecutionYear.beginDate"), true);
            comparatorChain.addComparator(new BeanComparator("infoExecutionPeriod.beginDate"), true);

            Collections.sort(infoCurriculum.getInfoCurricularCourse().getInfoAssociatedExecutionCourses(), comparatorChain);
        }

        // order list by year, semester
        if (infoCurriculum.getInfoCurricularCourse() != null
                && infoCurriculum.getInfoCurricularCourse().getCurricularCourseExecutionScope() != null) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            Collections.sort(infoCurriculum.getInfoCurricularCourse().getInfoScopes(), comparatorChain);
        }
        infoCurriculum.prepareEnglishPresentation(getLocale(request));
        request.setAttribute("infoCurriculum", infoCurriculum);

        return mapping.findForward("showCurricularCourseSite");

    }

    public ActionForward showExecutionCourseSite(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        String executionCourseId = getFromRequest("executionCourseID", request);
        request.setAttribute("executionCourseID", executionCourseId);

        ISiteComponent firstPageComponent = new InfoSiteFirstPage();
        ISiteComponent commonComponent = new InfoSiteCommon();

        Object[] args = { commonComponent, firstPageComponent, null, executionCourseId, null, null };
        ExecutionCourseSiteView siteView = null;

        try {
            siteView =
                    ExecutionCourseSiteComponentService.runExecutionCourseSiteComponentService(commonComponent,
                            firstPageComponent, null, executionCourseId, null, null);

            request.setAttribute("objectCode", ((InfoSiteFirstPage) siteView.getComponent()).getSiteExternalId());
            request.setAttribute("siteView", siteView);
            request.setAttribute("executionCourseCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse()
                    .getExternalId());
            request.setAttribute("executionPeriodCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse()
                    .getInfoExecutionPeriod().getExternalId());
            if (siteView.getComponent() instanceof InfoSiteSection) {
                request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent()).getSection());
            }
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A disciplina", e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showExecutionCourseSite");
    }

}