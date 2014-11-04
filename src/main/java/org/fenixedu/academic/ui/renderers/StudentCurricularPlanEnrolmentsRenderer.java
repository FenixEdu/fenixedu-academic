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
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.CurriculumModuleBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DegreeModuleToEnrolKeyConverter;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.controllers.CopyCheckBoxValuesController;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlMultipleHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;

public class StudentCurricularPlanEnrolmentsRenderer extends InputRenderer {

    private Integer initialWidth = 70;

    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";

    private String enrolmentClasses = "smalltxt, smalltxt aright, smalltxt aright, smalltxt aright, aright";

    private String curricularCoursesToEnrol = "smalltxt, smalltxt aright, smalltxt aright, aright";

    private String linkURL;

    public Integer getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(Integer initialWidth) {
        this.initialWidth = initialWidth;
    }

    public Integer getWidthDecreasePerLevel() {
        return widthDecreasePerLevel;
    }

    public void setWidthDecreasePerLevel(Integer widthDecreasePerLevel) {
        this.widthDecreasePerLevel = widthDecreasePerLevel;
    }

    public String getTablesClasses() {
        return tablesClasses;
    }

    public void setTablesClasses(String tablesClasses) {
        this.tablesClasses = tablesClasses;
    }

    public String getGroupRowClasses() {
        return groupRowClasses;
    }

    public void setGroupRowClasses(String groupRowClasses) {
        this.groupRowClasses = groupRowClasses;
    }

    private String[] getEnrolmentClasses() {
        return enrolmentClasses.split(",");
    }

    public void setEnrolmentClasses(String enrolmentClasses) {
        this.enrolmentClasses = enrolmentClasses;
    }

    private String getEnrolmentNameClasses() {
        return getEnrolmentClasses()[0];
    }

    private String getEnrolmentYearClasses() {
        return getEnrolmentClasses()[1];
    }

    private String getEnrolmentSemesterClasses() {
        return getEnrolmentClasses()[2];
    }

    private String getEnrolmentEctsClasses() {
        return getEnrolmentClasses()[3];
    }

    private String getEnrolmentCheckBoxClasses() {
        return getEnrolmentClasses()[4];
    }

    private String[] getCurricularCourseClasses() {
        return curricularCoursesToEnrol.split(",");
    }

    public void setCurricularCourseClasses(String curricularCoursesToEnrol) {
        this.curricularCoursesToEnrol = curricularCoursesToEnrol;
    }

    private String getCurricularCourseNameClasses() {
        return getCurricularCourseClasses()[0];
    }

    private String getCurricularCourseYearClasses() {
        return getCurricularCourseClasses()[1];
    }

    private String getCurricularCourseEctsClasses() {
        return getCurricularCourseClasses()[2];
    }

    private String getCurricularCourseCheckBoxClasses() {
        return getCurricularCourseClasses()[3];
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public StudentCurricularPlanEnrolmentsRenderer() {
        super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new StudentCurricularPlanEnrolmentLayout();
    }

    private class StudentCurricularPlanEnrolmentLayout extends Layout {

        private final CopyCheckBoxValuesController enrollmentsController = new CopyCheckBoxValuesController();

        private final CopyCheckBoxValuesController degreeModulesToEnrolController = new CopyCheckBoxValuesController();

        private StudentEnrolmentBean studentEnrolmentBean = null;

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            studentEnrolmentBean = (StudentEnrolmentBean) object;

            HtmlBlockContainer container = new HtmlBlockContainer();

            if (studentEnrolmentBean == null) {
                return new HtmlText();
            }

            HtmlMultipleHiddenField hiddenEnrollments = new HtmlMultipleHiddenField();
            hiddenEnrollments.bind(getInputContext().getMetaObject(), "curriculumModules"); // slot
            // refered
            // by
            // name
            hiddenEnrollments.setConverter(new DomainObjectKeyArrayConverter());
            hiddenEnrollments.setController(enrollmentsController);

            HtmlMultipleHiddenField hiddenDegreeModulesToEnrol = new HtmlMultipleHiddenField();
            hiddenDegreeModulesToEnrol.bind(getInputContext().getMetaObject(), "degreeModulesToEnrol"); // slot
            // refered
            // by
            // name
            hiddenDegreeModulesToEnrol.setConverter(new DegreeModuleToEnrolKeyConverter());
            hiddenDegreeModulesToEnrol.setController(degreeModulesToEnrolController);

            container.addChild(hiddenEnrollments);
            container.addChild(hiddenDegreeModulesToEnrol);

            generateModules(container, studentEnrolmentBean.getStudentCurricularPlan(),
                    studentEnrolmentBean.getCurriculumModuleBean(), studentEnrolmentBean.getExecutionPeriod(), 0);
            return container;
        }

        private void generateModules(HtmlBlockContainer blockContainer, StudentCurricularPlan studentCurricularPlan,
                CurriculumModuleBean curriculumModuleBean, ExecutionSemester executionSemester, int depth) {
            final HtmlTable groupTable = new HtmlTable();
            blockContainer.addChild(groupTable);
            groupTable.setClasses(getTablesClasses());
            groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            HtmlTableRow htmlTableRow = groupTable.createRow();
            htmlTableRow.setClasses(getGroupRowClasses());
            htmlTableRow.createCell().setBody(new HtmlText(curriculumModuleBean.getCurriculumModule().getName().getContent()));

            HtmlTableCell checkBoxCell = htmlTableRow.createCell();
            checkBoxCell.setClasses("aright");

            HtmlCheckBox checkBox = new HtmlCheckBox(true);
            MetaObject enrolmentMetaObject =
                    MetaObjectFactory.createObject(curriculumModuleBean.getCurriculumModule(), new Schema(CurriculumGroup.class));
            checkBox.setName("enrolmentCheckBox" + curriculumModuleBean.getCurriculumModule().getExternalId());
            checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
            checkBoxCell.setBody(checkBox);

            if (!curriculumModuleBean.getGroupsEnroled().isEmpty()
                    || !curriculumModuleBean.getCurricularCoursesEnroled().isEmpty()) {
                checkBox.setDisabled(true);
            } else {
                enrollmentsController.addCheckBox(checkBox);
            }

            final HtmlTable coursesTable = new HtmlTable();
            blockContainer.addChild(coursesTable);
            coursesTable.setClasses(getTablesClasses());
            coursesTable.setStyle("width: " + (getInitialWidth() - depth - getWidthDecreasePerLevel()) + "em; margin-left: "
                    + (depth + getWidthDecreasePerLevel()) + "em;");

            generateEnrolments(curriculumModuleBean, executionSemester, coursesTable);
            generateCurricularCoursesToEnrol(coursesTable, curriculumModuleBean, executionSemester);
            generateGroups(blockContainer, curriculumModuleBean, studentCurricularPlan, executionSemester, depth);
        }

        private void generateGroups(HtmlBlockContainer blockContainer, CurriculumModuleBean curriculumModuleBean,
                StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester, int depth) {
            List<DegreeModuleToEnrol> courseGroupsToEnrol =
                    new ArrayList<DegreeModuleToEnrol>(curriculumModuleBean.getGroupsToEnrol());
            Collections.sort(courseGroupsToEnrol, new BeanComparator("context"));

            List<CurriculumModuleBean> curriculumGroups =
                    new ArrayList<CurriculumModuleBean>(curriculumModuleBean.getGroupsEnroled());
            Collections.sort(curriculumGroups, new CurriculumModuleComparator(executionSemester));

            while (!courseGroupsToEnrol.isEmpty() || !curriculumGroups.isEmpty()) {

                if (!curriculumGroups.isEmpty() && courseGroupsToEnrol.isEmpty()) {
                    generateModules(blockContainer, studentCurricularPlan, curriculumGroups.iterator().next(), executionSemester,
                            depth + getWidthDecreasePerLevel());
                    curriculumGroups.remove(0);
                } else if (curriculumGroups.isEmpty() && !courseGroupsToEnrol.isEmpty()) {
                    generateCourseGroupToEnroll(blockContainer, courseGroupsToEnrol.iterator().next(), depth
                            + getWidthDecreasePerLevel());
                    courseGroupsToEnrol.remove(0);
                } else {
                    Context context = courseGroupsToEnrol.iterator().next().getContext();
                    CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumGroups.iterator().next().getCurriculumModule();
                    if (curriculumGroup.getChildOrder(executionSemester) <= context.getChildOrder()) {
                        generateModules(blockContainer, studentCurricularPlan, curriculumGroups.iterator().next(),
                                executionSemester, depth + getWidthDecreasePerLevel());
                        curriculumGroups.remove(0);
                    } else {
                        generateCourseGroupToEnroll(blockContainer, courseGroupsToEnrol.iterator().next(), depth
                                + getWidthDecreasePerLevel());
                        courseGroupsToEnrol.remove(0);
                    }
                }
            }
        }

        private void generateCourseGroupToEnroll(HtmlBlockContainer blockContainer, DegreeModuleToEnrol degreeModuleToEnrol,
                int depth) {
            final HtmlTable groupTable = new HtmlTable();
            blockContainer.addChild(groupTable);
            groupTable.setClasses(getTablesClasses());
            groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");
            HtmlTableRow htmlTableRow = groupTable.createRow();
            htmlTableRow.setClasses(getGroupRowClasses());
            htmlTableRow.createCell().setBody(new HtmlText(degreeModuleToEnrol.getContext().getChildDegreeModule().getName()));
            HtmlTableCell checkBoxCell = htmlTableRow.createCell();
            checkBoxCell.setClasses("aright");

            HtmlCheckBox checkBox = new HtmlCheckBox(false);
            checkBox.setName("degreeModuleToEnrolCheckBox" + degreeModuleToEnrol.getContext().getExternalId() + ":"
                    + degreeModuleToEnrol.getCurriculumGroup().getExternalId());
            checkBox.setUserValue(degreeModuleToEnrol.getKey());
            degreeModulesToEnrolController.addCheckBox(checkBox);
            checkBoxCell.setBody(checkBox);
        }

        private void generateCurricularCoursesToEnrol(HtmlTable groupTable, CurriculumModuleBean curriculumModuleBean,
                ExecutionSemester executionSemester) {
            List<DegreeModuleToEnrol> coursesToEnrol = curriculumModuleBean.getCurricularCoursesToEnrol();
            Collections.sort(coursesToEnrol, new BeanComparator("context"));

            for (DegreeModuleToEnrol degreeModuleToEnrol : coursesToEnrol) {
                CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEnrol.getContext().getChildDegreeModule();

                HtmlTableRow htmlTableRow = groupTable.createRow();
                HtmlTableCell cellName = htmlTableRow.createCell();
                cellName.setClasses(getCurricularCourseNameClasses());
                cellName.setBody(new HtmlText(curricularCourse.getName()));

                // Year
                final HtmlTableCell yearCell = htmlTableRow.createCell();
                yearCell.setClasses(getCurricularCourseYearClasses());
                yearCell.setColspan(2);

                final StringBuilder year = new StringBuilder();
                year.append(degreeModuleToEnrol.getContext().getCurricularPeriod().getFullLabel());
                yearCell.setBody(new HtmlText(year.toString()));

                if (!curricularCourse.isOptionalCurricularCourse()) {
                    // Ects
                    final HtmlTableCell ectsCell = htmlTableRow.createCell();
                    ectsCell.setClasses(getCurricularCourseEctsClasses());

                    final StringBuilder ects = new StringBuilder();
                    ects.append(curricularCourse.getEctsCredits()).append(" ")
                            .append(BundleUtil.getString(Bundle.ACADEMIC, "credits.abbreviation"));
                    ectsCell.setBody(new HtmlText(ects.toString()));

                    HtmlTableCell checkBoxCell = htmlTableRow.createCell();
                    checkBoxCell.setClasses(getCurricularCourseCheckBoxClasses());

                    HtmlCheckBox checkBox = new HtmlCheckBox(false);
                    checkBox.setName("degreeModuleToEnrolCheckBox" + degreeModuleToEnrol.getContext().getExternalId() + ":"
                            + degreeModuleToEnrol.getCurriculumGroup().getExternalId());
                    checkBox.setUserValue(degreeModuleToEnrol.getKey());
                    degreeModulesToEnrolController.addCheckBox(checkBox);
                    checkBoxCell.setBody(checkBox);
                } else {
                    final HtmlTableCell cell = htmlTableRow.createCell();
                    cell.setClasses(getCurricularCourseEctsClasses());
                    cell.setBody(new HtmlText(""));

                    HtmlTableCell linkTableCell = htmlTableRow.createCell();
                    linkTableCell.setClasses(getCurricularCourseCheckBoxClasses());

                    final HtmlLink htmlLink = new HtmlLink();
                    htmlLink.setText(BundleUtil.getString(Bundle.ACADEMIC, "link.option.choose.curricular.course"));
                    htmlLink.setUrl(getLinkURL());
                    htmlLink.setParameter("scpID", studentEnrolmentBean.getStudentCurricularPlan().getExternalId());
                    htmlLink.setParameter("executionPeriodID", studentEnrolmentBean.getExecutionPeriod().getExternalId());
                    htmlLink.setParameter("curriculumGroupID", degreeModuleToEnrol.getCurriculumGroup().getExternalId());
                    htmlLink.setParameter("contextID", degreeModuleToEnrol.getContext().getExternalId());
                    linkTableCell.setBody(htmlLink);
                }
            }
        }

        private void generateEnrolments(CurriculumModuleBean curriculumModuleBean, ExecutionSemester executionSemester,
                final HtmlTable groupTable) {
            for (CurriculumModuleBean curriculumModule : curriculumModuleBean.getCurricularCoursesEnroled()) {
                if (((CurriculumLine) curriculumModule.getCurriculumModule()).isEnrolment()) {
                    Enrolment enrolment = (Enrolment) curriculumModule.getCurriculumModule();
                    if (enrolment.getExecutionPeriod().equals(executionSemester) && enrolment.isEnroled()) {
                        generateEnrolment(groupTable, enrolment);
                    }
                }
            }
        }

        private void generateEnrolment(final HtmlTable groupTable, Enrolment enrolment) {
            HtmlTableRow htmlTableRow = groupTable.createRow();
            HtmlTableCell cellName = htmlTableRow.createCell();
            cellName.setClasses(getEnrolmentNameClasses());
            cellName.setBody(new HtmlText(enrolment.getName().getContent()));

            // Year
            final HtmlTableCell yearCell = htmlTableRow.createCell();
            yearCell.setClasses(getEnrolmentYearClasses());

            final StringBuilder year = new StringBuilder();
            year.append(enrolment.getExecutionPeriod().getExecutionYear().getYear());
            yearCell.setBody(new HtmlText(year.toString()));

            // Semester
            final HtmlTableCell semesterCell = htmlTableRow.createCell();
            semesterCell.setClasses(getEnrolmentSemesterClasses());

            final StringBuilder semester = new StringBuilder();
            semester.append(enrolment.getExecutionPeriod().getSemester().toString());
            semester.append(" ");
            semester.append(BundleUtil.getString(Bundle.ENUMERATION, "SEMESTER.ABBREVIATION"));
            semesterCell.setBody(new HtmlText(semester.toString()));

            // Ects
            final HtmlTableCell ectsCell = htmlTableRow.createCell();
            ectsCell.setClasses(getEnrolmentEctsClasses());

            final StringBuilder ects = new StringBuilder();
            ects.append(enrolment.getCurricularCourse().getEctsCredits()).append(" ")
                    .append(BundleUtil.getString(Bundle.ACADEMIC, "credits.abbreviation"));
            ectsCell.setBody(new HtmlText(ects.toString()));

            MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(enrolment, new Schema(Enrolment.class));

            HtmlCheckBox checkBox = new HtmlCheckBox(true);
            checkBox.setName("enrolmentCheckBox" + enrolment.getExternalId());
            checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
            enrollmentsController.addCheckBox(checkBox);

            HtmlTableCell cellCheckBox = htmlTableRow.createCell();
            cellCheckBox.setClasses(getEnrolmentCheckBoxClasses());
            cellCheckBox.setBody(checkBox);
        }
    }

    public static class CurriculumModuleComparator implements Comparator<CurriculumModuleBean> {

        private final ExecutionSemester executionSemester;

        public CurriculumModuleComparator(ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
        }

        @Override
        public int compare(CurriculumModuleBean o1, CurriculumModuleBean o2) {
            CurriculumGroup c1 = (CurriculumGroup) o1.getCurriculumModule();
            CurriculumGroup c2 = (CurriculumGroup) o2.getCurriculumModule();
            return c1.getChildOrder(executionSemester).compareTo(c2.getChildOrder(executionSemester));
        }

    }
}
