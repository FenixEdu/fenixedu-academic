/*
 * Created on 3/Set/2003, 15:35:33
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.Seminaries.InfoEquivalency;
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
 * Created at 3/Set/2003, 15:35:33
 * 
 */
public class GetAllEquivalencies implements IServico
{
    private static GetAllEquivalencies service = new GetAllEquivalencies();
    /**
     * The singleton access method of this class.
     **/
    public static GetAllEquivalencies getService()
    {
        return service;
    }
    /**
     * The actor of this class.
     **/
    private GetAllEquivalencies()
    {
    }
    /**
     * Returns The Service Name */
    public final String getNome()
    {
        return "Seminaries.GetAllEquivalencies";
    }
    public List run() throws BDException
    {
        List infoEquivalencies = new LinkedList();
        try
        {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCurricularCourseEquivalency persistentEquivalency =
                persistenceSupport.getIPersistentSeminaryCurricularCourseEquivalency();
            List equivalencies = persistentEquivalency.readAll();
            for (Iterator iterator = equivalencies.iterator(); iterator.hasNext();)
            {
                ICourseEquivalency equivalency = (ICourseEquivalency) iterator.next();
                
                //CLONER
                //infoEquivalencies.add(Cloner.copyIEquivalency2InfoEquivalency(equivalency));
                infoEquivalencies.add(InfoEquivalency.newInfoFromDomain(equivalency));
            }
        } catch (ExcepcaoPersistencia ex)
        {
            throw new BDException(
                "Got an error while trying to retrieve mutiple modalities from the database",
                ex);
        }
        return infoEquivalencies;
    }
}
