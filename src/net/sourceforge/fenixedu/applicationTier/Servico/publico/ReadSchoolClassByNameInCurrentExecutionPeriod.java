/*
 * Created on Oct 25, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.dto.SchoolClassDTO;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 *  
 */
public class ReadSchoolClassByNameInCurrentExecutionPeriod implements IService {

    /**
     *  
     */
    public ReadSchoolClassByNameInCurrentExecutionPeriod() {
        super();
    }

    public SchoolClassDTO run(final String schoolClassName) throws FenixServiceException {
        try {
            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
                    .getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            ISchoolClass schoolClass = (ISchoolClass) CollectionUtils.find(executionPeriod.getSchoolClasses(),
                    new Predicate() {

                        public boolean evaluate(Object arg0) {
                            ISchoolClass schoolClass = (ISchoolClass) arg0;
                            return schoolClass.getNome().equals(schoolClassName);
                        }
                    });
            if (schoolClass != null) {
                IDegreeCurricularPlan degreeCurricularPlan = schoolClass.getExecutionDegree()
                        .getDegreeCurricularPlan();
                IDegree degree = degreeCurricularPlan.getDegree();
                SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
                schoolClassDTO.setExecutionPeriodId(executionPeriod.getIdInternal());
                schoolClassDTO.setSchoolClassId(schoolClass.getIdInternal());
                schoolClassDTO.setSchoolClassName(schoolClass.getNome());
                schoolClassDTO.setDegreeCurricularPlanId(degreeCurricularPlan.getIdInternal());
                schoolClassDTO.setDegreeCurricularPlanName(degreeCurricularPlan.getName());
                schoolClassDTO.setDegreeId(degree.getIdInternal());
                schoolClassDTO.setDegreeInitials(degree.getSigla());
                return schoolClassDTO;
            }
            return null;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}