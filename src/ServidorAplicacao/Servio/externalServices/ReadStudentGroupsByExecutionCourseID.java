/*
 * Created on 28/Fev/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ServidorAplicacao.Servio.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import DataBeans.InfoGroupPropertiesExecutionCourse;
import Dominio.GroupPropertiesExecutionCourse;
import Dominio.IGroupPropertiesExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupPropertiesExecutionCourse;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author gedl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReadStudentGroupsByExecutionCourseID
{

    public Collection run(Integer executionCourseID) throws ExcepcaoPersistencia
    {   
        Collection result = new ArrayList();
        IPersistentGroupPropertiesExecutionCourse persistentGroupProperties = SuportePersistenteOJB.getInstance().getIPersistentGroupPropertiesExecutionCourse();
        Collection domainResult = persistentGroupProperties.readByExecutionCourseId(executionCourseID);
        for (Iterator iter = domainResult.iterator(); iter.hasNext();)
        {
            IGroupPropertiesExecutionCourse element = (IGroupPropertiesExecutionCourse) iter.next();
            result.add(InfoGroupPropertiesExecutionCourse.newInfoFromDomain(element));            
        }
        
        
        
        return result;
        
    }
    
}
