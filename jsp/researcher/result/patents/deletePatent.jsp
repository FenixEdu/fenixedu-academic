<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="result" name="result"/>
	<bean:define id="requestParameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.management.title"/></em>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.delete.useCase.title"/></h3>
	
	<%-- Action Messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<jsp:include page="patentInfo.jsp"></jsp:include>
	<br/>
	<br/>
	
	<fr:form action="<%= "/resultPatents/delete.do?" + requestParameters %>">
		<html:submit property="confirm">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
		</html:submit>
		<html:submit property="cancel">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:submit>
	</fr:form>	
</logic:present>
