package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author TJBF & PFON
 *  
 */
public class ReadAuthorPublicationsToInsert implements IService {

    /**
     * Executes the service.
     */
    public SiteView run(String user) throws FenixServiceException {
        try {
            InfoSitePublications infoSitePublications = new InfoSitePublications();

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher); 
            infoSitePublications.setInfoTeacher(infoTeacher);

            
            List infoPublications = getInfoPublications(sp, teacher);

            infoSitePublications.setInfoPublications(infoPublications);

            return new SiteView(infoSitePublications);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private List getInfoPublications(ISuportePersistente sp, ITeacher teacher) {

        IPerson pessoa = teacher.getPerson();
        
        List<IPublication> authorPublications = new ArrayList<IPublication>();
        List<IPublication> newAuthorPublications = new ArrayList<IPublication>();
        List<InfoPublication> infoAuthorPublications = new ArrayList<InfoPublication>();
        
        for (IAuthorship authorship : pessoa.getPersonAuthorships()) {
            authorPublications.add(authorship.getPublication());
        } 

        List publicationTeachersInTeachersList = teacher.getTeacherPublications();
        
        List publicationsInTeacherSList = 
            (List) CollectionUtils.collect(publicationTeachersInTeachersList,
                    new Transformer() {
                        public Object transform(Object input) {
                            IPublicationTeacher publicationTeacher = (IPublicationTeacher) input;
                            return publicationTeacher.getPublication();
                        }
                    });

        if (authorPublications != null || authorPublications.size() != PublicationConstants.ZERO_VALUE) {
            if ((publicationsInTeacherSList != null) || (publicationsInTeacherSList.size() != 0)) {

                newAuthorPublications = deletePublicationsTeacherInAuthorPublications(
                        authorPublications, publicationsInTeacherSList);
            }
            infoAuthorPublications = (List) CollectionUtils.collect(newAuthorPublications,
                    new Transformer() {
                        public Object transform(Object o) {
                            IPublication publication = (IPublication) o;
                            IPublication publication2 = publication;
                            publication2.setPublicationString(publication.toString());

                            return InfoPublication.newInfoFromDomain(publication2);
                        }
                    });
        }

        return infoAuthorPublications;
    }

    public List deletePublicationsTeacherInAuthorPublications(List publicationsAuthor,
            List publicationsTeacher) {

        List newPublicationsAuthor = new ArrayList();
        Iterator iteratorTeachers = publicationsTeacher.iterator();
        Iterator iteratorAuthors = publicationsAuthor.iterator();
        Boolean contains = Boolean.FALSE;

        while (iteratorAuthors.hasNext()) {
            IPublication publicationAuthor = (IPublication) iteratorAuthors.next();

            contains = Boolean.FALSE;
            iteratorTeachers = publicationsTeacher.iterator();
            while (iteratorTeachers.hasNext()) {
                IPublication publicationTeacher = (IPublication) iteratorTeachers.next();

                
                if (publicationTeacher.getIdInternal().intValue() == publicationAuthor.getIdInternal()
                        .intValue()) {
                    contains = Boolean.TRUE;
                }
            }
            
            if (contains.booleanValue() == Boolean.FALSE.booleanValue()) {
                newPublicationsAuthor.add(publicationAuthor);
            }

        }

        return newPublicationsAuthor;
    }

}