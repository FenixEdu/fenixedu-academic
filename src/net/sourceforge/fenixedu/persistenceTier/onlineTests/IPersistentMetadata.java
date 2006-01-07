/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentMetadata extends IPersistentObject {

    public abstract List<Metadata> readByExecutionCourse(ExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public abstract List<Metadata> readByExecutionCourseAndVisibility(Integer executionCourseId) throws ExcepcaoPersistencia;

    public abstract List<Metadata> readByExecutionCourseAndNotTest(Integer executionCourseId, Integer testId) throws ExcepcaoPersistencia;

    public abstract List<Metadata> readByExecutionCourseAndNotDistributedTest(Integer executionCourseId, Integer distributedTestId)
            throws ExcepcaoPersistencia;

    public abstract int getNumberOfQuestions(Metadata metadata) throws ExcepcaoPersistencia;

    public abstract int countByExecutionCourse(Integer executionCourseId) throws ExcepcaoPersistencia;

    public abstract void cleanMetadatas() throws ExcepcaoPersistencia;
}