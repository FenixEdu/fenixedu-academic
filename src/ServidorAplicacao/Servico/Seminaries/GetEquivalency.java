/*
 * Created on 4/Ago/2003, 13:05:42
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;
import DataBeans.Seminaries.InfoEquivalency;
import DataBeans.util.Cloner;
import Dominio.Seminaries.CourseEquivalency;
import Dominio.Seminaries.ICourseEquivalency;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 4/Ago/2003, 13:05:42
 * 
 */
public class GetEquivalency implements IServico
{
	private static GetEquivalency service= new GetEquivalency();
	/**
	 * The singleton access method of this class.
	 **/
	public static GetEquivalency getService()
	{
		return service;
	}
	/**
	 * The actor of this class.
	 **/
	private GetEquivalency()
	{
	}
	/**
	 * Returns The Service Name */
	public final String getNome()
	{
		return "Seminaries.GetEquivalency";
	}
	public InfoEquivalency run(Integer equivalencyID) throws BDException
	{
		InfoEquivalency infoEquivalency= null;
		try
		{
			ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
			IPersistentSeminaryCurricularCourseEquivalency persistentEquivalency=
				persistenceSupport.getIPersistentSeminaryCurricularCourseEquivalency();
			ICourseEquivalency equivalency= new CourseEquivalency();
			equivalency.setIdInternal(equivalencyID);
			equivalency= (ICourseEquivalency) persistentEquivalency.readByOId(equivalency, false);
			if (equivalency != null)
				infoEquivalency= Cloner.copyIEquivalency2InfoEquivalency(equivalency);
           
		}
		catch (ExcepcaoPersistencia ex)
		{
			throw new BDException(
				"Got an error while trying to retrieve an seminary/curricular course equivalency from the database",
				ex);
		}
		return infoEquivalency;
	}
}
