/*
 * Created on Feb 18, 2004
 *  
 */
package ServidorAplicacao.Filtro.publication;

import java.util.Iterator;

import Dominio.ITeacher;
import Dominio.publication.IAuthor;
import Dominio.publication.IPublication;
import Dominio.publication.Publication;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentAuthor;
import ServidorPersistente.publication.IPersistentPublication;
import Util.RoleType;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentPublication persistentPublication = sp.getIPersistentPublication();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            IPublication publication = (IPublication) persistentPublication.readByOID(
                    Publication.class, objectId);
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            IAuthor author = persistentAuthor.readAuthorByKeyPerson(teacher.getPerson().getIdInternal());
            System.out.println("author we're looking for: "+author.getKeyPerson());
            
            //check if the teacher is any of the owners of the publication
            Iterator iterator = publication.getPublicationAuthors().iterator();
            while (iterator.hasNext()) {
                IAuthor ownerAuthor = (IAuthor) iterator.next();
                System.out.println("teachernumber: "+ownerAuthor.getKeyPerson());
                if (ownerAuthor.getKeyPerson().equals(author.getKeyPerson()))
                        return true;
            }
            return false;
            
            
        } catch (ExcepcaoPersistencia e) {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            return false;
        }
    }
}