/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package ServidorPersistente;

import Dominio.ITeacherShiftPercentage;

/**
 * @author jpvl
 */
public interface IPersistentTeacherShiftPercentage extends IPersistentObject {
	ITeacherShiftPercentage readByUnique(ITeacherShiftPercentage teacherShiftPercentage) throws ExcepcaoPersistencia;
	void delete (ITeacherShiftPercentage teacherShiftPercentage) throws ExcepcaoPersistencia;
}
