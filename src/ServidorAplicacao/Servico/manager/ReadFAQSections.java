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
import DataBeans.support.InfoFAQSection;
import Dominio.support.IFAQSection;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFAQSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class ReadFAQSections implements IService {

    public Collection run() throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentFAQSection dao = sp.getIPersistentFAQSection();

        List faqSections = dao.readAll();
        return CollectionUtils.collect(faqSections, new Transformer() {
            public Object transform(Object arg0) {
                IFAQSection faqSection = (IFAQSection) arg0;
                return InfoFAQSection.newInfoFromDomain(faqSection);
            }
        });
    }

}