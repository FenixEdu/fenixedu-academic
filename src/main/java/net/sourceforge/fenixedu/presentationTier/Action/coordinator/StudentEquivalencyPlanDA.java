package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.CreateStudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.DeleteEquivalencePlanEntry;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.StudentEquivalencyPlanEntryCreator;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.util.search.StudentSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/degreeCurricularPlan/studentEquivalencyPlan", module = "manager")
@Forwards({ @Forward(name = "showPlan", path = "/academicAdminOffice/degreeCurricularPlan/showStudentEquivalencyPlan.jsp"),
        @Forward(name = "addEquivalency", path = "/academicAdminOffice/degreeCurricularPlan/addStudentEquivalency.jsp") })
public class StudentEquivalencyPlanDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DegreeCurricularPlan selectedDegreeCurricularPlan = getSelectedDegreeCurricularPlan(request);
        if (selectedDegreeCurricularPlan == null) {
            final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
            degreeTypes.add(DegreeType.BOLONHA_DEGREE);
            degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);
            degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
            request.setAttribute("degreeCurricularPlans", selectedDegreeCurricularPlan.getDegreeCurricularPlans(degreeTypes));
        } else {
            request.setAttribute("selectedDegreeCurricularPlan", selectedDegreeCurricularPlan);
        }

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        request.setAttribute("degreeCurricularPlan",
                degreeCurricularPlan != null ? degreeCurricularPlan : selectedDegreeCurricularPlan);

        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showPlan(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Student student = getStudent(request);
        if (student != null) {
            final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                    getStudentCurricularPlanEquivalencePlan(request, student);
            if (studentCurricularPlanEquivalencePlan != null) {

                final DegreeCurricularPlan degreeCurricularPlan =
                        (DegreeCurricularPlan) request.getAttribute("selectedDegreeCurricularPlan");
                if (degreeCurricularPlan != null) {
                    request.setAttribute("studentCurricularPlanEquivalencePlan", studentCurricularPlanEquivalencePlan);
                    studentCurricularPlanEquivalencePlan.getRootEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan);
                    request.setAttribute("rootEquivalencyPlanEntryCurriculumModuleWrapper", studentCurricularPlanEquivalencePlan
                            .getRootEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan));
                }

            }
        }
        return mapping.findForward("showPlan");
    }

    public ActionForward showTable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Student student = getStudent(request);
        if (student != null) {
            final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                    getStudentCurricularPlanEquivalencePlan(request, student);
            if (studentCurricularPlanEquivalencePlan != null) {
                request.setAttribute("studentCurricularPlanEquivalencePlan", studentCurricularPlanEquivalencePlan);
                final DegreeCurricularPlan degreeCurricularPlan =
                        (DegreeCurricularPlan) request.getAttribute("selectedDegreeCurricularPlan");
                final CurriculumModule curriculumModule = getCurriculumModule(request);
                request.setAttribute("equivalencePlanEntryWrappers", studentCurricularPlanEquivalencePlan
                        .getEquivalencePlanEntryWrappers(degreeCurricularPlan, curriculumModule));
            }
        }
        return mapping.findForward("showPlan");
    }

    public ActionForward prepareAddEquivalency(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Student student = getStudent(request);
        final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                getStudentCurricularPlanEquivalencePlan(request, student);
        final DegreeCurricularPlan degreeCurricularPlan =
                (DegreeCurricularPlan) request.getAttribute("selectedDegreeCurricularPlan");

        StudentEquivalencyPlanEntryCreator studentEquivalencyPlanEntryCreator = getRenderedObject();
        if (studentEquivalencyPlanEntryCreator == null) {
            studentEquivalencyPlanEntryCreator =
                    new StudentEquivalencyPlanEntryCreator(studentCurricularPlanEquivalencePlan,
                            degreeCurricularPlan.getEquivalencePlan());
        }

        final CurriculumModule curriculumModule = getCurriculumModule(request);
        if (curriculumModule != null) {
            studentEquivalencyPlanEntryCreator.setOriginDegreeModuleToAdd(curriculumModule.getDegreeModule());
            studentEquivalencyPlanEntryCreator.addOrigin(curriculumModule.getDegreeModule());
        }

        request.setAttribute("studentEquivalencyPlanEntryCreator", studentEquivalencyPlanEntryCreator);
        return mapping.findForward("addEquivalency");
    }

    public ActionForward deleteEquivalency(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        getStudent(request);
        final EquivalencePlanEntry equivalencePlanEntry = getEquivalencePlanEntry(request);
        DeleteEquivalencePlanEntry.runDeleteEquivalencePlanEntry(equivalencePlanEntry);
        return showTable(mapping, actionForm, request, response);
    }

    public ActionForward activate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return changeActiveState(mapping, actionForm, request, response, "ActivateEquivalencePlanEntry");
    }

    public ActionForward deactivate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return changeActiveState(mapping, actionForm, request, response, "DeActivateEquivalencePlanEntry");
    }

    public ActionForward changeActiveState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, final String service) throws Exception {
        final Student student = getStudent(request);
        final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                getStudentCurricularPlanEquivalencePlan(request, student);
        final EquivalencePlanEntry equivalencePlanEntry = getEquivalencePlanEntry(request);
        final Object[] args = { studentCurricularPlanEquivalencePlan, equivalencePlanEntry };
//        ServiceManagerServiceFactory.executeService(service, args);
//        return showTable(mapping, actionForm, request, response);
        throw new UnsupportedOperationException("Service " + service + " no longer exists");
    }

    private EquivalencePlanEntry getEquivalencePlanEntry(HttpServletRequest request) {
        final String equivalencePlanEntryIDString = request.getParameter("equivalencePlanEntryID");
        return (EquivalencePlanEntry) AbstractDomainObject.fromExternalId(equivalencePlanEntryIDString);
    }

    private StudentCurricularPlanEquivalencePlan getStudentCurricularPlanEquivalencePlan(final HttpServletRequest request,
            final Student student) throws FenixServiceException {
        return CreateStudentCurricularPlanEquivalencePlan.run(student);
    }

    private Student getStudent(final HttpServletRequest request) {
        StudentSearchBean studentSearchBean =
                getRenderedObject("net.sourceforge.fenixedu.domain.util.search.StudentSearchBeanWithDegreeCurricularPlan");
        if (studentSearchBean == null) {
            studentSearchBean = getRenderedObject("net.sourceforge.fenixedu.domain.util.search.StudentSearchBean");
        }
        if (studentSearchBean == null) {
            studentSearchBean = new StudentSearchBean();
            final String studentNumber = request.getParameter("studentNumber");
            if (studentNumber != null && studentNumber.length() > 0) {
                studentSearchBean.setStudentNumber(Integer.valueOf(studentNumber));
            }

            studentSearchBean
                    .setDegreeCurricularPlan((DegreeCurricularPlan) request.getAttribute("selectedDegreeCurricularPlan"));

            studentSearchBean.setOldDegreeCurricularPlan((DegreeCurricularPlan) request.getAttribute("degreeCurricularPlan"));

        } else {
            final DegreeCurricularPlan degreeCurricularPlan = studentSearchBean.getDegreeCurricularPlan();
            if (degreeCurricularPlan != null) {
                request.setAttribute("selectedDegreeCurricularPlan", degreeCurricularPlan);
            }
        }
        request.setAttribute("studentSearchBean", studentSearchBean);
        final Student student = studentSearchBean.search();
        if (student != null) {
            request.setAttribute("student", student);
        }

        return student;
    }

    private CurriculumModule getCurriculumModule(final HttpServletRequest request) {
        final String curriculumModuleIDString = request.getParameter("curriculumModuleID");
        return AbstractDomainObject.fromExternalId(curriculumModuleIDString);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request) {
        return getDegreeCurricularPlan(request, "degreeCurricularPlanID");
    }

    private DegreeCurricularPlan getSelectedDegreeCurricularPlan(final HttpServletRequest request) {
        final StudentSearchBean studentSearchBean =
                getRenderedObject("net.sourceforge.fenixedu.domain.util.search.StudentSearchBeanWithDegreeCurricularPlan");
        return studentSearchBean != null ? studentSearchBean.getDegreeCurricularPlan() : getDegreeCurricularPlan(request,
                "selectedDegreeCurricularPlanID");
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request, final String attrName) {
        final String degreeCurricularPlanIDString = request.getParameter(attrName);
        return AbstractDomainObject.fromExternalId(degreeCurricularPlanIDString);
    }

}
