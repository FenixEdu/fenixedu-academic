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

//	public void delete(ICurricularCourseGroup ccGroup) throws ExcepcaoPersistencia
//	{
//		super.delete(ccGroup);
//	}
//
//	public void deleteAll() throws ExcepcaoPersistencia
//	{
//		try
//		{
//			String oqlQuery = "select all from " + CurricularCourseGroup.class.getName();
//			super.deleteAll(oqlQuery);
//		}
//		catch (ExcepcaoPersistencia ex)
//		{
//			throw ex;
//		}
//	}
//
//	public void lockWrite(ICurricularCourseGroup curricularCourseGroupToWrite)
//		throws ExcepcaoPersistencia, ExistingPersistentException
//	{
//
//		ICurricularCourseGroup curricularCourseGroupFromDB = null;
//
//		// If there is nothing to write, simply return.
//		if (curricularCourseGroupToWrite == null)
//		{
//			return;
//		}
//
//		// Read curricular course group from database.
//		curricularCourseGroupFromDB =
//			this.readByBranchAndAreaType(
//				curricularCourseGroupToWrite.getBranch(),
//				curricularCourseGroupToWrite.getAreaType());
//
//		// If curricular course group is not in database, then write it.
//		if (curricularCourseGroupFromDB == null)
//		{
//			super.lockWrite(curricularCourseGroupToWrite);
//			// else If the curricular course group is mapped to the database, then write any existing changes.
//		}
//		else if (
//			(curricularCourseGroupToWrite instanceof CurricularCourseGroup)
//				&& ((CurricularCourseGroup) curricularCourseGroupFromDB).getIdInternal().equals(
//					((CurricularCourseGroup) curricularCourseGroupToWrite).getIdInternal()))
//		{
//			super.lockWrite(curricularCourseGroupToWrite);
//			// else Throw an already existing exception
//		}
//		else
//			throw new ExistingPersistentException();
//	}

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
}