/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package ServidorPersistente;

import Dominio.IProfessorship;
import Dominio.IShiftProfessorship;
import Dominio.ITurno;

/**
 * @author jpvl
 */
public interface IPersistentShiftProfessorship extends IPersistentObject {
	IShiftProfessorship readByUnique(IShiftProfessorship teacherShiftPercentage) throws ExcepcaoPersistencia;
	void delete (IShiftProfessorship teacherShiftPercentage) throws ExcepcaoPersistencia;
    IShiftProfessorship readByProfessorshipAndShift(IProfessorship professorship, ITurno shift) throws ExcepcaoPersistencia;
}
