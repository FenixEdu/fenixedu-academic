/*
 * Created on 25/Nov/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.AreaCurricularCourseGroup;
import Dominio.CurricularCourseGroup;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
import Dominio.IScientificArea;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import Util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public class CurricularCourseGroupOJB extends ObjectFenixOJB implements IPersistentCurricularCourseGroup
{

	public CurricularCourseGroupOJB()
	{
	}

	public List readByBranchAndAreaType(IBranch branch, AreaType areaType) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyBranch", branch.getIdInternal());
		criteria.addEqualTo("areaType", areaType);
		return queryList(AreaCurricularCourseGroup.class, criteria);
	}

    public List readByBranch(IBranch branch) throws ExcepcaoPersistencia
    {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyBranch", branch.getIdInternal());
		return queryList(CurricularCourseGroup.class, criteria);
    }

    public ICurricularCourseGroup readByBranchAndCurricularCourseAndAreaType(
		IBranch branch,
		ICurricularCourse curricularCourse,
		AreaType areaType)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("branch.idInternal", branch.getIdInternal());
		criteria.addEqualTo("curricularCourses.idInternal", curricularCourse.getIdInternal());
		criteria.addEqualTo("areaType", areaType);
		return (ICurricularCourseGroup) queryObject(AreaCurricularCourseGroup.class, criteria);
	}

	public ICurricularCourseGroup readByBranchAndScientificAreaAndAreaType(
		IBranch branch,
		IScientificArea scientificArea,
		AreaType areaType)
		throws ExcepcaoPersistencia
    {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("branch.idInternal", branch.getIdInternal());
    	criteria.addEqualTo("scientificAreas.idInternal", scientificArea.getIdInternal());
    	criteria.addEqualTo("areaType", areaType);
    	return (ICurricularCourseGroup) queryObject(AreaCurricularCourseGroup.class, criteria);
    }

}