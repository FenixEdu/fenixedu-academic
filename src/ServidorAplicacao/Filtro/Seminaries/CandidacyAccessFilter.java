/*
 * Created on 26/Ago/2003, 13:35:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Filtro.Seminaries;
import java.util.Collection;
import java.util.Iterator;

import DataBeans.InfoRole;
import Dominio.IStudent;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
import Util.RoleType;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 26/Ago/2003, 13:35:57
 * 
 */
public class CandidacyAccessFilter extends Filtro
{
	public final static CandidacyAccessFilter instance= new CandidacyAccessFilter();
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
		if ((!this.checkCandidacyOwnership(id, argumentos))
			&& (!this.checkCoordinatorRole(id, argumentos)))
			throw new NotAuthorizedException();
	}
	boolean checkCoordinatorRole(IUserView id, Object[] arguments) throws Exception
	{
		boolean result= true;
		Collection roles= id.getRoles();
		Iterator iter= roles.iterator();
		while (iter.hasNext())
		{
			InfoRole role= (InfoRole) iter.next();
            System.out.println("Tenho este role: " + role);            
		} 
		if (((id != null
			&& id.getRoles() != null
			&& !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())))
			|| (id == null)
			|| (id.getRoles() == null))
		{
			result= false;
		}
		return result;
	}
	boolean checkCandidacyOwnership(IUserView id, Object[] arguments) throws Exception
	{
		boolean result= true;
		Integer candidacyID= (Integer) arguments[0];
        System.out.println("#############Vou ler a candidatura " + candidacyID);        
		ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
		IPersistentSeminaryCandidacy persistentCandidacy= persistenceSupport.getIPersistentSeminaryCandidacy();
		//
		IStudent student= persistenceSupport.getIPersistentStudent().readByUsername(id.getUtilizador());
		if (student != null)
		{
			ICandidacy candidacy= (ICandidacy) persistentCandidacy.readByOID(Candidacy.class, candidacyID);
            System.out.println(" O aluno e " + student + "e a candidatura tem o nº " + candidacy.getStudentIdInternal());            
			//
			if ((candidacy != null)
				&& (candidacy.getStudentIdInternal().intValue() != student.getIdInternal().intValue()))
				result= false;
		}
		else
        {
			result= false;
            System.out.println("O aluno e nullllllll !!!");
        }
		return result;
	}
	private RoleType getRoleType()
	{
		return RoleType.SEMINARIES_COORDINATOR;
	}
}
