/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentMetadata extends IPersistentObject {

    public abstract List<IMetadata> readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public abstract List<IMetadata> readByExecutionCourseAndVisibility(Integer executionCourseId) throws ExcepcaoPersistencia;

    public abstract List<IMetadata> readByExecutionCourseAndNotTest(Integer executionCourseId, Integer testId) throws ExcepcaoPersistencia;

    public abstract List<IMetadata> readByExecutionCourseAndNotDistributedTest(Integer executionCourseId, Integer distributedTestId)
            throws ExcepcaoPersistencia;

    public abstract int getNumberOfQuestions(IMetadata metadata) throws ExcepcaoPersistencia;

    public abstract int countByExecutionCourse(Integer executionCourseId) throws ExcepcaoPersistencia;

    public abstract void cleanMetadatas() throws ExcepcaoPersistencia;
}