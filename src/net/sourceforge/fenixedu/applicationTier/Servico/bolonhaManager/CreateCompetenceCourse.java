/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateCompetenceCourse implements IService {

    public ICompetenceCourse run(String name, Double ectsCredits, Boolean basic, Double theoreticalHours,
            Double problemsHours, Double labHours, Double projectHours, Double seminaryHours,
            RegimeType regime, Integer unitID) throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        //TODO: this should be modified to receive ExecutionYear, but for now we just read the '2006/2007'
        IExecutionYear executionYear = persistentSupport.getIPersistentExecutionYear().readExecutionYearByName("2006/2007");
        IUnit unit = (IUnit) persistentSupport.getIPersistentObject().readByOID(Unit.class, unitID);
        if (unit == null) {
            throw new FenixServiceException("error.invalidUnit");
        }        
        return DomainFactory.makeCompetenceCourse(name, "", ectsCredits, basic, theoreticalHours,
                problemsHours, labHours, projectHours, seminaryHours, regime, CurricularStage.DRAFT,
                unit, executionYear);
    }
}
