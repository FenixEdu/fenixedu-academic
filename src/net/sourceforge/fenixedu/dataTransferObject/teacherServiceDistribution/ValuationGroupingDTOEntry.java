package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;

public class ValuationGroupingDTOEntry {
	Integer level;

	ValuationGrouping valuationGrouping;

	public ValuationGroupingDTOEntry(ValuationGrouping valuationGrouping, Integer level) {
		this.level = level;
		this.valuationGrouping = valuationGrouping;
	}

	public String getName() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < level; i++) {
			sb.append(" - ");
		}

		sb.append(" ");
		sb.append(valuationGrouping.getName());

		return sb.toString();
	}

	public Integer getIdInternal() {
		return valuationGrouping.getIdInternal();
	}

	public ValuationGrouping getValuationGrouping() {
		return valuationGrouping;
	}
	
	
	
	public static List<ValuationGroupingDTOEntry> getValuationGroupingOptionEntriesForPerson(ValuationPhase valuationPhase,
			Person person, 
			boolean testCoursesAndTeachersManagementGroupPermission,
			boolean testCoursesAndTeachersValuationManagerPermission) {
		List<ValuationGroupingDTOEntry> valuationGroupingOptionEntries = new ArrayList<ValuationGroupingDTOEntry>();

		buildValuationGroupingOptionEntriesRec(
				valuationGroupingOptionEntries,
				0,
				valuationPhase.getRootValuationGrouping(),
				person,
				testCoursesAndTeachersManagementGroupPermission,
				testCoursesAndTeachersValuationManagerPermission);

		return valuationGroupingOptionEntries;
	}
	
	public static List<ValuationGroupingDTOEntry> getValuationGroupingOptionEntries(ValuationPhase valuationPhase) {
		List<ValuationGroupingDTOEntry> valuationGroupingOptionEntries = new ArrayList<ValuationGroupingDTOEntry>();

		buildValuationGroupingOptionEntriesRec(
				valuationGroupingOptionEntries,
				0,
				valuationPhase.getRootValuationGrouping(),
				null,
				false,
				false);

		return valuationGroupingOptionEntries;
	}

	private static void buildValuationGroupingOptionEntriesRec(
			List<ValuationGroupingDTOEntry> valuationGroupingOptionEntries,
			int level,
			ValuationGrouping grouping,
			Person person,
			boolean testCoursesAndTeachersManagementGroupPermission,
			boolean testCoursesAndTeachersValuationManagerPermission) {
		
				
		if(person == null || (testCoursesAndTeachersManagementGroupPermission && grouping.getIsMemberOfCoursesAndTeachersManagementGroup(person)) 
			|| (testCoursesAndTeachersValuationManagerPermission && grouping.getIsMemberOfCoursesAndTeachersValuationManagers(person)) 
			||  grouping.getValuationPhase().getTeacherServiceDistribution().getHasSuperUserPermission(person)){
			valuationGroupingOptionEntries.add(new ValuationGroupingDTOEntry(grouping, level));
			person = null;
		} 
		
		List<ValuationGrouping> childsList = new ArrayList<ValuationGrouping>(grouping.getChilds());
		Collections.sort(childsList, new BeanComparator("name"));
				
		for (ValuationGrouping valuationGrouping : childsList) {
			buildValuationGroupingOptionEntriesRec(
					valuationGroupingOptionEntries,
					level + 1,
					valuationGrouping,
					person,
					testCoursesAndTeachersManagementGroupPermission,
					testCoursesAndTeachersValuationManagerPermission);
		}
	}
}
