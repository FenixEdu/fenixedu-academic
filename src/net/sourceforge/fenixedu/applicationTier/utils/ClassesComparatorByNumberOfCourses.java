/*
 * Created on Mar 14, 2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;

import org.apache.commons.collections.CollectionUtils;

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
        ISchoolClass domainClass1 = (ISchoolClass) obj1;
        ISchoolClass domainClass2 = (ISchoolClass) obj2;
        List shifts1 = domainClass1.getAssociatedShifts();
        List shifts2 = domainClass2.getAssociatedShifts();
        List executionCourses1 = getExecutionCoursesFromShifts(shifts1);
        List executionCourses2 = getExecutionCoursesFromShifts(shifts2);

        return CollectionUtils.intersection(getExecutionCoursesAttended(), executionCourses2).size()
                - CollectionUtils.intersection(getExecutionCoursesAttended(), executionCourses1).size();
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
            IShift shift = (IShift) iter.next();
            executionCourses.add(shift.getDisciplinaExecucao());
        }
        return executionCourses;

    }

}