/*
 * IPersistentExam.java Created on 2003/03/29
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public interface IPersistentExamExecutionCourse extends IPersistentObject {

    public IExamExecutionCourse readBy(IExam exam, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(IExamExecutionCourse examExecutionCourse) throws ExcepcaoPersistencia;

    public void delete(IExam exam) throws ExcepcaoPersistencia;

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

}