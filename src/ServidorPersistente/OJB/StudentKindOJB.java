

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


		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentType", studentType);
		
		return (IStudentKind) queryObject(StudentKind.class, criteria);
	
	}
}
