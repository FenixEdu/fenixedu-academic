/*
 * Created on 2004/08/30
 * 
 */
package ServidorAplicacao.Servico.manager;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.support.InfoGlossaryEntry;
import Dominio.support.IGlossaryEntry;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGlossaryEntries;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class ReadGlossaryEntries implements IService {

    public Collection run() throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentGlossaryEntries dao = sp.getIPersistentGlossaryEntries();

        List glossaryEntries = dao.readAll();
        return CollectionUtils.collect(glossaryEntries, new Transformer() {
            public Object transform(Object arg0) {
                IGlossaryEntry glossaryEntry = (IGlossaryEntry) arg0;
                return InfoGlossaryEntry.newInfoFromDomain(glossaryEntry);
            }
        });
    }

}