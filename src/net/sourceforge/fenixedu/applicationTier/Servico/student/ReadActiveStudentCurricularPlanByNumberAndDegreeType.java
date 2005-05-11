package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * Created on 24/Set/2003, 11:26:29
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 24/Set/2003, 11:26:29
 * 
 */
public class ReadActiveStudentCurricularPlanByNumberAndDegreeType implements IService {

    public InfoStudentCurricularPlan run(Integer studentNumber, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IStudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente()
                .readActiveByStudentNumberAndDegreeType(studentNumber, degreeType);

        if (studentCurricularPlan != null) {
            return InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan
                    .newInfoFromDomain(studentCurricularPlan);
        }
        return null;
    }

}