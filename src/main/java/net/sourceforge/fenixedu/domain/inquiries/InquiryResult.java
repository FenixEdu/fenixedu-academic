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
package net.sourceforge.fenixedu.domain.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class InquiryResult extends InquiryResult_Base {

    public final static Comparator<InquiryResult> INQUIRY_RESULT_SCALE_VALUES_COMPARATOR = new Comparator<InquiryResult>() {

        @Override
        public int compare(InquiryResult iq1, InquiryResult iq2) {
            InquiryQuestionHeader questionHeader = iq1.getInquiryQuestion().getInquiryQuestionHeader();
            if (questionHeader == null) {
                questionHeader = iq1.getInquiryQuestion().getInquiryGroupQuestion().getInquiryQuestionHeader();
            }
            String[] scale = questionHeader.getScaleHeaders().getScaleValues();
            Integer index1 = ArrayUtils.indexOf(scale, iq1.getScaleValue());
            Integer index2 = ArrayUtils.indexOf(scale, iq2.getScaleValue());
            return index1.compareTo(index2);
        }

    };

    public InquiryResult() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setLastModifiedDate(new DateTime());
    }

    public InquiryResultComment getInquiryResultComment(Person person, ResultPersonCategory personCategory) {
        for (InquiryResultComment inquiryResultComment : getInquiryResultComments()) {
            if (inquiryResultComment.getPerson() == person && inquiryResultComment.getPersonCategory().equals(personCategory)) {
                return inquiryResultComment;
            }
        }
        return null;
    }

    public static void importResults(String stringResults, DateTime resultDate) {
        String[] allRows = stringResults.split("\r\n");
        String[] rows = new String[199];
        for (int iter = 0, cycleCount = 0; iter < allRows.length; iter++, cycleCount++) {
            if (iter == 0) {
                continue;
            }
            rows[cycleCount] = allRows[iter];
            if (cycleCount == 199 - 1) {

                WriteRows writeRows = new WriteRows(rows, resultDate);
                writeRows.start();
                try {
                    writeRows.join();
                } catch (InterruptedException e) {
                    if (writeRows.domainException != null) {
                        throw writeRows.domainException;
                    }
                    throw new Error(e);
                }
                //importRows(rows, resultDate);
                cycleCount = 0;
                rows = new String[199];
            }
        }
        WriteRows writeRows = new WriteRows(rows, resultDate);
        writeRows.start();
        try {
            writeRows.join();
        } catch (InterruptedException e) {
            if (writeRows.domainException != null) {
                throw writeRows.domainException;
            }
            throw new Error(e);
        }
        //	importRows(rows, resultDate);
    }

    public static class WriteRows extends Thread {
        String[] rows;
        DateTime resultDate;
        DomainException domainException;

        public WriteRows(String[] rows, DateTime resultDate) {
            this.rows = rows;
            this.resultDate = resultDate;
        }

        @Override
        @Atomic(mode = TxMode.READ)
        public void run() {
            try {
                importRows(rows, resultDate);
            } catch (DomainException e) {
                domainException = e;
                throw e;
            }
        }
    }

    @Atomic
    private static void importRows(String[] rows, DateTime resultDate) {
        for (String row : rows) {
            if (row != null) {
                String[] columns = row.split("\t");
                //TODO rever indices das colunas
                //columns[columns.length - 1] = columns[columns.length - 1].split("\r")[0];
                //meter aqui algumas validações
                //se vier com valor + classificação dá erro

                InquiryResult inquiryResult = new InquiryResult();
                inquiryResult.setResultDate(resultDate);

                setConnectionType(columns, inquiryResult);
                setClassification(columns, inquiryResult);
                setInquiryRelation(columns, inquiryResult);
                setExecutionSemester(columns, inquiryResult);
                setResultType(columns, inquiryResult);
                setValue(columns, inquiryResult);
            }
        }
    }

    @Atomic
    public static void updateRows(String rows, DateTime resultDate) {
        String[] allRows = rows.split("\r\n");// \r\n
        for (int iter = 1; iter < allRows.length; iter++) {
            String row = allRows[iter];
            if (row != null) {
                String[] columns = row.split("\t");
                InquiryResultBean inquiryResultBean = new InquiryResultBean(columns);
                InquiryResult inquiryResult = getInquiryResult(inquiryResultBean);
                if (inquiryResult != null) {
                    inquiryResult.setValue(inquiryResultBean.getValue());
                    inquiryResult.setResultClassification(inquiryResultBean.getResultClassification());
                } else {
                    throw new DomainException("result not found: " + getPrintableColumns(columns));
                }
            }
        }
    }

    private static InquiryResult getInquiryResult(InquiryResultBean inquiryResultBean) {
        for (InquiryResult inquiryResult : inquiryResultBean.getExecutionSemester().getInquiryResultsSet()) {
            if (inquiryResult.getInquiryQuestion() == inquiryResultBean.getInquiryQuestion()
                    && inquiryResult.getExecutionCourse() == inquiryResultBean.getExecutionCourse()
                    && inquiryResult.getExecutionDegree() == inquiryResultBean.getExecutionDegree()
                    && inquiryResult.getProfessorship() == inquiryResultBean.getProfessorship()
                    && inquiryResult.getResultType() == inquiryResultBean.getResultType()
                    && inquiryResult.getShiftType() == inquiryResultBean.getShiftType()
                    && inquiryResult.getScaleValue().equalsIgnoreCase(inquiryResultBean.getScaleValue())
                    && inquiryResult.getConnectionType() == inquiryResultBean.getConnectionType()) {
                return inquiryResult;
            }
        }
        return null;
    }

    private static void setValue(String[] columns, InquiryResult inquiryResult) {
        String value = columns[5] != null ? columns[5].replace(",", ".") : columns[5];
        String scaleValue = columns[6];
        inquiryResult.setValue(value);
        inquiryResult.setScaleValue(scaleValue);
    }

    private static void setConnectionType(String[] columns, InquiryResult inquiryResult) {
        String connectionTypeString = columns[10];
        if (StringUtils.isEmpty(connectionTypeString)) {
            throw new DomainException("connectionType: " + getPrintableColumns(columns));
        }
        InquiryConnectionType connectionType = InquiryConnectionType.valueOf(connectionTypeString);
        inquiryResult.setConnectionType(connectionType);
    }

    private static void setResultType(String[] columns, InquiryResult inquiryResult) {
        String resultTypeString = columns[1];
        if (!StringUtils.isEmpty(resultTypeString)) {
            InquiryResultType inquiryResultType = InquiryResultType.valueOf(resultTypeString);
            if (inquiryResultType == null) {
                throw new DomainException("resultType: " + getPrintableColumns(columns));
            }
            inquiryResult.setResultType(inquiryResultType);
        }
    }

    private static void setClassification(String[] columns, InquiryResult inquiryResult) {
        String resultClassificationString = columns[4];
        if (!StringUtils.isEmpty(resultClassificationString)) {
            ResultClassification classification = ResultClassification.valueOf(resultClassificationString);
            if (classification == null) {
                throw new DomainException("classification: " + getPrintableColumns(columns));
            }
            inquiryResult.setResultClassification(classification);
        }
    }

    private static void setExecutionSemester(String[] columns, InquiryResult inquiryResult) {
        String executionPeriodOID = columns[3];
        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodOID);
        if (executionSemester == null) {
            throw new DomainException("executionPeriod: " + getPrintableColumns(columns));
        }
        inquiryResult.setExecutionPeriod(executionSemester);
    }

    /*
     * OID_EXECUTION_DEGREE RESULT_TYPE OID_EXECUTION_COURSE OID_EXECUTION_PERIOD RESULT_CLASSIFICATION VALUE_ SCALE_VALUE
     * OID_INQUIRY_QUESTION OID_PROFESSORSHIP SHIFT_TYPE CONNECTION_TYPE
     */
    private static void setInquiryRelation(String[] columns, InquiryResult inquiryResult) {
        String inquiryQuestionOID = columns[7];
        String executionCourseOID = columns[2];
        String executionDegreeOID = columns[0];
        String professorshipOID = columns[8];
        String shiftTypeString = columns[9];
        ExecutionCourse executionCourse =
                !StringUtils.isEmpty(executionCourseOID) ? (ExecutionCourse) FenixFramework.getDomainObject(executionCourseOID) : null;
        ExecutionDegree executionDegree =
                !StringUtils.isEmpty(executionDegreeOID) ? (ExecutionDegree) FenixFramework.getDomainObject(executionDegreeOID) : null;
        Professorship professorship =
                !StringUtils.isEmpty(professorshipOID) ? (Professorship) FenixFramework.getDomainObject(professorshipOID) : null;
        ShiftType shiftType = !StringUtils.isEmpty(shiftTypeString) ? ShiftType.valueOf(shiftTypeString) : null;
        inquiryResult.setExecutionCourse(executionCourse);
        inquiryResult.setExecutionDegree(executionDegree);
        inquiryResult.setProfessorship(professorship);
        inquiryResult.setShiftType(shiftType);

        if (!(StringUtils.isEmpty(inquiryQuestionOID) && ResultClassification.GREY
                .equals(inquiryResult.getResultClassification()))) {
            InquiryQuestion inquiryQuestion = FenixFramework.getDomainObject(inquiryQuestionOID);
            if (inquiryQuestion == null) {
                throw new DomainException("não tem question: " + getPrintableColumns(columns));
            }
            inquiryResult.setInquiryQuestion(inquiryQuestion);
        }
    }

    private static String getPrintableColumns(String[] columns) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : columns) {
            stringBuilder.append(value).append("\t");
        }
        return stringBuilder.toString();
    }

    public String getPresentationValue() {
        if (InquiryResultType.PERCENTAGE.equals(getResultType())) {
            Double value = Double.valueOf(getValue()) * 100;
            int roundedValue = (int) StrictMath.round(value);
            return String.valueOf(roundedValue);
        }
        return getValue();
    }

    public void delete() {
        if (!getInquiryResultComments().isEmpty()) {
            throw new DomainException("error.inquiryResult.hasComments", getInquiryQuestion().getLabel().toString(),
                    getExecutionCourse().getExternalId(),
                    getExecutionDegree() != null ? getExecutionDegree().getExternalId() : StringUtils.EMPTY,
                    getProfessorship() != null ? getProfessorship().getExternalId() : StringUtils.EMPTY);
        }
        setExecutionCourse(null);
        setExecutionDegree(null);
        setExecutionPeriod(null);
        setInquiryQuestion(null);
        setProfessorship(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public List<InquiryResultComment> getCommentsWithLowerPermissions(ResultPersonCategory personCategory) {
        List<InquiryResultComment> result = new ArrayList<InquiryResultComment>();
        for (InquiryResultComment inquiryResultComment : getInquiryResultComments()) {
            if (inquiryResultComment.getPersonCategory().getPermissionOrder() < personCategory.getPermissionOrder()) {
                result.add(inquiryResultComment);
            }
        }
        return result;
    }

    private static class InquiryResultBean implements Serializable {

        private static final long serialVersionUID = 1L;
        private String value;
        private String scaleValue;
        private InquiryConnectionType connectionType;
        private InquiryResultType resultType;
        private ResultClassification resultClassification;
        private ExecutionSemester executionSemester;
        private ExecutionDegree executionDegree;
        private ExecutionCourse executionCourse;
        private InquiryQuestion inquiryQuestion;
        private Professorship professorship;
        private ShiftType shiftType;

        public InquiryResultBean(String[] row) {

            //TODO rever indices das colunas
            //columns[columns.length - 1] = columns[columns.length - 1].split("\r")[0];
            //meter aqui algumas validações
            //se vier com valor + classificação dá erro
            String executionDegreeOID = row[0];
            ExecutionDegree executionDegree =
                    !StringUtils.isEmpty(executionDegreeOID) ? (ExecutionDegree) FenixFramework
                            .getDomainObject(executionDegreeOID) : null;
            setExecutionDegree(executionDegree);

            String resultTypeString = row[1];
            if (!StringUtils.isEmpty(resultTypeString)) {
                InquiryResultType inquiryResultType = InquiryResultType.valueOf(resultTypeString);
                if (inquiryResultType == null) {
                    throw new DomainException("resultType doesn't exists: " + getPrintableColumns(row));
                }
                setResultType(inquiryResultType);
            }

            String executionCourseOID = row[2];
            ExecutionCourse executionCourse =
                    !StringUtils.isEmpty(executionCourseOID) ? (ExecutionCourse) FenixFramework
                            .getDomainObject(executionCourseOID) : null;
            setExecutionCourse(executionCourse);

            String executionPeriodOID = row[3];
            ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodOID);
            if (executionSemester == null) {
                throw new DomainException("executionPeriod resultType doesn't exists: " + getPrintableColumns(row));
            }
            setExecutionSemester(executionSemester);

            String resultClassificationString = row[4];
            if (!StringUtils.isEmpty(resultClassificationString)) {
                ResultClassification classification = ResultClassification.valueOf(resultClassificationString);
                if (classification == null) {
                    throw new DomainException("classification doesn't exists: : " + getPrintableColumns(row));
                }
                setResultClassification(classification);
            }

            String value = row[5] != null ? row[5].replace(",", ".") : row[5];
            String scaleValue = row[6];
            setValue(value);
            setScaleValue(scaleValue);

            String inquiryQuestionOID = row[7];//TODO ver melhor
            if (!(StringUtils.isEmpty(inquiryQuestionOID) && ResultClassification.GREY.equals(getResultClassification()))) {
                InquiryQuestion inquiryQuestion = FenixFramework.getDomainObject(inquiryQuestionOID);
                if (inquiryQuestion == null) {
                    throw new DomainException("não tem question: " + getPrintableColumns(row));
                }
                setInquiryQuestion(inquiryQuestion);
            }

            String professorshipOID = row[8];
            Professorship professorship =
                    !StringUtils.isEmpty(professorshipOID) ? (Professorship) FenixFramework.getDomainObject(professorshipOID) : null;
            setProfessorship(professorship);

            String shiftTypeString = row[9];
            ShiftType shiftType = !StringUtils.isEmpty(shiftTypeString) ? ShiftType.valueOf(shiftTypeString) : null;
            setShiftType(shiftType);

            String connectionTypeString = row[10];
            if (StringUtils.isEmpty(connectionTypeString)) {
                throw new DomainException("connectionType doesn't exists: " + getPrintableColumns(row));
            }
            InquiryConnectionType connectionType = InquiryConnectionType.valueOf(connectionTypeString);
            setConnectionType(connectionType);
        }

        private String getPrintableColumns(String[] columns) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : columns) {
                stringBuilder.append(value).append("\t");
            }
            return stringBuilder.toString();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getScaleValue() {
            return scaleValue;
        }

        public void setScaleValue(String scaleValue) {
            this.scaleValue = scaleValue;
        }

        public InquiryConnectionType getConnectionType() {
            return connectionType;
        }

        public void setConnectionType(InquiryConnectionType connectionType) {
            this.connectionType = connectionType;
        }

        public InquiryResultType getResultType() {
            return resultType;
        }

        public void setResultType(InquiryResultType resultType) {
            this.resultType = resultType;
        }

        public ResultClassification getResultClassification() {
            return resultClassification;
        }

        public void setResultClassification(ResultClassification resultClassification) {
            this.resultClassification = resultClassification;
        }

        public ExecutionSemester getExecutionSemester() {
            return executionSemester;
        }

        public void setExecutionSemester(ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
        }

        public ExecutionDegree getExecutionDegree() {
            return executionDegree;
        }

        public void setExecutionDegree(ExecutionDegree executionDegree) {
            this.executionDegree = executionDegree;
        }

        public ExecutionCourse getExecutionCourse() {
            return executionCourse;
        }

        public void setExecutionCourse(ExecutionCourse executionCourse) {
            this.executionCourse = executionCourse;
        }

        public InquiryQuestion getInquiryQuestion() {
            return inquiryQuestion;
        }

        public void setInquiryQuestion(InquiryQuestion inquiryQuestion) {
            this.inquiryQuestion = inquiryQuestion;
        }

        public Professorship getProfessorship() {
            return professorship;
        }

        public void setProfessorship(Professorship professorship) {
            this.professorship = professorship;
        }

        public ShiftType getShiftType() {
            return shiftType;
        }

        public void setShiftType(ShiftType shiftType) {
            this.shiftType = shiftType;
        }

    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment> getInquiryResultComments() {
        return getInquiryResultCommentsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryResultComments() {
        return !getInquiryResultCommentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasResultClassification() {
        return getResultClassification() != null;
    }

    @Deprecated
    public boolean hasScaleValue() {
        return getScaleValue() != null;
    }

    @Deprecated
    public boolean hasShiftType() {
        return getShiftType() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasInquiryQuestion() {
        return getInquiryQuestion() != null;
    }

    @Deprecated
    public boolean hasConnectionType() {
        return getConnectionType() != null;
    }

    @Deprecated
    public boolean hasResultDate() {
        return getResultDate() != null;
    }

    @Deprecated
    public boolean hasResultType() {
        return getResultType() != null;
    }

}
