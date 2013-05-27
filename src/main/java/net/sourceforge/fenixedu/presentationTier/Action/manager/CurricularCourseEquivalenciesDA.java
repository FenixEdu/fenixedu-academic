package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
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
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class CurricularCourseEquivalenciesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final IUserView userView = UserView.getUser();
        final DynaActionForm actionForm = (DynaActionForm) form;

        setInfoDegreesToManage(request, userView);

        final String degreeIDString = (String) actionForm.get("degreeID");
        if (isValidObjectID(degreeIDString)) {
            setInfoDegreeCurricularPlans(request, userView, Integer.valueOf(degreeIDString), "infoDegreeCurricularPlans");

            final String degreeCurricularPlanIDString = (String) actionForm.get("degreeCurricularPlanID");
            if (isValidObjectID(degreeCurricularPlanIDString)) {
                DegreeCurricularPlan degreeCurricularPlan =
                        rootDomainObject.readDegreeCurricularPlanByOID(Integer.valueOf(degreeCurricularPlanIDString));
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
        final IUserView userView = UserView.getUser();
        final DynaActionForm actionForm = (DynaActionForm) form;

        final String degreeCurricularPlanIDString = (String) actionForm.get("degreeCurricularPlanID");
        if (isValidObjectID(degreeCurricularPlanIDString)) {
            setInfoDegreesToAdd(request, userView);

            final String degreeIDString = (String) actionForm.get("degreeID");
            if (isValidObjectID(degreeIDString)) {
                setInfoDegreeCurricularPlans(request, userView, Integer.valueOf(degreeIDString), "infoDegreeCurricularPlans");
            }

            setInfoCurricularCourses(request, userView, Integer.valueOf(degreeCurricularPlanIDString), "infoCurricularCourses");

            final String oldDegreeIDString = (String) actionForm.get("oldDegreeID");
            if (isValidObjectID(oldDegreeIDString)) {
                setInfoDegreeCurricularPlans(request, userView, Integer.valueOf(oldDegreeIDString),
                        "oldInfoDegreeCurricularPlans");
            }

            final String oldDegreeCurricularPlanIDString = (String) actionForm.get("oldDegreeCurricularPlanID");
            if (isValidObjectID(oldDegreeCurricularPlanIDString)) {
                setInfoCurricularCourses(request, userView, Integer.valueOf(oldDegreeCurricularPlanIDString),
                        "oldInfoCurricularCourses");
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
                CreateCurricularCourseEquivalency.run(Integer.valueOf(degreeCurricularPlanIDString),
                        Integer.valueOf(curricularCourseIDString), Integer.valueOf(oldCurricularCourseIDString));
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
                DeleteCurricularCourseEquivalency.run(Integer.valueOf(curricularCourseEquivalencyIDString));
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }

        return prepare(mapping, form, request, response);
    }

    private void setInfoDegreesToManage(final HttpServletRequest request, final IUserView userView) throws 
            FenixServiceException {

        final SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        degrees.addAll(AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(),
                AcademicOperationType.MANAGE_EQUIVALENCES));
        request.setAttribute("infoDegrees", degrees);
    }

    private void setInfoDegreesToAdd(final HttpServletRequest request, final IUserView userView) throws 
            FenixServiceException {

        final SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.DEGREE));
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE));
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE));
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
        request.setAttribute("infoDegrees", degrees);
    }

    private void setInfoDegreeCurricularPlans(final HttpServletRequest request, final IUserView userView, final Integer degreeID,
            final String attributeName) throws  FenixServiceException {

        final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlans = ReadDegreeCurricularPlansByDegree.run(degreeID);
        sortInfoDegreeCurricularPlans(infoDegreeCurricularPlans);
        request.setAttribute(attributeName, infoDegreeCurricularPlans);
    }

    private void setInfoCurricularCourses(final HttpServletRequest request, final IUserView userView,
            final Integer degreeCurricularPlanID, final String attribute) throws  FenixServiceException {

        final List<InfoCurricularCourse> infoCurricularCourses =
                ReadCurricularCoursesByDegreeCurricularPlan.run(degreeCurricularPlanID);
        sortInfoCurricularCourses(infoCurricularCourses);
        request.setAttribute(attribute, infoCurricularCourses);
    }

    private boolean isValidObjectID(final String objectIDString) {
        return objectIDString != null && objectIDString.length() > 0 && StringUtils.isNumeric(objectIDString);
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