/*
 * Created on 9/Dez/2003
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWDisciplinaGrupoIleec;
import middleware.middlewareDomain.MWGrupoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinaGrupoIleec;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MWDisciplinaGrupoIleecOJB
    extends ObjectFenixOJB
    implements IPersistentMWDisciplinaGrupoIleec
{

    public MWDisciplinaGrupoIleecOJB()
    {
    }

	public List readByGrupo(MWGrupoIleec grupoILeec) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idGrupo",grupoILeec.getIdGrupo());
		return queryList(MWDisciplinaGrupoIleec.class,criteria);
	}
	

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(MWDisciplinaGrupoIleec.class, criteria);
    }

}
