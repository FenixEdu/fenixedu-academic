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
package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.accessControl.SearchDegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class CoordinatorStudentsBackingBean extends FenixBackingBean {

    private static final int RESULTS_PER_PAGE = 100;

    private String degreeCurricularPlanID = null;

    private String executionDegreeId = null;

    private String sortBy = null;

    private String studentCurricularPlanStateString = StudentCurricularPlanState.ACTIVE.toString();

    private String registrationStateTypeString;

    private String minGradeString = "";

    private String maxGradeString = "";

    private String minNumberApprovedString = "";

    private String maxNumberApprovedString = "";

    private String minStudentNumberString = "";

    private String maxStudentNumberString = "";

    private String minimumYearString = "";

    private String maximumYearString = "";

    private Integer minIndex = null;

    private Integer maxIndex = null;

    private Boolean showPhoto = null;

    public String getDegreeCurricularPlanID() {
        return (degreeCurricularPlanID == null) ? degreeCurricularPlanID = getAndHoldStringParameter("degreeCurricularPlanID") : degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(String degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public String getExecutionDegreeId() {
        return (this.executionDegreeId == null) ? this.executionDegreeId = getAndHoldStringParameter("executionDegreeId") : executionDegreeId;
    }

    public void setExecutionDegreeId(String executionDegreeId) {
        this.executionDegreeId = executionDegreeId;
    }

    public String getSortBy() {
        return (sortBy == null) ? sortBy = getAndHoldStringParameter("sortBy") : sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        final String degreeCurricularPlanID = getDegreeCurricularPlanID();
        return FenixFramework.getDomainObject(degreeCurricularPlanID);
    }

    public String getStudentCurricularPlanStateString() {
        return (studentCurricularPlanStateString == null) ? studentCurricularPlanStateString =
                getAndHoldStringParameter("studentCurricularPlanStateString") : studentCurricularPlanStateString;
    }

    public void setStudentCurricularPlanStateString(String studentCurricularPlanStateString) {
        this.studentCurricularPlanStateString = studentCurricularPlanStateString;
    }

    public String getRegistrationStateTypeString() {
        if (registrationStateTypeString == null) {
            registrationStateTypeString = getAndHoldStringParameter("registrationStateTypeString");
        }
        return (registrationStateTypeString == null) ? RegistrationStateType.REGISTERED.toString() : registrationStateTypeString;
    }

    public void setRegistrationStateTypeString(String registrationStateTypeString) {
        this.registrationStateTypeString = registrationStateTypeString;
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
        return (minNumberApprovedString != null && minNumberApprovedString.length() > 0) ? Double
                .valueOf(minNumberApprovedString) : null;
    }

    public Double getMaxNumberApproved() {
        final String maxNumberApprovedString = getMaxNumberApprovedString();
        return (maxNumberApprovedString != null && maxNumberApprovedString.length() > 0) ? Double
                .valueOf(maxNumberApprovedString) : null;
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

    public int getNumberResults() {
        return filterAllStudentCurricularPlans().size();
    }

    public String getMinimumYearString() {
        if (minimumYearString == null || minimumYearString.length() == 0) {
            minimumYearString = getAndHoldStringParameter("minimumYearString");
        }
        return (minimumYearString != null) ? minimumYearString : "";
    }

    public Integer getMinimumYear() {
        final String minimumYearString = getMinimumYearString();
        return (minimumYearString != null && minimumYearString.length() > 0) ? Integer.valueOf(minimumYearString) : null;
    }

    public void setMinimumYearString(String minimumYearString) {
        this.minimumYearString = minimumYearString;
    }

    public String getMaximumYearString() {
        if (maximumYearString == null || maximumYearString.length() == 0) {
            maximumYearString = getAndHoldStringParameter("maximumYearString");
        }
        return (maximumYearString != null) ? maximumYearString : "";
    }

    public Integer getMaximumYear() {
        final String maximumYearString = getMaximumYearString();
        return (maximumYearString != null && maximumYearString.length() > 0) ? Integer.valueOf(maximumYearString) : null;
    }

    public void setMaximumYearString(String maximumYearString) {
        this.maximumYearString = maximumYearString;
    }

    public List<Entry<StudentCurricularPlan, RegistrationStateType>> getStudentCurricularPlans() throws FenixServiceException {
        Map<StudentCurricularPlan, RegistrationStateType> studentCurricularPlans = filterPageStudentCurricularPlans();

        RegistrationStateType registrationState;

        for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlans.keySet()) {
            if (studentCurricularPlan.getRegistration() == null) {
                registrationState = null;
            }
            if (studentCurricularPlan.getRegistration().getLastRegistrationState(getExecutionYear()) == null) {
                registrationState = null;
            }
            registrationState =
                    studentCurricularPlan.getRegistration().getLastRegistrationState(getExecutionYear()).getStateType();
            studentCurricularPlans.put(studentCurricularPlan, registrationState);
        }

        return new ArrayList<Entry<StudentCurricularPlan, RegistrationStateType>>(studentCurricularPlans.entrySet());

    }

    public ExecutionYear getExecutionYear() {
        if (executionDegreeId != null) {
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
            return executionDegree.getExecutionYear();
        }
        return ExecutionYear.readCurrentExecutionYear();
    }

    private SearchDegreeStudentsGroup getSearchCriteriaGroup() {
        return new SearchDegreeStudentsGroup(getDegreeCurricularPlan(), getExecutionYear(), getSortBy(),
                getRegistrationStateType(), getMinGrade(), getMaxGrade(), getMinNumberApproved(), getMaxNumberApproved(),
                getMinStudentNumber(), getMaxStudentNumber(), getMinimumYear(), getMaximumYear());
    }

    private Map<StudentCurricularPlan, RegistrationStateType> filterAllStudentCurricularPlans() {
        SearchDegreeStudentsGroup searchGroup = getSearchCriteriaGroup();

        final Map<StudentCurricularPlan, RegistrationStateType> map = searchGroup.searchStudentCurricularPlans(null, null);
        return map;
    }

    private Map<StudentCurricularPlan, RegistrationStateType> filterPageStudentCurricularPlans() {
        SearchDegreeStudentsGroup searchGroup = getSearchCriteriaGroup();

        final Map<StudentCurricularPlan, RegistrationStateType> map =
                searchGroup.searchStudentCurricularPlans(getMinIndex(), getMaxIndex());

        return map;
    }

    public String getApplicationResourcesString(String name) {
        return BundleUtil.getString(Bundle.APPLICATION, name);
    }

    public String getSerializedFilteredStudents() {
        SearchDegreeStudentsGroup searchGroup = getSearchCriteriaGroup();
        return searchGroup.serialize();
    }

    public StudentCurricularPlanState getStudentCurricularPlanState() {
        final String studentCurricularPlanStateString = getStudentCurricularPlanStateString();
        return (studentCurricularPlanStateString != null && studentCurricularPlanStateString.length() > 0) ? StudentCurricularPlanState
                .valueOf(studentCurricularPlanStateString) : null;
    }

    public RegistrationStateType getRegistrationStateType() {
        final String registrationStateTypeString = getRegistrationStateTypeString();
        return (registrationStateTypeString != null && registrationStateTypeString.length() > 0 && !registrationStateTypeString
                .equals("SHOWALL")) ? RegistrationStateType.valueOf(registrationStateTypeString) : null;
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

    public List<Integer> getIndexes() throws FenixServiceException {
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

    public void exportStudentsToExcel() throws IOException {
        final Spreadsheet spreadsheet = generateSpreadsheet();

        getResponse().setContentType("application/vnd.ms-excel");
        getResponse().setHeader("Content-disposition", "attachment; filename=" + getFilename() + ".xls");
        spreadsheet.exportToXLSSheet(getResponse().getOutputStream());
        getResponse().getOutputStream().flush();
        getResponse().flushBuffer();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private Spreadsheet generateSpreadsheet() {
        final Spreadsheet spreadsheet = createSpreadSheet();
        for (final StudentCurricularPlan studentCurricularPlan : filterAllStudentCurricularPlans().keySet()) {
            final Row row = spreadsheet.addRow();

            row.setCell(studentCurricularPlan.getRegistration().getNumber());
            row.setCell(studentCurricularPlan.getPerson().getName());
            row.setCell(studentCurricularPlan.getPerson().getInstitutionalOrDefaultEmailAddressValue());
            row.setCell(studentCurricularPlan.getRegistration().getLastRegistrationState(getExecutionYear()).getStateType()
                    .getDescription());
            row.setCell(studentCurricularPlan.getRegistration().getNumberOfCurriculumEntries());
            row.setCell(studentCurricularPlan.getRegistration().getEctsCredits());
            row.setCell(getAverageInformation(studentCurricularPlan));
            row.setCell(studentCurricularPlan.getRegistration().getCurricularYear());

            final Tutorship tutorship = studentCurricularPlan.getActiveTutorship();
            row.setCell((tutorship != null) ? tutorship.getPerson().getName() : "");
        }

        return spreadsheet;
    }

    private String getFilename() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.students.lowercase");
    }

    private String getAverageInformation(final StudentCurricularPlan studentCurricularPlan) {
        final Registration registration = studentCurricularPlan.getRegistration();

        if (registration.isConcluded()) {
            if (registration.isRegistrationConclusionProcessed()
                    && (!registration.isBolonha() || studentCurricularPlan.getInternalCycleCurriculumGroupsSize().intValue() == 1)) {
                return registration.getAverage().setScale(2, RoundingMode.HALF_EVEN).toPlainString();
            } else {
                return " - ";
            }
        } else {
            return registration.getAverage().setScale(2, RoundingMode.HALF_EVEN).toPlainString();
        }
    }

    private Spreadsheet createSpreadSheet() {
        final Spreadsheet spreadsheet = new Spreadsheet(BundleUtil.getString(Bundle.APPLICATION, "list.students"));

        spreadsheet.setHeaders(new String[] {

        BundleUtil.getString(Bundle.APPLICATION, "label.number"),

        BundleUtil.getString(Bundle.APPLICATION, "label.name"),

        BundleUtil.getString(Bundle.APPLICATION, "label.email"),

        BundleUtil.getString(Bundle.APPLICATION, "label.student.curricular.plan.state"),

        BundleUtil.getString(Bundle.APPLICATION, "label.number.approved.curricular.courses"),

        BundleUtil.getString(Bundle.APPLICATION, "label.ects"),

        BundleUtil.getString(Bundle.APPLICATION, "label.aritmeticAverage"),

        BundleUtil.getString(Bundle.APPLICATION, "label.student.curricular.year"),

        " ", " " });

        return spreadsheet;
    }
}
