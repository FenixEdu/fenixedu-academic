/*
 * Created on 13/Nov/2003
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
public interface IPersistentCareer extends IPersistentObject {

	List readAllProfessionalCareersByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
	List readAllTeachingCareerByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
	List readAllByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}
