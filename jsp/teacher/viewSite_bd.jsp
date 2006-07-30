<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p>
<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:messagesPresent property="error.exception.notAuthorized">
	<span class="error"><!-- Error messages go here -->
		<bean:message key="label.notAuthorized.courseInformation" />
	</span>	
</logic:messagesPresent>

<logic:messagesNotPresent property="error.exception.notAuthorized">
	<img src="<%= request.getContextPath() %>/images/title_adminDisc.gif" alt="<bean:message key="title_adminDisc" bundle="IMAGE_RESOURCES" />" />
	<p><bean:message key="label.instructions" /></p>
</logic:messagesNotPresent>