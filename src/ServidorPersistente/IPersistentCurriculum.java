/*
 * ICurriculumPersistente.java
 *
 * Created on 6 de Janeiro de 2003, 21:21
 */

package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurriculum;

public interface IPersistentCurriculum extends IPersistentObject {
	
	public ICurriculum readCurriculumByCurricularCourse(ICurricularCourse curricularCourse)	throws ExcepcaoPersistencia;
    public void lockWrite(ICurriculum curriculum) throws ExcepcaoPersistencia;
    public void delete(ICurriculum curriculum) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
	public List readAll()throws ExcepcaoPersistencia;
	
}