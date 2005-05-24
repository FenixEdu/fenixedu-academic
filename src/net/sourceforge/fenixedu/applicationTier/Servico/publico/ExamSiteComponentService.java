/*
 * Created on 6/Mai/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.ExamSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 * 
 */
public class ExamSiteComponentService implements IService {

    public ExamSiteComponentService() {

    }

    public Object run(ISiteComponent bodyComponent, String executionYearName,
            String executionPeriodName, String degreeInitials, String nameDegreeCurricularPlan,
            List curricularYears) throws FenixServiceException {

        SiteView siteView = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            IExecutionYear executionYear = persistentExecutionYear
                    .readExecutionYearByName(executionYearName);

            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    executionPeriodName, executionYear.getYear());

            IExecutionDegree executionDegree = executionDegreeDAO
                    .readByDegreeCurricularPlanAndExecutionYear(nameDegreeCurricularPlan,
                            degreeInitials, executionYearName);
            
            ExamSiteComponentBuilder componentBuilder = ExamSiteComponentBuilder.getInstance();

            bodyComponent = componentBuilder.getComponent(bodyComponent, executionPeriod,
                    executionDegree, curricularYears);
            siteView = new SiteView(bodyComponent);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return siteView;
    }
}