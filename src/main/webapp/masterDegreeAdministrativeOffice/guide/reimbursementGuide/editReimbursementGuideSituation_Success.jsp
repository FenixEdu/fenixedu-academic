<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<center>
	<h2><bean:message key="message.masterDegree.reimbursementGuide.editReimbursementGuideSuccess"/></h2>
	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<bean:define id="reimbursementGuideId" name="<%= PresentationConstants.REIMBURSEMENT_GUIDE %>" scope="request"/>
	<bean:define id="linkViewReimbursementGuideDetails">
		/viewReimbursementGuideDetails.do?method=view&id=<bean:write name="reimbursementGuideId" />
	</bean:define>
	<bean:define id="linkPrintReimbursementGuide">
		/printReimbursementGuide.do?method=print&id=<bean:write name="reimbursementGuideId" />
	</bean:define>	

	<p>
		<html:link page="<%= linkViewReimbursementGuideDetails %>"><bean:message key="link.masterDegree.administrativeOffice.viewReimbursementGuide"/></html:link>
		&nbsp;-&nbsp;
		<html:link page="<%= linkPrintReimbursementGuide %>" target="_blank"><bean:message key="link.masterDegree.administrativeOffice.printReimbursementGuide"/></html:link>
	</p>

</center>