/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCareers implements IServico {
    private static ReadCareers service = new ReadCareers();

    /**
     *  
     */
    private ReadCareers() {

    }

    public static ReadCareers getService() {

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadCareers";
    }

    public SiteView run(CareerType careerType, String user) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            InfoTeacher infoTeacher = InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher);

            IPersistentCareer persistentCareer = persistentSuport.getIPersistentCareer();
            List careers = persistentCareer.readAllByTeacherIdAndCareerType(teacher.getIdInternal(), careerType);

            List result = (List) CollectionUtils.collect(careers, new Transformer() {
                public Object transform(Object o) {
                    ICareer career = (ICareer) o;
                    return InfoCareer.newInfoFromDomain(career);
                }
            });

            InfoSiteCareers bodyComponent = new InfoSiteCareers();
            bodyComponent.setInfoCareers(result);
            bodyComponent.setCareerType(careerType);
            bodyComponent.setInfoTeacher(infoTeacher);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}