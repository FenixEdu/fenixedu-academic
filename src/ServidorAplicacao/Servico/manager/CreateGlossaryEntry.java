/*
 * Created on 2004/08/30
 * 
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.support.InfoGlossaryEntry;
import Dominio.support.GlossaryEntry;
import Dominio.support.IGlossaryEntry;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class CreateGlossaryEntry implements IService {

    public void run(InfoGlossaryEntry infoGlossaryEntry) throws FenixServiceException,
            ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentObject dao = sp.getIPersistentObject();

        IGlossaryEntry glossaryEntry = new GlossaryEntry();
        dao.simpleLockWrite(glossaryEntry);
        glossaryEntry.setTerm(infoGlossaryEntry.getTerm());
        glossaryEntry.setDefinition(infoGlossaryEntry.getDefinition());
    }

}