/*
 * Created on 25/May/2004
 *  
 */
package ServidorAplicacao.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.publication.InfoPublication;
import DataBeans.publication.InfoSitePublications;
import Dominio.IPerson;
import Dominio.ITeacher;
import Dominio.publication.Author;
import Dominio.publication.IPublication;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentAuthor;
import constants.publication.PublicationConstants;

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
            

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            
            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);

            List infoPublications = getInfoPublications(sp, teacher);

            List infoPublicationsDidactic = getInfoPublicationsType(infoPublications,
                    PublicationConstants.DIDATIC);

            List infoPublicationsCientific = getInfoPublicationsType(infoPublications,
                    PublicationConstants.CIENTIFIC);

            InfoSitePublications infoSitePublications = new InfoSitePublications();
            infoSitePublications.setInfoTeacher(infoTeacher);
            infoSitePublications.setInfoDidaticPublications(infoPublicationsDidactic);
            infoSitePublications.setInfoCientificPublications(infoPublicationsCientific);

            return new SiteView(infoSitePublications);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private List getInfoPublications(ISuportePersistente sp, ITeacher teacher)
            throws ExcepcaoPersistencia {

        IPerson pessoa = teacher.getPerson();
        Integer keyPerson = pessoa.getIdInternal();

        IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();
        Author author = persistentAuthor.readAuthorByKeyPerson(keyPerson);

        List publications = new ArrayList();
        List infoPublications = new ArrayList();

        if (author == null) {
            Author newAuthor = new Author();
            newAuthor.setKeyPerson(keyPerson);
            newAuthor.setPerson(pessoa);
            persistentAuthor.lockWrite(newAuthor);

        } else {
            publications = author.getPublications();
        }

        if (publications != null || publications.size() != PublicationConstants.ZERO_VALUE) {
            infoPublications = (List) CollectionUtils.collect(publications, new Transformer() {
                        public Object transform(Object o) {
                            IPublication publication = (IPublication) o;
                            IPublication publication2 = publication;
                            publication2.setPublicationString(publication.toString());
                            InfoPublication infoPublication = new InfoPublication();
                            infoPublication.copyFromDomain(publication2);
                            return infoPublication;
                        }
                    });
        }

        return infoPublications;
    }

    List getInfoPublicationsType(List infoPublications, Integer typePublication) {

        List newInfoPublications = new ArrayList();

        if (infoPublications != null || infoPublications.size() != PublicationConstants.ZERO_VALUE) {
            Iterator iterator = infoPublications.iterator();
            while (iterator.hasNext()) {
                InfoPublication infoPublication = (InfoPublication) iterator.next();
                if (infoPublication.getDidatic().intValue() == typePublication.intValue()) {
                    newInfoPublications.add(infoPublication);
                }
            }
        }
        return newInfoPublications;
    }

}