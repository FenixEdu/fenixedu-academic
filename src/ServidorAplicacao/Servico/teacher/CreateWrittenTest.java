/*
 * Created on Feb 18, 2004
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation -
 * Code and Comments
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoWrittenTest;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IWrittenTest;
import Dominio.WrittenTest;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentWrittenTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis&Nuno
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code
 * Generation - Code and Comments
 */
public class CreateWrittenTest implements IService
{
    public CreateWrittenTest()
    {
    }

    public Boolean run(Integer execCourse, InfoWrittenTest infoWrittenTest)
        throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
			IPersistentWrittenTest persistentWrittenTest = sp.getIPersistentWrittenTest();
            IExecutionCourse executionCourse = new ExecutionCourse();
            executionCourse.setIdInternal(execCourse);
            executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
            
            IWrittenTest writtenTest = new WrittenTest();
            persistentWrittenTest.simpleLockWrite(writtenTest);
           
            PropertyUtils.copyProperties(infoWrittenTest, writtenTest);
            
            List associatedExecutionCourses = new ArrayList();
            associatedExecutionCourses.add(executionCourse);
            writtenTest.setAssociatedExecutionCourses(associatedExecutionCourses);

			return new Boolean(true);
			
        } catch (ExcepcaoPersistencia ex)
        {
			throw new FenixServiceException(ex.getMessage());
        }
        catch (Exception e)
        {
            if (e instanceof FenixServiceException)
               {
                throw (FenixServiceException) e;
            }
            throw new FenixServiceException(e);
        }

    }
}
