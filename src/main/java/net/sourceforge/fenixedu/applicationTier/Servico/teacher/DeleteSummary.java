/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Summary;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 *         21/Jul/2003 fenix-head ServidorAplicacao.Servico.teacher
 * 
 */
public class DeleteSummary extends FenixService {

    public Boolean run(ExecutionCourse executionCourse, Summary summary, Professorship professorship)
            throws FenixServiceException {

        if (summary == null) {
            throw new InvalidArgumentsServiceException();
        }

        summary.delete();
        return true;
    }

}