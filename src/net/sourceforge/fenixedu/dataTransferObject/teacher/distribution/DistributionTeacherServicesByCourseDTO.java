package net.sourceforge.fenixedu.dataTransferObject.teacher.distribution;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

/**
 * amak, jpmsit
 * 
 */
public class DistributionTeacherServicesByCourseDTO extends DataTranferObject {

    public class TeacherExecutionCourseServiceDTO {
	private Integer teacherIdInternal;

	private String teacherName;

	private Integer hoursSpentByTeacher;

	private Boolean teacherOfDepartment;

	public TeacherExecutionCourseServiceDTO(Integer teacherIdInternal, String teacherName, Integer hoursSpentByTeacher,
		Boolean teacherOfDepartment) {
	    this.teacherIdInternal = teacherIdInternal;
	    this.teacherName = teacherName;
	    this.hoursSpentByTeacher = hoursSpentByTeacher;
	    this.teacherOfDepartment = teacherOfDepartment;

	}

	public Integer getHoursSpentByTeacher() {
	    return hoursSpentByTeacher;
	}

	public Integer getTeacherIdInternal() {
	    return teacherIdInternal;
	}

	public String getTeacherName() {
	    return teacherName;
	}

	public String getDescription() {
	    StringBuilder finalString = new StringBuilder(teacherName);

	    finalString.append(" - ");
	    finalString.append(hoursSpentByTeacher);

	    return finalString.toString();

	}

	public Boolean getTeacherOfDepartment() {
	    return teacherOfDepartment;
	}

    }

    public class ExecutionCourseDistributionServiceEntryDTO implements Comparable {
	private Integer executionCourseIdInternal;

	private String executionCourseName;

	private String executionCourseCampus;

	private Set<String> executionCourseDegreeList;

	private Set<String> executionCourseCurricularYearsList;

	private Integer executionCourseSemester;

	private Integer executionCourseFirstTimeEnrollementStudentNumber;

	private Integer executionCourseSecondTimeEnrollementStudentNumber;

	private Double executionCourseTheoreticalHours;

	private Double executionCoursePraticalHours;

	private Double executionCourseLaboratorialHours;

	private Double executionCourseTheoPratHours;

	private Integer executionCourseTotalHoursLecturedByTeachers;

	private Double executionCourseStudentsNumberByTheoreticalShift;

	private Double executionCourseStudentsNumberByPraticalShift;

	private Double executionCourseStudentsNumberByLaboratorialShift;

	private Double executionCourseStudentsNumberByTheoPraticalShift;

	private List<TeacherExecutionCourseServiceDTO> teacherExecutionCourseServiceList;

	public ExecutionCourseDistributionServiceEntryDTO(Integer executionCourseIdInternal, String executionCourseName,
		String executionCourseCampus, String executionCourseDegreeName, Set<String> executionCourseCurricularYearsList,
		Integer executionCourseSemester, Integer executionCourseFirstTimeEnrollementStudentNumber,
		Integer executionCourseSecondTimeEnrollementStudentNumber, Double executionCourseTheoreticalHours,
		Double executionCoursePraticalHours, Double executionCourseLaboratorialHours,
		Double executionCourseTheoPratHours, Double executionCourseStudentsNumberByTheoreticalShift,
		Double executionCourseStudentsNumberByPraticalShift, Double executionCourseStudentsNumberByLaboratorialShift,
		Double executionCourseStudentsNumberByTheoPraticalShift) {

	    this.executionCourseIdInternal = executionCourseIdInternal;
	    this.executionCourseName = executionCourseName;
	    this.executionCourseCampus = executionCourseCampus;
	    this.executionCourseCurricularYearsList = executionCourseCurricularYearsList;
	    this.executionCourseSemester = executionCourseSemester;
	    this.executionCourseFirstTimeEnrollementStudentNumber = executionCourseFirstTimeEnrollementStudentNumber;
	    this.executionCourseSecondTimeEnrollementStudentNumber = executionCourseSecondTimeEnrollementStudentNumber;
	    this.executionCourseTheoreticalHours = executionCourseTheoreticalHours;
	    this.executionCoursePraticalHours = executionCoursePraticalHours;
	    this.executionCourseTheoPratHours = executionCourseTheoPratHours;

	    teacherExecutionCourseServiceList = new ArrayList<TeacherExecutionCourseServiceDTO>();

	    this.executionCourseDegreeList = new HashSet<String>();

	    this.executionCourseDegreeList.add(executionCourseDegreeName);

	    this.executionCourseTotalHoursLecturedByTeachers = new Integer(0);

	    this.executionCourseLaboratorialHours = executionCourseLaboratorialHours;
	    ;

	    this.executionCourseStudentsNumberByTheoreticalShift = executionCourseStudentsNumberByTheoreticalShift;
	    this.executionCourseStudentsNumberByPraticalShift = executionCourseStudentsNumberByPraticalShift;
	    this.executionCourseStudentsNumberByLaboratorialShift = executionCourseStudentsNumberByLaboratorialShift;
	    this.executionCourseStudentsNumberByTheoPraticalShift = executionCourseStudentsNumberByTheoPraticalShift;

	}

	public String getExecutionCourseCampus() {
	    return executionCourseCampus;
	}

	public Set<String> getExecutionCourseCurricularYear() {
	    return executionCourseCurricularYearsList;
	}

	public Set<String> getExecutionCourseDegreeName() {
	    return executionCourseDegreeList;
	}

	public Integer getExecutionCourseFirstTimeEnrollementStudentNumber() {
	    return executionCourseFirstTimeEnrollementStudentNumber;
	}

	public Integer getExecutionCourseIdInternal() {
	    return executionCourseIdInternal;
	}

	public Integer getExecutionCourseTotalHoursLecturedByTeachers() {
	    return executionCourseTotalHoursLecturedByTeachers;
	}

	public String getExecutionCourseName() {
	    return executionCourseName;
	}

	public Double getExecutionCoursePraticalHours() {
	    return executionCoursePraticalHours;
	}

	public Integer getExecutionCourseSecondTimeEnrollementStudentNumber() {
	    return executionCourseSecondTimeEnrollementStudentNumber;
	}

	public Integer getExecutionCourseSemester() {
	    return executionCourseSemester;
	}

	public Double getExecutionCourseTheoreticalHours() {
	    return executionCourseTheoreticalHours;
	}

	public List<TeacherExecutionCourseServiceDTO> getTeacherExecutionCourseServiceList() {
	    return teacherExecutionCourseServiceList;
	}

	public Integer getExecutionCourseStudentsTotalNumber() {
	    return executionCourseFirstTimeEnrollementStudentNumber + executionCourseSecondTimeEnrollementStudentNumber;
	}

	public Double getExecutionCourseTotalHours() {
	    return executionCoursePraticalHours + executionCourseTheoreticalHours + executionCourseTheoPratHours
		    + executionCourseLaboratorialHours;
	}

	public void addTeacher(TeacherExecutionCourseServiceDTO teacher) {
	    teacherExecutionCourseServiceList.add(teacher);
	}

	public void addToCourseDegreesList(String degreeName) {
	    executionCourseDegreeList.add(degreeName);
	}

	public void addToCurricularYears(Set<String> curricularYearsList) {
	    executionCourseCurricularYearsList.addAll(curricularYearsList);
	}

	public Set<String> getExecutionCourseCurricularYearsList() {
	    return executionCourseCurricularYearsList;
	}

	public Set<String> getExecutionCourseDegreeList() {
	    return executionCourseDegreeList;
	}

	public void updateHoursLecturedByTeachers(Integer hoursSpentByTeacher) {
	    this.executionCourseTotalHoursLecturedByTeachers += hoursSpentByTeacher;
	}

	public Double getExecutionCourseHoursBalance() {
	    return getExecutionCourseTotalHours() - executionCourseTotalHoursLecturedByTeachers;
	}

	public Double getExecutionCourseTheoPratHours() {
	    return executionCourseTheoPratHours;
	}

	public String getFormattedExecutionCourseTheoPratHours() {

	    return getFormattedValue(getExecutionCourseTheoPratHours());
	}

	public String getFormattedExecutionCoursePraticalHours() {

	    return getFormattedValue(getExecutionCoursePraticalHours());
	}

	public String getFormattedExecutionCourseLaboratorialHours() {

	    return getFormattedValue(getExecutionCourseLaboratorialHours());
	}

	public String getFormattedExecutionCourseTheoreticalHours() {
	    return getFormattedValue(getExecutionCourseTheoreticalHours());
	}

	private String getFormattedValue(Double value) {
	    StringBuilder sb = new StringBuilder();
	    Formatter formatter = new Formatter(sb);

	    formatter.format("%.1f", value);
	    return sb.toString();
	}

	public Double getExecutionCourseLaboratorialHours() {
	    return executionCourseLaboratorialHours;
	}

	public Double getExecutionCourseStudentsNumberByLaboratorialShift() {
	    return executionCourseStudentsNumberByLaboratorialShift;
	}

	public Double getExecutionCourseStudentsNumberByPraticalShift() {
	    return executionCourseStudentsNumberByPraticalShift;
	}

	public Double getExecutionCourseStudentsNumberByTheoPraticalShift() {
	    return executionCourseStudentsNumberByTheoPraticalShift;
	}

	public Double getExecutionCourseStudentsNumberByTheoreticalShift() {
	    return executionCourseStudentsNumberByTheoreticalShift;
	}

	public String getFormattedExecutionCourseStudentsNumberByLaboratorialShift() {
	    return getFormattedValue(getExecutionCourseStudentsNumberByLaboratorialShift());
	}

	public String getFormattedExecutionCourseStudentsNumberByPraticalShift() {
	    return getFormattedValue(getExecutionCourseStudentsNumberByPraticalShift());
	}

	public String getFormattedExecutionCourseStudentsNumberByTheoPraticalShift() {
	    return getFormattedValue(getExecutionCourseStudentsNumberByTheoPraticalShift());
	}

	public String getFormattedExecutionCourseStudentsNumberByTheoreticalShift() {
	    return getFormattedValue(getExecutionCourseStudentsNumberByTheoreticalShift());
	}

	public int compareTo(Object obj) {
	    if (obj instanceof ExecutionCourseDistributionServiceEntryDTO) {
		ExecutionCourseDistributionServiceEntryDTO executionCourse1 = (ExecutionCourseDistributionServiceEntryDTO) obj;
		return this.getExecutionCourseName().compareTo(executionCourse1.getExecutionCourseName());
	    }
	    return 0;
	}

	public String getDegreeListString() {

	    StringBuilder finalString = new StringBuilder();
	    String[] stringArrayDegrees = (String[]) executionCourseDegreeList.toArray(new String[] {});

	    if (stringArrayDegrees.length > 0) {
		finalString.append(stringArrayDegrees[0]);

		for (int i = 1; i < stringArrayDegrees.length; i++) {
		    finalString.append(", ");
		    finalString.append(stringArrayDegrees[i]);
		}
	    }
	    return finalString.toString();
	}

	public String getCurricularYearListString() {
	    StringBuilder finalString = new StringBuilder();
	    String[] stringArrayYears = (String[]) executionCourseCurricularYearsList.toArray(new String[] {});

	    if (stringArrayYears.length > 0) {
		finalString.append(stringArrayYears[0]);

		for (int i = 1; i < stringArrayYears.length; i++) {
		    finalString.append(", ");
		    finalString.append(stringArrayYears[i]);
		}
	    }
	    return finalString.toString();
	}
    }

    private Map<Integer, ExecutionCourseDistributionServiceEntryDTO> executionCourseMap;

    public DistributionTeacherServicesByCourseDTO() {
	executionCourseMap = new HashMap<Integer, ExecutionCourseDistributionServiceEntryDTO>();
    }

    public void addExecutionCourse(Integer executionCourseIdInternal, String executionCourseName, String executionCourseCampus,
	    String executionCourseDegreeName, Set<String> executionCourseCurricularYear, Integer executionCourseSemester,
	    Integer executionCourseFirstTimeEnrollementStudentNumber, Integer executionCourseSecondTimeEnrollementStudentNumber,
	    Double executionCourseTheoreticalHours, Double executionCoursePraticalHours, Double executionCourseLaboratorialHours,
	    Double executionCourseTheoPratHours, Double executionCourseStudentsNumberByTheoreticalShift,
	    Double executionCourseStudentsNumberByPraticalShift, Double executionCourseStudentsNumberByLaboratorialShift,
	    Double executionCourseStudentsNumberByTheoPraticalShift) {
	ExecutionCourseDistributionServiceEntryDTO t = new ExecutionCourseDistributionServiceEntryDTO(executionCourseIdInternal,
		executionCourseName, executionCourseCampus, executionCourseDegreeName, executionCourseCurricularYear,
		executionCourseSemester, executionCourseFirstTimeEnrollementStudentNumber,
		executionCourseSecondTimeEnrollementStudentNumber, executionCourseTheoreticalHours, executionCoursePraticalHours,
		executionCourseLaboratorialHours, executionCourseTheoPratHours, executionCourseStudentsNumberByTheoreticalShift,
		executionCourseStudentsNumberByPraticalShift, executionCourseStudentsNumberByLaboratorialShift,
		executionCourseStudentsNumberByTheoPraticalShift);

	if (!executionCourseMap.containsKey(executionCourseIdInternal)) {
	    executionCourseMap.put(executionCourseIdInternal, t);
	}
    }

    public void addTeacherToExecutionCourse(Integer keyExecutionCourse, Integer idInternalTeacher, String name,
	    Integer hoursSpentByTeacher, boolean teacherBelongsToDepartment) {
	TeacherExecutionCourseServiceDTO teacher = new TeacherExecutionCourseServiceDTO(idInternalTeacher, name,
		hoursSpentByTeacher, teacherBelongsToDepartment);

	ExecutionCourseDistributionServiceEntryDTO executionCourseEntry = executionCourseMap.get(keyExecutionCourse);
	executionCourseEntry.updateHoursLecturedByTeachers(hoursSpentByTeacher);
	executionCourseEntry.addTeacher(teacher);
    }

    public Map<Integer, ExecutionCourseDistributionServiceEntryDTO> getExecutionCourseMap() {
	return executionCourseMap;
    }

    public void addDegreeNameToExecutionCourse(Integer keyExecutionCourse, String degreeName) {

	executionCourseMap.get(keyExecutionCourse).addToCourseDegreesList(degreeName);
	return;

    }

    public void addCurricularYearsToExecutionCourse(Integer keyExecutionCourse, Set<String> curricularYearsList) {

	executionCourseMap.get(keyExecutionCourse).addToCurricularYears(curricularYearsList);
    }

}
