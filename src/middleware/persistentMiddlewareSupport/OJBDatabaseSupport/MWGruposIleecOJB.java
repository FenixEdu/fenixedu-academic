/*
 * Created on 4/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWAreasEspecializacaoIleec;
import middleware.middlewareDomain.MWAreaSecundariaIleec;
import middleware.middlewareDomain.MWGruposIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWGruposIleec;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularCourseGroup;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import Util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MWGruposIleecOJB extends ObjectFenixOJB implements IPersistentMWGruposIleec
{

	public MWGruposIleecOJB()
	{
	}

	public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return queryList(MWGruposIleec.class, criteria);
	}

	public List readByFenixGroup(CurricularCourseGroup ccg)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		String areaName = ccg.getBranch().getName();

		if (ccg.getAreaType().equals(AreaType.SPECIALIZATION_OBJ))
		{
			MWAreasEspecializacaoIleecOJB mwAreasEspecOJB = new MWAreasEspecializacaoIleecOJB();
			MWAreasEspecializacaoIleec mwAreasEspec = mwAreasEspecOJB.readByName(areaName);

			criteria.addEqualTo("id_area_especializacao", mwAreasEspec.getIdAreaEspecializacao());
		}
		else
		{
			MWAreaSecundariaIleecOJB mwAreaSecunOJB = new MWAreaSecundariaIleecOJB();
			MWAreaSecundariaIleec mwAreaSecun = mwAreaSecunOJB.readByName(areaName);

			criteria.addEqualTo("id_area_secundaria", mwAreaSecun.getIdAreaSecundaria());
		}

		return queryList(MWGruposIleec.class, criteria);
	}

	public List readByAreaName(String areaName) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		Criteria orCriteria = new Criteria();

		MWAreasEspecializacaoIleecOJB mwAreasEspecOJB = new MWAreasEspecializacaoIleecOJB();
		MWAreasEspecializacaoIleec mwAreasEspec = mwAreasEspecOJB.readByName(areaName);
		
		MWAreaSecundariaIleecOJB mwAreaSecunOJB = new MWAreaSecundariaIleecOJB();
		MWAreaSecundariaIleec mwAreaSecun = mwAreaSecunOJB.readByName(areaName);

		criteria.addEqualTo("id_area_especializacao", mwAreasEspec.getIdAreaEspecializacao());
		orCriteria.addEqualTo("id_area_secundaria", mwAreaSecun.getIdAreaSecundaria());
		
		criteria.addOrCriteria(orCriteria);
		return queryList(MWGruposIleec.class, criteria);

	}
	
	

}
