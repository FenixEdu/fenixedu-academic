/*
 * Created on 15/Nov/2003
 *
 */
package ServidorPersistente.teacher;

import java.util.List;

import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentExternalActivity extends IPersistentObject {
    public List readAllByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}