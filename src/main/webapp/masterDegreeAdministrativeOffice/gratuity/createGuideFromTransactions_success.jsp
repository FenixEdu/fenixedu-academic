<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuide" %>

<center>
	<h2><bean:message key="message.masterDegree.gratuity.createGuideSuccess"/></h2>
	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<bean:define id="guide" name="<%= PresentationConstants.GUIDE %>" scope="request"/>
	
	<bean:define id="linkPrintGuide">
		/printGuide.do?method=print&page=0&year=<bean:write name="guide" property="year" />&number=<bean:write name="guide" property="number" />&version=<bean:write name="guide" property="version" />&copies=2
	</bean:define>	

	<p>
		<html:link page="<%= linkPrintGuide %>" target="_blank"><bean:message key="link.masterDegree.administrativeOffice.printGuide"/></html:link>
	</p>

</center>