/*
 * Created on 12/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.GroupProperties;
import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GroupPropertiesOJB extends ObjectFenixOJB implements IPersistentGroupProperties{
	
	
	public IGroupProperties readBy(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia {

			Criteria criteria = new Criteria(); 
			criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());	
			return (IGroupProperties) queryObject(GroupProperties.class, criteria);
	}
		
	public List readAll() throws ExcepcaoPersistencia {

	try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + GroupProperties.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
					throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
				list.add((IGroupProperties) iterator.next());
			}
			return list;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
		}
			
	public void lockWrite(IGroupProperties groupPropertiesToWrite) throws ExcepcaoPersistencia {
       
		IGroupProperties groupPropertiesFromDB = null;
		if (groupPropertiesToWrite == null)
		// If there is nothing to write, simply return.
			return;

		// read studentGroup from DB	
		groupPropertiesFromDB = readBy(groupPropertiesToWrite.getExecutionCourse());
		

		// if (studentGroup not in database) then write it
		if (groupPropertiesFromDB == null)
			super.lockWrite(groupPropertiesToWrite);
		// else if (item is mapped to the database then write any existing changes)
			 else if ((groupPropertiesToWrite instanceof IGroupProperties) &&
					   ((IGroupProperties) groupPropertiesFromDB).getIdInternal().equals(
						((IGroupProperties) groupPropertiesToWrite).getIdInternal())) {

						  super.lockWrite(groupPropertiesToWrite);
				  // else throw an AlreadyExists exception.
			  } else
				  throw new ExistingPersistentException();
		  }
		  
	
		  
    
	public void delete(IGroupProperties groupProperties) throws ExcepcaoPersistencia {
			try {
				super.delete(groupProperties);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}
		}
		
		
	public void deleteAll() throws ExcepcaoPersistencia {
			try {
				String oqlQuery = "select all from " + GroupProperties.class.getName();
				super.deleteAll(oqlQuery);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}
		}
	
}
