/*
 * Created on 9/Dez/2003
 *
 */
package ServidorPersistente;

import Dominio.ICurricularCourseGroup;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularCourseScopeGroup;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentCurricularCourseScopeGroup
{
    public ICurricularCourseScopeGroup readByCurricularCouseGroupAndCurricularCourseScope(
        ICurricularCourseGroup ccGroup,
        ICurricularCourseScope ccScope)
        throws ExcepcaoPersistencia;

}
