/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.publication.InfoPublication;
import DataBeans.publication.InfoSitePublications;
import Dominio.ITeacher;
import Dominio.publication.IPublication;
import Dominio.publication.IPublicationTeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublicationTeacher;
import Util.PublicationArea;
import constants.publication.PublicationConstants;

/**
 * @author TJBF & PFON
 *  
 */
public class ReadPublications implements IServico {
    private static ReadPublications service = new ReadPublications();

    /**
     *  
     */
    private ReadPublications() {

    }

    public static ReadPublications getService() {

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadPublications";
    }

    public SiteView run(String user, String publicationType) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);

            IPersistentPublicationTeacher persistentPublicationTeacher = persistentSuport
                    .getIPersistentPublicationTeacher();
            List publicationsTeacher = persistentPublicationTeacher.readByTeacherAndPublicationArea(teacher, PublicationArea
                    .getEnum(publicationType));
            
            List publications = new ArrayList();
            Iterator iter = publicationsTeacher.iterator();
            while(iter.hasNext()){
                IPublicationTeacher publicationTeacher = (IPublicationTeacher)iter.next();
                IPublication publication = publicationTeacher.getPublication();
                publication.setPublicationString(publicationTeacher.getPublication().toString());
                publications.add(InfoPublication.newInfoFromDomain(publication));
            }

            Integer typePublication = new Integer(0);

            if (publicationType.equalsIgnoreCase(PublicationConstants.DIDATIC_STRING)) {
                typePublication = PublicationConstants.DIDATIC;
            }

            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
                //Cloner.copyITeacher2InfoTeacher(teacher);

//            List publications = teacher.getTeacherPublications();
//            List newPublications = new ArrayList();
//            List result = new ArrayList();
//
//            if ((publications != null) || (publications.size() != PublicationConstants.ZERO_VALUE)) {
//                Iterator iterator = publications.iterator();
//                while (iterator.hasNext()) {
//                    IPublication publication = (IPublication) iterator.next();
//                    if (publication.getDidatic().intValue() == typePublication.intValue()) {
//                        newPublications.add(publication);
//                    }
//                }
//
//                result = (List) CollectionUtils.collect(newPublications, new Transformer() {
//                    public Object transform(Object obj) {
//                        IPublication publication = (IPublication) obj;
//                        IPublication publication2 = publication;
//                        publication2.setPublicationString(publication.toString());
//                        return Cloner.copyIPublication2InfoPublication(publication2);
//                    }
//                });
//            }

            InfoSitePublications bodyComponent = new InfoSitePublications();
//            if (typePublication.intValue() == PublicationConstants.CIENTIFIC.intValue()) {
//                bodyComponent.setInfoCientificPublications(result);
//            } else {
//                bodyComponent.setInfoDidaticPublications(result);
//            }

            //TODO o que vai ser mostrado é a juncao das 2, mudar isto
            bodyComponent.setInfoCientificPublications(publications);
            bodyComponent.setInfoDidaticPublications(new ArrayList());
            bodyComponent.setInfoTeacher(infoTeacher);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}