/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentDistributedTest extends IPersistentObject {
    public List<DistributedTest> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia;

    public List<DistributedTest> readByStudent(Integer studentId) throws ExcepcaoPersistencia;

    public List<DistributedTest> readByStudentAndExecutionCourse(Integer studentId, Integer executionCourseId) throws ExcepcaoPersistencia;

    public List<DistributedTest> readAll() throws ExcepcaoPersistencia;
}