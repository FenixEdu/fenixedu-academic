/*
 * Created on 25/May/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * TJBF & PFON
 *  
 */
public class ReadAuthorPublications implements IServico {
    public ReadAuthorPublications() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadAuthorPublications";
    }

    /**
     * Executes the service.
     */
    public SiteView run(String user) throws FenixServiceException {
        try {
            

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            
            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);

            List infoPublications = getInfoPublications(sp, teacher);

            InfoSitePublications infoSitePublications = new InfoSitePublications();
            infoSitePublications.setInfoTeacher(infoTeacher);
            infoSitePublications.setInfoPublications(infoPublications);

            return new SiteView(infoSitePublications);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private List getInfoPublications(ISuportePersistente sp, ITeacher teacher)
            throws ExcepcaoPersistencia {

        IPerson pessoa = teacher.getPerson();
        Integer keyPerson = pessoa.getIdInternal();

        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        IPerson author = (IPerson)persistentPerson.readByOID(Person.class,keyPerson);

        List publications = new ArrayList();
        List infoPublications = new ArrayList();

        if (author == null) {
            throw new RuntimeException("Author should always exist in this scenario");
            
        }
        
        List<IAuthorship> authorships = author.getPersonAuthorships();
        publications = (List<IPublication>)CollectionUtils.collect(authorships,new Transformer() {
            public Object transform(Object object) {
                IAuthorship authorship = (IAuthorship) object;
                return authorship.getPublication();
            }
        });

        if (publications != null || publications.size() != PublicationConstants.ZERO_VALUE) {
            infoPublications = (List) CollectionUtils.collect(publications, new Transformer() {
                        public Object transform(Object o) {
                            IPublication publication = (IPublication) o;
                            InfoPublication infoPublication = new InfoPublication();
                            infoPublication.copyFromDomain(publication);
                            if (publication != null) {
                                infoPublication.setPublicationString(publication.toString());
                            }
                            return infoPublication;
                        }
                    });
        }

        return infoPublications;
    }

}