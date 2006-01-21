/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteExternalActivities;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadExternalActivities extends Service {

    public SiteView run(String user) throws ExcepcaoPersistencia {
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        Teacher teacher = persistentTeacher.readTeacherByUsername(user);
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);

        IPersistentExternalActivity persistentExternalActivity = persistentSupport
                .getIPersistentExternalActivity();
        List externalActivities = persistentExternalActivity.readByTeacherId(teacher.getIdInternal());

        List result = (List) CollectionUtils.collect(externalActivities, new Transformer() {
            public Object transform(Object o) {
                ExternalActivity externalActivity = (ExternalActivity) o;
                return InfoExternalActivity.newInfoFromDomain(externalActivity);
            }
        });

        InfoSiteExternalActivities bodyComponent = new InfoSiteExternalActivities();
        bodyComponent.setInfoExternalActivities(result);
        bodyComponent.setInfoTeacher(infoTeacher);

        SiteView siteView = new SiteView(bodyComponent);
        return siteView;
    }

}