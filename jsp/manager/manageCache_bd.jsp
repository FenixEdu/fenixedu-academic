<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.manage.cache"/></h2>
<br />

<logic:present name="<%= SessionConstants.NUMBER_CACHED_ITEMS %>" scope="request">
	<bean:message key="cache.domain.number"/>
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

<logic:notPresent name="<%= SessionConstants.NUMBER_CACHED_ITEMS %>" scope="request">
	<bean:message key="cache.domain.state.undetermined"/>
	<br />
	<html:link page="/manageCache.do?method=prepare">
		<bean:message key="label.cache.refresh"/>
	</html:link>
</logic:notPresent>

<logic:present name="<%= SessionConstants.NUMBER_CACHED_RESPONSES %>" scope="request">
	<br /> <br /><br /> <br />
	<bean:message key="cache.response.number"/>:
	<bean:write name="<%= SessionConstants.NUMBER_CACHED_RESPONSES %>"/>
	<br />
	<logic:present name="<%= SessionConstants.CACHED_RESPONSES_TIMEOUT %>" scope="request">
		<bean:message key="cache.response.refresh.timeout"/>:
		<bean:define id="currentRefreshValue"><bean:write name="<%= SessionConstants.CACHED_RESPONSES_TIMEOUT %>"/></bean:define>
		<html:form action="/manageCache" focus="executionDegreeOID">
			<html:hidden property="method" value="setResponseRefreshTimeout"/>
			<html:hidden property="page" value="1"/>
			<html:hidden property="responseRefreshTimeout" value="<%= currentRefreshValue %>"/>
			<html:text
				property="responseRefreshTimeout"
				size="6"
				onchange='document.manageCache.submit();'
				/>
			<bean:message key="cache.response.refresh.timeout.units"/>
		</html:form>
	</logic:present>
	<br /> <br />
	<html:link page="/manageCache.do?method=prepare">
		<bean:message key="label.cache.refresh"/>
	</html:link>
	<br />
	<html:link page="/manageCache.do?method=clearResponseCache">
		<bean:message key="label.cache.clear"/>
	</html:link>
</logic:present>
<logic:notPresent name="<%= SessionConstants.NUMBER_CACHED_RESPONSES %>" scope="request">
</logic:notPresent>
