/*
 * Created on 3/Set/2003, 14:22:08
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoCaseStudy;
import Dominio.Seminaries.ICaseStudy;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudy;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 3/Set/2003, 14:22:08
 *  
 */
public class GetAllCasesStudy implements IService {

    public GetAllCasesStudy() {
    }

    public List run() throws BDException {
        List infoCases = new LinkedList();
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCaseStudy persistentCaseStudy = persistenceSupport
                    .getIPersistentSeminaryCaseStudy();
            List cases = persistentCaseStudy.readAll();

            for (Iterator iterator = cases.iterator(); iterator.hasNext();) {
                ICaseStudy caseStudy = (ICaseStudy) iterator.next();

                infoCases.add(InfoCaseStudy.newInfoFromDomain(caseStudy));
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve mutiple case studies from the database", ex);
        }
        return infoCases;
    }

}