<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="participations" name="result" property="orderedResultParticipations"/>
	<bean:define id="result" name="result"/>
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>

	<!-- Action paths definitions -->
	<bean:define id="prepareEdit" value="<%="/resultParticipations/prepareEdit.do?" + parameters%>"/>
	<bean:define id="create" value="<%="/resultParticipations/create.do?" + parameters%>"/>
	<bean:define id="cancel" value="<%="/resultParticipations/backToResult.do?" + parameters%>"/>
	<logic:equal name="bean" property="beanExternal" value="true">
		<bean:define id="cancel" value="<%=prepareEdit%>"/>
	</logic:equal>
		
	<!-- Schema definitions -->
	<bean:define id="createSchema" name="createSchema" type="java.lang.String"/>
	
	<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultParticipation.add"/></b></p>
 	<logic:equal name="bean" property="beanExternal" value="true">
	 	<p><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultParticipation.addExternal"/></p>
 	</logic:equal>
 	
 	<%-- From to Create Participation --%>
	<fr:edit id="bean" name="bean" schema="<%= createSchema %>" action="<%= create %>">
		<fr:layout name="tabular">
	        <fr:property name="classes" value="style1"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
		<fr:destination name="exception" path="<%= prepareEdit %>"/>	
		<fr:destination name="invalid" path="<%= prepareEdit %>"/>	
		<fr:destination name="cancel" path="<%= cancel %>"/>	
	</fr:edit>
</logic:present>