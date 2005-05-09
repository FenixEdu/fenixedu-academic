package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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
public class ReadActiveStudentCurricularPlanByNumberAndDegreeType implements IServico {
    private static ReadActiveStudentCurricularPlanByNumberAndDegreeType servico = new ReadActiveStudentCurricularPlanByNumberAndDegreeType();

    /**
     * The singleton access method of this class.
     */
    public static ReadActiveStudentCurricularPlanByNumberAndDegreeType getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadActiveStudentCurricularPlanByNumberAndDegreeType() {
    }

    /**
     * Returns The Service Name
     */
    public final String getNome() {
        return "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType";
    }

    public InfoStudentCurricularPlan run(Integer studentNumber, DegreeType degreeType)
            throws ExcepcaoInexistente, FenixServiceException {
        ISuportePersistente sp = null;
        IStudentCurricularPlan studentCurricularPlan = null;
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            studentCurricularPlan = sp.getIStudentCurricularPlanPersistente()
                    .readActiveByStudentNumberAndDegreeType(studentNumber, degreeType);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        if (studentCurricularPlan != null)
            infoStudentCurricularPlan = Cloner
                    .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);
        return infoStudentCurricularPlan;
    }
}