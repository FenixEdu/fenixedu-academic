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
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.IService;

public class CriarTurma implements IService {

    public Object run(final InfoClass infoTurma) throws ExcepcaoPersistencia, ExistingServiceException {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ITurmaPersistente schoolClassDAO = sp.getITurmaPersistente();
        final List<SchoolClass> listSchoolClasses = schoolClassDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(
                infoTurma.getInfoExecutionPeriod().getIdInternal(), 
                infoTurma.getAnoCurricular(), 
                infoTurma.getInfoExecutionDegree().getIdInternal());

        final SchoolClass existingClass = (SchoolClass) CollectionUtils.find(listSchoolClasses,
                new Predicate() {

                    public boolean evaluate(Object arg0) {
                        final SchoolClass schoolClass = (SchoolClass) arg0;
                        return infoTurma.getNome().equalsIgnoreCase(schoolClass.getNome());
                    }

                });

        if (existingClass != null) {
            throw new ExistingServiceException("Duplicate Entry: " + infoTurma.getNome());
        }

        SchoolClass schoolClass = DomainFactory.makeSchoolClass();
        schoolClass.setNome(infoTurma.getNome());
        schoolClass.setAnoCurricular(infoTurma.getAnoCurricular());
        
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();        
        schoolClass.setExecutionDegree(
                executionDegreeDAO.readByDegreeCurricularPlanAndExecutionYear(
                        infoTurma.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName(), 
                        infoTurma.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree().getSigla(), 
                        infoTurma.getInfoExecutionDegree().getInfoExecutionYear().getYear()));

        final IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
        schoolClass.setExecutionPeriod(
                executionPeriodDAO.readByNameAndExecutionYear(
                        infoTurma.getInfoExecutionPeriod().getName(), 
                        infoTurma.getInfoExecutionPeriod().getInfoExecutionYear().getYear()));

        return InfoClass.newInfoFromDomain(schoolClass);
    }

}
