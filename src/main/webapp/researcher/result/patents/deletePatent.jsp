<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="resultId" name="result" property="externalId"/>
<bean:define id="result" name="result"/>
<bean:define id="requestParameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>

<em>Patentes</em> <!-- tobundle -->
<h2>Apagar Patente</h2> <!-- tobundle -->

<p class="mvert2"><span class="warning0"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.delete.useCase.title"/></span></p>

<%-- Action Messages --%>
<logic:messagesPresent name="messages" message="true">
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
	</html:messages>
</logic:messagesPresent>

<jsp:include page="patentInfo.jsp"></jsp:include>

<fr:form action="<%= "/resultPatents/delete.do?" + requestParameters %>">
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:submit>
	</p>
</fr:form>	
