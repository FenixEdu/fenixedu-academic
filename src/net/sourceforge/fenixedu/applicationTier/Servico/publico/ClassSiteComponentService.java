/*
 * Created on 6/Mai/2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.PublicSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 *  
 */
public class ClassSiteComponentService implements IService {

    public ClassSiteComponentService() {

    }

    public Object run(ISiteComponent bodyComponent, String executionYearName,
            String executionPeriodName, String degreeInitials, String nameDegreeCurricularPlan,
            String className, Integer curricularYear, Integer classId) throws FenixServiceException {

        SiteView siteView = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
            ITurmaPersistente persistentSchoolClass = sp.getITurmaPersistente();

            IExecutionYear executionYear = persistentExecutionYear
                    .readExecutionYearByName(executionYearName);

            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    executionPeriodName, executionYear);

            IExecutionDegree executionDegree = executionDegreeDAO
                    .readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(degreeInitials,
                            nameDegreeCurricularPlan, executionYear);
            PublicSiteComponentBuilder componentBuilder = PublicSiteComponentBuilder.getInstance();
            ISchoolClass domainClass;
            if (classId == null) {
                domainClass = getDomainClass(className, curricularYear, executionPeriod,
                        executionDegree, sp);
                if (domainClass == null) {
                    throw new NonExistingServiceException();
                }
            } else {

                domainClass = (ISchoolClass) persistentSchoolClass.readByOID(SchoolClass.class, classId);
            }
            bodyComponent = componentBuilder.getComponent(bodyComponent, domainClass);
            siteView = new SiteView(bodyComponent);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return siteView;
    }

    private ISchoolClass getDomainClass(String className, Integer curricularYear,
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        ITurmaPersistente persistentClass = sp.getITurmaPersistente();
        ISchoolClass domainClass = null;
        List domainList = new ArrayList();
        if (curricularYear == null) {
            domainClass = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod(className,
                    executionDegree, executionPeriod);

        } else {
            if (className == null && curricularYear == null) {

                domainList = persistentClass.readByExecutionDegreeAndDegreeAndExecutionPeriod(
                        executionDegree, executionDegree.getDegreeCurricularPlan().getDegree(),
                        executionPeriod);

                if (domainList.size() != 0) {
                    domainClass = (ISchoolClass) domainList.get(0);
                }
            } else {
                domainClass = new SchoolClass();
                domainClass.setAnoCurricular(curricularYear);
                domainClass.setExecutionDegree(executionDegree);
                domainClass.setExecutionPeriod(executionPeriod);

            }
        }
        return domainClass;
    }
}