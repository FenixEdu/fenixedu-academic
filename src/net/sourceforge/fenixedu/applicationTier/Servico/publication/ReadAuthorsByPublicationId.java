/*
 * Created on Jun 7, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReadAuthorsByPublicationId implements IService {

    public List run(Integer publicationId, IUserView userView) throws ExcepcaoPersistencia {
        List authors = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentPublication persitentPublication = sp.getIPersistentPublication();

        IPublication publication = (IPublication) persitentPublication.readByOID(Publication.class,
                publicationId);

        List publicationAuthors = new ArrayList(publication.getPublicationAuthors());

        //Será que o ordenamento dos autores deve ser feito a este nível ou a nível da apresentação
        Collections.sort(publicationAuthors, new BeanComparator("order"));

        authors = (List) CollectionUtils.collect(publicationAuthors, new Transformer() {
            public Object transform(Object obj) {
                IPublicationAuthor pa = (IPublicationAuthor) obj;
                return pa.getAuthor();
            }
        });

        return authors;
    }

}
