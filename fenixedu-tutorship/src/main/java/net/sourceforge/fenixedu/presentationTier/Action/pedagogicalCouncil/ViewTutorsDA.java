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
package org.fenixedu.academic.ui.struts.action.pedagogicalCouncil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.TutorshipIntention;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.PedagogicalCouncilApp.TutorshipApp;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = TutorshipApp.class, path = "view-tutors", titleKey = "title.tutorship.view")
@Mapping(path = "/viewTutors", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewTutors", path = "/pedagogicalCouncil/tutorship/viewTutors.jsp"),
        @Forward(name = "viewStudentOfTutor", path = "/pedagogicalCouncil/tutorship/viewStudentsOfTutor.jsp") })
public class ViewTutorsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward listTutors(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ViewTutorsBean bean = getRenderedObject("tutorsBean");
        if (bean == null) {
            bean = new ViewTutorsBean();
        }
        request.setAttribute("tutorsBean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("viewTutors");
    }

    public ActionForward viewStudentsOfTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tutorshipIntentionID = (String) getFromRequest(request, "tutorshipIntentionID");

        TutorshipIntention tutorshipIntention = FenixFramework.getDomainObject(tutorshipIntentionID);
        request.setAttribute("tutorshipIntention", tutorshipIntention);

        return mapping.findForward("viewStudentOfTutor");
    }

    public ActionForward backToTutors(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tutorshipIntentionID = (String) getFromRequest(request, "tutorshipIntentionID");

        TutorshipIntention tutorshipIntention = FenixFramework.getDomainObject(tutorshipIntentionID);
        ExecutionDegree executionDegree =
                tutorshipIntention.getDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(
                        tutorshipIntention.getAcademicInterval());
        ExecutionSemester firstExecutionPeriod = executionDegree.getExecutionYear().getFirstExecutionPeriod();

        ViewTutorsBean bean = new ViewTutorsBean();
        bean.setExecutionDegree(executionDegree);
        bean.setExecutionSemester(firstExecutionPeriod);

        request.setAttribute("tutorsBean", bean);
        return mapping.findForward("viewTutors");
    }

    public static class ContextDegreesProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object arg1) {
            final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
            final ViewTutorsBean bean = (ViewTutorsBean) source;
            final ExecutionSemester executionPeriod = bean.getExecutionSemester();
            if (executionPeriod != null) {
                final ExecutionYear executionYear = executionPeriod.getExecutionYear();
                for (ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
                    DegreeType degreeType = executionDegree.getDegreeType();
                    if (degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE || degreeType == DegreeType.BOLONHA_DEGREE) {
                        executionDegrees.add(executionDegree);
                    }
                }
            }
            Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
            return executionDegrees;
        }
    }

    public static class ExecutionSemestersProvider implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();
            for (ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
                if (executionSemester.isFirstOfYear()) {
                    executionSemesters.add(executionSemester);
                }
            }
            Collections.sort(executionSemesters, new ReverseComparator());
            return executionSemesters;
        }

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }
    }

    public static class ViewTutorsBean implements Serializable {

        private ExecutionSemester executionSemester;
        private ExecutionDegree executionDegree;

        public List<TutorshipIntention> getTutors() {
            if (getExecutionDegree() != null) {
                return TutorshipIntention.getTutorshipIntentions(getExecutionDegree());
            }
            return null;
        }

        public ExecutionSemester getExecutionSemester() {
            return executionSemester;
        }

        public void setExecutionSemester(ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
        }

        public ExecutionDegree getExecutionDegree() {
            return executionDegree;
        }

        public void setExecutionDegree(ExecutionDegree executionDegree) {
            this.executionDegree = executionDegree;
        }
    }
}
