<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	 

<span class="error"><html:errors/></span>
</br>

<logic:messagesPresent property="error.exception.notAuthorized">
	<span class="error">
		<bean:message key="label.notAuthorized.courseInformation" />
	</span>	
</logic:messagesPresent>

<logic:messagesNotPresent property="error.exception.notAuthorized">
	<img alt="Administração de disciplina" src="<%= request.getContextPath() %>/images/title_adminDisc.gif" />
	<p><bean:message key="label.instructions" /></p>
</logic:messagesNotPresent>