/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadTests implements IService {

    public List<InfoTest> run(Integer executionCourseId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<ITest> tests = persistentSuport.getIPersistentTest().readByTestScope(ExecutionCourse.class.getName(), executionCourseId);
        List<InfoTest> infoTestList = new ArrayList<InfoTest>();
        for (ITest test : tests) {
            infoTestList.add(InfoTest.newInfoFromDomain(test));
        }
        return infoTestList;
    }
}