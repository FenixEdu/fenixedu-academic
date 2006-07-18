<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.cache"/></h2>
<br />

<logic:present name="<%= SessionConstants.NUMBER_CACHED_ITEMS %>" scope="request">
	<bean:message bundle="MANAGER_RESOURCES" key="cache.domain.number"/>
	<bean:write name="<%= SessionConstants.NUMBER_CACHED_ITEMS %>"/>
	<br /> <br />
	<html:link module="/manager" page="/manageCache.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="label.cache.refresh"/>
	</html:link>
	<br />
<%--
	<html:link module="/manager" page="/manageCache.do?method=clearCache">
--%>
		<bean:message bundle="MANAGER_RESOURCES" key="label.cache.clear"/>
<%--
	</html:link>
--%>
</logic:present>

<logic:notPresent name="<%= SessionConstants.NUMBER_CACHED_ITEMS %>" scope="request">
	<bean:message bundle="MANAGER_RESOURCES" key="cache.domain.state.undetermined"/>
	<br />
	<html:link module="/manager" page="/manageCache.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="label.cache.refresh"/>
	</html:link>
</logic:notPresent>

<logic:present name="<%= SessionConstants.NUMBER_CACHED_RESPONSES %>" scope="request">
	<br /> <br /><br /> <br />
	<bean:message bundle="MANAGER_RESOURCES" key="cache.response.number"/>:
	<bean:write name="<%= SessionConstants.NUMBER_CACHED_RESPONSES %>"/>
	<br />
	<logic:present name="<%= SessionConstants.CACHED_RESPONSES_TIMEOUT %>" scope="request">
		<bean:message bundle="MANAGER_RESOURCES" key="cache.response.refresh.timeout"/>:
		<bean:define id="currentRefreshValue"><bean:write name="<%= SessionConstants.CACHED_RESPONSES_TIMEOUT %>"/></bean:define>
		<html:form action="/manageCache" focus="executionDegreeOID">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setResponseRefreshTimeout"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<html:text
				property="responseRefreshTimeout"
				size="6"
				onchange='document.manageCache.submit();'
				/>
			<bean:message bundle="MANAGER_RESOURCES" key="cache.response.refresh.timeout.units"/>
		</html:form>
	</logic:present>
	<br /> <br />
	<html:link module="/manager" page="/manageCache.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="label.cache.refresh"/>
	</html:link>
	<br />
	<html:link module="/manager" page="/manageCache.do?method=clearResponseCache">
		<bean:message bundle="MANAGER_RESOURCES" key="label.cache.clear"/>
	</html:link>
</logic:present>
<logic:notPresent name="<%= SessionConstants.NUMBER_CACHED_RESPONSES %>" scope="request">
</logic:notPresent>

<br/>
<br />
<html:link module="/manager" page="/manageCache.do?method=loadAllObjectsToCache">
	<bean:message bundle="MANAGER_RESOURCES" key="label.cache.load.all.objects"/>
</html:link>
<br />