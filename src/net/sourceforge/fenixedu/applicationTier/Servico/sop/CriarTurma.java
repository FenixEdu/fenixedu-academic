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
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CriarTurma extends Service {

    public Object run(final InfoClass infoClass) throws ExcepcaoPersistencia, ExistingServiceException {
        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoClass.getInfoExecutionDegree().getIdInternal());
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoClass.getInfoExecutionPeriod().getIdInternal());
        final Set<SchoolClass> classes = executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(executionPeriod, infoClass.getAnoCurricular());

        final SchoolClass existingClass = (SchoolClass) CollectionUtils.find(classes,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        final SchoolClass schoolClass = (SchoolClass) arg0;
                        return infoClass.getNome().equalsIgnoreCase(schoolClass.getNome());
                    }

                });

        if (existingClass != null) {
            throw new ExistingServiceException("Duplicate Entry: " + infoClass.getNome());
        }

        SchoolClass schoolClass = DomainFactory.makeSchoolClass();
        schoolClass.setNome(infoClass.getNome());
        schoolClass.setAnoCurricular(infoClass.getAnoCurricular());
        
        final IPersistentExecutionDegree executionDegreeDAO = persistentSupport.getIPersistentExecutionDegree();        
        schoolClass.setExecutionDegree(
                executionDegreeDAO.readByDegreeCurricularPlanAndExecutionYear(
                		infoClass.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName(), 
                		infoClass.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree().getSigla(), 
                		infoClass.getInfoExecutionDegree().getInfoExecutionYear().getYear()));

        final IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();
        schoolClass.setExecutionPeriod(
                executionPeriodDAO.readByNameAndExecutionYear(
                		infoClass.getInfoExecutionPeriod().getName(), 
                		infoClass.getInfoExecutionPeriod().getInfoExecutionYear().getYear()));

        return InfoClass.newInfoFromDomain(schoolClass);
    }

}
