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
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
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

    public Object run(ISiteComponent bodyComponent, String executionYearName,
            String executionPeriodName, String degreeInitials, String nameDegreeCurricularPlan,
            String className, Integer curricularYear, Integer classId) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        final IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
        final ITurmaPersistente persistentSchoolClass = sp.getITurmaPersistente();

        ExecutionYear executionYear = persistentExecutionYear
                .readExecutionYearByName(executionYearName);

        ExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                executionPeriodName, executionYear.getYear());

        ExecutionDegree executionDegree = executionDegreeDAO
                .readByDegreeCurricularPlanAndExecutionYear(nameDegreeCurricularPlan, degreeInitials,
                        executionYear.getYear());
        PublicSiteComponentBuilder componentBuilder = PublicSiteComponentBuilder.getInstance();
        SchoolClass domainClass;
        if (classId == null) {
            domainClass = getDomainClass(className, curricularYear, executionPeriod, executionDegree, sp);
            if (domainClass == null) {
                throw new NonExistingServiceException();
            }
        } else {

            domainClass = (SchoolClass) persistentSchoolClass.readByOID(SchoolClass.class, classId);
        }
        bodyComponent = componentBuilder.getComponent(bodyComponent, domainClass);
        SiteView siteView = new SiteView(bodyComponent);

        return siteView;
    }

    private SchoolClass getDomainClass(String className, Integer curricularYear,
            ExecutionPeriod executionPeriod, ExecutionDegree executionDegree, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        ITurmaPersistente persistentClass = sp.getITurmaPersistente();
        SchoolClass domainClass = null;
        List domainList = new ArrayList();
        if (curricularYear == null) {
            domainClass = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod(className,
                    executionDegree.getIdInternal(), executionPeriod.getIdInternal());

        } else {
            if (className == null && curricularYear == null) {

                domainList = persistentClass.readByExecutionDegreeAndDegreeAndExecutionPeriod(
                        executionDegree.getIdInternal(), executionDegree.getDegreeCurricularPlan()
                                .getDegree().getIdInternal(), executionPeriod.getIdInternal());

                if (domainList.size() != 0) {
                    domainClass = (SchoolClass) domainList.get(0);
                }
            }
        }
        return domainClass;
    }
}