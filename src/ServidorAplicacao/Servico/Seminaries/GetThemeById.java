/*
 * Created on 26/Ago/2003, 9:14:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;
import DataBeans.Seminaries.InfoTheme;
import Dominio.Seminaries.ITheme;
import Dominio.Seminaries.Theme;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryTheme;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 26/Ago/2003, 9:14:57
 * 
 */
public class GetThemeById implements IServico
{
	private static GetThemeById service= new GetThemeById();
	/**
	 * The singleton access method of this class.
	 **/
	public static GetThemeById getService()
	{
		return service;
	}
	/**
	 * The actor of this class.
	 **/
	private GetThemeById()
	{
	}
	/**
	 * Returns The Service Name */
	public final String getNome()
	{
		return "Seminaries.GetThemeById";
	}
	public InfoTheme run(Integer themeID) throws BDException
	{
		InfoTheme infoTheme= null;
		if (themeID != null)
		{
			try
			{
				ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
				IPersistentSeminaryTheme persistentTheme= persistenceSupport.getIPersistentSeminaryTheme();
				ITheme theme= (ITheme) persistentTheme.readByOID(Theme.class, themeID);
				
				//CLONER
				//infoTheme= Cloner.copyITheme2InfoTheme(theme);
				infoTheme = InfoTheme.newInfoFromDomain(theme);
			}
			catch (ExcepcaoPersistencia ex)
			{
				throw new BDException(
					"Got an error while trying to retrieve mutiple case studies from the database",
					ex);
			}
		}
		return infoTheme;
	}
}
