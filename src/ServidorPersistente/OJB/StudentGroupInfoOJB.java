

package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.IStudentGroupInfo;
import Dominio.StudentGroupInfo;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroupInfo;
import Util.StudentType;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class StudentGroupInfoOJB extends ObjectFenixOJB implements IPersistentStudentGroupInfo {
    
    public StudentGroupInfoOJB() {}
    
	public IStudentGroupInfo readByStudentType(StudentType studentType) throws ExcepcaoPersistencia{
		String oqlQuery = "select all from "+ StudentGroupInfo.class.getName()
						+ " where studentType = $1 ";
	 	try {
			query.create(oqlQuery);
			query.bind(studentType.getState());
				
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0)
				return (IStudentGroupInfo) result.get(0);
			return null;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}	
	}
}
