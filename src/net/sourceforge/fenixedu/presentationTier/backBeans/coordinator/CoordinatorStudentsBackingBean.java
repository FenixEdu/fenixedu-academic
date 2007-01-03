package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;

public class CoordinatorStudentsBackingBean extends FenixBackingBean {

    private static final int RESULTS_PER_PAGE = 100;

    private Integer degreeCurricularPlanID = null;

    private String sortBy = null;

    private String studentCurricularPlanStateString = StudentCurricularPlanState.ACTIVE.toString();

    private String minGradeString = "";

    private String maxGradeString = "";

    private String minNumberApprovedString = "";

    private String maxNumberApprovedString = "";

    private String minStudentNumberString = "";

    private String maxStudentNumberString = "";

    private Integer minIndex = null;

    private Integer maxIndex = null;

    private Boolean showPhoto = null;

    public Integer getDegreeCurricularPlanID() {
        return (degreeCurricularPlanID == null) ? degreeCurricularPlanID = getAndHoldIntegerParameter("degreeCurricularPlanID") : degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public String getSortBy() {
        return (sortBy == null) ? sortBy = getAndHoldStringParameter("sortBy") : sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        final Integer degreeCurricularPlanID = getDegreeCurricularPlanID();
        return rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
    }

    public String getStudentCurricularPlanStateString() {
        return (studentCurricularPlanStateString == null) ? studentCurricularPlanStateString = getAndHoldStringParameter("studentCurricularPlanStateString") : studentCurricularPlanStateString;
    }

    public void setStudentCurricularPlanStateString(String studentCurricularPlanStateString) {
        this.studentCurricularPlanStateString = studentCurricularPlanStateString;
    }

    public String getMaxGradeString() {
        if (maxGradeString == null || maxGradeString.length() == 0) {
            maxGradeString = getAndHoldStringParameter("maxGradeString");
        }
        return (maxGradeString != null) ? maxGradeString : "";
    }

    public void setMaxGradeString(String maxGradeString) {
        this.maxGradeString = maxGradeString;
    }

    public String getMinGradeString() {
        if (minGradeString == null || minGradeString.length() == 0) {
            minGradeString = getAndHoldStringParameter("minGradeString");
        }
        return (minGradeString != null) ? minGradeString : "";
    }

    public void setMinGradeString(String minGradeString) {
        this.minGradeString = minGradeString;
    }

    public Double getMinGrade() {
        final String minGradeString = getMinGradeString();
        return (minGradeString != null && minGradeString.length() > 0) ? Double.valueOf(minGradeString) : null;
    }

    public Double getMaxGrade() {
        final String maxGradeString = getMaxGradeString();
        return (maxGradeString != null && maxGradeString.length() > 0) ? Double.valueOf(maxGradeString) : null;
    }

    public String getMaxNumberApprovedString() {
        if (maxNumberApprovedString == null || maxNumberApprovedString.length() == 0) {
            maxNumberApprovedString = getAndHoldStringParameter("maxNumberApprovedString");
        }
        return (maxNumberApprovedString != null) ? maxNumberApprovedString : "";
    }

    public void setMaxNumberApprovedString(String maxNumberApprovedString) {
        this.maxNumberApprovedString = maxNumberApprovedString;
    }

    public String getMinNumberApprovedString() {
        if (minNumberApprovedString == null || minNumberApprovedString.length() == 0) {
            minNumberApprovedString = getAndHoldStringParameter("minNumberApprovedString");
        }
        return (minNumberApprovedString != null) ? minNumberApprovedString : "";
    }

    public void setMinNumberApprovedString(String minNumberApprovedString) {
        this.minNumberApprovedString = minNumberApprovedString;
    }

    public Double getMinNumberApproved() {
        final String minNumberApprovedString = getMinNumberApprovedString();
        return (minNumberApprovedString != null && minNumberApprovedString.length() > 0) ? Double.valueOf(minNumberApprovedString) : null;
    }

    public Double getMaxNumberApproved() {
        final String maxNumberApprovedString = getMaxNumberApprovedString();
        return (maxNumberApprovedString != null && maxNumberApprovedString.length() > 0) ? Double.valueOf(maxNumberApprovedString) : null;
    }

    public String getMinStudentNumberString() {
        if (minStudentNumberString == null || minStudentNumberString.length() == 0) {
            minStudentNumberString = getAndHoldStringParameter("minStudentNumberString");
        }
        return (minStudentNumberString != null) ? minStudentNumberString : "";
    }

    public void setMinStudentNumberString(String minStudentNumberString) {
        this.minStudentNumberString = minStudentNumberString;
    }

    public String getMaxStudentNumberString() {
        if (maxStudentNumberString == null || maxStudentNumberString.length() == 0) {
            maxStudentNumberString = getAndHoldStringParameter("maxStudentNumberString");
        }
        return (maxStudentNumberString != null) ? maxStudentNumberString : "";
    }

    public void setMaxStudentNumberString(String maxStudentNumberString) {
        this.maxStudentNumberString = maxStudentNumberString;
    }

    public Double getMinStudentNumber() {
        final String minStudentNumberString = getMinStudentNumberString();
        return (minStudentNumberString != null && minStudentNumberString.length() > 0) ? Double.valueOf(minStudentNumberString) : null;
    }

    public Double getMaxStudentNumber() {
        final String maxStudentNumberString = getMaxStudentNumberString();
        return (maxStudentNumberString != null && maxStudentNumberString.length() > 0) ? Double.valueOf(maxStudentNumberString) : null;
    }

    public int getNumberResults() throws FenixFilterException, FenixServiceException {
        int matches = 0;
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlans()) {
            if (matchesSelectCriteria(studentCurricularPlan)) {
                matches++;
            }
        }

        return matches;
    }

    public Collection<StudentCurricularPlan> getStudentCurricularPlans() throws FenixFilterException, FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
        for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlans()) {
            if (matchesSelectCriteria(studentCurricularPlan)) {
                studentCurricularPlans.add(studentCurricularPlan);
            }
        }

        final String sortBy = (getSortBy() != null) ? getSortBy() : "student.number";
        Collections.sort(studentCurricularPlans, new BeanComparator(sortBy));

        return studentCurricularPlans.subList(getMinIndex() - 1, Math.min(getMaxIndex(), studentCurricularPlans.size()));
    }

    private boolean matchesSelectCriteria(final StudentCurricularPlan studentCurricularPlan) {
        final StudentCurricularPlanState studentCurricularPlanState = getStudentCurricularPlanState();
        if (studentCurricularPlanState != null && studentCurricularPlanState != studentCurricularPlan.getCurrentState()) {
            return false;
        }

        final double arithmeticMean = studentCurricularPlan.getRegistration().getArithmeticMean();

        final Double minGrade = getMinGrade();
        if (minGrade != null && minGrade.doubleValue() > arithmeticMean) {
            return false;
        }

        final Double maxGrade = getMaxGrade();
        if (maxGrade != null && maxGrade.doubleValue() < arithmeticMean) {
            return false;
        }

        final int approvedEnrollmentsNumber = studentCurricularPlan.getRegistration().getApprovedEnrollmentsNumber();

        final Double minNumberApproved = getMinNumberApproved();
        if (minNumberApproved != null && minNumberApproved.doubleValue() > approvedEnrollmentsNumber) {
            return false;
        }

        final Double maxNumberApproved = getMaxNumberApproved();
        if (maxNumberApproved != null && maxNumberApproved.doubleValue() < approvedEnrollmentsNumber) {
            return false;
        }

        final int studentNumber = studentCurricularPlan.getRegistration().getNumber();

        final Double minStudentNumber = getMinStudentNumber();
        if (minStudentNumber != null && minStudentNumber.doubleValue() > studentNumber) {
            return false;
        }

        final Double maxStudentNumber = getMaxStudentNumber();
        if (maxStudentNumber != null && maxStudentNumber.doubleValue() < studentNumber) {
            return false;
        }

        return true;
    }

    public StudentCurricularPlanState getStudentCurricularPlanState() {
        final String studentCurricularPlanStateString = getStudentCurricularPlanStateString();
        return (studentCurricularPlanStateString != null && studentCurricularPlanStateString.length() > 0) ?
                StudentCurricularPlanState.valueOf(studentCurricularPlanStateString) : null;
    }

    public Integer getMaxIndex() {
        if (maxIndex == null) {
            maxIndex = getAndHoldIntegerParameter("maxIndex");
        }
        return (maxIndex == null) ? RESULTS_PER_PAGE : maxIndex;
    }

    public void setMaxIndex(Integer maxIndex) {
        this.maxIndex = maxIndex;
    }

    public Integer getMinIndex() {
        if (minIndex == null) {
            minIndex = getAndHoldIntegerParameter("minIndex");
        }
        return (minIndex == null) ? 1 : minIndex;
    }

    public void setMinIndex(Integer minIndex) {
        this.minIndex = minIndex;
    }

    public List<Integer> getIndexes() throws FenixFilterException, FenixServiceException {
        final List<Integer> indexes = new ArrayList<Integer>();
        final double numberIndexes = Math.ceil(0.5 + getNumberResults() / RESULTS_PER_PAGE);
        for (int i = 1; i <= numberIndexes; i++) {
            indexes.add(Integer.valueOf((i - 1) * RESULTS_PER_PAGE + 1));
        }
        return indexes;
    }

    public Integer getResultsPerPage() {
        return Integer.valueOf(RESULTS_PER_PAGE);
    }

    public Boolean getShowPhoto() {
	final String showPhotoString = getAndHoldStringParameter("showPhoto");
	return ("true".equals(showPhotoString)) ? Boolean.TRUE : showPhoto;
    }

    public void setShowPhoto(Boolean showPhoto) {
	this.showPhoto = showPhoto;
    }

}
