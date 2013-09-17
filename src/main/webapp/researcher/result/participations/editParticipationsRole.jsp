<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="result" name="result"/>
<bean:define id="participations" name="result" property="orderedResultParticipations"/>
<bean:define id="resultId" name="result" property="externalId"/>
<bean:define id="listSchema" name="listSchema" type="java.lang.String"/>
<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
<logic:present name="unit">
	<bean:define id="unitID" name="unit" property="externalId"/>
	<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
</logic:present>
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
