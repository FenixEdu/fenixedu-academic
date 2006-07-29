<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<center>
	<h2><bean:message key="message.masterDegree.reimbursementGuide.editReimbursementGuideSuccess"/></h2>
	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<bean:define id="reimbursementGuideId" name="<%= SessionConstants.REIMBURSEMENT_GUIDE %>" scope="request"/>
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