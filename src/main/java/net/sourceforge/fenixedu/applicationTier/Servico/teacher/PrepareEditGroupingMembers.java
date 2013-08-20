/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author joaosa & rmalo
 */

public class PrepareEditGroupingMembers {

    @Service
    public static List run(String executionCourseCode, String groupingID) throws FenixServiceException {
        final Grouping grouping = AbstractDomainObject.fromExternalId(groupingID);
        if (grouping == null) {
            throw new InvalidArgumentsServiceException();
        }

        final List<Attends> attends = new ArrayList<Attends>();
        final List<InfoStudent> infoStudents = new ArrayList<InfoStudent>();

        for (final ExportGrouping exportGrouping : grouping.getExportGroupings()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                for (final Attends attend : exportGrouping.getExecutionCourse().getAttends()) {
                    if (!attends.contains(attend) && !grouping.getAttends().contains(attend)) {
                        attends.add(attend);
                        infoStudents.add(InfoStudent.newInfoFromDomain(attend.getRegistration()));
                    }
                }
            }
        }
        return infoStudents;
    }
}