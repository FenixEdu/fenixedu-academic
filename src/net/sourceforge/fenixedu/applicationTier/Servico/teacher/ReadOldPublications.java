/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteOldPublications;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IOldPublication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.util.OldPublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadOldPublications implements IService {

    public SiteView run(OldPublicationType oldPublicationType, String user) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
        ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);

        IPersistentOldPublication persistentOldPublication = persistentSuport
                .getIPersistentOldPublication();
        List publications = persistentOldPublication.readAllByTeacherAndOldPublicationType(teacher,
                oldPublicationType);

        List result = (List) CollectionUtils.collect(publications, new Transformer() {
            public Object transform(Object o) {
                IOldPublication oldPublication = (IOldPublication) o;
                return InfoOldPublication.newInfoFromDomain(oldPublication);
            }
        });

        InfoSiteOldPublications bodyComponent = new InfoSiteOldPublications();
        bodyComponent.setInfoOldPublications(result);
        bodyComponent.setOldPublicationType(oldPublicationType);
        bodyComponent.setInfoTeacher(infoTeacher);

        SiteView siteView = new SiteView(bodyComponent);
        return siteView;
    }

}