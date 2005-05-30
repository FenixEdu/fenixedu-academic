/*
 * CriarTurma.java
 *
 * Created on 25 de Outubro de 2002, 18:34
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o CriarTurma
 * 
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CriarTurma implements IService {

    public Object run(final InfoClass infoTurma) throws ExcepcaoPersistencia, ExistingServiceException {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ISchoolClass turma = Cloner.copyInfoClass2Class(infoTurma);

        final ITurmaPersistente classDAO = sp.getITurmaPersistente();
        final List listClasses = classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(turma
                .getExecutionPeriod().getIdInternal(), turma.getAnoCurricular(), turma
                .getExecutionDegree().getIdInternal());

        final ISchoolClass existingClass = (ISchoolClass) CollectionUtils.find(listClasses,
                new Predicate() {

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
        turma.setExecutionDegree(executionDegreeDAO.readByDegreeCurricularPlanAndExecutionYear(turma
                .getExecutionDegree().getDegreeCurricularPlan().getName(), turma.getExecutionDegree()
                .getDegreeCurricularPlan().getDegree().getSigla(), turma.getExecutionDegree()
                .getExecutionYear().getYear()));

        turma.setExecutionPeriod(executionPeriodDAO
                .readByNameAndExecutionYear(turma.getExecutionPeriod().getName(), turma
                        .getExecutionPeriod().getExecutionYear().getYear()));

        return InfoClass.newInfoFromDomain(turma);
    }

}