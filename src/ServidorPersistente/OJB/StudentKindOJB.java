

package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IStudentKind;
import Dominio.StudentKind;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentKind;
import Util.StudentType;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class StudentKindOJB extends ObjectFenixOJB implements IPersistentStudentKind {
    
    public StudentKindOJB() {}
    
	public IStudentKind readByStudentType(StudentType studentType) throws ExcepcaoPersistencia{
//		String oqlQuery = "select all from "+ StudentKind.class.getName()
//						+ " where studentType = $1 ";
//	 	try {
//			query.create(oqlQuery);
//			query.bind(studentType.getState());
//				
//			List result = (List) query.execute();
//			super.lockRead(result);
//			if (result.size() != 0)
//				return (IStudentKind) result.get(0);
//			return null;
//		} catch (QueryException ex) {
//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//		}

		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentType", studentType);
		
		return (IStudentKind) queryObject(StudentKind.class, criteria);
	
	}
}
