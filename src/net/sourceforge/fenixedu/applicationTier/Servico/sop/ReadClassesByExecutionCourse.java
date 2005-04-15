package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author LuisCruz & Sara Ribeiro
 */
public class ReadClassesByExecutionCourse implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                .readByOID(ExecutionCourse.class, infoExecutionCourse.getIdInternal());

        final List classes = sp.getITurmaPersistente().readByExecutionCourse(executionCourse);

        final List infoClasses = (List) CollectionUtils.collect(classes, new Transformer() {
            public Object transform(Object arg0) {
                final ISchoolClass schoolClass = (ISchoolClass) arg0;
                final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);

                final IExecutionDegree executionDegree = schoolClass.getExecutionDegree();
                final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                infoClass.setInfoExecutionDegree(infoExecutionDegree);

                final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
                infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

                final IDegree degree = degreeCurricularPlan.getDegree();
                final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                infoDegreeCurricularPlan.setInfoDegree(infoDegree);

                return infoClass;
            }
        });

        return infoClasses;
    }
}