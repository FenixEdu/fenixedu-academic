/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentDistributedTest extends IPersistentObject {
    public List<IDistributedTest> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia;

    public List<IDistributedTest> readByStudent(Integer studentId) throws ExcepcaoPersistencia;

    public List<IDistributedTest> readByStudentAndExecutionCourse(Integer studentId, Integer executionCourseId) throws ExcepcaoPersistencia;

    public List<IDistributedTest> readAll() throws ExcepcaoPersistencia;
}