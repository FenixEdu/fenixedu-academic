<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<logic:present name="<%= PresentationConstants.DOCUMENT_REASON_LIST %>">	
	<p>Este certificado destina-se a fins comprovativos de:</p>
	<p><b><logic:iterate id="item" name="<%= PresentationConstants.DOCUMENT_REASON_LIST %>" >
			<bean:message name="item" bundle="ENUMERATION_RESOURCES"/><br />
		</logic:iterate></b></p>
</logic:present>
<logic:notPresent name="<%= PresentationConstants.DOCUMENT_REASON_LIST %>">
	<p>Este certificado destina-se a fins comprovativos.</p>
</logic:notPresent>