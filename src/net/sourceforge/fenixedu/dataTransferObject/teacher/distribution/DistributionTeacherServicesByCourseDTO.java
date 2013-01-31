package net.sourceforge.fenixedu.dataTransferObject.teacher.distribution;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * amak, jpmsit
 * 
 */
public class DistributionTeacherServicesByCourseDTO extends DataTranferObject {

	public class TeacherExecutionCourseServiceDTO {
		private Integer teacherIdInternal;

		private String teacherName;

		private Double hoursSpentByTeacher;

		private Duration timeSpentByTeacher;

		private Boolean teacherOfDepartment;

		public TeacherExecutionCourseServiceDTO(Integer teacherIdInternal, String teacherName, Double hoursSpentByTeacher,
				Duration timeSpentByTeacher, Boolean teacherOfDepartment) {
			this.teacherIdInternal = teacherIdInternal;
			this.teacherName = teacherName;
			this.hoursSpentByTeacher = hoursSpentByTeacher;
			this.timeSpentByTeacher = timeSpentByTeacher;
			this.teacherOfDepartment = teacherOfDepartment;

		}

		public Double getHoursSpentByTeacher() {
			return hoursSpentByTeacher;
		}

		public Duration getTimeSpentByTeacher() {
			return timeSpentByTeacher;
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
			PeriodFormatter periodFormatter =
					new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendSuffix(":")
							.appendMinutes().toFormatter();
			finalString.append(periodFormatter.print(getTimeSpentByTeacher().toPeriod()));
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

		private Duration totalDuration;

		private Double executionCourseTheoreticalHours;

		private Double executionCoursePraticalHours;

		private Double executionCourseLaboratorialHours;

		private Double executionCourseTheoPratHours;

		private Double executionCourseSeminaryHours;
		private Double executionCourseProblemsHours;
		private Double executionCourseTutorialOrientationHours;

		private Double executionCourseFieldWorkHours;
		private Double executionCourseTrainingPeriodHours;

		private Double executionCourseTotalHoursLecturedByTeachers;

		private Duration executionCourseTotalTimeLecturedByTeachers;

		private Double executionCourseStudentsNumberByTheoreticalShift;

		private Double executionCourseStudentsNumberByPraticalShift;

		private Double executionCourseStudentsNumberByLaboratorialShift;

		private Double executionCourseStudentsNumberByTheoPraticalShift;

		private Double executionCourseStudentsNumberBySeminaryShift;
		private Double executionCourseStudentsNumberByProblemsShift;
		private Double executionCourseStudentsNumberByTutorialOrientationShift;
		private Double executionCourseStudentsNumberByFieldWorkShift;
		private Double executionCourseStudentsNumberByTrainingPeriodShift;

		private List<TeacherExecutionCourseServiceDTO> teacherExecutionCourseServiceList;

		public ExecutionCourseDistributionServiceEntryDTO(Integer executionCourseIdInternal, String executionCourseName,
				String executionCourseCampus, String executionCourseDegreeName, Set<String> executionCourseCurricularYearsList,
				Integer executionCourseSemester, Integer executionCourseFirstTimeEnrollementStudentNumber,
				Integer executionCourseSecondTimeEnrollementStudentNumber, Duration totalDuration,
				Double executionCourseTheoreticalHours, Double executionCoursePraticalHours,
				Double executionCourseLaboratorialHours, Double executionCourseTheoPratHours,
				Double executionCourseSeminaryHours, Double executionCourseProblemsHours,
				Double executionCourseTutorialOrientationHours, Double executionCourseFieldWorkHours,
				Double executionCourseTrainingPeriodHours, Double executionCourseStudentsNumberByTheoreticalShift,
				Double executionCourseStudentsNumberByPraticalShift, Double executionCourseStudentsNumberByLaboratorialShift,
				Double executionCourseStudentsNumberByTheoPraticalShift, Double seminaryStudentsNumberPerShift,
				Double problemsStudentsNumberPerShift, Double tutorialOrientationStudentsNumberPerShift,
				Double fieldWorkStudentsNumberPerShift, Double trainingPeriodStudentsNumberPerShift) {

			this.executionCourseIdInternal = executionCourseIdInternal;
			this.executionCourseName = executionCourseName;
			this.executionCourseCampus = executionCourseCampus;
			this.executionCourseCurricularYearsList = executionCourseCurricularYearsList;
			this.executionCourseSemester = executionCourseSemester;
			this.executionCourseFirstTimeEnrollementStudentNumber = executionCourseFirstTimeEnrollementStudentNumber;
			this.executionCourseSecondTimeEnrollementStudentNumber = executionCourseSecondTimeEnrollementStudentNumber;
			this.executionCourseTheoreticalHours = executionCourseTheoreticalHours;
			this.totalDuration = totalDuration;
			this.executionCoursePraticalHours = executionCoursePraticalHours;
			this.executionCourseTheoPratHours = executionCourseTheoPratHours;
			this.executionCourseSeminaryHours = executionCourseSeminaryHours;
			this.executionCourseProblemsHours = executionCourseProblemsHours;
			this.executionCourseTutorialOrientationHours = executionCourseTutorialOrientationHours;
			this.executionCourseFieldWorkHours = executionCourseFieldWorkHours;
			this.executionCourseTrainingPeriodHours = executionCourseTrainingPeriodHours;

			teacherExecutionCourseServiceList = new ArrayList<TeacherExecutionCourseServiceDTO>();

			this.executionCourseDegreeList = new HashSet<String>();

			this.executionCourseDegreeList.add(executionCourseDegreeName);

			this.executionCourseTotalHoursLecturedByTeachers = Double.valueOf(0);
			this.executionCourseTotalTimeLecturedByTeachers = Duration.ZERO;

			this.executionCourseLaboratorialHours = executionCourseLaboratorialHours;

			this.executionCourseStudentsNumberByTheoreticalShift = executionCourseStudentsNumberByTheoreticalShift;
			this.executionCourseStudentsNumberByPraticalShift = executionCourseStudentsNumberByPraticalShift;
			this.executionCourseStudentsNumberByLaboratorialShift = executionCourseStudentsNumberByLaboratorialShift;
			this.executionCourseStudentsNumberByTheoPraticalShift = executionCourseStudentsNumberByTheoPraticalShift;

			this.executionCourseStudentsNumberBySeminaryShift = seminaryStudentsNumberPerShift;
			this.executionCourseStudentsNumberByProblemsShift = problemsStudentsNumberPerShift;
			this.executionCourseStudentsNumberByTutorialOrientationShift = tutorialOrientationStudentsNumberPerShift;

			this.executionCourseStudentsNumberByFieldWorkShift = fieldWorkStudentsNumberPerShift;
			this.executionCourseStudentsNumberByTrainingPeriodShift = trainingPeriodStudentsNumberPerShift;

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

		public Double getExecutionCourseTotalHoursLecturedByTeachers() {
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

		public Double getExecutionCourseSeminaryHours() {
			return executionCourseSeminaryHours;
		}

		public Double getExecutionCourseProblemsHours() {
			return executionCourseProblemsHours;
		}

		public Double getExecutionCourseTutorialOrientationHours() {
			return executionCourseTutorialOrientationHours;
		}

		public Double getExecutionCourseFieldWorkHours() {
			return executionCourseFieldWorkHours;
		}

		public Double getExecutionCourseTrainingPeriodHours() {
			return executionCourseTrainingPeriodHours;
		}

		public List<TeacherExecutionCourseServiceDTO> getTeacherExecutionCourseServiceList() {
			return teacherExecutionCourseServiceList;
		}

		public Integer getExecutionCourseStudentsTotalNumber() {
			return executionCourseFirstTimeEnrollementStudentNumber + executionCourseSecondTimeEnrollementStudentNumber;
		}

		public Double getExecutionCourseTotalHours() {
			return executionCoursePraticalHours + executionCourseTheoreticalHours + executionCourseTheoPratHours
					+ executionCourseLaboratorialHours + executionCourseSeminaryHours + executionCourseProblemsHours
					+ executionCourseTutorialOrientationHours + executionCourseFieldWorkHours
					+ executionCourseTrainingPeriodHours;
		}

		public Duration getTotalDuration() {
			return totalDuration;
		}

		public String getTotalDurationString() {
			PeriodFormatter periodFormatter =
					new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendSuffix(":")
							.appendMinutes().toFormatter();
			return periodFormatter.print(getTotalDuration().toPeriod());
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

		public void updateHoursLecturedByTeachers(Double hoursSpentByTeacher) {
			this.executionCourseTotalHoursLecturedByTeachers += hoursSpentByTeacher.doubleValue();
		}

		public void updateTimeLecturedByTeachers(Duration timeSpentByTeacher) {
			this.executionCourseTotalTimeLecturedByTeachers =
					this.executionCourseTotalTimeLecturedByTeachers.plus(timeSpentByTeacher);
		}

		public Double getExecutionCourseHoursBalance() {
			DecimalFormat df = new DecimalFormat("#.##");
			DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
			decimalFormatSymbols.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(decimalFormatSymbols);
			return new Double(df.format(getExecutionCourseTotalHours() - executionCourseTotalHoursLecturedByTeachers));
		}

		public String getExecutionCourseDurationBalance() {
			Duration balance = getTotalDuration().minus(executionCourseTotalTimeLecturedByTeachers);
			PeriodFormatter periodFormatter =
					new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendSuffix(":")
							.appendMinutes().toFormatter();
			return periodFormatter.print(balance.toPeriod());
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

		public Double getExecutionCourseStudentsNumberBySeminaryShift() {
			return executionCourseStudentsNumberBySeminaryShift;
		}

		public Double getExecutionCourseStudentsNumberByProblemsShift() {
			return executionCourseStudentsNumberByProblemsShift;
		}

		public Double getExecutionCourseStudentsNumberByTutorialOrientationShift() {
			return executionCourseStudentsNumberByTutorialOrientationShift;
		}

		public Double getExecutionCourseStudentsNumberByFieldWorkShift() {
			return executionCourseStudentsNumberByFieldWorkShift;
		}

		public Double getExecutionCourseStudentsNumberByTrainingPeriodShift() {
			return executionCourseStudentsNumberByTrainingPeriodShift;
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

		public String getFormattedExecutionCourseStudentsNumberBySeminaryShift() {
			return getFormattedValue(executionCourseStudentsNumberBySeminaryShift);
		}

		public String getFormattedExecutionCourseStudentsNumberByProblemsShift() {
			return getFormattedValue(executionCourseStudentsNumberByProblemsShift);
		}

		public String getFormattedExecutionCourseStudentsNumberByTutorialOrientationShift() {
			return getFormattedValue(executionCourseStudentsNumberByTutorialOrientationShift);
		}

		@Override
		public int compareTo(Object obj) {
			if (obj instanceof ExecutionCourseDistributionServiceEntryDTO) {
				ExecutionCourseDistributionServiceEntryDTO executionCourse1 = (ExecutionCourseDistributionServiceEntryDTO) obj;
				return this.getExecutionCourseName().compareTo(executionCourse1.getExecutionCourseName());
			}
			return 0;
		}

		public String getDegreeListString() {

			StringBuilder finalString = new StringBuilder();
			String[] stringArrayDegrees = executionCourseDegreeList.toArray(new String[] {});

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
			String[] stringArrayYears = executionCourseCurricularYearsList.toArray(new String[] {});

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
			Duration totalDuration, Double executionCourseTheoreticalHours, Double executionCoursePraticalHours,
			Double executionCourseLaboratorialHours, Double executionCourseTheoPratHours, Double executionCourseSeminaryHours,
			Double executionCourseProblemsHours, Double executionCourseTutorialOrientationHours,
			Double executionCourseFieldWorkHours, Double executionCourseTrainingPeriodHours,
			Double executionCourseStudentsNumberByTheoreticalShift, Double executionCourseStudentsNumberByPraticalShift,
			Double executionCourseStudentsNumberByLaboratorialShift, Double executionCourseStudentsNumberByTheoPraticalShift,
			Double seminaryStudentsNumberPerShift, Double problemsStudentsNumberPerShift,
			Double tutorialOrientationStudentsNumberPerShift, Double fieldWorkStudentsNumberPerShift,
			Double trainingPeriodStudentsNumberPerShift) {
		ExecutionCourseDistributionServiceEntryDTO t =
				new ExecutionCourseDistributionServiceEntryDTO(executionCourseIdInternal, executionCourseName,
						executionCourseCampus, executionCourseDegreeName, executionCourseCurricularYear, executionCourseSemester,
						executionCourseFirstTimeEnrollementStudentNumber, executionCourseSecondTimeEnrollementStudentNumber,
						totalDuration, executionCourseTheoreticalHours, executionCoursePraticalHours,
						executionCourseLaboratorialHours, executionCourseTheoPratHours, executionCourseSeminaryHours,
						executionCourseProblemsHours, executionCourseTutorialOrientationHours, executionCourseFieldWorkHours,
						executionCourseTrainingPeriodHours, executionCourseStudentsNumberByTheoreticalShift,
						executionCourseStudentsNumberByPraticalShift, executionCourseStudentsNumberByLaboratorialShift,
						executionCourseStudentsNumberByTheoPraticalShift, seminaryStudentsNumberPerShift,
						problemsStudentsNumberPerShift, tutorialOrientationStudentsNumberPerShift,
						fieldWorkStudentsNumberPerShift, trainingPeriodStudentsNumberPerShift);

		if (!executionCourseMap.containsKey(executionCourseIdInternal)) {
			executionCourseMap.put(executionCourseIdInternal, t);
		}
	}

	public void addTeacherToExecutionCourse(Integer keyExecutionCourse, Integer idInternalTeacher, String name,
			Double hoursSpentByTeacher, Duration timeSpentByTeacher, boolean teacherBelongsToDepartment) {
		TeacherExecutionCourseServiceDTO teacher =
				new TeacherExecutionCourseServiceDTO(idInternalTeacher, name, hoursSpentByTeacher, timeSpentByTeacher,
						teacherBelongsToDepartment);

		ExecutionCourseDistributionServiceEntryDTO executionCourseEntry = executionCourseMap.get(keyExecutionCourse);
		executionCourseEntry.updateHoursLecturedByTeachers(hoursSpentByTeacher);
		executionCourseEntry.updateTimeLecturedByTeachers(timeSpentByTeacher);
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
