<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="result" name="result"/>
	<bean:define id="participations" name="result" property="orderedResultParticipations"/>
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="listSchema" name="listSchema" type="java.lang.String"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
	<bean:define id="prepareEdit" value="<%="/resultParticipations/prepareEdit.do?" + parameters%>"/>

	<b><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></b>
	<logic:notEmpty name="participations">
		<fr:edit id="editRole" name="participations" schema="<%= listSchema %>" action="<%= prepareEdit %>">
			<fr:layout name="tabular-row">
				<fr:property name="classes" value="tstyle2 thlight"/>
				<fr:property name="columnClasses" value=",aleft,acenter,aleft"/>
			</fr:layout>
			<fr:destination name="exception" path="<%= prepareEdit + "&editRoles=true"%>"/>
			<fr:destination name="invalid" path="<%= prepareEdit + "&editRoles=true" %>"/>	
			<fr:destination name="cancel" path="<%= prepareEdit %>"/>
		</fr:edit>
	</logic:notEmpty>
</logic:present>