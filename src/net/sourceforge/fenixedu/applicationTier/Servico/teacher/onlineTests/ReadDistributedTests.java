/*
 * Created on 20/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTests extends Service {

    public List<InfoDistributedTest> run(Integer executionCourseId) throws ExcepcaoPersistencia {
        final TestScope testScope = TestScope.readByDomainObject(ExecutionCourse.class, executionCourseId);
        List<InfoDistributedTest> infoDistributedTestList = new ArrayList<InfoDistributedTest>();
        if (testScope != null) {
            for (DistributedTest distributedTest : testScope.getDistributedTests()) {
                infoDistributedTestList.add(InfoDistributedTest.newInfoFromDomain(distributedTest));
            }
        }
        return infoDistributedTestList;
    }
}