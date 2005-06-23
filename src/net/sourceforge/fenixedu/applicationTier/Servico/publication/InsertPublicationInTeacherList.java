/*
 * Created on Jun 10, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class InsertPublicationInTeacherList implements IService {

    public SiteView run(Integer teacherId, Integer publicationId, String publicationArea)
            throws ExcepcaoPersistencia {
        InfoSitePublications infoSitePublications = new InfoSitePublications();
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherId);
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
        return null;
    }

    public List getInfoPublications(ITeacher teacher, IPublication publication,
            IPersistentTeacher persistentTeacher, IPersistentPublication persistentPublication)
            throws FenixServiceException, ExcepcaoPersistencia {
        List publicationTeachers = teacher.getTeacherPublications();
        List publications = (List) CollectionUtils.collect(publicationTeachers, new Transformer() {
            public Object transform(Object input) {
                IPublicationTeacher publicationTeacher = (IPublicationTeacher) input;
                return publicationTeacher.getPublication();
            }
        });
        List infoPublications = new ArrayList();

        Iterator iterator = publications.iterator();
        while (iterator.hasNext()) {
            IPublication publicationTeacher = (IPublication) iterator.next();
            if (publicationTeacher.getIdInternal().equals(publication.getIdInternal())) {
                throw new ExistingServiceException();
            }
        }

        persistentTeacher.simpleLockWrite(teacher);
        persistentPublication.simpleLockWrite(publication);

        PublicationTeacher publicationTeacher = new PublicationTeacher();
        publicationTeacher.setPublication(publication);
        publicationTeacher.setTeacher(teacher);

        publication.getPublicationTeachers().add(publicationTeacher);
        teacher.getTeacherPublications().add(publicationTeacher);
        List newPublications = teacher.getTeacherPublications();
        infoPublications = (List) CollectionUtils.collect(newPublications, new Transformer() {
            public Object transform(Object object) {
                IPublicationTeacher publicationTeacher = (IPublicationTeacher) object;
                IPublication publication = publicationTeacher.getPublication();
                IPublication publication2 = publication;
                publication2.setPublicationString(publication.toString());
                InfoPublication infoPublication = new InfoPublication();
                infoPublication.copyFromDomain(publication2);
                return infoPublication;
            }
        });

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentPublicationTeacher persistentPublicationTeacher = sp
                .getIPersistentPublicationTeacher();
        persistentPublicationTeacher.simpleLockWrite(publicationTeacher);

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