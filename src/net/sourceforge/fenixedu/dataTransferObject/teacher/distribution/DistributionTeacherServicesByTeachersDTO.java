package net.sourceforge.fenixedu.dataTransferObject.teacher.distribution;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

/**
 *  amak, jpmsit
 * 
 */
public class DistributionTeacherServicesByTeachersDTO extends DataTranferObject {

	public class ExecutionCourseTeacherServiceDTO {
		private Integer executionCourseIdInternal;

		private String executionCourseName;

		private Integer hoursSpentByTeacher;
		
		private Map<Integer, String> courseDegreesList;
		
		private String executionPeriodName;
		
		private Map<Integer, Set<String>> executionYearsSet;
		

		public ExecutionCourseTeacherServiceDTO(Integer idInternal,
				String name, Integer hours, Map<Integer, String> executionCourseDegreesNameMap, Map<Integer, Set<String>> executionYearsMap, String periodName) {
			super();

			this.executionCourseIdInternal = idInternal;
			this.hoursSpentByTeacher = hours;
			this.executionCourseName = name;
			this.executionYearsSet = executionYearsMap;
			this.executionPeriodName = periodName;
			this.courseDegreesList = executionCourseDegreesNameMap;
		}

		public Integer getExecutionCourseIdInternal() {
			return executionCourseIdInternal;
		}

		public String getExecutionCourseName() {
			return executionCourseName;
		}

		public Integer getHoursSpentByTeacher() {
			return hoursSpentByTeacher;
		}

		public Map<Integer, String> getCourseDegreesList() {
			return courseDegreesList;
		}
		
		public String getExecutionPeriodName() {
			return executionPeriodName;
		}
			
		public String getDescription(){
			StringBuilder finalString = new StringBuilder(getExecutionCourseName());
				
			finalString.append("(");
			
			Set<Integer> degreeIdSet = courseDegreesList.keySet();
			
			Iterator<Integer> iteratorDegreeIdSet = degreeIdSet.iterator();
					
			
			if(iteratorDegreeIdSet.hasNext()) {
				Integer firstDegreeIdInternal = iteratorDegreeIdSet.next();
						
				finalString.append(courseDegreesList.get(firstDegreeIdInternal));
				finalString.append(" (");
						
				Set<String> firstCurricularYearsSet = executionYearsSet.get(firstDegreeIdInternal);
				
				Iterator<String> iteratorFirstCurricularYearsSet = firstCurricularYearsSet.iterator();
				
				if(iteratorFirstCurricularYearsSet.hasNext()) {
					finalString.append(iteratorFirstCurricularYearsSet.next());
					
					while(iteratorFirstCurricularYearsSet.hasNext()) {
						finalString.append(", ");
						finalString.append(iteratorFirstCurricularYearsSet.next());
					}
					
					finalString.append("ºano");
				}
				
				finalString.append(")");
				
				
				while(iteratorDegreeIdSet.hasNext()) {
					finalString.append(", ");
					
					Integer degreeIdInternal = iteratorDegreeIdSet.next();
					
					finalString.append(courseDegreesList.get(degreeIdInternal));
					finalString.append(" (");
	
					Set<String> curricularYearsSet = executionYearsSet.get(degreeIdInternal);
					
					Iterator<String> iteratorCurricularYearsSet = curricularYearsSet.iterator();
					
					if(iteratorCurricularYearsSet.hasNext()) {
						finalString.append(iteratorCurricularYearsSet.next());
						
						while(iteratorCurricularYearsSet.hasNext()) {
							finalString.append(", ");
							finalString.append(iteratorCurricularYearsSet.next());
						}
						
						finalString.append("ºano");
					}
					finalString.append(")");
					
				}
				
				finalString.append(")");
			}
			
			finalString.append("/");
			finalString.append("(");
			finalString.append(executionPeriodName);
			finalString.append(")");
			finalString.append(" - ");
			finalString.append(hoursSpentByTeacher);
			
		
		
			return finalString.toString();
		}
		
	}

	
	public class TeacherCreditsInfoDTO {
		private String functionName;
		private Double credits;
		
		TeacherCreditsInfoDTO(String functionName, Double credits) {
			this.functionName = functionName;
			this.credits = credits;
		}

		public Double getCredits() {
			return credits;
		}

		public String getFunctionName() {
			return functionName;
		}
	}
	
	public class TeacherDistributionServiceEntryDTO implements Comparable {
		private Integer teacherIdInternal;
		
		private Integer teacherNumber;

		private String teacherCategory;

		private String teacherName;

		private Integer teacherRequiredHours;
		
		private Double teacherAccumulatedCredits;

		List<ExecutionCourseTeacherServiceDTO> executionCourseTeacherServiceList;
		
		List<TeacherCreditsInfoDTO> managementFunctionList;
		
		List<TeacherCreditsInfoDTO> exemptionSituationList;

		
		public TeacherDistributionServiceEntryDTO(Integer internal, Integer teacherNumber, String category,  String name, Integer hours, Double accumulatedCredits) {
			this.teacherNumber = teacherNumber;
			teacherCategory = category;
			teacherIdInternal = internal;
			teacherName = name;
			teacherRequiredHours = hours;
			teacherAccumulatedCredits = accumulatedCredits;
			
			executionCourseTeacherServiceList = new ArrayList<ExecutionCourseTeacherServiceDTO>();
			
			managementFunctionList = new ArrayList<TeacherCreditsInfoDTO>();
			
			exemptionSituationList = new ArrayList<TeacherCreditsInfoDTO>();
		}


		public List<ExecutionCourseTeacherServiceDTO> getExecutionCourseTeacherServiceList() {
			return executionCourseTeacherServiceList;
		}


		public String getTeacherCategory() {
			return teacherCategory;
		}


		public Integer getTeacherIdInternal() {
			return teacherIdInternal;
		}


		public String getTeacherName() {
			return teacherName;
		}


		public Integer getTeacherRequiredHours() {
			return teacherRequiredHours;
		}


		public Double getTeacherSpentCredits() {
			double credits = 0d;
			
			for (TeacherCreditsInfoDTO managementCredits : managementFunctionList) {
				credits += managementCredits.getCredits();
			}
			for (TeacherCreditsInfoDTO exemptionCredits : exemptionSituationList) {
				credits += exemptionCredits.getCredits();
			}
			
			return credits;
		}


		public void addExecutionCourse(ExecutionCourseTeacherServiceDTO executionCourse) {	
			executionCourseTeacherServiceList.add(executionCourse);
		}


		public Integer getTeacherNumber() {
			return teacherNumber;
		}
		
		private String getFormattedValue(Double value){
			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb);
			
			formatter.format("%.1f", value);
			return sb.toString();
		}
		
		public String getFormattedTeacherSpentCredits() {

			return getFormattedValue(getTeacherSpentCredits());
		}
		
		public String getFormattedTeacherAccumulatedCredits() {

			return getFormattedValue(getAccumulatedCredits());
		}
		
		public Integer getTotalLecturedHours() {
			int totalHours = 0;
			
			for(ExecutionCourseTeacherServiceDTO executionCourse: executionCourseTeacherServiceList) {
				totalHours += executionCourse.getHoursSpentByTeacher();
			}
			
			return totalHours;
		}
		
		public Integer getAvailability(){
			double availability = getTeacherRequiredHours() - getTotalLecturedHours() - getTeacherSpentCredits();
			
			return new Double(StrictMath.ceil(StrictMath.abs(availability)) * StrictMath.signum(availability)).intValue();
		}
		
		public Double getAccumulatedCredits() {
			return teacherAccumulatedCredits;
		}

		public int compareTo(Object obj) {
			if(obj instanceof TeacherDistributionServiceEntryDTO) {
				TeacherDistributionServiceEntryDTO teacher1 = (TeacherDistributionServiceEntryDTO) obj;	
				return this.getTeacherNumber().compareTo(teacher1.getTeacherNumber());
			}
			return 0;
		}


		public void setTeacherRequiredHours(Integer teacherRequiredHours) {
			this.teacherRequiredHours = teacherRequiredHours;
		}


		public List<TeacherCreditsInfoDTO> getManagementFunctionList() {
			return managementFunctionList;
		}
		
		public void addToManagementFunction(String function, Double credits) {
			managementFunctionList.add(new TeacherCreditsInfoDTO(function, credits));
		}
		
		public void addToExemptionSituation(String exemptionType, Double credits) {
			exemptionSituationList.add(new TeacherCreditsInfoDTO(exemptionType, credits));
		}

		public List<TeacherCreditsInfoDTO> getExemptionSituationList() {
			return exemptionSituationList;
		}
	}

	
	
	private Map<Integer, TeacherDistributionServiceEntryDTO> teachersMap;

	public DistributionTeacherServicesByTeachersDTO() {
		teachersMap = new HashMap<Integer, TeacherDistributionServiceEntryDTO>();
	}

	public void addTeacher(Integer key, Integer teacherNumber, String category, String name, Integer hours, Double accumulatedCredits) {
		TeacherDistributionServiceEntryDTO t = new TeacherDistributionServiceEntryDTO(key, teacherNumber, category, name, hours, accumulatedCredits);

		if(!teachersMap.containsKey(key)) {
			teachersMap.put(key, t);
		}
	}

	public void addExecutionCourseToTeacher(Integer keyTeacher, Integer executionCourseIdInternal, String executionCourseName,
			Integer hours, Map<Integer, String> executionCourseDegreesNameSet, Map<Integer, Set<String>> curricularYearsSet, String periodName) {
		ExecutionCourseTeacherServiceDTO executionCourse = new ExecutionCourseTeacherServiceDTO(executionCourseIdInternal, executionCourseName, hours, executionCourseDegreesNameSet, curricularYearsSet, periodName);
		
		teachersMap.get(keyTeacher).addExecutionCourse(executionCourse);	
			
	}
	
	public void addHoursToTeacher(Integer keyTeacher, Integer hours){
		TeacherDistributionServiceEntryDTO teacher = teachersMap.get(keyTeacher);
		
		if(teacher != null){
			teacher.setTeacherRequiredHours(teacher.getTeacherRequiredHours() + hours);
		}
	}
		
	public boolean isTeacherPresent(Integer keyTeacher){
		return teachersMap.containsKey((keyTeacher));
	}
	
	public Map<Integer, TeacherDistributionServiceEntryDTO> getTeachersMap() {
		return teachersMap;
	}
	
	public void addManagementFunctionToTeacher(Integer keyTeacher, String managementFunction, Double credits) {
		TeacherDistributionServiceEntryDTO teacher = teachersMap.get(keyTeacher);
		
		if(teacher != null) {
			teacher.addToManagementFunction(managementFunction, credits);
		}
	}
	
	public void addExemptionSituationToTeacher(Integer keyTeacher, String exemptionType, Double credits) {
		TeacherDistributionServiceEntryDTO teacher = teachersMap.get(keyTeacher);
		
		if(teacher != null) {
			teacher.addToExemptionSituation(exemptionType, credits);
		}
	}
}
