package ServidorAplicacao.Servico.student;

import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

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

    public InfoStudentCurricularPlan run(Integer studentNumber, TipoCurso degreeType)
            throws ExcepcaoInexistente, FenixServiceException {
        ISuportePersistente sp = null;
        IStudentCurricularPlan studentCurricularPlan = null;
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
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