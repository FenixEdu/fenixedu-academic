package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

import org.apache.commons.beanutils.BeanComparator;

public class TeacherServiceDistributionDTOEntry {
	Integer level;

	TeacherServiceDistribution tsd;

	public TeacherServiceDistributionDTOEntry(TeacherServiceDistribution tsd, Integer level) {
		this.level = level;
		this.tsd = tsd;
	}

	public String getName() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < level; i++) {
			sb.append(" - ");
		}

		sb.append(" ");
		sb.append(tsd.getName());

		return sb.toString();
	}

	public Integer getIdInternal() {
		return tsd.getIdInternal();
	}

	public TeacherServiceDistribution getTeacherServiceDistribution() {
		return tsd;
	}
	
	
	
	public static List<TeacherServiceDistributionDTOEntry> getTeacherServiceDistributionOptionEntriesForPerson(TSDProcessPhase tsdProcessPhase,
			Person person, 
			boolean testCoursesAndTeachersManagementGroupPermission,
			boolean testCoursesAndTeachersValuationManagerPermission) {
		List<TeacherServiceDistributionDTOEntry> tsdOptionEntries = new ArrayList<TeacherServiceDistributionDTOEntry>();

		buildTeacherServiceDistributionOptionEntriesRec(
				tsdOptionEntries,
				0,
				tsdProcessPhase.getRootTSD(),
				person,
				testCoursesAndTeachersManagementGroupPermission,
				testCoursesAndTeachersValuationManagerPermission);

		return tsdOptionEntries;
	}
	
	public static List<TeacherServiceDistributionDTOEntry> getTeacherServiceDistributionOptionEntries(TSDProcessPhase tsdProcessPhase) {
		List<TeacherServiceDistributionDTOEntry> tsdOptionEntries = new ArrayList<TeacherServiceDistributionDTOEntry>();

		buildTeacherServiceDistributionOptionEntriesRec(
				tsdOptionEntries,
				0,
				tsdProcessPhase.getRootTSD(),
				null,
				false,
				false);

		return tsdOptionEntries;
	}

	private static void buildTeacherServiceDistributionOptionEntriesRec(
			List<TeacherServiceDistributionDTOEntry> tsdOptionEntries,
			int level,
			TeacherServiceDistribution grouping,
			Person person,
			boolean testCoursesAndTeachersManagementGroupPermission,
			boolean testCoursesAndTeachersValuationManagerPermission) {
		
				
		if(person == null || (testCoursesAndTeachersManagementGroupPermission && grouping.getIsMemberOfCoursesAndTeachersManagementGroup(person)) 
			|| (testCoursesAndTeachersValuationManagerPermission && grouping.getIsMemberOfCoursesAndTeachersValuationManagers(person)) 
			||  grouping.getTSDProcessPhase().getTSDProcess().getHasSuperUserPermission(person)){
			tsdOptionEntries.add(new TeacherServiceDistributionDTOEntry(grouping, level));
			person = null;
		} 
		
		List<TeacherServiceDistribution> childsList = new ArrayList<TeacherServiceDistribution>(grouping.getChilds());
		Collections.sort(childsList, new BeanComparator("name"));
				
		for (TeacherServiceDistribution tsd : childsList) {
			buildTeacherServiceDistributionOptionEntriesRec(
					tsdOptionEntries,
					level + 1,
					tsd,
					person,
					testCoursesAndTeachersManagementGroupPermission,
					testCoursesAndTeachersValuationManagerPermission);
		}
	}
}
