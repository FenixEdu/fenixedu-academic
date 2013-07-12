/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteExternalActivities;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadExternalActivities {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Atomic
    public static SiteView run(String user) {
        Teacher teacher = Teacher.readTeacherByUsername(user);
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);

        Collection<ExternalActivity> externalActivities = teacher.getAssociatedExternalActivities();

        List result = (List) CollectionUtils.collect(externalActivities, new Transformer() {
            @Override
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