/*
 * Created on 25/Nov/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourseGroup;
import Util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public interface IPersistentCurricularCourseGroup extends IPersistentObject
{
	public ICurricularCourseGroup readByBranchAndAreaType(IBranch branch, AreaType areaType)
		throws ExcepcaoPersistencia;

	public List readByBranch(IBranch branch) throws ExcepcaoPersistencia;
}
