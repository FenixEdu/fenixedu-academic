/*
 * Created on 13/Nov/2003
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
import DataBeans.publication.InfoSitePublications;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.publication.IPublication;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
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

            Integer typePublication = new Integer(0);

            if (publicationType.equalsIgnoreCase(PublicationConstants.DIDATIC_STRING)) {
                typePublication = PublicationConstants.DIDATIC;
            }

            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            List publications = teacher.getTeacherPublications();
            List newPublications = new ArrayList();
            List result = new ArrayList();

            if ((publications != null) || (publications.size() != PublicationConstants.ZERO_VALUE)) {
                Iterator iterator = publications.iterator();
                while (iterator.hasNext()) {
                    IPublication publication = (IPublication) iterator.next();
                    if (publication.getDidatic().intValue() == typePublication.intValue()) {
                        newPublications.add(publication);
                    }
                }

                result = (List) CollectionUtils.collect(newPublications, new Transformer() {
                    public Object transform(Object o) {
                        IPublication publication = (IPublication) o;
                        IPublication publication2 = publication;
                        publication2.setPublicationString(publication.toString());
                        return Cloner.copyIPublication2InfoPublication(publication2);
                    }
                });
            }

            InfoSitePublications bodyComponent = new InfoSitePublications();
            if (typePublication.intValue() == PublicationConstants.CIENTIFIC.intValue()) {
                bodyComponent.setInfoCientificPublications(result);
            } else {
                bodyComponent.setInfoDidaticPublications(result);
            }
            bodyComponent.setInfoTeacher(infoTeacher);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}