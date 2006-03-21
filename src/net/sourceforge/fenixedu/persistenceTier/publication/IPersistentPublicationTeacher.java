/*
 * Created on 18/Nov/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.Publication;
import net.sourceforge.fenixedu.domain.research.result.PublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.PublicationArea;

/**
 * @author Ricardo Rodrigues
 *  
 */
public interface IPersistentPublicationTeacher extends IPersistentObject {

    public PublicationTeacher readByTeacherAndPublication(Teacher teacher, Publication publication)
            throws ExcepcaoPersistencia;

    public List readByTeacherAndPublicationArea(Teacher teacher, PublicationArea publicationArea)
            throws ExcepcaoPersistencia;

}
