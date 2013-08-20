<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="participations" name="result" property="orderedResultParticipations"/>
<bean:define id="result" name="result"/>
<bean:define id="resultId" name="result" property="externalId"/>
<bean:define id="listSchema" name="listSchema" type="java.lang.String"/>
<bean:define id="parameters" value="<%="resultId=" + resultId + "&amp;resultType=" + result.getClass().getSimpleName()%>"/>
	<logic:present name="unit">
	<bean:define id="unitID" name="unit" property="externalId"/>
	<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
</logic:present>
<bean:define id="prepareEdit" value="<%="/resultParticipations/prepareEdit.do?" + parameters%>"/>
<bean:define id="prepareEditRoles" value="<%="/resultParticipations/prepareEditRoles.do?" + parameters%>"/>
<bean:define id="prepareAlterOrder" value="<%="/resultParticipations/prepareAlterOrder.do?" + parameters%>"/>
<bean:define id="remove" value="<%="/resultParticipations/remove.do?" + parameters%>"/>

<p class="mbottom05"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></strong></p>

<logic:notPresent name="deleteConfirmation">
		
		<logic:greaterThan name="result" property="resultParticipationsCount" value="1">
		  <bean:define id="showOptions" value="true" toScope="request"/>
		</logic:greaterThan>
		<logic:lessThan name="result" property="resultParticipationsCount" value="2">
	  		<logic:equal name="result" property="isPossibleSelectPersonRole" value="true">
				  <bean:define id="showOptions" value="true" toScope="request"/>
	  		</logic:equal>
		</logic:lessThan>

		<logic:greaterThan name="result" property="resultParticipationsCount" value="1">
			<html:link page="<%= prepareAlterOrder %>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.alterOrder"/></html:link>
		</logic:greaterThan>
		<logic:equal name="result" property="isPossibleSelectPersonRole" value="true">
			<logic:greaterThan name="result" property="resultParticipationsCount" value="1">|</logic:greaterThan>
			<html:link page="<%= prepareEditRoles %>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.editRoles"/></html:link>
		</logic:equal>

</logic:notPresent>

<logic:empty name="participations">
	<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultParticipation.emptyList"/></em></p>
</logic:empty>	

<logic:notEmpty name="participations">
	<fr:view name="participations" schema="<%= listSchema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight"/>
			<fr:property name="columnClasses" value="acenter,,,aleft,"/>
			<fr:property name="sortBy" value="personOrder"/>
			
			<fr:property name="link(remove)" value="<%= remove %>"/>
			<fr:property name="param(remove)" value="externalId/participationId"/>
			<fr:property name="key(remove)" value="link.remove"/>
			<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			<fr:property name="visibleIf(remove)" value="canBeRemoved"/>
		</fr:layout>
	</fr:view>

	
	<logic:present name="deleteConfirmation">
		<bean:define id="deleteConfirmationId" name="deleteConfirmation"/>

		<fr:form action="<%= remove + "&amp;participationId=" + deleteConfirmationId %>">
			<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.participation.remove"/></b></p>
			<p><span class="warning0"><bean:message bundle="RESEARCHER_RESOURCES" key="label.participation.removeWarning"/></span></p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
			</html:submit>
		</fr:form>
	</logic:present>
</logic:notEmpty>
