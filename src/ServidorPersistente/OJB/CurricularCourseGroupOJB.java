/*
 * Created on 25/Nov/2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularCourseGroup;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
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

	public ICurricularCourseGroup readByBranchAndAreaType(IBranch branch, AreaType areaType) throws ExcepcaoPersistencia
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("keyBranch", branch.getIdInternal());
		crit.addEqualTo("areaType", areaType);
		return (ICurricularCourseGroup) queryObject(CurricularCourseGroup.class, crit);
	}

    public List readByBranch(IBranch branch) throws ExcepcaoPersistencia
    {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyBranch", branch.getIdInternal());
		return queryList(CurricularCourseGroup.class, criteria);
    }

    public ICurricularCourseGroup readByBranchAndCurricularCourse(IBranch branch, ICurricularCourse curricularCourse) throws ExcepcaoPersistencia
    {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("branch.idInternal", branch.getIdInternal());
    	criteria.addEqualTo("curricularCourses.idInternal", curricularCourse.getIdInternal());
    	return (ICurricularCourseGroup) queryObject(CurricularCourseGroup.class, criteria);
    }

}