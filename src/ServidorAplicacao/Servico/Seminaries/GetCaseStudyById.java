/*
 * Created on 29/Ago/2003, 12:23:33
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;
import DataBeans.Seminaries.InfoCaseStudy;
import Dominio.Seminaries.CaseStudy;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudy;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 29/Ago/2003, 12:23:33
 * 
 */
public class GetCaseStudyById implements IServico
{
	private static GetCaseStudyById service= new GetCaseStudyById();
	/**
	 * The singleton access method of this class.
	 **/
	public static GetCaseStudyById getService()
	{
		return service;
	}
	/**
	 * The actor of this class.
	 **/
	private GetCaseStudyById()
	{
	}
	/**
	 * Returns The Service Name */
	public final String getNome()
	{
		return "Seminaries.GetCaseStudyById";
	}
	public InfoCaseStudy run(Integer id) throws BDException
	{
        InfoCaseStudy infoCaseStudy= null;
		try
		{
			ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
			IPersistentSeminaryCaseStudy persistentCaseStudy= persistenceSupport.getIPersistentSeminaryCaseStudy();
            CaseStudy caseStudy = (CaseStudy) persistentCaseStudy.readByOID(CaseStudy.class,id);
            //CLONER
            //infoCaseStudy = Cloner.copyICaseStudy2InfoCaseStudy(caseStudy);
            infoCaseStudy=InfoCaseStudy.newInfoFromDomain(caseStudy);
		}
		catch (ExcepcaoPersistencia ex)
		{
			throw new BDException(
				"Got an error while trying to retrieve a case study from the database",
				ex);
		}
		return infoCaseStudy;
	}
}
