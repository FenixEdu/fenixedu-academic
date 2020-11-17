/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.DismissalType;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedOptionalCurricularCourse;

import pt.ist.fenixWebFramework.rendererExtensions.controllers.CopyCheckBoxValuesController;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlMultipleHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButtonGroup;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;

public class StudentDismissalRenderer extends InputRenderer {

    private Integer initialWidth = 60;

    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";

    private String curricularCourseRowClasses = "";

    private String groupCellClasses = "smalltxt, aright";

    private String curricularCourseCellClasses = ", aright";

    private String dismissalType;

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

    public String getCurricularCourseRowClasses() {
        return curricularCourseRowClasses;
    }

    public void setCurricularCourseRowClasses(String curricularCourseRowClasses) {
        this.curricularCourseRowClasses = curricularCourseRowClasses;
    }

    private String[] getGroupCellClasses() {
        return groupCellClasses.split(",");
    }

    public void setGroupCellClasses(String groupCellClasses) {
        this.groupCellClasses = groupCellClasses;
    }

    public String getGroupNameClasses() {
        return getGroupCellClasses()[0];
    }

    public String getGroupRadioClasses() {
        return getGroupCellClasses()[1];
    }

    private String[] getCurricularCourseCellClasses() {
        return curricularCourseCellClasses.split(",");
    }

    public void setCurricularCourseCellClasses(String curricularCourseCellClasses) {
        this.curricularCourseCellClasses = curricularCourseCellClasses;
    }

    public String getCurricularCourseNameClasses() {
        return getCurricularCourseCellClasses()[0];
    }

    public String getCurricularCourseCheckBoxClasses() {
        return getCurricularCourseCellClasses()[1];
    }

    public String getDismissalType() {
        return dismissalType;
    }

    public void setDismissalType(String dismissalType) {
        this.dismissalType = dismissalType;
    }

    public StudentDismissalRenderer() {
        super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new StudentDismissalLayout();
    }

    protected class StudentDismissalLayout extends Layout {

        private final CopyCheckBoxValuesController curricularCoursesController = new CopyCheckBoxValuesController();

        private final CopyCheckBoxValuesController optionalCurricularCoursesController = new CopyCheckBoxValuesController();

        private HtmlRadioButtonGroup radioButtonGroup = null;

        private DismissalBean dismissalBean = null;

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            dismissalBean = (DismissalBean) object;

            HtmlBlockContainer container = new HtmlBlockContainer();
            if (dismissalBean == null || dismissalBean.getExecutionPeriod() == null || dismissalBean.getStudentCurricularPlan()
                    .getStartExecutionPeriod().isAfter(dismissalBean.getExecutionPeriod())) {
                return new HtmlText();
            }

            DismissalType dismissalTypeValue =
                    getDismissalType() == null ? dismissalBean.getDismissalType() : DismissalType.valueOf(getDismissalType());

            if (dismissalTypeValue == DismissalType.CURRICULUM_GROUP_CREDITS) {
                radioButtonGroup = new HtmlRadioButtonGroup();

                // slot refered by name
                radioButtonGroup.bind(getInputContext().getMetaObject(), "courseGroup");

                radioButtonGroup.setConverter(new DomainObjectKeyConverter());
                container.addChild(radioButtonGroup);
                generateCourseGroupCycles(container, dismissalBean.getStudentCurricularPlan(),
                        dismissalBean.getExecutionPeriod());

            } else if (dismissalTypeValue == DismissalType.CURRICULAR_COURSE_CREDITS) {
                final HtmlMultipleHiddenField hiddenCurricularCourses = new HtmlMultipleHiddenField();

                // slot refered by name
                hiddenCurricularCourses.bind(getInputContext().getMetaObject(), "dismissals");

                hiddenCurricularCourses.setConverter(new SelectedCurricularCoursesKeyConverter());
                hiddenCurricularCourses.setController(curricularCoursesController);
                container.addChild(hiddenCurricularCourses);

                final HtmlMultipleHiddenField hiddenOptionalCurricularCourses = new HtmlMultipleHiddenField();

                // slot refered by name
                hiddenOptionalCurricularCourses.bind(getInputContext().getMetaObject(), "optionalDismissals");

                hiddenOptionalCurricularCourses.setConverter(new SelectedOptionalCurricularCoursesKeyConverter());
                hiddenOptionalCurricularCourses.setController(optionalCurricularCoursesController);
                container.addChild(hiddenOptionalCurricularCourses);

                generateCurricularCourses(container, dismissalBean.getStudentCurricularPlan(),
                        dismissalBean.getExecutionPeriod());
            } else {
                radioButtonGroup = new HtmlRadioButtonGroup();

                // slot refered by name
                radioButtonGroup.bind(getInputContext().getMetaObject(), "curriculumGroup");

                radioButtonGroup.setConverter(new DomainObjectKeyConverter());
                container.addChild(radioButtonGroup);
                generateNoCourseGroupCurriculumGroups(container, dismissalBean.getStudentCurricularPlan());
            }

            return container;
        }

        protected void generateNoCourseGroupCurriculumGroups(HtmlBlockContainer container,
                StudentCurricularPlan studentCurricularPlan) {
            for (NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup : studentCurricularPlan
                    .getNoCourseGroupCurriculumGroups()) {
                generateNoCourseGroupCurriculumGroup(container, studentCurricularPlan, noCourseGroupCurriculumGroup, 0);
            }

        }

        protected void generateNoCourseGroupCurriculumGroup(HtmlBlockContainer container,
                StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, int depth) {
            final HtmlTable groupTable = new HtmlTable();
            container.addChild(groupTable);
            groupTable.setClasses(getTablesClasses());
            groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            final HtmlTableRow htmlTableRow = groupTable.createRow();
            htmlTableRow.setClasses(getGroupRowClasses());

            final HtmlTableCell nameCell = htmlTableRow.createCell();
            nameCell.setBody(new HtmlText(curriculumGroup.getFullPath()));
            nameCell.setClasses(getGroupNameClasses());

            final HtmlTableCell radioButtonCell = htmlTableRow.createCell();
            final HtmlRadioButton radioButton = radioButtonGroup.createRadioButton();
            radioButton.setUserValue(
                    MetaObjectFactory.createObject(curriculumGroup, new Schema(CurriculumGroup.class)).getKey().toString());
            radioButton.setChecked(curriculumGroup == dismissalBean.getCurriculumGroup());
            radioButtonCell.setBody(radioButton);
            radioButtonCell.setClasses(getGroupRadioClasses());
            radioButtonCell.setStyle("width: 2em;");

        }

        protected void generateCourseGroupCycles(final HtmlBlockContainer blockContainer,
                final StudentCurricularPlan studentCurricularPlan, final ExecutionInterval executionInterval) {
            for (final CycleType cycleType : studentCurricularPlan.getDegreeType().getSupportedCyclesToEnrol()) {
                final CourseGroup courseGroup = getCourseGroupWithCycleType(studentCurricularPlan, cycleType);
                if (courseGroup != null) {
                    generateCourseGroups(blockContainer, studentCurricularPlan, courseGroup, executionInterval, 0);
                }
            }
        }

        protected CourseGroup getCourseGroupWithCycleType(final StudentCurricularPlan studentCurricularPlan,
                final CycleType cycleType) {
            final CycleCurriculumGroup curriculumGroup = studentCurricularPlan.getCycle(cycleType);
            return curriculumGroup != null ? curriculumGroup.getDegreeModule() : studentCurricularPlan.getDegreeCurricularPlan()
                    .getCycleCourseGroup(cycleType);
        }

        protected void generateCurricularCourses(final HtmlBlockContainer blockContainer,
                final StudentCurricularPlan studentCurricularPlan, final ExecutionInterval executionInterval) {
            final HtmlTable groupTable = new HtmlTable();
            blockContainer.addChild(groupTable);
            groupTable.setClasses(getTablesClasses());
            groupTable.setStyle("width: " + getInitialWidth() + "em; margin-left: 0em;");

            final List<CurricularCourse> orderedCurricularCourses =
                    new ArrayList<CurricularCourse>(dismissalBean.getAllCurricularCoursesToDismissal());
            Collections.sort(orderedCurricularCourses, new BeanComparator("name", Collator.getInstance()));

            for (final CurricularCourse curricularCourse : orderedCurricularCourses) {
                final HtmlTableRow htmlTableRow = groupTable.createRow();
                htmlTableRow.setClasses(getCurricularCourseRowClasses());

                final HtmlTableCell nameCell = htmlTableRow.createCell();

                final String code = curricularCourse.getCode();
                final String oneFullName = curricularCourse.getOneFullName(executionInterval);
                final String name = " <span class='bold'>" + curricularCourse.getName(dismissalBean.getExecutionPeriod())
                        + "</span> (" + oneFullName.substring(0, oneFullName.lastIndexOf(">")) + ")";
                final String codeAndname = StringUtils.isEmpty(code) ? name : code + " - " + name;
                nameCell.setBody(new HtmlText(codeAndname, false));

                nameCell.setClasses(getCurricularCourseNameClasses());

                final HtmlTableCell checkBoxCell = htmlTableRow.createCell();
                checkBoxCell.setClasses(getCurricularCourseCheckBoxClasses());

                final HtmlCheckBox checkBox =
                        new HtmlCheckBox(dismissalBean.containsDismissalOrOptionalDismissal(curricularCourse));
                checkBox.setName("curricularCourseCheckBox" + curricularCourse.getExternalId());
                if (curricularCourse.isOptionalCurricularCourse()) {
                    final OptionalCurricularCourse optionalCurricularCourse = (OptionalCurricularCourse) curricularCourse;
                    checkBox.setUserValue(
                            new DismissalBean.SelectedOptionalCurricularCourse(optionalCurricularCourse, studentCurricularPlan)
                                    .getKey());
                    checkBoxCell.setBody(checkBox);
                    optionalCurricularCoursesController.addCheckBox(checkBox);
                } else {
                    checkBox.setUserValue(
                            new DismissalBean.SelectedCurricularCourse(curricularCourse, studentCurricularPlan).getKey());
                    checkBoxCell.setBody(checkBox);
                    curricularCoursesController.addCheckBox(checkBox);
                }
            }
        }

        protected void generateCourseGroups(final HtmlBlockContainer blockContainer,
                final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup,
                final ExecutionInterval executionInterval, int depth) {
            final HtmlTable groupTable = new HtmlTable();
            blockContainer.addChild(groupTable);
            groupTable.setClasses(getTablesClasses());
            groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            final HtmlTableRow htmlTableRow = groupTable.createRow();
            htmlTableRow.setClasses(getGroupRowClasses());

            final HtmlTableCell nameCell = htmlTableRow.createCell();
            nameCell.setBody(new HtmlText(courseGroup.getName()));
            nameCell.setClasses(getGroupNameClasses());

            final HtmlTableCell currentCreditsCell = htmlTableRow.createCell();
            final double ectsCreditsForCourseGroup =
                    studentCurricularPlan.getCreditsConcludedForCourseGroup(courseGroup).doubleValue();
            if (ectsCreditsForCourseGroup == 0d) {
                currentCreditsCell.setBody(new HtmlText("ECTS:  -"));
            } else {
                currentCreditsCell.setBody(new HtmlText("ECTS: " + ectsCreditsForCourseGroup));
            }
            currentCreditsCell.setClasses("smalltxt");
            currentCreditsCell.setStyle("width: 6em;");

            final HtmlTableCell creditsMinCell = htmlTableRow.createCell();
            creditsMinCell.setBody(new HtmlText("Min: " + courseGroup.getMinEctsCredits(executionInterval)));
            creditsMinCell.setClasses("smalltxt");
            creditsMinCell.setStyle("width: 6em;");

            final HtmlTableCell creditsMaxCell = htmlTableRow.createCell();
            creditsMaxCell.setBody(new HtmlText("Max: " + courseGroup.getMaxEctsCredits(executionInterval)));
            creditsMaxCell.setClasses("smalltxt");
            creditsMaxCell.setStyle("width: 6em;");

            final HtmlTableCell radioButtonCell = htmlTableRow.createCell();
            final HtmlRadioButton radioButton = radioButtonGroup.createRadioButton();
            radioButton
                    .setUserValue(MetaObjectFactory.createObject(courseGroup, new Schema(CourseGroup.class)).getKey().toString());
            radioButton.setChecked(courseGroup == dismissalBean.getCourseGroup());
            radioButtonCell.setBody(radioButton);
            radioButtonCell.setClasses(getGroupRadioClasses());
            radioButtonCell.setStyle("width: 2em;");

            for (final Context context : courseGroup.getOpenChildContexts(CourseGroup.class, executionInterval).stream().sorted()
                    .collect(Collectors.toList())) {
                generateCourseGroups(blockContainer, studentCurricularPlan, (CourseGroup) context.getChildDegreeModule(),
                        executionInterval, depth + getWidthDecreasePerLevel());
            }
        }
    }

    private static class SelectedCurricularCoursesKeyConverter extends Converter {

        static private final long serialVersionUID = 1L;

        @Override
        public Object convert(Class type, Object value) {

            if (value == null) {
                return null;
            }

            final DomainObjectKeyConverter converter = new DomainObjectKeyConverter();
            final List<SelectedCurricularCourse> result = new ArrayList<SelectedCurricularCourse>();

            final String[] values = (String[]) value;
            for (String key : values) {
                String[] parts = key.split(",");
                if (parts.length < 3) {
                    throw new ConversionException("invalid key format: " + key);
                }

                final CurricularCourse curricularCourse = (CurricularCourse) converter.convert(type, parts[0]);
                final CurriculumGroup curriculumGroup = (CurriculumGroup) converter.convert(type, parts[1]);
                final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) converter.convert(type, parts[2]);

                final SelectedCurricularCourse selectedCurricularCourse =
                        new SelectedCurricularCourse(curricularCourse, studentCurricularPlan);
                selectedCurricularCourse.setCurriculumGroup(curriculumGroup);
                result.add(selectedCurricularCourse);
            }
            return result;
        }
    }

    private static class SelectedOptionalCurricularCoursesKeyConverter extends Converter {

        static private final long serialVersionUID = 1L;

        @Override
        public Object convert(Class type, Object value) {

            if (value == null) {
                return null;
            }

            final DomainObjectKeyConverter converter = new DomainObjectKeyConverter();
            final List<SelectedOptionalCurricularCourse> result = new ArrayList<SelectedOptionalCurricularCourse>();

            final String[] values = (String[]) value;
            for (String key : values) {
                String[] parts = key.split(",");
                if (parts.length < 3) {
                    throw new ConversionException("invalid key format: " + key);
                }

                final OptionalCurricularCourse curricularCourse = (OptionalCurricularCourse) converter.convert(type, parts[0]);
                final CurriculumGroup curriculumGroup = (CurriculumGroup) converter.convert(type, parts[1]);
                final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) converter.convert(type, parts[2]);

                final SelectedOptionalCurricularCourse selectedCurricularCourse =
                        new SelectedOptionalCurricularCourse(curricularCourse, studentCurricularPlan);
                selectedCurricularCourse.setCurriculumGroup(curriculumGroup);
                result.add(selectedCurricularCourse);
            }
            return result;
        }
    }
}
