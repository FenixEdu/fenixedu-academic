/*
 * Created on 4/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWAreaEspecializacaoIleec;
import middleware.middlewareDomain.MWAreaSecundariaIleec;
import middleware.middlewareDomain.MWGrupoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWGrupoIleec;
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
public class MWGrupoIleecOJB extends ObjectFenixOJB implements IPersistentMWGrupoIleec
{

	public MWGrupoIleecOJB()
	{
	}

	public List readAll() throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return queryList(MWGrupoIleec.class, criteria);
	}

	public List readByFenixGroup(CurricularCourseGroup ccg)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		String areaName = ccg.getBranch().getName();

		if (ccg.getAreaType().equals(AreaType.SPECIALIZATION_OBJ))
		{
			MWAreaEspecializacaoIleecOJB mwAreasEspecOJB = new MWAreaEspecializacaoIleecOJB();
			MWAreaEspecializacaoIleec mwAreasEspec = mwAreasEspecOJB.readByName(areaName);

			criteria.addEqualTo("id_area_especializacao", mwAreasEspec.getIdAreaEspecializacao());
		}
		else
		{
			MWAreaSecundariaIleecOJB mwAreaSecunOJB = new MWAreaSecundariaIleecOJB();
			MWAreaSecundariaIleec mwAreaSecun = mwAreaSecunOJB.readByName(areaName);

			criteria.addEqualTo("id_area_secundaria", mwAreaSecun.getIdAreaSecundaria());
		}

		return queryList(MWGrupoIleec.class, criteria);
	}

	public List readByAreaName(String areaName) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		Criteria orCriteria = new Criteria();

		MWAreaEspecializacaoIleecOJB mwAreasEspecOJB = new MWAreaEspecializacaoIleecOJB();
		MWAreaEspecializacaoIleec mwAreasEspec = mwAreasEspecOJB.readByName(areaName);
		
		MWAreaSecundariaIleecOJB mwAreaSecunOJB = new MWAreaSecundariaIleecOJB();
		MWAreaSecundariaIleec mwAreaSecun = mwAreaSecunOJB.readByName(areaName);

		criteria.addEqualTo("id_area_especializacao", mwAreasEspec.getIdAreaEspecializacao());
		orCriteria.addEqualTo("id_area_secundaria", mwAreaSecun.getIdAreaSecundaria());
		
		criteria.addOrCriteria(orCriteria);
		return queryList(MWGrupoIleec.class, criteria);

	}
	
	

}
