/*
 * Created on 3/Set/2003, 15:35:33
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.ICourseEquivalency;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 3/Set/2003, 15:35:33
 *  
 */
public class GetAllEquivalencies implements IService {

    public GetAllEquivalencies() {
    }

    public List run() throws BDException {
        List infoEquivalencies = new LinkedList();
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCurricularCourseEquivalency persistentEquivalency = persistenceSupport
                    .getIPersistentSeminaryCurricularCourseEquivalency();
            List equivalencies = persistentEquivalency.readAll();
            for (Iterator iterator = equivalencies.iterator(); iterator.hasNext();) {
                ICourseEquivalency equivalency = (ICourseEquivalency) iterator.next();

                infoEquivalencies.add(InfoEquivalency.newInfoFromDomain(equivalency));
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve mutiple modalities from the database", ex);
        }
        return infoEquivalencies;
    }
}