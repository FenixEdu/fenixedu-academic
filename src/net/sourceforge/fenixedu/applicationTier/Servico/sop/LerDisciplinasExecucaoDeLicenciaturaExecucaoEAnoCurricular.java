package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular implements IServico {

    private static LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular _servico = new LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular();

    /**
     * The singleton access method of this class.
     */
    public static LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular";
    }

    public List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) {

        List listDCDE = null;
        List listInfoDE = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

            ICurricularYear curricularYearFromDB = (ICurricularYear) sp.getIPersistentCurricularYear()
                    .readByOID(CurricularYear.class, curricularYear);

            listDCDE = executionCourseDAO
                    .readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(executionPeriod,
                            executionDegree, curricularYearFromDB, "");

            Iterator iterator = listDCDE.iterator();
            listInfoDE = new ArrayList();
            while (iterator.hasNext()) {
                IExecutionCourse elem = (IExecutionCourse) iterator.next();

                listInfoDE.add(Cloner.get(elem));

            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return listInfoDE;
    }

}