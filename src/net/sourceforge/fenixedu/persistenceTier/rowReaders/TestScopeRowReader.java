/*
 * Created on 5/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.rowReaders;

import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ITestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.ojb.broker.accesslayer.RowReaderDefaultImpl;
import org.apache.ojb.broker.metadata.ClassDescriptor;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public class TestScopeRowReader extends RowReaderDefaultImpl {

    public TestScopeRowReader(ClassDescriptor arg0) {
        super(arg0);
    }

    public Object readObjectFrom(Map row) {
        ITestScope testScope = (ITestScope) super.readObjectFrom(row);
        if (testScope.getClassName().equals(ExecutionCourse.class.getName())) {
            ISuportePersistente persistentSuport;
            try {
                persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

                IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                        .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,
                                testScope.getKeyClass());
                testScope.setDomainObject(executionCourse);
            } catch (ExcepcaoPersistencia e) {
                return testScope;
            }

        }
        return testScope;
    }

}