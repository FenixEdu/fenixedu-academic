/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorPersistente.teacher;

import java.util.List;

import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentOldPublication extends IPersistentObject {

    List readAllByTeacherAndOldPublicationType(ITeacher teacher, OldPublicationType oldPublicationType)
            throws ExcepcaoPersistencia;
}