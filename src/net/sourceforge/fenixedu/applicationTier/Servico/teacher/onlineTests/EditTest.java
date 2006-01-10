/*
 * Created on 1/Ago/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class EditTest implements IService {

    public void run(Integer executionCourseId, Integer testId, String title, String information) throws ExcepcaoPersistencia {
        Test test = (Test) PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentTest().readByOID(Test.class, testId);
        test.setTitle(title);
        test.setInformation(information);
        test.setLastModifiedDate(Calendar.getInstance().getTime());
    }
}