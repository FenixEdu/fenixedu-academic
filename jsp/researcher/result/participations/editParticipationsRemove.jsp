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
	<bean:define id="listSchema" name="listSchema" type="java.lang.String"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
	<bean:define id="prepareEdit" value="<%="/resultParticipations/prepareEdit.do?" + parameters%>"/>
	<bean:define id="prepareEditRoles" value="<%="/resultParticipations/prepareEditRoles.do?" + parameters%>"/>
	<bean:define id="prepareAlterOrder" value="<%="/resultParticipations/prepareAlterOrder.do?" + parameters%>"/>
	<bean:define id="remove" value="<%="/resultParticipations/remove.do?" + parameters%>"/>
	
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></b>
	<logic:empty name="participations">
 		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultParticipation.emptyList"/></em></p>
 	</logic:empty>	
	<logic:notEmpty name="participations">
		<fr:view name="participations" schema="<%= listSchema %>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
				<fr:property name="columnClasses" value="acenter,,,aleft,"/>
				<fr:property name="sortBy" value="personOrder"/>
				
				<fr:property name="link(remove)" value="<%= remove %>"/>
				<fr:property name="param(remove)" value="idInternal/participationId"/>
				<fr:property name="key(remove)" value="link.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="visibleIfNot(remove)" value="isLastParticipation"/>
			</fr:layout>
		</fr:view>
		
		<logic:notPresent name="deleteConfirmation">
			<logic:greaterThan name="result" property="resultParticipationsCount" value="1">
				<html:link page="<%= prepareAlterOrder %>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.alterOrder"/></html:link>
			</logic:greaterThan>
			<logic:equal name="result" property="isPossibleSelectPersonRole" value="true">
				<html:link page="<%= prepareEditRoles %>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.editRoles"/></html:link>
			</logic:equal>
		</logic:notPresent>
		
		<logic:present name="deleteConfirmation">
			<bean:define id="deleteConfirmationId" name="deleteConfirmation"/>

			<fr:form action="<%= remove + "&participationId=" + deleteConfirmationId %>">
				<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.participation.remove"/></b></p>
				<p><bean:message bundle="RESEARCHER_RESOURCES" key="label.participation.removeWarning"/></p>
				<html:submit property="confirm">
					<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
				</html:submit>
				<html:submit property="cancel">
					<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
				</html:submit>
			</fr:form>
		</logic:present>
 	</logic:notEmpty>
</logic:present>