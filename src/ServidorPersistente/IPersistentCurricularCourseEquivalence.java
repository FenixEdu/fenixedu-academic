package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseEquivalence;


/**
 * @author David Santos in Jun 29, 2004
 */

public interface IPersistentCurricularCourseEquivalence extends IPersistentObject{
  
    public List readByOldCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
    public List readByEquivalentCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
    public ICurricularCourseEquivalence readByEquivalence(ICurricularCourse oldCurricularCourse,ICurricularCourse equivalentCurricularCourse) throws ExcepcaoPersistencia;
}