/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.publication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a>
 * @author <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 */
public class PublicationAuthorAuthorizationFilter extends DomainObjectAuthorizationFilter {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#verifyCondition(ServidorAplicacao.IUserView,
     *      java.lang.Integer)
     */
    protected boolean verifyCondition(IUserView id, Integer objectId) {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentPublication persistentPublication = sp.getIPersistentPublication();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class,
                    objectId);
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            IAuthor author = persistentAuthor.readAuthorByKeyPerson(teacher.getPerson().getIdInternal());

            //check if the teacher is any of the owners of the publication
            List publicationAuthors = new ArrayList(publication.getPublicationAuthors());
            Collections.sort(publicationAuthors, new BeanComparator("order"));
            List authors = (List) CollectionUtils.collect(publicationAuthors, new Transformer() {
                public Object transform(Object obj){
                    IPublicationAuthor pa = (IPublicationAuthor) obj;
                    return pa.getAuthor();
                }
            });
            
            Iterator iterator = authors.iterator();
            while (iterator.hasNext()) {
                IAuthor ownerAuthor = (IAuthor) iterator.next();
                if (ownerAuthor.getPerson() != null
                        && ownerAuthor.getPerson().getIdInternal().equals(
                                author.getPerson().getIdInternal()))
                    return true;
            }
            return false;

        } catch (ExcepcaoPersistencia e) {
            return false;
        }

    }
    
    protected boolean verifyCondition(IUserView id, InfoPublication infoPublication) {
        return verifyCondition(id,infoPublication.getIdInternal());
    }
}