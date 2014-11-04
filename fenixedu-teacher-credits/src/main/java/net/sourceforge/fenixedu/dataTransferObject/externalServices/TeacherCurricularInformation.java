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
package org.fenixedu.academic.dto.externalServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.QualificationType;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.personnelSection.contracts.GiafProfessionalData;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonProfessionalData;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalRegime;
import org.fenixedu.academic.domain.phd.InternalPhdParticipant;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.teacher.Career;
import org.fenixedu.academic.domain.teacher.DegreeTeachingService;
import org.fenixedu.academic.domain.teacher.DegreeTeachingServiceCorrection;
import org.fenixedu.academic.domain.teacher.OtherService;
import org.fenixedu.academic.domain.teacher.ProfessionalCareer;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeacherCurricularInformation implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TeacherCurricularInformation.class);

    protected Teacher teacher;
    protected Degree degree;
    protected SortedSet<ExecutionSemester> executionSemesters = new TreeSet<ExecutionSemester>(
            ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
    protected SortedSet<QualificationBean> qualificationBeans = new TreeSet<QualificationBean>(new ReverseComparator(
            new Comparator<QualificationBean>() {
                @Override
                public int compare(final QualificationBean o1, final QualificationBean o2) {
                    int compareType = o1.type.compareTo(o2.type);
                    if (compareType == 0) {
                        if (o1.year == null && o2.year == null) {
                            return 0;
                        } else if (o1.year == null) {
                            return -1;
                        } else if (o2.year == null) {
                            return 1;
                        }
                        return o1.year.compareTo(o2.year);
                    }
                    if ((o1.getType().equals(QualificationType.AGGREGATION) && o2.getType().isDoctorate())
                            || (o2.getType().equals(QualificationType.AGGREGATION) && o1.getType().isDoctorate())) {
                        return -compareType;
                    }
                    return compareType;
                }
            }));

    protected List<LecturedCurricularUnit> lecturedUCs = new ArrayList<LecturedCurricularUnit>();
    protected List<String> teacherPublications = new ArrayList<String>();

    public TeacherCurricularInformation(Teacher teacher, Degree degree, List<ExecutionSemester> executionSemester,
            List<String> list) {
        super();
        this.teacher = teacher;
        this.degree = degree;
        this.executionSemesters.addAll(executionSemester);
        for (Qualification qualification : teacher.getPerson().getAssociatedQualificationsSet()) {
            if (qualification.getType() != null
                    && (qualification.getType().isDegree() || qualification.getType().isMaster()
                            || qualification.getType().isDoctorate() || qualification.getType().equals(
                            QualificationType.AGGREGATION))) {
                this.qualificationBeans.add(new QualificationBean(qualification));
            }
        }
        if (list != null) {
            this.teacherPublications = list;
        }
        setlecturedUCs();
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public SortedSet<QualificationBean> getQualifications() {
        return qualificationBeans;
    }

    public void setQualifications(SortedSet<QualificationBean> qualificationBeans) {
        this.qualificationBeans = qualificationBeans;
    }

    public String getUnitName() {
        Department lastDepartment =
                getTeacher()
                        .getLatestTeacherAuthorizationInInterval(
                                new Interval(executionSemesters.first().getBeginDateYearMonthDay().toDateTimeAtMidnight(),
                                        executionSemesters.last().getEndDateYearMonthDay().plusDays(1).toDateTimeAtMidnight()))
                        .map(a -> a.getDepartment()).orElse(null);
        return lastDepartment == null ? null : lastDepartment.getName();
    }

    public String getProfessionalCategoryName() {
        ProfessionalCategory lastCategory =
                ProfessionalCategory.getLastCategory(getTeacher(), executionSemesters.first().getBeginDateYearMonthDay()
                        .toLocalDate(), executionSemesters.last().getEndDateYearMonthDay().toLocalDate());

        if (lastCategory != null) {
            if (lastCategory.getName().getContent().equalsIgnoreCase("Assistente")) {
                return "Assistente";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Assistente Convidado")) {
                return "Assistente convidado";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip. Assistente")
                    || lastCategory.getName().getContent().equalsIgnoreCase("Equiparado Assistente")) {
                return "Equiparado a Assistente";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip. Assistente Convidado")) {
                return "Equiparado a Assistente";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip. Prof. Auxiliar")
                    || lastCategory.getName().getContent().equalsIgnoreCase("Equiparado Professor Auxiliar")
                    || lastCategory.getName().getContent().equalsIgnoreCase("Professor Auxiliar")) {
                return "Professor Auxiliar";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip.Assistente Estagiario")) {
                return "Assistente Estagiário";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip.Monitor S/Lic")
                    || lastCategory.getName().getContent().equalsIgnoreCase("Monitor-E.C.D.U. c/Licenciatura")
                    || lastCategory.getName().getContent().equalsIgnoreCase("Monitor-E.C.D.U.s/Licenciatura")) {
                return "Monitor";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip.Prof.Auxiliar Convidado")
                    || lastCategory.getName().getContent().equalsIgnoreCase("Prof Auxiliar Convidado")) {
                return "Professor Auxiliar convidado";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Professor Associado")) {
                return "Professor Associado";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Professor Associado Convidado")) {
                return "Professor Associado convidado";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Professor Catedrático")) {
                return "Professor Catedrático";
            } else if (lastCategory.getName().getContent().equalsIgnoreCase("Professor Catedrático Convidado")) {
                return "Professor Catedrático convidado";
            }
            logger.debug("Invalid category: " + lastCategory.getName().getContent());
        }
        return lastCategory == null ? null : lastCategory.getName().getContent();
    }

    public Float getProfessionalRegimeTime() {
        float maxRegimeTime = 100f;
        PersonProfessionalData personProfessionalData = getTeacher().getPerson().getPersonProfessionalData();
        ProfessionalRegime lastProfessionalRegime = null;
        if (personProfessionalData != null) {
            GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalData();
            if (giafProfessionalData != null) {
                lastProfessionalRegime =
                        personProfessionalData.getLastProfessionalRegime(giafProfessionalData, executionSemesters.first()
                                .getBeginDateYearMonthDay().toLocalDate(), executionSemesters.last().getEndDateYearMonthDay()
                                .toLocalDate());
            }
        }
        if (lastProfessionalRegime != null) {
            String regime = StringNormalizer.normalize(lastProfessionalRegime.getName().getContent());
            if (regime.matches(".*?" + StringNormalizer.normalize("Tempo Integral") + ".*?")) {
                return maxRegimeTime;
            } else if (regime.matches(".*?" + StringNormalizer.normalize("Exclusividade") + ".*?")) {
                return maxRegimeTime;
            } else if (regime.matches(".*?" + StringNormalizer.normalize("Tempo Parcial") + ".*?")) {
                Integer weighting = lastProfessionalRegime.getWeighting();
                if (weighting != null) {
                    return Math.min(maxRegimeTime, weighting);
                }
            }
        }
        Float authorizationRegimeTime = getAuthorizationRegimeTime(executionSemesters.last());
        if (authorizationRegimeTime == 0f) {
            authorizationRegimeTime = getAuthorizationRegimeTime(executionSemesters.first());
        }

        return authorizationRegimeTime;
    }

    protected Float getAuthorizationRegimeTime(ExecutionSemester executionSemester) {
        return getTeacher().getTeacherAuthorization(executionSemester.getAcademicInterval())
                .map(a -> new Float(Math.round(a.getLessonHours() * 100 / 12))).orElse(0f);
    }

    public List<String> getTop5ResultParticipation() {
        return addUntil5Elements(teacherPublications);
    }

    public List<String> getTop5ProfessionalDevelomentActivities() {
        return addUntil5Elements(new ArrayList<String>());
    }

    public List<String> getTop5ProfessionalCareer() {
        List<String> result = new ArrayList<String>();
        List<ProfessionalCareer> sorted = new ArrayList<ProfessionalCareer>();
        for (Career career : teacher.getPerson().getAssociatedCareersSet()) {
            if (career instanceof ProfessionalCareer) {
                sorted.add((ProfessionalCareer) career);
            }
        }
        Collections.sort(sorted, ProfessionalCareer.CAREER_DATE_COMPARATOR);
        for (ProfessionalCareer professionalCareer : sorted.subList(0, Math.min(5, sorted.size()))) {
            result.add(professionalCareer.getBeginYear()
                    + (professionalCareer.getEndYear() != null ? " - " + professionalCareer.getEndYear() : "") + " "
                    + professionalCareer.getFunction() + " (" + professionalCareer.getEntity() + ")");
        }
        return addUntil5Elements(result);
    }

    private List<String> addUntil5Elements(List<String> top5) {
        for (int i = top5.size(); i < 5; i++) {
            top5.add(StringUtils.EMPTY);
        }
        return top5;
    }

    private static String formatParticipant(String name) {
        String[] parts = name.split("\\s+");
        return name.charAt(0) + ". " + parts[parts.length - 1];
    }

    private String filter(String... text) {
        Set<String> parts = new HashSet<String>();
        for (String string : text) {
            if (StringUtils.isNotBlank(string)) {
                parts.add(string.replace('\n', ' ').replace('\r', ' ').trim());
            }
        }
        return StringUtils.join(parts, ", ");
    }

    private String join(String journal, String volume, Integer year) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(journal)) {
            builder.append(journal);
        }
        if (StringUtils.isNotBlank(volume)) {
            builder.append(" - " + volume);
        }
        if (year != null) {
            builder.append(" " + year);
        }
        return builder.toString();
    }

    private void insert(List<String> publication, Integer part) {
        if (part != null) {
            publication.add(Integer.toString(part));
        }
    }

    private void insert(List<String> publication, String part) {
        if (StringUtils.isNotBlank(part)) {
            publication.add(part);
        }
    }

    public void setlecturedUCs() {
        for (ExecutionSemester executionSemester : executionSemesters) {
            for (Professorship professorship : teacher.getProfessorships(executionSemester)) {
                getLecturedCurricularUnitForProfessorship(professorship, executionSemester);
            }
            for (ThesisEvaluationParticipant thesisEvaluationParticipant : teacher.getPerson().getThesisEvaluationParticipants(
                    executionSemester)) {
                final ThesisParticipationType type = thesisEvaluationParticipant.getType();
                if (type == ThesisParticipationType.ORIENTATOR || type == ThesisParticipationType.COORIENTATOR) {
                    ExecutionCourse executionCourse =
                            thesisEvaluationParticipant.getThesis().getEnrolment().getExecutionCourseFor(executionSemester);
                    if (executionCourse != null) {
                        addLecturedCurricularUnit(executionCourse.getDegreePresentationString(), executionCourse.getName(), "OT",
                                (float) 0);
                    }
                }
            }
            if (degree.getPhdProgram() != null) {
                for (InternalPhdParticipant participant : teacher.getPerson().getInternalParticipantsSet()) {
                    PhdIndividualProgramProcess phdIndividualProgramProcess = participant.getIndividualProcess();
                    if (phdIndividualProgramProcess.getPhdProgram().equals(degree.getPhdProgram())
                            && phdIndividualProgramProcess.isActive(executionSemester.getAcademicInterval().toInterval())
                            && phdIndividualProgramProcess.isGuiderOrAssistentGuider(teacher.getPerson())
                            && PersonProfessionalData.isTeacherActiveOrHasAuthorizationForSemester(teacher, executionSemester)) {
                        addLecturedCurricularUnit(phdIndividualProgramProcess.getPhdProgram().getAcronym(), "Dissertação", "OT",
                                (float) 0);
                    }
                }
            }
        }
        return;
    }

    public List<LecturedCurricularUnit> getLecturedUCs() {
        Collections.sort(lecturedUCs);
        return lecturedUCs;
    }

    public String getDegreeSiglas(ExecutionCourse executionCourse) {
        Set<String> degreeSiglas = new HashSet<String>();
        for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            degreeSiglas.add(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
        }
        return StringUtils.join(degreeSiglas, ", ");
    }

    protected void getLecturedCurricularUnitForProfessorship(Professorship professorship, ExecutionSemester executionSemester) {
        Map<String, Float> hoursByTypeMap = new HashMap<String, Float>();
        TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
        final StringBuilder shiftTypeDescription = new StringBuilder();
        if (teacherService != null) {
            List<DegreeTeachingService> degreeTeachingServices =
                    teacherService.getDegreeTeachingServiceByProfessorship(professorship);
            for (DegreeTeachingService degreeTeachingService : degreeTeachingServices) {
                for (CourseLoad courseLoad : degreeTeachingService.getShift().getCourseLoadsSet()) {
                    final ShiftType type = courseLoad.getType();
                    appendShiftType(shiftTypeDescription, type);
                    Float duration = hoursByTypeMap.get(StringUtils.EMPTY);
                    Float weeklyHours =
                            courseLoad.getTotalQuantity().floatValue()
                                    * (degreeTeachingService.getPercentage().floatValue() / 100);
                    hoursByTypeMap.put(StringUtils.EMPTY, duration == null ? weeklyHours : duration + weeklyHours);
                }
            }

            for (OtherService otherService : teacherService.getOtherServices()) {
                if (otherService instanceof DegreeTeachingServiceCorrection) {
                    DegreeTeachingServiceCorrection degreeTeachingServiceCorrection =
                            (DegreeTeachingServiceCorrection) otherService;
                    if (degreeTeachingServiceCorrection.getProfessorship().equals(professorship)
                            && (!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse().isDissertation())
                            && (!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse()
                                    .getProjectTutorialCourse())) {
                        Float duration = hoursByTypeMap.get(StringUtils.EMPTY);
                        Float weeklyHours = degreeTeachingServiceCorrection.getCorrection().floatValue();
                        hoursByTypeMap.put(StringUtils.EMPTY, duration == null ? weeklyHours : duration + weeklyHours);
                    }
                }
            }

        }
        String name = professorship.getExecutionCourse().getName();
        if (hoursByTypeMap.isEmpty()) {
            addLecturedCurricularUnit(professorship.getExecutionCourse().getDegreePresentationString(), name, "O", (float) 0);
        } else {
            for (String shiftType : hoursByTypeMap.keySet()) {
                addLecturedCurricularUnit(professorship.getExecutionCourse().getDegreePresentationString(), name, /*shiftType*/
                        shiftTypeDescription.toString(), hoursByTypeMap.get(StringUtils.EMPTY));
            }
        }
    }

    private void appendShiftType(final StringBuilder builder, final ShiftType type) {
        if (type != null) {
            final String t = convertShiftType(type);
            if (t != null && !t.isEmpty()) {
                String string = builder.toString();
                if (!string.equals(t) && !string.startsWith(t + ',') && !string.endsWith("," + t)) {
                    if (!string.isEmpty()) {
                        builder.append(',');
                    }
                    builder.append(t);
                }
            }
        }
    }

    private void addLecturedCurricularUnit(String degree, String name, String shiftType, Float hours) {
        for (LecturedCurricularUnit lecturedCurricularUnit : lecturedUCs) {
            if (lecturedCurricularUnit.getName().equals(name) && lecturedCurricularUnit.getOriginalShiftType().equals(shiftType)) {
                lecturedUCs.remove(lecturedCurricularUnit);
                lecturedCurricularUnit.addHours(hours);
                lecturedCurricularUnit.addDegree(degree);
                lecturedUCs.add(lecturedCurricularUnit);
                return;
            }
        }
        lecturedUCs.add(new LecturedCurricularUnit(degree, name, shiftType, hours));
    }

    public class QualificationBean {
        protected QualificationType type;
        protected String degree;
        protected String scientificArea;
        protected Integer year;
        protected String institution;
        protected String classification;

        public QualificationBean(Qualification qualification) {
            type = qualification.getType();
            degree = qualification.getType() != null ? qualification.getType().getLocalizedName().trim() : "Sem Grau";
            if (qualification.getType().name().startsWith("DOCTORATE_DEGREE")) {
                degree = QualificationType.DOCTORATE_DEGREE.getLocalizedName();
            }
            scientificArea = qualification.getDegree();
            year = qualification.getYear() != null ? Integer.parseInt(qualification.getYear()) : null;
            institution = qualification.getSchool();
            classification = qualification.getMark() != null ? qualification.getMark() : "-";
        }

        public QualificationType getType() {
            return type;
        }

        public String getDegree() {
            return degree;
        }

        public String getScientificArea() {
            return scientificArea;
        }

        public Integer getYear() {
            return year;
        }

        public String getInstitution() {
            return institution;
        }

        public String getClassification() {
            return classification;
        }

    }

    private static String convertShiftType(final ShiftType shiftType) {
        if (shiftType == null) {
            return "0";
        }
        if (shiftType == ShiftType.LABORATORIAL) {
            return "PL";
        }
        if (shiftType == ShiftType.PROBLEMS) {
            return "TP";
        }
        return shiftType.getSiglaTipoAula();
    }

    public class LecturedCurricularUnit implements Comparable<LecturedCurricularUnit> {
        protected Set<String> degrees = new HashSet<String>();
        protected String name;
        protected String shiftType;
        protected float hours;

        public LecturedCurricularUnit(String degree, String name, String shiftType, float hoursValue) {
            addDegree(degree);
            this.name = name;
            this.shiftType = shiftType;
            this.hours = hoursValue;
        }

        public void addDegree(String degrees) {
            for (String thisDegree : degrees.split(",")) {
                this.degrees.add(thisDegree.trim());
            }
        }

        public void addHours(Float hours) {
            this.hours = this.hours + hours;
        }

        protected String getOriginalShiftType() {
            return shiftType;
        }

        public String getShiftType() {
            if (shiftType == null) {
                return null;
            } else if (shiftType.equals("T") || shiftType.equals("TP") || shiftType.equals("TC") || shiftType.equals("S")
                    || shiftType.equals("E") || shiftType.equals("OT") || shiftType.equals("PL")) {
                return shiftType;
            } else if (shiftType.equals("L")) {
                return "PL";
            } else if (shiftType.equals("PB")) {
                return "TP";
            } else if (shiftType.equals(StringUtils.EMPTY)) {
                return StringUtils.EMPTY;
            } else if (shiftType.indexOf(',') > 0) {
                return shiftType;
            }
            return "O";
        }

        public String getName() {
            return name;
        }

        public float getHours() {
            return ((float) Math.round(hours * 100)) / 100;
        }

        public String getDegree() {
            return StringUtils.join(degrees, ", ");
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof LecturedCurricularUnit) {
                LecturedCurricularUnit o = (LecturedCurricularUnit) obj;
                return equal(getDegree(), o.getDegree()) && equal(getName(), o.getName())
                        && equal(getShiftType(), o.getShiftType()) && equal(getHours(), o.getHours());
            }
            return false;
        }

        protected boolean equal(Object obj1, Object obj2) {
            if (obj1 == null && obj2 == null) {
                return true;
            }
            if (obj1 == null || obj2 == null) {
                return false;
            }
            return obj1.equals(obj2);
        }

        @Override
        public int hashCode() {
            return getDegree().hashCode() + getName().hashCode() + (getShiftType() != null ? getShiftType().hashCode() : 0)
                    + (int) getHours();
        }

        @Override
        public int compareTo(LecturedCurricularUnit o) {
            String sigla = TeacherCurricularInformation.this.degree.getSigla();
            if (getDegree().matches("(.*, )*" + sigla + "(, .*)*") && !o.getDegree().matches("(.*, )*" + sigla + "(, .*)*")
                    && getHours() != 0) {
                return -1;
            } else if ((!getDegree().matches("(.*, )*" + sigla + "(, .*)*"))
                    && o.getDegree().matches("(.*, )*" + sigla + "(, .*)*") && o.getHours() != 0) {
                return 1;
            }
            return ((Float) o.getHours()).compareTo(getHours());
        }
    }

}
