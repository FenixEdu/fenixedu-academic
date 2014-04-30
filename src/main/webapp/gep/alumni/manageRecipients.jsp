<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.alumni" bundle="GEP_RESOURCES"/></em>
<h2><bean:message key="title.alumni.recipients.management" bundle="GEP_RESOURCES"/></h2>
					   
<bean:define id="url"><%= request.getContextPath() %>/messaging/emails.do?method=newEmail</bean:define>
<ul>
	<li><html:link href="<%= url %>"><bean:message key="link.alumni.sendEmail" bundle="GEP_RESOURCES"/></html:link></li>	
	<li><html:link page="/alumni.do?method=prepareAddRecipients"><bean:message key="link.alumni.recipients.add" bundle="GEP_RESOURCES"/></html:link></li>
	<li><html:link page="/alumni.do?method=prepareRemoveRecipients"><bean:message key="link.alumni.recipients.remove" bundle="GEP_RESOURCES"/></html:link></li>
</ul>
