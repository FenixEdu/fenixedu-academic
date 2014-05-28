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
package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearIntervalBean;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.research.ResearcherApplication.CurriculumApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = CurriculumApp.class, path = "curriculum", titleKey = "link.viewCurriculum")
@Mapping(module = "researcher", path = "/viewCurriculum")
@Forwards(value = { @Forward(name = "Success", path = "/researcher/viewCurriculum.jsp") })
public class ViewCurriculumDispatchAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String personId = request.getParameter("personOID");

        final Person person =
                ((personId != null && personId.length() > 0) ? (Person) FenixFramework.getDomainObject(personId) : getLoggedPerson(request));

        request.setAttribute("person", person);

        ExecutionYearIntervalBean bean = retrieveExecutionYearBeanFromRequest(request);

        ExecutionYear firstExecutionYear = bean.getFirstExecutionYear();
        ExecutionYear finalExecutionYear = bean.getFinalExecutionYear();

        ExecutionYear firstOfAll = ExecutionYear.readFirstExecutionYear();

        if (firstExecutionYear == null) {
            firstExecutionYear = firstOfAll;
        }
        if (finalExecutionYear == null || finalExecutionYear.isBefore(firstExecutionYear)) {
            finalExecutionYear = ExecutionYear.readLastExecutionYear();
        }

        bean.setFinalExecutionYear(finalExecutionYear);
        bean.setFirstExecutionYear(firstExecutionYear);

        putInformationOnRequestForGivenExecutionYear(firstExecutionYear, finalExecutionYear, person, request);

        return mapping.findForward("Success");
    }

    private ExecutionYearIntervalBean retrieveExecutionYearBeanFromRequest(HttpServletRequest request) {
        IViewState viewState = RenderUtils.getViewState("executionYearIntervalBean");
        ExecutionYearIntervalBean bean =
                (viewState != null) ? (ExecutionYearIntervalBean) viewState.getMetaObject().getObject() : new ExecutionYearIntervalBean();
        request.setAttribute("executionYearIntervalBean", bean);
        RenderUtils.invalidateViewState("executionYearIntervalBean");
        return bean;
    }

    private void putInformationOnRequestForGivenExecutionYear(ExecutionYear firstExecutionYear,
            ExecutionYear finaltExecutionYear, Person person, HttpServletRequest request) {

        Set<Advise> final_works = new HashSet<Advise>();
        Set<MasterDegreeThesisDataVersion> guidances = new HashSet<MasterDegreeThesisDataVersion>();
        SortedSet<ExecutionCourse> lectures =
                new TreeSet<ExecutionCourse>(new ReverseComparator(
                        ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME));
        Set<Thesis> orientedThesis = new HashSet<Thesis>();
        Set<PersonFunction> functions = new HashSet<PersonFunction>();
        SortedSet<Career> career = new TreeSet<Career>(Career.CAREER_DATE_COMPARATOR);

        ExecutionYear stoppageYear = finaltExecutionYear.getNextExecutionYear();
        ExecutionYear iteratorYear = firstExecutionYear;
        Teacher teacher = person.getTeacher();

        while (iteratorYear != stoppageYear) {

            if (teacher != null) {
                final_works.addAll(teacher.getAdvisesByAdviseTypeAndExecutionYear(AdviseType.FINAL_WORK_DEGREE, iteratorYear));

                guidances.addAll(teacher.getGuidedMasterDegreeThesisByExecutionYear(iteratorYear));
                lectures.addAll(teacher.getLecturedExecutionCoursesByExecutionYear(iteratorYear));
            }

            orientedThesis.addAll(person.getOrientedOrCoorientedThesis(iteratorYear));

            functions.addAll(person.getPersonFuntions(iteratorYear.getBeginDateYearMonthDay(),
                    iteratorYear.getEndDateYearMonthDay()));
            iteratorYear = iteratorYear.getNextExecutionYear();
        }

        career.addAll(person.getCareersByTypeAndInterval(CareerType.PROFESSIONAL, new Interval(firstExecutionYear
                .getBeginDateYearMonthDay().toDateTimeAtMidnight(), finaltExecutionYear.getEndDateYearMonthDay()
                .toDateTimeAtMidnight())));

        List<PersonFunction> functionsList = new ArrayList<PersonFunction>(functions);
        Collections.sort(functionsList, new ReverseComparator(new BeanComparator("beginDateInDateType")));
        request.setAttribute("functions", functionsList);
        List<Advise> final_worksList = new ArrayList<Advise>(final_works);
        Collections.sort(final_worksList, new BeanComparator("student.number"));

        request.setAttribute("final_works", final_worksList);
        request.setAttribute("guidances", guidances);
        request.setAttribute("lectures", lectures);
        request.setAttribute("orientedThesis", orientedThesis);
        if (!(guidances.isEmpty() && orientedThesis.isEmpty())) {
            request.setAttribute("secondCycleThesis", true);
        }
        request.setAttribute("career", career);
    }
}