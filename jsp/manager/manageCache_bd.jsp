<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.manage.cache"/></h2>
<br />

<logic:present name="<%= SessionConstants.NUMBER_CACHED_ITEMS %>"
			   scope="request">
	Número de objectos em cache = 
	<bean:write name="<%= SessionConstants.NUMBER_CACHED_ITEMS %>"/>
	<br /> <br />
	<html:link page="/manageCache.do?method=prepare">
		<bean:message key="label.cache.refresh"/>
	</html:link>
	<br />
	<html:link page="/manageCache.do?method=clearCache">
		<bean:message key="label.cache.clear"/>
	</html:link>
</logic:present>

<logic:notPresent name="<%= SessionConstants.NUMBER_CACHED_ITEMS %>"
			      scope="request">
	Não é possivel determinar o estado da cache.
	<br />
	<html:link page="/manageCache.do?method=prepare">
		<bean:message key="label.cache.refresh"/>
	</html:link>
</logic:notPresent>

