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
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean.StudentAttendsStateType;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlEMailLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlLabel;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableHeader;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ExecutionCourseAttendsSpreadSheetRenderer extends OutputRenderer {

    private String attendsListTableClasses;
    private String summaryClasses;

    public String getAttendsListTableClasses() {
        return attendsListTableClasses;
    }

    public void setAttendsListTableClasses(String attendsListTableClasses) {
        this.attendsListTableClasses = attendsListTableClasses;
    }

    public String getSummaryClasses() {
        return summaryClasses;
    }

    public void setSummaryClasses(String summaryClasses) {
        this.summaryClasses = summaryClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new ExecutionCourseAttendsSpreadSheetLayout();
    }

    private SearchExecutionCourseAttendsBean bean;

    private class ExecutionCourseAttendsSpreadSheetLayout extends Layout {

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            bean = (SearchExecutionCourseAttendsBean) object;
            HtmlContainer blockContainer = new HtmlBlockContainer();
            blockContainer.addChild(renderAttendsList());
            return blockContainer;
        }

        private String getGroupURL(StudentGroup studentGroup) {
            StringBuilder stringBuilder = new StringBuilder("/viewStudentGroupInformation.do?");
            stringBuilder.append("studentGroupCode=" + studentGroup.getExternalId());
            stringBuilder.append("&method=viewStudentGroupInformation");
            stringBuilder.append("&objectCode=" + bean.getExecutionCourse().getExternalId());
            stringBuilder.append("&groupPropertiesCode=" + studentGroup.getGrouping().getExternalId());
            return stringBuilder.toString();
        }

        private HtmlTable renderAttendsList() {
            HtmlTable htmlTable = new HtmlTable();
            htmlTable.setClasses(getAttendsListTableClasses());
            HtmlTableHeader tableHeader = htmlTable.createHeader();

            List<Grouping> groupings = new ArrayList<Grouping>(bean.getExecutionCourse().getGroupings());
            Collections.sort(groupings, Grouping.COMPARATOR_BY_ENROLMENT_BEGIN_DATE);

            List<ShiftType> shiftTypes = new ArrayList<ShiftType>(bean.getExecutionCourse().getShiftTypes());
            Collections.sort(shiftTypes);

            Integer rowSpan;
            if (groupings.isEmpty() && shiftTypes.isEmpty()) {
                rowSpan = 1;
            } else {
                rowSpan = 2;
            }

            HtmlTableRow row1 = tableHeader.createRow();
            if (bean.getViewPhoto()) {
                HtmlTableCell photoCell = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.photo"));
                photoCell.setRowspan(rowSpan);
            }
            HtmlTableCell numberCell = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.number"));
            numberCell.setRowspan(rowSpan);

            HtmlTableCell numberOfEnrolmentsCell = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.numberOfEnrollments"));
            numberOfEnrolmentsCell.setRowspan(rowSpan);

            HtmlTableCell enrolmentStateCell1 = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.attends.enrollmentState"));
            enrolmentStateCell1.setRowspan(rowSpan);

            HtmlTableCell degreeCell = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.Degree"));
            degreeCell.setRowspan(rowSpan);

            HtmlTableCell registrationStateCell = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.registration.state"));
            registrationStateCell.setRowspan(rowSpan);

            HtmlTableCell nameCell = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.name"));
            nameCell.setRowspan(rowSpan);

            if (!groupings.isEmpty()) {
                HtmlTableCell groupingCell = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.projectGroup"));
                groupingCell.setColspan(groupings.size());
            }

            HtmlTableCell emailCell = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.mail"));
            emailCell.setRowspan(rowSpan);

            if (!shiftTypes.isEmpty()) {
                HtmlTableCell shiftCell = row1.createCell(BundleUtil.getString(Bundle.APPLICATION, "label.attends.shifts"));
                shiftCell.setColspan(shiftTypes.size());
            }

            HtmlTableRow row2 = tableHeader.createRow();
            for (Grouping grouping : groupings) {
                row2.createCell(grouping.getName());
            }
            for (ShiftType shiftType : shiftTypes) {
                row2.createCell(BundleUtil.getString(Bundle.ENUMERATION, shiftType.getName()));
            }

            List<Attends> attendsResult = new ArrayList<Attends>(bean.getAttendsResult());
            Collections.sort(attendsResult, Attends.COMPARATOR_BY_STUDENT_NUMBER);
            for (Attends attends : attendsResult) {
                HtmlTableRow row = htmlTable.createRow();
                if (bean.getViewPhoto()) {
                    HtmlImage htmlImage = new HtmlImage();
                    htmlImage.setSource(RenderUtils.getContextRelativePath("")
                            + "/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="
                            + attends.getRegistration().getStudent().getPerson().getExternalId());
                    row.createCell().setBody(htmlImage);
                }
                row.createCell(attends.getRegistration().getStudent().getNumber().toString());
                if (attends.getEnrolment() == null) {
                    row.createCell("--");
                } else {
                    row.createCell(String.valueOf(attends.getEnrolment().getNumberOfTotalEnrolmentsInThisCourse(
                            attends.getEnrolment().getExecutionPeriod())));
                }

                final StudentAttendsStateType stateType = attends.getAttendsStateType();
                row.createCell(stateType != null ? BundleUtil.getString(Bundle.ENUMERATION, stateType.getQualifiedName()) : "--");
                row.createCell(attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan().getName());
                final RegistrationState registrationState =
                        attends.getRegistration().getLastRegistrationState(attends.getExecutionYear());
                row.createCell(registrationState == null ? "" : registrationState.getStateType().getDescription());
                row.createCell(attends.getRegistration().getStudent().getPerson().getFirstAndLastName());

                for (Grouping grouping : groupings) {
                    StudentGroup studentGroup = attends.getStudentGroupByGrouping(grouping);
                    if (studentGroup == null) {
                        row.createCell("N/A");
                    } else {
                        HtmlLink groupLink = new HtmlLink();
                        groupLink.setText(studentGroup.getGroupNumber().toString());
                        groupLink.setUrl(getGroupURL(studentGroup));
                        row.createCell().setBody(groupLink);
                    }
                }

                String email = attends.getRegistration().getStudent().getPerson().getEmail();
                row.createCell().setBody(email != null ? new HtmlEMailLink(email) : new HtmlLabel(""));

                for (ShiftType shiftType : shiftTypes) {
                    Shift shift = attends.getRegistration().getShiftFor(bean.getExecutionCourse(), shiftType);
                    if (shift == null) {
                        row.createCell("N/A");
                    } else {
                        row.createCell(shift.getNome());
                    }
                }
            }

            return htmlTable;
        }
    }

}
