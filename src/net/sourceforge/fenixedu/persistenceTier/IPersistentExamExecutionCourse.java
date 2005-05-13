/*
 * IPersistentExam.java Created on 2003/03/29
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public interface IPersistentExamExecutionCourse extends IPersistentObject {

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

}