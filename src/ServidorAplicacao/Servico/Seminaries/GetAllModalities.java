/*
 * Created on 3/Set/2003, 15:10:43
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.Seminaries.IModality;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryModality;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 3/Set/2003, 15:10:43
 * 
 */
public class GetAllModalities implements IServico
{
    private static GetAllModalities service = new GetAllModalities();
    /**
     * The singleton access method of this class.
     **/
    public static GetAllModalities getService()
    {
        return service;
    }
    /**
     * The actor of this class.
     **/
    private GetAllModalities()
    {
    }
    /**
     * Returns The Service Name */
    public final String getNome()
    {
        return "Seminaries.GetAllModalities";
    }
    public List run() throws BDException
    {
        List infoCases = new LinkedList();
        try
        {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryModality persistentModality =
                persistenceSupport.getIPersistentSeminaryModality();
            List cases = persistentModality.readAll();

            for (Iterator iterator = cases.iterator(); iterator.hasNext();)
            {
                IModality modality = (IModality) iterator.next();
                infoCases.add(Cloner.copyIModality2InfoModality(modality));
            }

        } catch (ExcepcaoPersistencia ex)
        {
            throw new BDException(
                "Got an error while trying to retrieve mutiple modalities from the database",
                ex);
        }
        return infoCases;
    }

}
