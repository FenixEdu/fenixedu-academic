/*
 * Created on 5/Fev/2004
 *
 */
package ServidorPersistente.rowReaders;

import java.util.Map;

import org.apache.ojb.broker.accesslayer.RowReaderDefaultImpl;
import org.apache.ojb.broker.metadata.ClassDescriptor;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITestScope;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
                persistentSuport = SuportePersistenteOJB.getInstance();

                IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                        .getIPersistentExecutionCourse().readByOID(
                                ExecutionCourse.class, testScope.getKeyClass());
                testScope.setDomainObject(executionCourse);
            } catch (ExcepcaoPersistencia e) {
                return testScope;
            }

        }
        return testScope;
    }

}