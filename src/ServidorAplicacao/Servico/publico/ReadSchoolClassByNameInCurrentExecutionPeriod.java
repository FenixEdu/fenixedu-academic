/*
 * Created on Oct 25, 2004
 *
 */
package ServidorAplicacao.Servico.publico;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.dto.SchoolClassDTO;
import Dominio.IDegree;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.ISchoolClass;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
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
                        .getCurricularPlan();
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