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
import DataBeans.support.InfoFAQEntry;
import Dominio.support.IFAQEntry;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFAQEntries;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class ReadFAQEntries implements IService {

    public Collection run() throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentFAQEntries dao = sp.getIPersistentFAQEntries();

        List faqEntries = dao.readAll();
        return CollectionUtils.collect(faqEntries, new Transformer() {
            public Object transform(Object arg0) {
                IFAQEntry faqEntry = (IFAQEntry) arg0;
                return InfoFAQEntry.newInfoFromDomain(faqEntry);
            }
        });
    }

}