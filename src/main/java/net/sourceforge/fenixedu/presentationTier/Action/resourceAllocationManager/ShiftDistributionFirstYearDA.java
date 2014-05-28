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
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMFirstYearShiftsApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.FileUtils;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = RAMFirstYearShiftsApp.class, path = "shift-distribution",
        titleKey = "link.firstTimeStudents.shiftDistribution", accessGroup = "nobody")
@Mapping(path = "/shiftDistributionFirstYear", module = "resourceAllocationManager")
@Forwards({
        @Forward(name = "shiftDistribution", path = "/resourceAllocationManager/firstTimeStudents/uploadAndDistributeShifts.jsp"),
        @Forward(name = "showStudentPerformanceGrid",
                path = "/resourceAllocationManager/firstTimeStudents/showStudentPerformanceGrid.jsp"),
        @Forward(name = "showStudentCurriculum", path = "/resourceAllocationManager/firstTimeStudents/showStudentCurriculum.jsp"),
        @Forward(name = "chooseRegistration", path = "/resourceAllocationManager/firstTimeStudents/chooseRegistration.jsp") })
public class ShiftDistributionFirstYearDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(ShiftDistributionFirstYearDA.class);

    private final Integer FIRST_CURRICULAR_YEAR = Integer.valueOf(1);
    private final int MAX_NUMBER_OF_STUDENTS = 210;
    private final int STUDENTS_PER_LINE = 20;

    private static final String COLUMN_SEPARATOR = "\t";
    private static final String LINE_SEPARATOR = "\n";

    private static final String[] NO_VACANCY_DEGREE_CODES = { "9032", // Territorio
            "9223", // Quimica
            "9099" // Mestrado Ambiente
    };

    @EntryPoint
    public ActionForward prepareShiftDistribution(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("fileBeanDistribution", new ShiftDistributionFileBean());
        return mapping.findForward("shiftDistribution");
    }

    public ActionForward uploadAndSimulateFileDistribution(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ShiftDistributionFileBean fileBean = getRenderedObject();
        String fileContents =
                FileUtils.readFile(new InputStreamReader(fileBean.getInputStream(), Charset.defaultCharset().name()));
        final String[] data = fileContents.split("\n");

        List<ShiftDistributionDTO> shiftDistributionFromFile = new ArrayList<ShiftDistributionDTO>(data.length);

        for (String dataLine : data) {
            ShiftDistributionDTO shiftDistributionDTO = new ShiftDistributionDTO();
            boolean shouldAdd = shiftDistributionDTO.fillWithFileLineData(dataLine);
            if (shouldAdd) {
                shiftDistributionFromFile.add(shiftDistributionDTO);
            }
        }

        List<String> errorLog = new ArrayList<String>();
        List<String> warningLog = new ArrayList<String>();
        final Map<DegreeCurricularPlan, List<SchoolClassDistributionInformation>> processedInformation =
                processInformationFrom(shiftDistributionFromFile, errorLog);
        final Map<DegreeCurricularPlan, List<Integer>> abstractStudentNumbers =
                generateAbstractStudentNumbers(fileBean.getPhaseNumber(), errorLog);
        final Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> distribution =
                distributeStudents(abstractStudentNumbers, processedInformation, warningLog);

        if (!errorLog.isEmpty()) {
            request.setAttribute("errorLog", errorLog);
            request.setAttribute("allowToWriteDistribution", "false");
            request.setAttribute("allowToGetStatistics", "false");
        } else {
            request.setAttribute("allowToWriteDistribution", "true");
            request.setAttribute("allowToGetStatistics", "true");
            fileBean.setDistribution(distribution);
            fileBean.setAbstractStudentNumbers(abstractStudentNumbers);
            request.setAttribute("fileBeanDistribution", fileBean);
        }

        Collections.sort(warningLog);
        request.setAttribute("warningLog", warningLog);
        return mapping.findForward("shiftDistribution");
    }

    public ActionForward writeDistribution(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ShiftDistributionFileBean fileBean = getRenderedObject();
        fileBean.writeDistribution();
        request.setAttribute("fileBeanDistribution", fileBean);
        request.setAttribute("allowToGetStatistics", "true");
        request.setAttribute("success", "true");
        return mapping.findForward("shiftDistribution");
    }

    public ActionForward exportStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ShiftDistributionFileBean fileBean = getRenderedObject();
        Spreadsheet spreadsheet =
                getStatisticsFromShiftDistribution(fileBean.getDistribution(), fileBean.getAbstractStudentNumbers());

        response.setHeader("Content-Disposition", "attachment; filename=estatisticas_distribuicao" + new DateTime() + ".csv");
        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.exportToCSV(writer, COLUMN_SEPARATOR, LINE_SEPARATOR);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    protected Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> distributeStudents(
            Map<DegreeCurricularPlan, List<Integer>> abstractStudentNumbers,
            Map<DegreeCurricularPlan, List<SchoolClassDistributionInformation>> processedInformation, List<String> warningLog) {

        final Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> result =
                new HashMap<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>>();

        for (final Entry<DegreeCurricularPlan, List<Integer>> entry : abstractStudentNumbers.entrySet()) {

            final List<SchoolClassDistributionInformation> schoolClassDistributions = processedInformation.get(entry.getKey());
            if (schoolClassDistributions == null) {
                warningLog.add(new StringBuilder("Não foi encontrada nenhuma informação para a distribuição de '")
                        .append(entry.getKey().getName()).append("'").toString());
                continue;
            }
            enrolStudents(result, entry, schoolClassDistributions, warningLog);
        }
        return result;
    }

    private void enrolStudents(final Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> result,
            final Entry<DegreeCurricularPlan, List<Integer>> entry,
            final List<SchoolClassDistributionInformation> schoolClassDistributions, List<String> warningLog) {

        int numberOfEnroled = 0;
        SchoolClassDistributionInformation schoolClassDistribution =
                getSchoolClassDistributionWithCapacity(schoolClassDistributions);
        for (final Integer studentNumber : entry.getValue()) {

            if (schoolClassDistribution.isDistributed()) {
                schoolClassDistribution = getSchoolClassDistributionWithCapacity(schoolClassDistributions);
                if (schoolClassDistribution == null) {
                    //Not enough space in schoolClass to distribute students for
                    //		    errorLog.add(new StringBuilder("Não há espaço suficiente na aula para distribuir os alunos de '").append(
                    //			    entry.getKey().getName()).append("'.").toString());
                    warningLog.add(new StringBuilder("\tInscritos ").append(numberOfEnroled).append(" alunos de '")
                            .append(entry.getKey().getName()).append("' de ").append(entry.getValue().size())
                            .append(" (teoricamente)").toString());
                    break;
                }
            }
            enrolStudentInAllShiftsFromDistribution(studentNumber, schoolClassDistribution, result, entry.getKey());
            numberOfEnroled++;
        }
    }

    private void enrolStudentInAllShiftsFromDistribution(Integer studentNumber,
            SchoolClassDistributionInformation schoolClassDistribution,
            Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> result, DegreeCurricularPlan degreeCurricularPlan) {

        for (final Shift shift : schoolClassDistribution.getShiftsToEnrol()) {
            List<GenericPair<DegreeCurricularPlan, Integer>> studentNumbers = result.get(shift);
            if (studentNumbers == null) {
                result.put(shift, studentNumbers = new ArrayList<GenericPair<DegreeCurricularPlan, Integer>>());
            }
            studentNumbers.add(new GenericPair<DegreeCurricularPlan, Integer>(degreeCurricularPlan, studentNumber));
        }
        schoolClassDistribution.decreaseCapacity();
    }

    private SchoolClassDistributionInformation getSchoolClassDistributionWithCapacity(
            List<SchoolClassDistributionInformation> schoolClassDistribution) {

        for (final SchoolClassDistributionInformation distributionInformation : schoolClassDistribution) {
            if (!distributionInformation.isDistributed()) {
                return distributionInformation;
            }
        }
        return null;
    }

    private Collection<Degree> readFirstYearFirstTimeValidDegrees() {
        final Collection<Degree> result = new ArrayList<Degree>();
        result.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE));
        result.addAll(Degree.readAllByDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
        return removeDegreesWithNoVacancy(result);
    }

    private Collection<Degree> removeDegreesWithNoVacancy(final Collection<Degree> result) {
        for (final String degreeCode : NO_VACANCY_DEGREE_CODES) {
            result.removeAll(Degree.readAllByDegreeCode(degreeCode));
        }
        return result;
    }

    protected Map<DegreeCurricularPlan, List<Integer>> generateAbstractStudentNumbers(int phase, List<String> errorLog) {

        final Collection<Degree> degrees = readFirstYearFirstTimeValidDegrees();

        int start = phase * degrees.size() * MAX_NUMBER_OF_STUDENTS;
        int maxLimit = start + MAX_NUMBER_OF_STUDENTS;
        final Map<DegreeCurricularPlan, List<Integer>> result = new HashMap<DegreeCurricularPlan, List<Integer>>();

        for (final Degree degree : degrees) {
            final DegreeCurricularPlan mostRecentDegreeCurricularPlan = degree.getMostRecentDegreeCurricularPlan();
            if (mostRecentDegreeCurricularPlan == null) {
                //Found NULL most recentDegreeCurricularPlan for
                errorLog.add(new StringBuilder("** O plano curricular do curso mais recente está a null para ").append(
                        degree.getSigla()).toString());
                continue;
            }
            for (; start <= maxLimit; start++) {
                addNumberToDegreeMap(result, mostRecentDegreeCurricularPlan, Integer.valueOf(start));
            }
            maxLimit += MAX_NUMBER_OF_STUDENTS;
        }
        return result;
    }

    private void addNumberToDegreeMap(Map<DegreeCurricularPlan, List<Integer>> map, DegreeCurricularPlan key, Integer value) {
        List<Integer> entry = map.get(key);
        if (entry == null) {
            map.put(key, entry = new ArrayList<Integer>());
        }
        entry.add(value);
    }

    private Map<DegreeCurricularPlan, List<SchoolClassDistributionInformation>> processInformationFrom(
            Collection<ShiftDistributionDTO> shiftDistributionFromFile, List<String> errorLog) {

        final Map<DegreeCurricularPlan, List<SchoolClassDistributionInformation>> result =
                new HashMap<DegreeCurricularPlan, List<SchoolClassDistributionInformation>>();
        Map<String, DegreeCurricularPlan> degreeCurricularPlans = new HashMap<String, DegreeCurricularPlan>();
        for (final ShiftDistributionDTO shiftDistributionDTO : shiftDistributionFromFile) {

            final DegreeCurricularPlan degreeCurricularPlan =
                    readDegreeCurricularPlanBy(shiftDistributionDTO.getDegreeCurricularPlanName(), degreeCurricularPlans);
            if (degreeCurricularPlan == null) {
                //Error reading degree with name
                errorLog.add(new StringBuilder("Erro ao ler o curso com o nome: '")
                        .append(shiftDistributionDTO.getDegreeCurricularPlanName()).append("' ignorando informação.").toString());
                continue;
            }

            final SchoolClass schoolClass = readSchoolClassFrom(degreeCurricularPlan, shiftDistributionDTO.getSchoolClassName());
            if (schoolClass == null) {
                errorLog.add(new StringBuilder("Erro ao ler aula com o nome: '")
                        .append(shiftDistributionDTO.getSchoolClassName()).append("' ignorando informação.").toString());
                continue;
            }

            final List<Shift> shiftsToEnrol = readShiftsToEnrolFrom(schoolClass, shiftDistributionDTO.getShiftNames(), errorLog);

            List<SchoolClassDistributionInformation> schoolClassInformation = result.get(degreeCurricularPlan);
            if (schoolClassInformation == null) {
                result.put(degreeCurricularPlan, schoolClassInformation = new ArrayList<SchoolClassDistributionInformation>());
            }

            schoolClassInformation.add(new SchoolClassDistributionInformation(schoolClass, shiftDistributionDTO
                    .getTemporarySchoolClass(), shiftDistributionDTO.getMaxCapacity(), shiftsToEnrol));
        }

        return result;
    }

    private List<Shift> readShiftsToEnrolFrom(SchoolClass schoolClass, List<String> shiftNames, List<String> errorLog) {
        final List<Shift> result = new ArrayList<Shift>();
        for (final String shiftName : shiftNames) {
            Shift shift = readShiftFrom(schoolClass, shiftName);
            if (shift == null) {
                errorLog.add(new StringBuilder("Não existe nenhum turno: '").append(shiftName).append("' associado à aula: '")
                        .append(schoolClass.getNome()).append("'.").toString());
                shift = readShiftByName(shiftName);
                if (shift == null) {
                    errorLog.add(new StringBuilder("Não existe o turno com o nome: '").append(shiftName)
                            .append("' na base de dados.").toString());
                    continue;
                }
            }
            result.add(shift);
        }
        return result;
    }

    private Shift readShiftByName(String shiftName) {
        for (final Shift shift : rootDomainObject.getShiftsSet()) {
            if (shift.getNome().equals(shiftName)) {
                return shift;
            }
        }
        return null;
    }

    private Shift readShiftFrom(SchoolClass schoolClass, String shiftName) {
        for (final Shift shift : schoolClass.getAssociatedShiftsSet()) {
            if (shift.getNome().equals(shiftName)) {
                return shift;
            }
        }
        return null;
    }

    private SchoolClass readSchoolClassFrom(DegreeCurricularPlan degreeCurricularPlan, String schoolClassName) {
        final ExecutionDegree executionDegree =
                degreeCurricularPlan.getExecutionDegreeByYear(ExecutionSemester.readActualExecutionSemester().getExecutionYear());
        if (executionDegree != null) {
            for (final SchoolClass schoolClass : executionDegree.getSchoolClassesSet()) {
                if (schoolClass.getAnoCurricular().equals(FIRST_CURRICULAR_YEAR)
                        && schoolClass.getExecutionPeriod() == ExecutionSemester.readActualExecutionSemester()
                        && schoolClass.getNome().equals(schoolClassName)) {
                    return schoolClass;
                }
            }
        }
        return null;
    }

    private DegreeCurricularPlan readDegreeCurricularPlanBy(String name, Map<String, DegreeCurricularPlan> degreeCurricularPlans) {

        DegreeCurricularPlan curricularPlan = degreeCurricularPlans.get(name);
        if (curricularPlan != null) {
            return curricularPlan;
        }

        for (final DegreeCurricularPlan degreeCurricularPlan : DegreeCurricularPlan.readNotEmptyDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getName().equals(name)) {
                degreeCurricularPlans.put(name, degreeCurricularPlan);
                return degreeCurricularPlan;
            }
        }
        return null;
    }

    protected void printAbstractStudentNumbers(Map<DegreeCurricularPlan, List<Integer>> abstractStudentNumbers) {
        final StringBuilder buffer = new StringBuilder();
        for (final Entry<DegreeCurricularPlan, List<Integer>> entry : abstractStudentNumbers.entrySet()) {
            buffer.append("[Plano curricular: ").append(entry.getKey().getName()).append(" -> (");
            final Iterator<Integer> studentNumbers = entry.getValue().iterator();
            while (studentNumbers.hasNext()) {
                buffer.append(studentNumbers.next()).append(studentNumbers.hasNext() ? ", " : ")");
            }
            buffer.append("\n");
        }
        //logger.info(buffer.toString());
    }

    protected void printProcessedInformation(
            Map<DegreeCurricularPlan, List<SchoolClassDistributionInformation>> processedInformation) {
        int numberOfEntries = 0;
        final StringBuilder buffer = new StringBuilder();
        for (final Entry<DegreeCurricularPlan, List<SchoolClassDistributionInformation>> entry : processedInformation.entrySet()) {
            buffer.append("[Plano curricular: ").append(entry.getKey().getName()).append(" (\n");
            for (final SchoolClassDistributionInformation information : entry.getValue()) {
                buffer.append(information.toString()).append("\n");
            }
            buffer.append(")]\n");
            numberOfEntries += entry.getValue().size();
        }
        buffer.append("Numero de entradas: " + numberOfEntries);
        //logger.info(buffer.toString());
    }

    protected void printShiftDistribution(Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> distribution) {
        final StringBuilder buffer = new StringBuilder();
        for (final Entry<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> entry : distribution.entrySet()) {
            buffer.append("[Turno: ").append(entry.getKey().getNome()).append(" (");
            int studentsPerLine = STUDENTS_PER_LINE;
            final Iterator<GenericPair<DegreeCurricularPlan, Integer>> studentNumbers = entry.getValue().iterator();
            while (studentNumbers.hasNext()) {
                buffer.append(studentNumbers.next().getRight());
                if (studentNumbers.hasNext()) {
                    buffer.append(", ");
                    studentsPerLine--;
                    if (studentsPerLine == 0) {
                        studentsPerLine = STUDENTS_PER_LINE;
                        buffer.append("\n").append("\t\t");
                    }
                }
            }
            buffer.append(")]\n");
        }
        buffer.append("Numero de Turnos: " + distribution.size());
        //logger.info(buffer.toString());
    }

    private Spreadsheet getStatisticsFromShiftDistribution(
            Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> distribution,
            Map<DegreeCurricularPlan, List<Integer>> abstractStudentNumbers) {

        final Spreadsheet spreadsheet = new Spreadsheet("Shifts");

        final List<DegreeCurricularPlan> sorted = new ArrayList<DegreeCurricularPlan>(abstractStudentNumbers.keySet());
        Collections.sort(sorted, new BeanComparator("name"));
        addHeader(spreadsheet, sorted);

        for (final Entry<Shift, Map<DegreeCurricularPlan, Integer>> shiftEntry : calculateStatistics(distribution,
                abstractStudentNumbers).entrySet()) {
            addRow(spreadsheet, shiftEntry, sorted);
        }
        return spreadsheet;
    }

    private void addHeader(Spreadsheet spreadsheet, List<DegreeCurricularPlan> degreeCurricularPlans) {
        int i = 0;
        spreadsheet.setHeader(i, "Turno");
        for (final DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            spreadsheet.setHeader(++i, degreeCurricularPlan.getName());
        }
    }

    private void addRow(Spreadsheet spreadsheet, Entry<Shift, Map<DegreeCurricularPlan, Integer>> shiftEntry,
            List<DegreeCurricularPlan> sorted) {
        final Row row = spreadsheet.addRow();

        int i = 0;
        row.setCell(i, shiftEntry.getKey().getNome());

        for (final DegreeCurricularPlan degreeCurricularPlan : sorted) {
            Integer count = shiftEntry.getValue().get(degreeCurricularPlan);
            count = (count != null) ? count : Integer.valueOf(0);
            row.setCell(++i, count.toString());
        }
    }

    private Map<Shift, Map<DegreeCurricularPlan, Integer>> calculateStatistics(
            Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> distribution,
            Map<DegreeCurricularPlan, List<Integer>> abstractStudentNumbers) {

        final Map<Shift, Map<DegreeCurricularPlan, Integer>> statistics =
                new HashMap<Shift, Map<DegreeCurricularPlan, Integer>>();
        for (final Entry<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> entry : distribution.entrySet()) {

            for (final GenericPair<DegreeCurricularPlan, Integer> studentNumber : entry.getValue()) {

                Map<DegreeCurricularPlan, Integer> degreeCurricularPlanMap = statistics.get(entry.getKey());
                if (degreeCurricularPlanMap == null) {
                    statistics.put(entry.getKey(), degreeCurricularPlanMap = new HashMap<DegreeCurricularPlan, Integer>());
                }
                Integer count = degreeCurricularPlanMap.get(studentNumber.getLeft());
                if (count == null) {
                    count = Integer.valueOf(0);
                }
                degreeCurricularPlanMap.put(studentNumber.getLeft(), Integer.valueOf(count.intValue() + 1));
            }
        }
        return statistics;
    }

    private class SchoolClassDistributionInformation {
        private final SchoolClass schoolClass;
        private final String temporarySchoolClassName;
        private int maxCapacity;
        private final List<Shift> shiftsToEnrol;

        SchoolClassDistributionInformation(SchoolClass schoolClass, String temporarySchoolClassName, int maxCapacity,
                List<Shift> shifts) {
            this.schoolClass = schoolClass;
            this.temporarySchoolClassName = temporarySchoolClassName;
            this.maxCapacity = maxCapacity;
            this.shiftsToEnrol = shifts;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public void decreaseCapacity() {
            maxCapacity = (--maxCapacity < 0) ? 0 : maxCapacity;
        }

        public boolean isDistributed() {
            return getMaxCapacity() == 0;
        }

        public SchoolClass getSchoolClass() {
            return schoolClass;
        }

        public List<Shift> getShiftsToEnrol() {
            return shiftsToEnrol;
        }

        public String getTemporarySchoolClassName() {
            return temporarySchoolClassName;
        }

        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder();
            buffer.append("[ S.C.D.I");
            buffer.append(" name: ").append(getSchoolClass().getNome());
            buffer.append(" tempSCName: ").append(getTemporarySchoolClassName());
            buffer.append(" capacity: ").append(getMaxCapacity()).append("\n\t(");
            final Iterator<Shift> shifts = getShiftsToEnrol().iterator();
            while (shifts.hasNext()) {
                buffer.append(shifts.next().getNome()).append(shifts.hasNext() ? ", " : "");
            }
            return buffer.append(")]").toString();
        }
    }

    private class ShiftDistributionDTO implements Serializable {

        private static final long serialVersionUID = 1L;
        private static final String COLUMN_SEPARATOR = "\t";

        private int maxCapacity;
        private String degreeCurricularPlanName, temporarySchoolClassName, schoolClassName;
        private List<String> shiftNames;

        public boolean fillWithFileLineData(String dataLine) {

            if (isToIgnoreLine(dataLine)) {
                return false;
            }

            String[] line = dataLine.split(COLUMN_SEPARATOR);

            if (line.length < 4) {
                logger.debug("Invalid line, ignoring it.");
                return false;
            }

            degreeCurricularPlanName = line[0].trim();
            temporarySchoolClassName = line[1].trim();
            maxCapacity = Integer.valueOf(line[2].trim()).intValue();
            schoolClassName = line[3].trim();

            shiftNames = new ArrayList<String>();
            for (int i = 4; i < line.length; i++) {
                if (line[i].trim().length() != 0) {
                    shiftNames.add(line[i].trim());
                } else {
                    logger.debug("found empty shift name");
                }
            }
            return true;
        }

        public String getDegreeCurricularPlanName() {
            return degreeCurricularPlanName;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public String getSchoolClassName() {
            return schoolClassName;
        }

        public List<String> getShiftNames() {
            return shiftNames;
        }

        public String getTemporarySchoolClass() {
            return temporarySchoolClassName;
        }

        private boolean isToIgnoreLine(String line) {
            return StringUtils.isEmpty(line) || line.startsWith("#") || line.equals("\n") || line.equals("\r\n")
                    || line.equals("\r") || line.startsWith("\t");
        }
    }
}
