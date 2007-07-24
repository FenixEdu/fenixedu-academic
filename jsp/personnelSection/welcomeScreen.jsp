<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.personnelSection" /></h2>

<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session.getAttribute(net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants.U_VIEW);
	if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessSectionStaffMember(user.getPerson())) {%>
	<h3><bean:message key="title.assiduousness.management" /></h3>
	<p><bean:message key="message.welcome.managementAssiduousness" /></p>
<%	} 
	if (net.sourceforge.fenixedu.domain.ManagementGroups.isPayrollSectionStaff(user.getPerson())) {%>
	<h3><bean:message key="title.payrollSectionStaff" /></h3>
	<p><bean:message key="message.welcome.payrollSectionStaff" /></p>
<%	}	%>
