package ServidorPersistente;

import java.util.List;

import Dominio.ITeacher;



/**
 * Created on 2003/10/27
 * @author João Mota
 * Package ServidorPersistente
 * 
 */
public interface IPersistentCoordinator extends IPersistentObject {

	public List readExecutionDegreesByTeacher(ITeacher teacher)
			throws ExcepcaoPersistencia;	
}
