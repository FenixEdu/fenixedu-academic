<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuide" %>

<center>
	<h2><bean:message key="message.masterDegree.gratuity.createGuideSuccess"/></h2>
	<span class="error"><html:errors/></span>

	<bean:define id="guide" name="<%= SessionConstants.GUIDE %>" scope="request"/>
	
	<bean:define id="linkPrintGuide">
		/printGuide.do?method=print&page=0&year=<bean:write name="guide" property="year" />&number=<bean:write name="guide" property="number" />&version=<bean:write name="guide" property="version" />&copies=2
	</bean:define>	
	<bean:define id="linkViewGuide">
		/chooseGuideDispatchAction.do?method=choose&guideYear=<bean:write name="guide" property="year" />&guideNumber=<bean:write name="guide" property="number" />
	</bean:define>	
	

	<p>
		<html:link page="<%= linkPrintGuide %>" target="_blank"><bean:message key="link.masterDegree.administrativeOffice.printGuide"/></html:link>
		 | 
		<html:link page="<%= linkViewGuide %>" ><bean:message key="link.masterDegree.administrativeOffice.viewGuide"/></html:link>
	</p>

</center>