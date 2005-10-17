package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteCareers;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.ICareer;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadCareers implements IService {

    public SiteView run(CareerType careerType, String user) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
        final ITeacher teacher = persistentTeacher.readTeacherByUsername(user);

        IPersistentCareer persistentCareer = persistentSuport.getIPersistentCareer();
        List careers = persistentCareer.readAllByTeacherIdAndCareerType(teacher.getIdInternal(),
                careerType);

        List result = (List) CollectionUtils.collect(careers, new Transformer() {
            public Object transform(Object o) {
                ICareer career = (ICareer) o;
                return InfoCareer.newInfoFromDomain(career);
            }
        });
        Collections.sort(result, new BeanComparator("beginYear"));

        final InfoSiteCareers bodyComponent = new InfoSiteCareers();
        bodyComponent.setInfoCareers(result);
        bodyComponent.setCareerType(careerType);

        final InfoTeacher infoTeacher = InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher);
        bodyComponent.setInfoTeacher(infoTeacher);

        SiteView siteView = new SiteView(bodyComponent);
        return siteView;
    }

}
