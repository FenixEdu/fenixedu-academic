/*
 * Created on Jun 11, 2004
 * 
 */
package ServidorAplicacao.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.publication.InfoPublication;
import DataBeans.publication.InfoSitePublications;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.publication.IPublication;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;
import constants.publication.PublicationConstants;

/**
 * @author TJBF & PFON
 * 
 */
public class DeletePublicationInTeacherList implements IService {

	public DeletePublicationInTeacherList() {
	}

	public SiteView run(Integer teacherId, Integer publicationId) throws FenixServiceException {
		InfoSitePublications infoSitePublications = new InfoSitePublications();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherId);
			InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
			infoSitePublications.setInfoTeacher(infoTeacher);

			IPersistentPublication persistentPublication = sp.getIPersistentPublication();
			//IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class,
				//	publicationId);

			List infoPublications = getInfoPublications(teacher, persistentTeacher, publicationId,
					persistentPublication);

			List infoPublicationCientifics = getInfoPublicationsType(infoPublications,
					PublicationConstants.CIENTIFIC);

			List infoPublicationDidatics = getInfoPublicationsType(infoPublications,
					PublicationConstants.DIDATIC);

			infoSitePublications.setInfoCientificPublications(infoPublicationCientifics);
			infoSitePublications.setInfoDidaticPublications(infoPublicationDidatics);

			return new SiteView(infoSitePublications);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	public List getInfoPublications(ITeacher teacher, IPersistentTeacher persistentTeacher, Integer publicationId, IPersistentPublication persistentPublication)
			throws FenixServiceException, ExcepcaoPersistencia {

		List publications = teacher.getTeacherPublications();
		List newPublications = new ArrayList();
		List infoPublications = new ArrayList();
		Boolean contains = Boolean.FALSE;

		IPublication publicationToRemove = null;
		Iterator iterator = publications.iterator();
		int i = 0;
		int iToRemove = -1;
		while (iterator.hasNext()) {
			IPublication publicationTeacher2 = (IPublication) iterator.next();
			if (publicationTeacher2.getIdInternal().intValue() == publicationId.intValue()) {
				contains = Boolean.TRUE;
				publicationToRemove = publicationTeacher2;
				iToRemove = i;
			}
			i++;
		}

		if (!contains.booleanValue()) {
			throw new NotExistingServiceException();
		}

		int kToRemove = -1;
		for (int k = 0; k < publicationToRemove.getPublicationTeachers().size(); k++) {
			ITeacher teacher2 = (ITeacher) publicationToRemove.getPublicationTeachers().get(k);
			if (teacher.getIdInternal().equals(teacher2.getIdInternal())) {
				kToRemove = k;
				System.out.println("k= " + k);
			} 
		}
		
//		IPublication publicationToRemove = null;
//		iterator = publications.iterator();
//		while (iterator.hasNext()) {
//			IPublication publicationTeacher = (IPublication) iterator.next();
//			if (publicationTeacher.getIdInternal().intValue() != publicationId.intValue()) {
//				newPublications.add(publicationTeacher);
//			} else {
//				publicationToRemove = (IPublication) publicationTeacher;
//			}
//		}

			//teacher.setTeacherPublications(newPublications);

			persistentPublication.simpleLockWrite(publicationToRemove);
			
			
			System.out.println("before teacher.getTeacherPublications().size()= " + teacher.getTeacherPublications().size());
			System.out.println("before publicationToRemove.getTeachers.size()= " + publicationToRemove.getPublicationTeachers().size());
			publicationToRemove.getPublicationTeachers().remove(kToRemove);

			
			System.out.println("after publicationToRemove.getTeachers.size()= " + publicationToRemove.getPublicationTeachers().size());
			
			persistentTeacher.simpleLockWrite(teacher);
			teacher.getTeacherPublications().remove(iToRemove);
			System.out.println("after teacher.getTeacherPublications().size()= " + teacher.getTeacherPublications().size());
			
			List newPublicationsEdited = teacher.getTeacherPublications();
			infoPublications = (List) CollectionUtils.collect(newPublicationsEdited, new Transformer() {
				public Object transform(Object object) {
					IPublication publication = (IPublication) object;
					//IPublication publication2 = publication;
					//publication2.setPublicationString(publication.toString());
					return Cloner.copyIPublication2InfoPublication(publication);
				}
			});

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