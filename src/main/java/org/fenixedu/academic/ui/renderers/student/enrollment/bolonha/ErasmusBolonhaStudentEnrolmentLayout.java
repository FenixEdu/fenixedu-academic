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
package org.fenixedu.academic.ui.renderers.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.ErasmusBolonhaStudentEnrollmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.ErasmusBolonhaStudentEnrollmentBean.ErasmusExtraCurricularEnrolmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.StudentCurriculumGroupBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.controllers.CopyCheckBoxValuesController;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.components.HtmlActionLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlMultipleHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ErasmusBolonhaStudentEnrolmentLayout extends BolonhaStudentEnrolmentLayout {

    protected boolean isAcademicRelationsOfficeMember() {
        final Person person = AccessControl.getPerson();
        return RoleType.INTERNATIONAL_RELATION_OFFICE.isMember(person.getUser()) || canPerformStudentEnrolments;
    }

    @Override
    protected void generateCurricularCoursesToEnrol(HtmlTable groupTable, StudentCurriculumGroupBean studentCurriculumGroupBean,
            List<IDegreeModuleToEvaluate> degreeModulesToSelect) {
        final List<IDegreeModuleToEvaluate> coursesToEvaluate = studentCurriculumGroupBean.getSortedDegreeModulesToEvaluate();
        generateCurricularCoursesToEnrol(groupTable, coursesToEvaluate, degreeModulesToSelect);
    }

    private void generateCurricularCoursesToEnrol(HtmlTable groupTable, final List<IDegreeModuleToEvaluate> coursesToEvaluate,
            final List<IDegreeModuleToEvaluate> degreeModulesToSelect) {
        ErasmusBolonhaStudentEnrollmentBean bean = (ErasmusBolonhaStudentEnrollmentBean) getBolonhaStudentEnrollmentBean();

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : coursesToEvaluate) {
            if (!bean.getCandidacy().getCurricularCoursesSet().contains(degreeModuleToEvaluate.getDegreeModule())) {
                // if(contains(bean.getCandidacy().getCurricularCourses(),
                // degreeModuleToEvaluate)) {
                continue;
            }

            HtmlTableRow htmlTableRow = groupTable.createRow();
            HtmlTableCell cellName = htmlTableRow.createCell();
            cellName.setClasses(getRenderer().getCurricularCourseToEnrolNameClasses());

            String degreeName = degreeModuleToEvaluate.getName();

            if (isAcademicRelationsOfficeMember() && degreeModuleToEvaluate.getDegreeModule() instanceof CurricularCourse) {
                if (!StringUtils.isEmpty(degreeModuleToEvaluate.getDegreeModule().getCode())) {
                    degreeName = degreeModuleToEvaluate.getDegreeModule().getCode() + " - " + degreeName;
                }

                CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();
                degreeName +=
                        " (" + BundleUtil.getString(Bundle.STUDENT, "label.grade.scale") + " - "
                                + curricularCourse.getGradeScaleChain().getDescription() + ") ";
            }

            cellName.setBody(new HtmlText(degreeName));

            // Year
            final HtmlTableCell yearCell = htmlTableRow.createCell();
            yearCell.setClasses(getRenderer().getCurricularCourseToEnrolYearClasses());
            yearCell.setColspan(2);
            yearCell.setBody(new HtmlText(degreeModuleToEvaluate.getYearFullLabel()));

            if (!degreeModuleToEvaluate.isOptionalCurricularCourse()) {
                // Ects
                final HtmlTableCell ectsCell = htmlTableRow.createCell();
                ectsCell.setClasses(getRenderer().getCurricularCourseToEnrolEctsClasses());

                final StringBuilder ects = new StringBuilder();
                ects.append(degreeModuleToEvaluate.getEctsCredits()).append(" ")
                        .append(BundleUtil.getString(Bundle.STUDENT, "label.credits.abbreviation"));
                ectsCell.setBody(new HtmlText(ects.toString()));

                HtmlTableCell checkBoxCell = htmlTableRow.createCell();
                checkBoxCell.setClasses(getRenderer().getCurricularCourseToEnrolCheckBoxClasses());

                HtmlCheckBox checkBox = new HtmlCheckBox(false);
                if (degreeModulesToSelect.contains(degreeModuleToEvaluate)) {
                    checkBox.setChecked(true);
                }
                checkBox.setName("degreeModuleToEnrolCheckBox" + degreeModuleToEvaluate.getKey());
                checkBox.setUserValue(degreeModuleToEvaluate.getKey());
                getDegreeModulesToEvaluateController().addCheckBox(checkBox);
                checkBoxCell.setBody(checkBox);
            } else {
                final HtmlTableCell cell = htmlTableRow.createCell();
                cell.setClasses(getRenderer().getCurricularCourseToEnrolEctsClasses());
                cell.setBody(new HtmlText(""));

                HtmlTableCell linkTableCell = htmlTableRow.createCell();
                linkTableCell.setClasses(getRenderer().getCurricularCourseToEnrolCheckBoxClasses());

                final HtmlActionLink actionLink = new HtmlActionLink();
                actionLink.setText(BundleUtil.getString(Bundle.STUDENT, "label.chooseOptionalCurricularCourse"));
                actionLink.setController(new OptionalCurricularCourseLinkController(degreeModuleToEvaluate));
                actionLink
                        .setOnClick("$(\\\"form[name='org.fenixedu.academic.ui.struts.FenixActionForm']\\\").method.value='prepareChooseOptionalCurricularCourseToEnrol';");
                actionLink.setName("optionalCurricularCourseLink" + degreeModuleToEvaluate.getCurriculumGroup().getExternalId()
                        + "_" + degreeModuleToEvaluate.getContext().getExternalId());
                linkTableCell.setBody(actionLink);
            }

            if (getRenderer().isEncodeCurricularRules()) {
                encodeCurricularRules(groupTable, degreeModuleToEvaluate, degreeModuleToEvaluate.getCurriculumGroup().getDegreeModule());
            }
        }
    }

    private static class ErasmusExtraCurricularEnrolmentConverter extends Converter {

        @Override
        public Object convert(Class type, Object value) {
            ArrayList<ErasmusExtraCurricularEnrolmentBean> list =
                    new ArrayList<ErasmusBolonhaStudentEnrollmentBean.ErasmusExtraCurricularEnrolmentBean>();
            final DomainObjectKeyConverter converter = new DomainObjectKeyConverter();

            for (String string : (String[]) value) {
                list.add(new ErasmusExtraCurricularEnrolmentBean((CurricularCourse) converter.convert(type, string), true));
            }

            return list;
        }

    }

    public CopyCheckBoxValuesController controller = new CopyCheckBoxValuesController();

    public boolean isContextValid(CurricularCourse curricularCourse) {
        Collection<Context> parentContexts = curricularCourse.getParentContextsSet();
        for (Context context : parentContexts) {
            if (context.isValid(getBolonhaStudentEnrollmentBean().getExecutionPeriod())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HtmlComponent createComponent(Object object, Class type) {
        setBolonhaStudentEnrollmentBean((BolonhaStudentEnrollmentBean) object);

        if (getBolonhaStudentEnrollmentBean() == null) {
            return new HtmlText();
        }

        final HtmlBlockContainer container = new HtmlBlockContainer();

        HtmlMultipleHiddenField hiddenEnrollments = new HtmlMultipleHiddenField();
        hiddenEnrollments.bind(getRenderer().getInputContext().getMetaObject(), "curriculumModulesToRemove");
        hiddenEnrollments.setConverter(new DomainObjectKeyArrayConverter());
        hiddenEnrollments.setController(getEnrollmentsController());

        HtmlMultipleHiddenField hiddenDegreeModulesToEvaluate = new HtmlMultipleHiddenField();
        hiddenDegreeModulesToEvaluate.bind(getRenderer().getInputContext().getMetaObject(), "degreeModulesToEvaluate");
        hiddenDegreeModulesToEvaluate.setConverter(getBolonhaStudentEnrollmentBean().getDegreeModulesToEvaluateConverter());
        hiddenDegreeModulesToEvaluate.setController(getDegreeModulesToEvaluateController());

        HtmlMultipleHiddenField hiddenExtraCurricularEnrollments = new HtmlMultipleHiddenField();
        hiddenExtraCurricularEnrollments.bind(getRenderer().getInputContext().getMetaObject(), "extraCurricularEnrolments");
        hiddenExtraCurricularEnrollments.setConverter(new ErasmusExtraCurricularEnrolmentConverter());
        hiddenExtraCurricularEnrollments.setController(controller);

        container.addChild(hiddenEnrollments);
        container.addChild(hiddenDegreeModulesToEvaluate);
        container.addChild(hiddenExtraCurricularEnrollments);

        generateGroup(container, getBolonhaStudentEnrollmentBean().getDegreeModulesToEvaluate(),
                getBolonhaStudentEnrollmentBean().getStudentCurricularPlan(), getBolonhaStudentEnrollmentBean()
                        .getRootStudentCurriculumGroupBean(), getBolonhaStudentEnrollmentBean().getExecutionPeriod(), 0);

        HtmlTable groupTable = createGroupTable(container, 0);

        HtmlTableRow htmlTableRow = groupTable.createRow();
        htmlTableRow.setClasses(getRenderer().getGroupRowClasses());
        htmlTableRow.createCell().setBody(new HtmlText("Other Curricular Units", false));
        HtmlTableCell cell = htmlTableRow.createCell();
        cell.setClasses("aright");

        HtmlCheckBox checkBox = new HtmlCheckBox(false);
        final String name = "degreeModuleToEnrolCheckBox";
        checkBox.setName(name);
        checkBox.setUserValue("true");
        checkBox.setChecked(true);

        cell.setBody(checkBox);
        groupTable = createCoursesTable(container, 0);
        NoCourseGroupCurriculumGroup group =
                getBolonhaStudentEnrollmentBean().getStudentCurricularPlan().getNoCourseGroupCurriculumGroup(
                        NoCourseGroupCurriculumGroupType.STANDALONE);
        HashSet<CurricularCourse> set = new HashSet<CurricularCourse>();
        ErasmusBolonhaStudentEnrollmentBean erasmusBolonhaStudentEnrollmentBean =
                (ErasmusBolonhaStudentEnrollmentBean) getBolonhaStudentEnrollmentBean();
        set.addAll(erasmusBolonhaStudentEnrollmentBean.getCandidacy().getCurricularCoursesSet());
        for (Enrolment enrolment : group.getEnrolments()) {
            set.add(enrolment.getCurricularCourse());
        }

        for (CurricularCourse curricularCourse : set) {
            if (erasmusBolonhaStudentEnrollmentBean.getStudentCurricularPlan().getEnrolmentByCurricularCourseAndExecutionPeriod(
                    curricularCourse, erasmusBolonhaStudentEnrollmentBean.getExecutionPeriod()) != null) {
                if (!group.hasEnrolmentWithEnroledState(curricularCourse,
                        erasmusBolonhaStudentEnrollmentBean.getExecutionPeriod())) {

                    continue;
                }
            }

            if (!isContextValid(curricularCourse)) {
                continue;
            }

            htmlTableRow = groupTable.createRow();
            HtmlTableCell cellName = htmlTableRow.createCell();
            cellName.setClasses(getRenderer().getCurricularCourseToEnrolNameClasses());

            String degreeName = curricularCourse.getName();

            if (isAcademicRelationsOfficeMember() && curricularCourse instanceof CurricularCourse) {
                if (!StringUtils.isEmpty(curricularCourse.getCode())) {
                    degreeName = curricularCourse.getCode() + " - " + degreeName;
                }

                degreeName +=
                        " (" + BundleUtil.getString(Bundle.STUDENT, "label.grade.scale") + " - "
                                + curricularCourse.getGradeScaleChain().getDescription() + ") ";
            }

            cellName.setBody(new HtmlText(degreeName));

            // Year
            final HtmlTableCell yearCell = htmlTableRow.createCell();
            yearCell.setClasses(getRenderer().getCurricularCourseToEnrolYearClasses());
            yearCell.setColspan(2);
            yearCell.setBody(new HtmlText(getBolonhaStudentEnrollmentBean().getExecutionPeriod().getQualifiedName()));

            final HtmlTableCell ectsCell = htmlTableRow.createCell();
            ectsCell.setClasses(getRenderer().getCurricularCourseToEnrolEctsClasses());

            final StringBuilder ects = new StringBuilder();
            ects.append(curricularCourse.getEctsCredits()).append(" ")
                    .append(BundleUtil.getString(Bundle.STUDENT, "label.credits.abbreviation"));
            ectsCell.setBody(new HtmlText(ects.toString()));

            HtmlTableCell checkBoxCell = htmlTableRow.createCell();
            checkBoxCell.setClasses(getRenderer().getCurricularCourseToEnrolCheckBoxClasses());

            checkBox = new HtmlCheckBox(false);
            checkBox.setName("extraCurricularEnrolments" + curricularCourse.getClass().getCanonicalName() + ":"
                    + curricularCourse.getExternalId());
            checkBox.setUserValue(curricularCourse.getClass().getCanonicalName() + ":" + curricularCourse.getExternalId());
            checkBoxCell.setBody(checkBox);
            controller.addCheckBox(checkBox);

            if (group.hasEnrolmentWithEnroledState(curricularCourse, erasmusBolonhaStudentEnrollmentBean.getExecutionPeriod())) {
                cellName.setClasses(getRenderer().getEnrolmentNameClasses());
                yearCell.setClasses(getRenderer().getEnrolmentYearClasses());
                ectsCell.setClasses(getRenderer().getEnrolmentEctsClasses());
                checkBoxCell.setClasses(getRenderer().getEnrolmentCheckBoxClasses());

                checkBox.setChecked(true);
            }

        }

        return container;
    }
}
