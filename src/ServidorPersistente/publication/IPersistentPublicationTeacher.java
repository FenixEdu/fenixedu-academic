/*
 * Created on 18/Nov/2004
 *
 */
package ServidorPersistente.publication;

import java.util.List;

import Dominio.ITeacher;
import Dominio.publication.IPublication;
import Dominio.publication.IPublicationTeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import Util.PublicationArea;

/**
 * @author Ricardo Rodrigues
 *  
 */
public interface IPersistentPublicationTeacher extends IPersistentObject {

    public List readByPublicationId(Integer publicationId) throws ExcepcaoPersistencia;

    public List readByTeacherId(Integer teacherId) throws ExcepcaoPersistencia;

    public IPublicationTeacher readByTeacherAndPublication(ITeacher teacher, IPublication publication)
            throws ExcepcaoPersistencia;

    public List readByTeacherAndPublicationArea(ITeacher teacher, PublicationArea publicationArea)
            throws ExcepcaoPersistencia;

}
