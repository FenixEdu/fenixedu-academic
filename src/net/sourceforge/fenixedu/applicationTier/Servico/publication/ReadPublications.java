/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

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
                        return InfoPublication.newInfoFromDomain(publication2);
                        //return Cloner.copyIPublication2InfoPublication(publication2);
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