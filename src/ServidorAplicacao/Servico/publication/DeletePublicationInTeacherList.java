/*
 * Created on Jun 11, 2004
 * 
 */
package ServidorAplicacao.Servico.publication;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.publication.IPublication;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author TJBF & PFON
 *  
 */
public class DeletePublicationInTeacherList implements IService {

	public DeletePublicationInTeacherList() {
	}

	public void run(Integer teacherId, final Integer publicationId)
			throws ExcepcaoPersistencia {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

		ITeacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class,teacherId);
		persistentTeacher.simpleLockWrite(teacher);
		CollectionUtils.filter(teacher.getTeacherPublications(), new Predicate() {
				public boolean evaluate(Object arg0) {
					IPublication publication = (IPublication) arg0;
					return !publication.getIdInternal().equals(publicationId);
				}});
	}

}