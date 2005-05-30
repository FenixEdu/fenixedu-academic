/*
 * Created on 18/Nov/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.PublicationArea;

/**
 * @author Ricardo Rodrigues
 *  
 */
public interface IPersistentPublicationTeacher extends IPersistentObject {

    public IPublicationTeacher readByTeacherAndPublication(ITeacher teacher, IPublication publication)
            throws ExcepcaoPersistencia;

    public List readByTeacherAndPublicationArea(ITeacher teacher, PublicationArea publicationArea)
            throws ExcepcaoPersistencia;

}
