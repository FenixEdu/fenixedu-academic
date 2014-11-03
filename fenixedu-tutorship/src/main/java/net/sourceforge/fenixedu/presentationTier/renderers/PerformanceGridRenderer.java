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
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine.PerformanceGridLineYearGroup;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.layouts.TabularLayout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class PerformanceGridRenderer extends OutputRenderer {

    private Integer numberOfFixedColumns;

    private String schema;

    private String emptySpace;

    private String approvedMonitoringYear;

    private String approvedAnotherYear;

    private String enroled;

    private String notApprovedMonitoringYear;

    private String notApprovedAnotherYear;

    private String enrolmentSchema;

    private String performanceGridBundle;

    private String columnClasses;

    public String getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public Integer getNumberOfFixedColumns() {
        return numberOfFixedColumns;
    }

    public void setNumberOfFixedColumns(Integer numberOfFixedColumns) {
        this.numberOfFixedColumns = numberOfFixedColumns;
    }

    public String getPerformanceGridBundle() {
        return performanceGridBundle;
    }

    public void setPerformanceGridBundle(String performanceGridBundle) {
        this.performanceGridBundle = performanceGridBundle;
    }

    public String getEnrolmentSchema() {
        return enrolmentSchema;
    }

    public void setEnrolmentSchema(String enrolmentSchema) {
        this.enrolmentSchema = enrolmentSchema;
    }

    public String getApprovedAnotherYear() {
        return approvedAnotherYear;
    }

    public void setApprovedAnotherYear(String approvedAnotherYear) {
        this.approvedAnotherYear = approvedAnotherYear;
    }

    public String getApprovedMonitoringYear() {
        return approvedMonitoringYear;
    }

    public void setApprovedMonitoringYear(String approvedMonitoringYear) {
        this.approvedMonitoringYear = approvedMonitoringYear;
    }

    public String getEmptySpace() {
        return emptySpace;
    }

    public void setEmptySpace(String emptySpace) {
        this.emptySpace = emptySpace;
    }

    public String getEnroled() {
        return enroled;
    }

    public void setEnroled(String enroled) {
        this.enroled = enroled;
    }

    public String getNotApprovedAnotherYear() {
        return notApprovedAnotherYear;
    }

    public void setNotApprovedAnotherYear(String notApprovedAnotherYear) {
        this.notApprovedAnotherYear = notApprovedAnotherYear;
    }

    public String getNotApprovedMonitoringYear() {
        return notApprovedMonitoringYear;
    }

    public void setNotApprovedMonitoringYear(String notApprovedMonitoringYear) {
        this.notApprovedMonitoringYear = notApprovedMonitoringYear;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new PerformanceGridRendererLayout((Collection<PerformanceGridLine>) object);
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public class PerformanceGridRendererLayout extends TabularLayout {

        List<MetaObject> metaObjects;

        private int numberOfSlots;

        public PerformanceGridRendererLayout(Collection collection) {
            this.metaObjects = new ArrayList<MetaObject>();

            for (PerformanceGridLine performanceByStudent : (Collection<PerformanceGridLine>) collection) {
                metaObjects.add(MetaObjectFactory.createObject(performanceByStudent, findSchema()));
            }
            numberOfSlots = this.metaObjects.iterator().next().getSlots().size();
        }

        @Override
        protected boolean hasHeader() {
            return true;
        }

        @Override
        protected boolean hasHeaderGroups() {
            return true;
        }

        private Schema findSchema() {
            return RenderKit.getInstance().findSchema(getSchema());
        }

        @Override
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
            if (columnIndex < numberOfSlots) {
                return renderSlot(this.metaObjects.get(rowIndex).getSlots().get(columnIndex));

            } else if (columnIndex >= numberOfSlots && columnIndex < numberOfSlots + 2) {
                PerformanceGridLine bean = (PerformanceGridLine) this.metaObjects.get(rowIndex).getObject();
                List<Integer> ratio = bean.getApprovedRatioBySemester();
                HtmlText text = new HtmlText(ratio.get(getSemester(columnIndex) - 1).toString() + "%");
                return text;
            } else {
                PerformanceGridLine bean = (PerformanceGridLine) this.metaObjects.get(rowIndex).getObject();

                PerformanceGridLineYearGroup yearBean = bean.getStudentPerformanceByYear().get(getYear(columnIndex) - 1);

                List<Enrolment> enrolments =
                        (getSemester(columnIndex) == 1 ? yearBean.getFirstSemesterEnrolments() : yearBean
                                .getSecondSemesterEnrolments());
                double approvedSemesterECTS =
                        (getSemester(columnIndex) == 1 ? yearBean.getApprovedFirstSemesterECTS() : yearBean
                                .getApprovedSecondSemesterECTS());
                double enrolledSemesterECTS =
                        (getSemester(columnIndex) == 1 ? yearBean.getEnrolledFirstSemesterECTS() : yearBean
                                .getEnrolledSecondSemesterECTS());

                HtmlInlineContainer container = new HtmlInlineContainer();

                for (Enrolment enrolment : enrolments) {
                    HtmlText text = new HtmlText("&nbsp;", false);
                    text.setClasses(getEnrolmentState(enrolment, bean.getCurrentMonitoringYearYear()) + " performanceGrid");
                    container.addChild(text);
                }
                if (enrolledSemesterECTS > 0.0) {
                    HtmlText textECTS =
                            new HtmlText("<br/> " + Double.toString(approvedSemesterECTS) + " / "
                                    + Double.toString(enrolledSemesterECTS), false);
                    textECTS.setClasses("smalltxt color888");
                    textECTS.setTitle(RenderUtils.getResourceString(getPerformanceGridBundle(), "label.ECTS.credits.ratio"));
                    container.addChild(textECTS);
                }
                return container;
            }

        }

        private String getEnrolmentState(Enrolment enrolment, ExecutionYear monitoringYear) {
            if (enrolment.getEnrollmentState().equals(EnrollmentState.NOT_APROVED)) {
                if (enrolment.getExecutionYear().equals(monitoringYear)) {
                    return notApprovedMonitoringYear;
                } else {
                    return notApprovedAnotherYear;
                }
            } else if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                if (enrolment.getExecutionYear().equals(monitoringYear)) {
                    return approvedMonitoringYear;
                } else {
                    return approvedAnotherYear;
                }
            } else if (enrolment.getEnrollmentState().equals(EnrollmentState.ENROLLED)
                    || enrolment.getEnrollmentState().equals(EnrollmentState.NOT_EVALUATED)) {
                if (enrolment.getExecutionYear().equals(monitoringYear)) {
                    return enroled;
                } else {
                    return notApprovedAnotherYear; // not evaluated in another
                    // year
                }
            }
            return emptySpace;
        }

        @Override
        protected HtmlComponent getHeaderComponent(int columnIndex) {
            MetaObject one = metaObjects.iterator().next();

            if (columnIndex < one.getSlots().size()) {
                return new HtmlText(one.getSlots().get(columnIndex).getLabel());
            } else {
                HtmlText text = new HtmlText(getSemester(columnIndex) + "º Sem");
                text.setClasses("nowrap");
                return text;
            }

        }

        @Override
        protected String getHeaderGroup(int columnIndex) {
            if (columnIndex < numberOfSlots) {
                return null;
            } else if (columnIndex >= numberOfSlots && columnIndex < numberOfSlots + 2) {
                return RenderUtils.getResourceString(getPerformanceGridBundle(), "label.approvedRatio");
            } else {
                return getYear(columnIndex) + "º Ano";
            }
        }

        @Override
        protected int getNumberOfColumns() {
            return numberOfSlots + numberOfFixedColumns;
        }

        @Override
        protected int getNumberOfRows() {
            return this.metaObjects.size();
        }

        public int getYear(int columnIndex) {
            columnIndex++; // because columns start at index 0

            int fixedColumn = columnIndex - (numberOfSlots + 2);

            return (fixedColumn - fixedColumn / 2);

        }

        public int getSemester(int columnIndex) {
            if (numberOfSlots % 2 == 0) {
                return (((columnIndex % 2) == 0) ? 1 : 2);
            } else {
                return (((columnIndex % 2) == 0) ? 2 : 1);
            }
        }

    }
}
