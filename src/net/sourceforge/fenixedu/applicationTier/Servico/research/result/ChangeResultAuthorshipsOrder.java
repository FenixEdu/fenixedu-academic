package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.Authorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangeResultAuthorshipsOrder extends Service {
    public void run(Integer resultId, Integer authorId, Integer offset) throws ExcepcaoPersistencia {
        List<Authorship> resultAuthorships = rootDomainObject.readResultByOID(resultId).getResultAuthorships();
        
        Authorship authorship = (Authorship) rootDomainObject.readAuthorshipByOID(authorId);
        Integer newOrder = authorship.getAuthorOrder() + offset; 
        
        if (newOrder >= 0 && newOrder < resultAuthorships.size()) {
            authorship.setAuthorOrder(newOrder);
        }
        
        for (Authorship author : resultAuthorships) {
            Integer order = author.getAuthorOrder();
            if ((order >= newOrder) && ((order - offset) < resultAuthorships.size()) && (author != authorship)) {
                author.setAuthorOrder(author.getAuthorOrder() - offset);
            }
        }
    }
}
