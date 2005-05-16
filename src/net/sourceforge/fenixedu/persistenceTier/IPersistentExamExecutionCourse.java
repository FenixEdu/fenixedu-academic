/*
 * IPersistentExam.java Created on 2003/03/29
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public interface IPersistentExamExecutionCourse extends IPersistentObject {

    public List readByExecutionCourse(String executionCourseAcronym, String executionPeriodName,
            String year) throws ExcepcaoPersistencia;

}