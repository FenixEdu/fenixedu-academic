/*
 * CriarTurma.java
 *
 * Created on 25 de Outubro de 2002, 18:34
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarTurma
 * 
 * @author tfc130
 */
import java.util.List;

import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ISchoolClass;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

import commons.CollectionUtils;

public class CriarTurma implements IService {

    public Object run(final InfoClass infoTurma) throws ExcepcaoPersistencia, ExistingServiceException {

        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        final ISchoolClass turma = Cloner.copyInfoClass2Class(infoTurma);

        final ITurmaPersistente classDAO = sp.getITurmaPersistente();
        final List listClasses = classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(turma
                .getExecutionPeriod(), turma.getAnoCurricular(), turma.getExecutionDegree());

        final ISchoolClass existingClass = (ISchoolClass) CollectionUtils.find(listClasses, new Predicate() {

            public boolean evaluate(Object arg0) {
                final ISchoolClass schoolClass = (ISchoolClass) arg0;
                return infoTurma.getNome().equalsIgnoreCase(schoolClass.getNome());
            }

        });

        if (existingClass != null) {
            throw new ExistingServiceException("Duplicate Entry: " + infoTurma.getNome());
        }

        final IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

        sp.getITurmaPersistente().simpleLockWrite(turma);
        turma.setExecutionDegree(executionDegreeDAO
                .readByDegreeCurricularPlanAndExecutionYear(turma.getExecutionDegree()
                        .getCurricularPlan(), turma.getExecutionDegree().getExecutionYear()));

        turma.setExecutionPeriod(executionPeriodDAO.readByNameAndExecutionYear(turma
                .getExecutionPeriod().getName(), turma.getExecutionPeriod().getExecutionYear()));

        return Cloner.copyClass2InfoClass(turma);
    }

}