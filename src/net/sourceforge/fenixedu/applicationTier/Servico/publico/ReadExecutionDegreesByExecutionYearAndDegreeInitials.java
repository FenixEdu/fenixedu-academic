package net.sourceforge.fenixedu.applicationTier.Servico.publico;

/**
 * Servico LerSalas
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class ReadExecutionDegreesByExecutionYearAndDegreeInitials implements IServico {

    private static ReadExecutionDegreesByExecutionYearAndDegreeInitials service = new ReadExecutionDegreesByExecutionYearAndDegreeInitials();

    /**
     * The singleton access method of this class.
     */
    public static ReadExecutionDegreesByExecutionYearAndDegreeInitials getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private ReadExecutionDegreesByExecutionYearAndDegreeInitials() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadExecutionDegreesByExecutionYearAndDegreeInitials";
    }

    public InfoExecutionDegree run(InfoExecutionYear infoExecutionYear, String degreeInitials,
            String nameDegreeCurricularPlan) {

        InfoExecutionDegree infoExecutionDegree = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
            IExecutionYear executionYear = Cloner
                    .copyInfoExecutionYear2IExecutionYear(infoExecutionYear);

            IExecutionDegree executionDegree = executionDegreeDAO
                    .readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(degreeInitials,
                            nameDegreeCurricularPlan, executionYear);
            if (executionDegree != null)
                infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return infoExecutionDegree;
    }

}