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

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StudentGroupOJB extends ObjectFenixOJB implements IPersistentStudentGroup{



	public IStudentGroup readStudentGroupByGroupPropertiesAndGroupNumberAndShift(IGroupProperties groupProperties,Integer studentGroupNumber,ITurno shift) throws ExcepcaoPersistencia {

		
		Criteria criteria1 = new Criteria();
		Criteria criteria2 = new Criteria();
		
		
		criteria1.addEqualTo("keyGroupProperties",groupProperties.getIdInternal());
		criteria2.addEqualTo("groupNumber",studentGroupNumber);
		criteria1.addAndCriteria(criteria2);
		if(shift!=null)
		{
			Criteria criteria3 = new Criteria();
			criteria3.addEqualTo("keyShift",shift.getIdInternal());
			criteria1.addAndCriteria(criteria3);
		}
		return (IStudentGroup) queryObject(StudentGroup.class, criteria1);
	}
	
	
	
	public List readAllStudentGroupByGroupProperties(IGroupProperties groupProperties) throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		
		criteria.addEqualTo("keyGroupProperties",groupProperties.getIdInternal());
		
		
		return (List) queryList(StudentGroup.class, criteria);
	}
	
	public List readAllStudentGroupByGroupPropertiesAndShift(IGroupProperties groupProperties,ITurno shift) throws ExcepcaoPersistencia {

			Criteria criteria1 = new Criteria();
		
			criteria1.addEqualTo("keyGroupProperties",groupProperties.getIdInternal());
			if(shift!=null)
			{
					Criteria criteria2 = new Criteria();
					criteria2.addEqualTo("keyShift",shift.getIdInternal());
					criteria1.addAndCriteria(criteria2);
			}
		
			return (List) queryList(StudentGroup.class, criteria1);
		}
			
	public List readAll() throws ExcepcaoPersistencia {

	try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + StudentGroup.class.getName();
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
				list.add((IStudentGroup) iterator.next());
			}
			return list;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
		}
			
	public void lockWrite(IStudentGroup studentGroupToWrite) throws ExcepcaoPersistencia {
       
		IStudentGroup studentGroupFromDB = null;
		if (studentGroupToWrite == null)
		// If there is nothing to write, simply return.
			return;

		// read studentGroup from DB	
		studentGroupFromDB = readStudentGroupByGroupPropertiesAndGroupNumberAndShift(studentGroupToWrite.getGroupProperties(),studentGroupToWrite.getGroupNumber(),studentGroupToWrite.getShift());
		
		System.out.println("NO OJB-LER DA DB"+studentGroupFromDB);
		// if (studentGroup not in database) then write it
		if (studentGroupFromDB == null)
			super.lockWrite(studentGroupToWrite);
		// else if (item is mapped to the database then write any existing changes)
			 else if ((studentGroupToWrite instanceof IStudentGroup) &&
					   ((IStudentGroup) studentGroupFromDB).getIdInternal().equals(
						((IStudentGroup) studentGroupToWrite).getIdInternal())) {

						  super.lockWrite(studentGroupToWrite);
				  // else throw an AlreadyExists exception.
			  } else
				  throw new ExistingPersistentException();
		  }
		  
	
		  
    
	public void delete(IStudentGroup studentGroup) throws ExcepcaoPersistencia {
			try {
				super.delete(studentGroup);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}
		}
		
		
	public void deleteAll() throws ExcepcaoPersistencia {
			try {
				String oqlQuery = "select all from " + StudentGroup.class.getName();
				super.deleteAll(oqlQuery);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}
		}
	
	
}
