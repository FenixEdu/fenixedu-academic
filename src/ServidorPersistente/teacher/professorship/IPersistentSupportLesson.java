/*
 * Created on Nov 23, 2003 by jpvl
 *
 */
package ServidorPersistente.teacher.professorship;

import java.util.List;

import Dominio.IProfessorship;
import Dominio.ISupportLesson;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentSupportLesson extends IPersistentObject
{

    List readByProfessorship(IProfessorship professorship) throws ExcepcaoPersistencia;

    /**
     * @param lesson
     * @return
     */
    ISupportLesson readByUnique(ISupportLesson lesson)  throws ExcepcaoPersistencia;

}
