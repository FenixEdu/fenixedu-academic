package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseExecutionYearToEditCandidatesDA;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editCandidates", module = "masterDegreeAdministrativeOffice", input = "/chooseCandidateList_bd.jsp",
        formBean = "listCandidatesForm", functionality = ChooseExecutionYearToEditCandidatesDA.class)
@Forwards({ @Forward(name = "PrepareReady", path = "/masterDegreeAdministrativeOffice/chooseCandidateList_bd.jsp"),
        @Forward(name = "ActionReady", path = "/masterDegreeAdministrativeOffice/editCandidate.do?method=prepareEdit&page=0"),
        @Forward(name = "ChooseCandidate", path = "/masterDegreeAdministrativeOffice/candidateList_bd.jsp") })
@Exceptions(@ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException",
        handler = FenixErrorExceptionHandler.class, type = ExistingActionException.class))
public class EditCandidatesDA extends EditCandidateDA {

}
