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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.student.equivalencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateCurricularCourseEquivalency;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteCurricularCourseEquivalency;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurricularCoursesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadDegreeCurricularPlansByDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminEquivalencesApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = AcademicAdminEquivalencesApp.class, path = "curricular-course-equivalences",
        titleKey = "label.studentDismissal.equivalences", accessGroup = "academic(MANAGE_EQUIVALENCES)")
@Mapping(module = "academicAdministration", path = "/curricularCourseEquivalencies",
        input = "/curricularCourseEquivalencies.do?method=prepare&page=0", formBean = "curricularCourseEquivalenciesForm")
@Forwards({
        @Forward(name = "showEquivalencies", path = "/academicAdministration/equivalences/curricularCourseEquivalencies.jsp"),
        @Forward(name = "showCreateEquivalencyForm",
                path = "/academicAdministration/equivalences/createCurricularCourseEquivalencies.jsp") })
@Exceptions(value = { @ExceptionHandling(type = DomainException.class, key = "error.exists.curricular.course.equivalency",
        handler = FenixErrorExceptionHandler.class, scope = "request") })
public class CurricularCourseEquivalenciesDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final User userView = Authenticate.getUser();
        final DynaActionForm actionForm = (DynaActionForm) form;

        setInfoDegreesToManage(request, userView);

        final String degreeIDString = (String) actionForm.get("degreeID");
        if (isValidObjectID(degreeIDString)) {
            setInfoDegreeCurricularPlans(request, userView, degreeIDString, "infoDegreeCurricularPlans");

            final String degreeCurricularPlanIDString = (String) actionForm.get("degreeCurricularPlanID");
            if (isValidObjectID(degreeCurricularPlanIDString)) {
                DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanIDString);
                List<CurricularCourseEquivalence> equivalences =
                        new ArrayList<CurricularCourseEquivalence>(degreeCurricularPlan.getCurricularCourseEquivalencesSet());
                sortInfoCurricularCourseEquivalences(equivalences);
                request.setAttribute("curricularCourseEquivalences", equivalences);
            }
        }

        return mapping.findForward("showEquivalencies");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final User userView = Authenticate.getUser();
        final DynaActionForm actionForm = (DynaActionForm) form;

        final String degreeCurricularPlanIDString = (String) actionForm.get("degreeCurricularPlanID");
        if (isValidObjectID(degreeCurricularPlanIDString)) {
            setInfoDegreesToAdd(request, userView);

            final String degreeIDString = (String) actionForm.get("degreeID");
            if (isValidObjectID(degreeIDString)) {
                setInfoDegreeCurricularPlans(request, userView, degreeIDString, "infoDegreeCurricularPlans");
            }

            setInfoCurricularCourses(request, userView, degreeCurricularPlanIDString, "infoCurricularCourses");

            final String oldDegreeIDString = (String) actionForm.get("oldDegreeID");
            if (isValidObjectID(oldDegreeIDString)) {
                setInfoDegreeCurricularPlans(request, userView, oldDegreeIDString, "oldInfoDegreeCurricularPlans");
            }

            final String oldDegreeCurricularPlanIDString = (String) actionForm.get("oldDegreeCurricularPlanID");
            if (isValidObjectID(oldDegreeCurricularPlanIDString)) {
                setInfoCurricularCourses(request, userView, oldDegreeCurricularPlanIDString, "oldInfoCurricularCourses");
            }

            return mapping.findForward("showCreateEquivalencyForm");
        } else {
            return prepare(mapping, form, request, response);
        }
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final DynaActionForm actionForm = (DynaActionForm) form;

        final String degreeCurricularPlanIDString = (String) actionForm.get("degreeCurricularPlanID");
        final String curricularCourseIDString = (String) actionForm.get("curricularCourseID");
        final String oldCurricularCourseIDString = (String) actionForm.get("oldCurricularCourseID");
        if (isValidObjectID(degreeCurricularPlanIDString) && isValidObjectID(curricularCourseIDString)
                && isValidObjectID(oldCurricularCourseIDString)) {

            try {
                CreateCurricularCourseEquivalency.run(degreeCurricularPlanIDString, curricularCourseIDString,
                        oldCurricularCourseIDString);
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }

        return prepare(mapping, form, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final DynaActionForm actionForm = (DynaActionForm) form;

        final String curricularCourseEquivalencyIDString = (String) actionForm.get("curricularCourseEquivalencyID");
        if (isValidObjectID(curricularCourseEquivalencyIDString)) {
            try {
                DeleteCurricularCourseEquivalency.run(curricularCourseEquivalencyIDString);
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }

        return prepare(mapping, form, request, response);
    }

    private void setInfoDegreesToManage(final HttpServletRequest request, final User userView) throws FenixServiceException {

        final SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        degrees.addAll(AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_EQUIVALENCES));
        request.setAttribute("infoDegrees", degrees);
    }

    private void setInfoDegreesToAdd(final HttpServletRequest request, final User userView) throws FenixServiceException {

        final SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.DEGREE));
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE));
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE));
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
        request.setAttribute("infoDegrees", degrees);
    }

    private void setInfoDegreeCurricularPlans(final HttpServletRequest request, final User userView, final String degreeID,
            final String attributeName) throws FenixServiceException {

        final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlans = ReadDegreeCurricularPlansByDegree.run(degreeID);
        sortInfoDegreeCurricularPlans(infoDegreeCurricularPlans);
        request.setAttribute(attributeName, infoDegreeCurricularPlans);
    }

    private void setInfoCurricularCourses(final HttpServletRequest request, final User userView,
            final String degreeCurricularPlanID, final String attribute) throws FenixServiceException {

        final List<InfoCurricularCourse> infoCurricularCourses =
                ReadCurricularCoursesByDegreeCurricularPlan.run(degreeCurricularPlanID);
        sortInfoCurricularCourses(infoCurricularCourses);
        request.setAttribute(attribute, infoCurricularCourses);
    }

    private boolean isValidObjectID(final String objectIDString) {
        return objectIDString != null && objectIDString.length() > 0;
    }

    private void sortInfoDegreeCurricularPlans(final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlans) {
        Collections.sort(infoDegreeCurricularPlans, new BeanComparator("name"));
    }

    private void sortInfoCurricularCourseEquivalences(final List<CurricularCourseEquivalence> equivalences) {
        final ComparatorChain chain = new ComparatorChain();
        chain.addComparator(CurricularCourseEquivalence.COMPARATOR_BY_EQUIVALENT_COURSE_NAME);
        chain.addComparator(CurricularCourseEquivalence.COMPARATOR_BY_EQUIVALENT_COURSE_CODE);
        Collections.sort(equivalences, chain);
    }

    private void sortInfoCurricularCourses(final List<InfoCurricularCourse> infoCurricularCourses) {
        Collections.sort(infoCurricularCourses, InfoCurricularCourse.COMPARATOR_BY_NAME_AND_ID);
    }

}
