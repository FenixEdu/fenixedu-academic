/*
 * Created on Jun 10, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
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
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.publication.IPublication;
import Dominio.publication.IPublicationTeacher;
import Dominio.publication.Publication;
import Dominio.publication.PublicationTeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;
import ServidorPersistente.publication.IPersistentPublicationTeacher;
import Util.PublicationArea;
import constants.publication.PublicationConstants;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class InsertPublicationInTeacherList implements IServico {

    /**
     *  
     */
    public InsertPublicationInTeacherList() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {

        return "InsertPublicationInTeacherList";
    }

    public SiteView run(Integer teacherId, Integer publicationId, String publicationArea)
            throws FenixServiceException, ExistingServiceException {
        InfoSitePublications infoSitePublications = new InfoSitePublications();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherId);
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            infoSitePublications.setInfoTeacher(infoTeacher);

            IPersistentPublication persistentPublication = sp.getIPersistentPublication();
            IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class,
                    publicationId);

            IPersistentPublicationTeacher persistentPublicationTeacher = sp
                    .getIPersistentPublicationTeacher();
            IPublicationTeacher publicationTeacher = persistentPublicationTeacher
                    .readByTeacherAndPublication(teacher, publication);

            if (publicationTeacher == null)
                publicationTeacher = new PublicationTeacher();

            persistentPublicationTeacher.simpleLockWrite(publicationTeacher);
            publicationTeacher.setPublication(publication);
            publicationTeacher.setTeacher(teacher);
            publicationTeacher.setPublicationArea(PublicationArea.getEnum(publicationArea));
            
            
            
//            List infoPublications = getInfoPublications(teacher, publication, persistentTeacher,
//                    persistentPublication);
//
//            List infoPublicationCientifics = getInfoPublicationsType(infoPublications,
//                    PublicationConstants.CIENTIFIC);
//
//            List infoPublicationDidatics = getInfoPublicationsType(infoPublications,
//                    PublicationConstants.DIDATIC);
//
//            infoSitePublications.setInfoCientificPublications(infoPublicationCientifics);
//            infoSitePublications.setInfoDidaticPublications(infoPublicationDidatics);
//
//            return new SiteView(infoSitePublications);
            
            return null;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    public List getInfoPublications(ITeacher teacher, IPublication publication,
            IPersistentTeacher persistentTeacher, IPersistentPublication persistentPublication)
            throws FenixServiceException {
        List publications = teacher.getTeacherPublications();
        List infoPublications = new ArrayList();

        Iterator iterator = publications.iterator();
        while (iterator.hasNext()) {
            IPublication publicationTeacher = (IPublication) iterator.next();
            if (publicationTeacher.getIdInternal().equals(publication.getIdInternal())) {
                throw new ExistingServiceException();
            }
        }

        try {
            persistentTeacher.simpleLockWrite(teacher);
            persistentPublication.simpleLockWrite(publication);

            publication.getPublicationTeachers().add(teacher);
            teacher.getTeacherPublications().add(publication);
            List newPublications = teacher.getTeacherPublications();
            infoPublications = (List) CollectionUtils.collect(newPublications, new Transformer() {
                public Object transform(Object object) {
                    IPublication publication = (IPublication) object;
                    IPublication publication2 = publication;
                    publication2.setPublicationString(publication.toString());
                    return Cloner.copyIPublication2InfoPublication(publication2);
                }
            });
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
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