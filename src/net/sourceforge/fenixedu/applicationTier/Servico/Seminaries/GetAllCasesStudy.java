/*
 * Created on 3/Set/2003, 14:22:08
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
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