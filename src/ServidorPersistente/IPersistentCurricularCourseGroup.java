/*
 * Created on 25/Nov/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
import Dominio.IScientificArea;
import Util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public interface IPersistentCurricularCourseGroup extends IPersistentObject
{
	public List readByBranchAndAreaType(IBranch branch, AreaType areaType) throws ExcepcaoPersistencia;
	public List readByBranch(IBranch branch) throws ExcepcaoPersistencia;
	public ICurricularCourseGroup readByBranchAndCurricularCourseAndAreaType(
		IBranch branch,
		ICurricularCourse curricularCourse,
		AreaType areaType)
		throws ExcepcaoPersistencia;
	public ICurricularCourseGroup readByBranchAndScientificAreaAndAreaType(
		IBranch branch,
		IScientificArea scientificArea,
		AreaType areaType)
		throws ExcepcaoPersistencia;
}
