package ServidorPersistente;

import Dominio.IStudentKind;
import Util.StudentType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentStudentKind extends IPersistentObject {

    IStudentKind readByStudentType(StudentType studentType) throws ExcepcaoPersistencia;

}