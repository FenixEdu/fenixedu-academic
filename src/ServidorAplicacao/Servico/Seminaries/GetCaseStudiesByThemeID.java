/*
 * Created on 4/Ago/2003, 18:58:03
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.Seminaries.ICaseStudy;
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
 * Created at 4/Ago/2003, 18:58:03
 * 
 */
public class GetCaseStudiesByThemeID implements IServico
{
    private static GetCaseStudiesByThemeID service = new GetCaseStudiesByThemeID();
    /**
     * The singleton access method of this class.
     **/
    public static GetCaseStudiesByThemeID getService()
    {
        return service;
    }
    /**
     * The actor of this class.
     **/
    private GetCaseStudiesByThemeID()
    {
    }
    /**
     * Returns The Service Name */
    public final String getNome()
    {
        return "Seminaries.GetCaseStudiesByThemeID";
    }
    public List run(Integer themeID) throws BDException
    {
        List infoCases = new LinkedList();
        try
        {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCaseStudy persistentCaseStudy =
                persistenceSupport.getIPersistentSeminaryCaseStudy();
            List cases = persistentCaseStudy.readByThemeID(themeID);

            for (Iterator iterator = cases.iterator(); iterator.hasNext();)
            {
                ICaseStudy caseStudy = (ICaseStudy) iterator.next();
                infoCases.add(Cloner.copyICaseStudy2InfoCaseStudy(caseStudy));
            }

        } catch (ExcepcaoPersistencia ex)
        {
            throw new BDException(
                "Got an error while trying to retrieve mutiple case studies from the database",
                ex);
        }
        return infoCases;
    }
}
