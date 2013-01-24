package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission.manager;

import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission.MarkSheetSearchDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/markSheetManagement", input = "show.markSheetManagement.search.page", attribute = "markSheetManagementForm", formBean = "markSheetManagementForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "searchMarkSheet", path = "/manager/markSheet/markSheetManagement.jsp"),
	@Forward(name = "listMarkSheet", path = "/manager/markSheet/viewMarkSheet.jsp") })
public class MarkSheetSearchDispatchActionForManager extends MarkSheetSearchDispatchAction {
}