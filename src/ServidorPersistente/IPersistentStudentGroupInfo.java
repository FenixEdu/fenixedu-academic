
package ServidorPersistente;


import Dominio.IStudentGroupInfo;
import Util.StudentType;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentStudentGroupInfo extends IPersistentObject{
    
	IStudentGroupInfo readByStudentType(StudentType studentType) throws ExcepcaoPersistencia;

}
