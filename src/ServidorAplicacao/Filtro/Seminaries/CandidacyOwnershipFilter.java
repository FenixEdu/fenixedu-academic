/*
 * Created on 26/Ago/2003, 13:35:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Filtro.Seminaries;
import Dominio.IStudent;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 26/Ago/2003, 13:35:57
 * 
 */
public class CandidacyOwnershipFilter extends Filtro
{
	public final static CandidacyOwnershipFilter instance= new CandidacyOwnershipFilter();
	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance()
	{
		return instance;
	}
	public void preFiltragem(IUserView id, Object[] argumentos) throws Exception
	{
		Integer candidacyID= (Integer) argumentos[0];
		ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
		IPersistentSeminaryCandidacy persistentCandidacy= persistenceSupport.getIPersistentSeminaryCandidacy();
		//
		IStudent student= persistenceSupport.getIPersistentStudent().readByUsername(id.getUtilizador());
		ICandidacy candidacy= (ICandidacy) persistentCandidacy.readByOID(Candidacy.class, candidacyID);
		//
		if ((candidacy != null)
			&& (candidacy.getStudentIdInternal().intValue() != student.getIdInternal().intValue()))
			throw new NotAuthorizedException();
	}
}
