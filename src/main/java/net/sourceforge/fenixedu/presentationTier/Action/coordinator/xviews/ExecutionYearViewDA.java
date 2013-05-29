package net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/xYear", module = "coordinator")
@Forwards({
        @Forward(name = "xViewsDisclaimer", path = "/coordinator/xviews/xViewsDisclaimer.jsp", tileProperties = @Tile(
                title = "private.coordinator.management.courses.analytictools.executionyear")),
        @Forward(name = "xYearEntry", path = "/coordinator/xviews/xYearEntry.jsp", tileProperties = @Tile(
                title = "private.coordinator.management.courses.analytictools.executionyear")),
        @Forward(name = "xYearDisplay", path = "/coordinator/xviews/xYearDisplay.jsp", tileProperties = @Tile(
                title = "private.coordinator.management.courses.analytictools.executionyear")) })
public class ExecutionYearViewDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showDisclaimer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        return mapping.findForward("xViewsDisclaimer");
    }

    public ActionForward showYearInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IUserView userView = UserView.getUser();

        YearViewBean searchFormBean = (YearViewBean) getRenderedObject("searchFormBean");
        RenderUtils.invalidateViewState();

        if (searchFormBean == null || searchFormBean.getExecutionYear() == null) {
            String degreeCurricularPlanID = null;
            DegreeCurricularPlan degreeCurricularPlan = null;
            if (request.getParameter("degreeCurricularPlanID") != null) {
                degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
                request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
                degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);
            }

            searchFormBean = new YearViewBean(degreeCurricularPlan);
            request.setAttribute("searchFormBean", searchFormBean);
            return mapping.findForward("xYearEntry");

        }

        /*
         * YearViewBean yearViewBean = new
         * YearViewBean(searchFormBean.getDegreeCurricularPlan());
         * yearViewBean.setExecutionYear(searchFormBean.getExecutionYear());
         * yearViewBean.setEnrolments();
         */

        request.setAttribute("dcpEId", searchFormBean.getDegreeCurricularPlan().getExternalId());
        request.setAttribute("eyEId", searchFormBean.getExecutionYear().getExternalId());

        /*
         * Inar totalInar = generateINAR(yearViewBean);
         * request.setAttribute("totalInar", totalInar);
         * 
         * int years =
         * yearViewBean.getDegreeCurricularPlan().getDegree().getDegreeType
         * ().getYears(); request.setAttribute("#years", years);
         * 
         * Map<CurricularYear, Inar> mapInarByYear =
         * generateInarByCurricularYear(yearViewBean); for (int i = 1; i <=
         * years; i++) { Inar value =
         * mapInarByYear.get(CurricularYear.readByYear(i)); String label =
         * "InarFor" + i + "Year"; request.setAttribute(label, value); }
         * 
         * Map<CurricularYear, String> mapAveragebyYear =
         * generateAverageByCurricularYear(yearViewBean); List averageByYear =
         * new LinkedList(mapAveragebyYear.entrySet());
         * request.setAttribute("averageByYear", averageByYear);
         * 
         * // @Deprecated // if(<3 == true) {you.marry(me) ? super.happy() :
         * null;} if (yearViewBean.hasBranchesByType(BranchType.MAJOR)) {
         * Map<BranchCourseGroup, Inar> mapInarByBranches =
         * generateInarByBranch(yearViewBean, BranchType.MAJOR);
         * yearViewBean.setHasMajorBranches(true);
         * yearViewBean.setMajorBranches(
         * yearViewBean.getDegreeCurricularPlan().getBranchesByType
         * (BranchType.MAJOR));
         * yearViewBean.setInarByMajorBranches(mapInarByBranches);
         * 
         * Map<BranchCourseGroup, String> mapAverageByBranches =
         * generateAverageByBranch(yearViewBean, BranchType.MAJOR);
         * yearViewBean.setAverageByMajorBranches(mapAverageByBranches); }
         * 
         * if (yearViewBean.hasBranchesByType(BranchType.MINOR)) {
         * Map<BranchCourseGroup, Inar> mapInarByBranches =
         * generateInarByBranch(yearViewBean, BranchType.MINOR);
         * yearViewBean.setHasMinorBranches(true);
         * yearViewBean.setMinorBranches(
         * yearViewBean.getDegreeCurricularPlan().getBranchesByType
         * (BranchType.MINOR));
         * yearViewBean.setInarByMinorBranches(mapInarByBranches);
         * 
         * Map<BranchCourseGroup, String> mapAverageByBranches =
         * generateAverageByBranch(yearViewBean, BranchType.MINOR);
         * yearViewBean.setAverageByMinorBranches(mapAverageByBranches); }
         * 
         * String resumedQUC = generateQUCResults(yearViewBean);
         * yearViewBean.setResumedQUC(resumedQUC);
         * 
         * request.setAttribute("yearViewBean", yearViewBean);
         */

        request.setAttribute("searchFormBean", searchFormBean);
        return mapping.findForward("xYearDisplay");
    }

    private Inar generateINAR(YearViewBean bean) {
        Inar inar = new Inar();

        for (Enrolment enrol : bean.getEnrolments()) {
            inar.incEnrolled();
            Grade grade = enrol.getGrade();
            if (grade == null || grade.isEmpty()) {
                inar.incFrequenting();
            } else if (grade.isApproved()) {
                inar.incApproved();
            } else if (grade.isNotEvaluated()) {
                inar.incNotEvaluated();
            } else if (grade.isNotApproved()) {
                inar.incFlunked();
            }
        }

        return inar;
    }

    private Map<CurricularYear, Inar> generateInarByCurricularYear(YearViewBean bean) {
        Map<CurricularYear, Inar> result = new TreeMap<CurricularYear, Inar>(new Comparator() {
            @Override
            public int compare(Object year1, Object year2) {
                return ((CurricularYear) year1).getYear().compareTo(((CurricularYear) year2).getYear());
            }
        });
        int years = bean.getDegreeCurricularPlan().getDegree().getDegreeType().getYears();
        for (int i = 1; i <= years; i++) {
            Inar inar = new Inar();
            result.put(CurricularYear.readByYear(i), inar);
        }

        for (Enrolment enrol : bean.getEnrolments()) {
            CurricularYear year = CurricularYear.readByYear(enrol.getRegistration().getCurricularYear());
            result.get(year).incEnrolled();
            Grade grade = enrol.getGrade();
            if (grade == null || grade.isEmpty()) {
                result.get(year).incFrequenting();
            } else if (grade.isApproved()) {
                result.get(year).incApproved();
            } else if (grade.isNotEvaluated()) {
                result.get(year).incNotEvaluated();
            } else if (grade.isNotApproved()) {
                result.get(year).incFlunked();
            }
        }

        return result;
    }

    private Map<CurricularYear, String> generateAverageByCurricularYear(YearViewBean bean) {
        Map<CurricularYear, String> result = new TreeMap<CurricularYear, String>(new Comparator() {
            @Override
            public int compare(Object year1, Object year2) {
                return ((CurricularYear) year1).getYear().compareTo(((CurricularYear) year2).getYear());
            }
        });

        int years = bean.getDegreeCurricularPlan().getDegree().getDegreeType().getYears();

        BigDecimal[] sigma = new BigDecimal[years + 1];
        BigDecimal[] cardinality = new BigDecimal[years + 1];
        BigDecimal[] average = new BigDecimal[years + 1];

        for (int i = 1; i <= years; i++) {
            sigma[i] = new BigDecimal(0);
            cardinality[i] = new BigDecimal(0);
            average[i] = new BigDecimal(0);
        }

        for (Enrolment enrol : bean.getEnrolments()) {
            CurricularYear year = CurricularYear.readByYear(enrol.getRegistration().getCurricularYear());
            Grade grade = enrol.getGrade();
            if (grade.isApproved() && grade.getGradeScale() == GradeScale.TYPE20) {
                BigDecimal biggy = sigma[year.getYear()];
                BigDecimal smalls = biggy.add(grade.getNumericValue());
                sigma[year.getYear()] = smalls;

                BigDecimal notorious = cardinality[year.getYear()];
                BigDecimal big = notorious.add(BigDecimal.ONE);
                cardinality[year.getYear()] = big;
            }
        }

        for (int i = 1; i <= years; i++) {
            if (cardinality[i].compareTo(BigDecimal.ZERO) == 0) {
                result.put(CurricularYear.readByYear(i), "-");
            } else {
                result.put(CurricularYear.readByYear(i), sigma[i].divide(cardinality[i], 2, RoundingMode.HALF_EVEN)
                        .toPlainString());
            }
        }

        return result;
    }

    private Map<BranchCourseGroup, Inar> generateInarByBranch(YearViewBean bean, BranchType bType) {
        Map<BranchCourseGroup, Inar> result = new TreeMap<BranchCourseGroup, Inar>(BranchCourseGroup.COMPARATOR_BY_NAME);
        Set<BranchCourseGroup> branches = bean.getDegreeCurricularPlan().getBranchesByType(bType);

        for (BranchCourseGroup branch : branches) {
            Inar inar = new Inar();
            result.put(branch, inar);
        }

        for (Enrolment enrol : bean.getEnrolments()) {
            BranchCourseGroup branch =
                    enrol.getParentBranchCurriculumGroup() != null ? enrol.getParentBranchCurriculumGroup().getDegreeModule() : null;
            if (branch != null && branch.getBranchType() == bType) {
                result.get(branch).incEnrolled();
                Grade grade = enrol.getGrade();
                if (grade == null || grade.isEmpty()) {
                    result.get(branch).incFrequenting();
                } else if (grade.isApproved()) {
                    result.get(branch).incApproved();
                } else if (grade.isNotEvaluated()) {
                    result.get(branch).incNotEvaluated();
                } else if (grade.isNotApproved()) {
                    result.get(branch).incFlunked();
                }
            }
        }

        return result;

    }

    private Map<BranchCourseGroup, String> generateAverageByBranch(YearViewBean bean, BranchType bType) {
        Map<BranchCourseGroup, String> result = new TreeMap<BranchCourseGroup, String>(BranchCourseGroup.COMPARATOR_BY_NAME);

        Set<BranchCourseGroup> branches = bean.getDegreeCurricularPlan().getBranchesByType(bType);

        Map<BranchCourseGroup, BigDecimal> sigma = new HashMap<BranchCourseGroup, BigDecimal>();
        Map<BranchCourseGroup, BigDecimal> cardinality = new HashMap<BranchCourseGroup, BigDecimal>();
        Map<BranchCourseGroup, BigDecimal> average = new HashMap<BranchCourseGroup, BigDecimal>();

        for (BranchCourseGroup branch : branches) {
            sigma.put(branch, new BigDecimal(0));
            cardinality.put(branch, new BigDecimal(0));
            average.put(branch, new BigDecimal(0));
        }

        for (Enrolment enrol : bean.getEnrolments()) {
            BranchCourseGroup branch =
                    enrol.getParentBranchCurriculumGroup() != null ? enrol.getParentBranchCurriculumGroup().getDegreeModule() : null;
            if (branch == null || branch.getBranchType() != bType) {
                continue;
            }

            Grade grade = enrol.getGrade();
            if (grade.isApproved() && grade.getGradeScale() == GradeScale.TYPE20) {
                BigDecimal biggy = sigma.get(branch);
                BigDecimal smalls = biggy.add(grade.getNumericValue());
                sigma.put(branch, smalls);

                BigDecimal notorious = cardinality.get(branch);
                BigDecimal big = notorious.add(BigDecimal.ONE);
                cardinality.put(branch, big);
            }
        }

        for (BranchCourseGroup branch : branches) {
            if (cardinality.get(branch).compareTo(BigDecimal.ZERO) == 0) {
                result.put(branch, "-");
            } else {
                result.put(branch, (sigma.get(branch)).divide(cardinality.get(branch), 2, RoundingMode.HALF_EVEN).toPlainString());
            }
        }

        return result;
    }

    /**
     * Ver onde sao guardados os StudentInquiries (QUC): ir por
     * CurricularCourses
     * 
     * 1. Extrair o ED 2. A partir do ED extrair duas collections: CourseResults
     * e TeacherResults 3. Magia para tirar um score desses results (ainda nao
     * sei o q possa ser esse score. vou extrair o 'average_NDE' e
     * 'average_P6_1' apenas a titulo de exemplo. nao tem qq valor real. os
     * values sao em double, passar a BigDecimals e trabalhar sempre neste
     * format). 4. Aplicar 50% à media do score de todos os CourseResults e 50%
     * à media do score de todos os TeacherResults 5. Mostrar esse score.
     */

    private String generateQUCResults(YearViewBean bean) {
        ExecutionDegree executionDegree =
                ExecutionDegree
                        .getByDegreeCurricularPlanAndExecutionYear(bean.getDegreeCurricularPlan(), bean.getExecutionYear());

        Set<StudentInquiriesCourseResult> courseResults = executionDegree.getStudentInquiriesCourseResultsSet();
        Set<StudentInquiriesTeachingResult> teachingResults = executionDegree.getStudentInquiriesTeachingResultsSet();

        BigDecimal sigmaCR = new BigDecimal(0);
        BigDecimal cardinalityCR = new BigDecimal(0);
        BigDecimal averageCR = new BigDecimal(0);

        BigDecimal sigmaTR = new BigDecimal(0);
        BigDecimal cardinalityTR = new BigDecimal(0);
        BigDecimal averageTR = new BigDecimal(0);

        BigDecimal partialCourse = new BigDecimal(0);
        BigDecimal partialTeaching = new BigDecimal(0);

        String result;

        for (StudentInquiriesCourseResult courseResult : courseResults) {
            BigDecimal converted = new BigDecimal(courseResult.getAverage_NDE() != null ? courseResult.getAverage_NDE() : 0);
            sigmaCR = sigmaCR.add(converted);
            cardinalityCR = cardinalityCR.add(BigDecimal.ONE);
        }
        if (cardinalityCR.compareTo(BigDecimal.ZERO) != 0) {
            averageCR = sigmaCR.divide(cardinalityCR, 4, RoundingMode.HALF_EVEN);
        } else {
            averageCR = BigDecimal.ZERO;
        }

        for (StudentInquiriesTeachingResult teachingResult : teachingResults) {
            BigDecimal converted =
                    new BigDecimal(teachingResult.getAverage_P6_1() != null ? teachingResult.getAverage_P6_1() : 0);
            sigmaTR = sigmaTR.add(converted);
            cardinalityTR = cardinalityTR.add(BigDecimal.ONE);
        }
        if (cardinalityCR.compareTo(BigDecimal.ZERO) != 0) {
            averageTR = sigmaTR.divide(cardinalityTR, 4, RoundingMode.HALF_EVEN);
        } else {
            averageTR = BigDecimal.ZERO;
        }

        partialCourse = averageCR.divide(new BigDecimal(2), 2, RoundingMode.HALF_EVEN);
        partialTeaching = averageTR.divide(new BigDecimal(2), 2, RoundingMode.HALF_EVEN);

        result = partialCourse.add(partialTeaching).toPlainString();

        return result;
    }

}
