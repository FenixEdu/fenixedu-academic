/*
 * Created on 1/Set/2003, 14:47:35
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCandidacyWithCaseStudyChoices;
import Dominio.Seminaries.ICandidacy;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 1/Set/2003, 14:47:35
 * 
 */
public class ReadCandidacies implements IServico
{
	private static ReadCandidacies service= new ReadCandidacies();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadCandidacies getService()
	{
		return service;
	}
	/**
	 * The actor of this class.
	 **/
	private ReadCandidacies()
	{
	}
	/**
	 * Returns The Service Name */
	public final String getNome()
	{
		return "Seminaries.ReadCandidacies";
	}
	public List run(
		Integer modalityID,
		Integer seminaryID,
		Integer themeID,
		Integer case1Id,
		Integer case2Id,
		Integer case3Id,
		Integer case4Id,
		Integer case5Id,
		Integer curricularCourseID,
		Integer degreeID,
        Boolean approved)
		throws BDException
	{
		List infoCandidacies= new LinkedList();
		try
		{
			ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
			IPersistentSeminaryCandidacy persistentCandidacy= persistenceSupport.getIPersistentSeminaryCandidacy();
			List candidacies=
				persistentCandidacy.readByUserInput(
					modalityID,
					seminaryID,
					themeID,
					case1Id,
					case2Id,
					case3Id,
					case4Id,
					case5Id,
					curricularCourseID,
					degreeID,
                    approved);
			for (Iterator iterator= candidacies.iterator(); iterator.hasNext();)
			{
				ICandidacy candidacy= (ICandidacy) iterator.next();
				
				//CLONER
				//InfoCandidacy infoCandidacy= Cloner.copyICandicacy2InfoCandidacy(candidacy);
				InfoCandidacy infoCandidacy = InfoCandidacyWithCaseStudyChoices.newInfoFromDomain(candidacy);
				
				infoCandidacies.add(infoCandidacy);
			}
		}
		catch (ExcepcaoPersistencia ex)
		{
			throw new BDException(
				"Got an error while trying to retrieve mutiple candidacies from the database",
				ex);
		}
		return infoCandidacies;
	}
}
