/*
 * Created on Nov 11, 2003 by jpvl
 *
 */
package ServidorPersistente.degree.finalProject;

import java.util.List;

import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentDegreeFinalProjectOrientation extends IPersistentObject
{ 

	/**
	 * Reads the teacher dfp orientations list along years.
	 * @param teacher
	 * @return List 
	 */
	public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

}
