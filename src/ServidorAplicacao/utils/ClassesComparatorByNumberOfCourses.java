/*
 * Created on Mar 14, 2003
 *
 */
package ServidorAplicacao.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import Dominio.ITurma;
import Dominio.ITurno;

/**
 * @author Luis Cruz e Sara Ribeiro
 *
 */
public class ClassesComparatorByNumberOfCourses implements Comparator {
	private List executionCoursesAttended;

	/**
	 * @return
	 */
	private List getExecutionCoursesAttended() {
		return executionCoursesAttended;
	}

	/**
	 * @param executionCoursesAttended
	 */
	private void setExecutionCoursesAttended(List executionCoursesAttended) {
		this.executionCoursesAttended = executionCoursesAttended;
	}

	public ClassesComparatorByNumberOfCourses(List executionCoursesAttended) {
		setExecutionCoursesAttended(executionCoursesAttended);
	}

	public int compare(Object obj1, Object obj2) {
		ITurma domainClass1 = (ITurma) obj1;
		ITurma domainClass2 = (ITurma) obj2;
		List shifts1 = domainClass1.getAssociatedShifts();
		List shifts2 = domainClass2.getAssociatedShifts();
		List executionCourses1 = getExecutionCoursesFromShifts(shifts1);
		List executionCourses2 = getExecutionCoursesFromShifts(shifts2);

		return CollectionUtils
			.intersection(getExecutionCoursesAttended(), executionCourses2)
			.size()
			- CollectionUtils
				.intersection(getExecutionCoursesAttended(), executionCourses1)
				.size();
	}

	/**
	 * @param list
	 * @return
	 */
	private List getExecutionCoursesFromShifts(List list) {
		
		if (list == null) {
			return null;
		} 
			Iterator iter = list.iterator();
			List executionCourses = new ArrayList();
			while (iter.hasNext()) {
				ITurno shift = (ITurno) iter.next();
				executionCourses.add(shift.getDisciplinaExecucao());
			}
			return executionCourses;
		

	}

}
