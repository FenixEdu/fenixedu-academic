<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="resultParticipations" name="result" property="resultParticipations"/>
	<bean:define id="resultParticipationsCount" name="result" property="resultParticipationsCount"/>	

	<bean:define id="newParticipationsSchema" value="result.participations" type="java.lang.String"/>
	<logic:present name="participationsSchema">
		<bean:define id="newParticipationsSchema" name="participationsSchema" type="java.lang.String"/>
	</logic:present>

	<bean:define id="newSchemaInternalPersonCreation" value="resultParticipation.internalPerson.creation" type="java.lang.String"/>
	<logic:present name="schemaInternalPersonCreation">
		<bean:define id="newSchemaInternalPersonCreation" name="schemaInternalPersonCreation" type="java.lang.String"/>
	</logic:present>
	<bean:define id="newSchemaExternalPersonSimpleCreation" value="resultParticipation.externalPerson.simpleCreation" type="java.lang.String"/>
	<logic:present name="schemaExternalPersonSimpleCreation">
		<bean:define id="newSchemaExternalPersonSimpleCreation" name="schemaExternalPersonSimpleCreation" type="java.lang.String"/>
	</logic:present>
	<bean:define id="newSchemaExternalPersonFullCreation" value="resultParticipation.externalPerson.fullCreation" type="java.lang.String"/>
	<logic:present name="schemaExternalPersonFullCreation">
		<bean:define id="newSchemaExternalPersonFullCreation" name="schemaExternalPersonFullCreation" type="java.lang.String"/>
	</logic:present>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>

	<%-- TITLE --%>		
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.participations.useCaseTitle"/></h2>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Participation List --%>
	<logic:empty name="resultParticipations">
 		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.emptyList"/></em></p>
 	</logic:empty>	
	<logic:notEmpty name="resultParticipations">
		<logic:equal name="resultParticipationsCount" value="1">
			<fr:view name="resultParticipations" schema="result.participations" layout="tabular">
				<fr:layout><fr:property name="sortBy" value="personOrder"/></fr:layout>
			</fr:view>
		</logic:equal>
		<logic:greaterThan name="resultParticipationsCount" value="1">
			<fr:edit 	name="resultParticipations" schema="<%= newParticipationsSchema %>" layout="tabular-editable"
						action="<%= "/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + resultId %>"  >
				<fr:hidden slot="changedBy" name="UserView" property="person.name"/>
				<fr:layout>
					<fr:property name="sortBy" value="personOrder"/>
					<fr:property name="link(moveDown)" value="<%= "/result/resultParticipationManagement.do?method=changePersonsOrder" +
						        								  "&resultId=" + resultId.toString() +
						        								  "&resultType=" + resultType +
						        								  "&offset=1"%>"/>
					<fr:property name="param(moveDown)" value="idInternal/oid"/>
					<fr:property name="key(moveDown)" value="link.moveDown"/>
					<fr:property name="bundle(moveDown)" value="RESEARCHER_RESOURCES"/>
					<fr:property name="order(moveDown)" value="1"/>
					<fr:property name="excludedFromLast(moveDown)" value="true"/>
					
					<fr:property name="link(moveUp)" value="<%= "/result/resultParticipationManagement.do?method=changePersonsOrder" + 
					        									"&resultId=" + resultId.toString() +
					        									"&resultType=" + resultType +
					        									"&offset=-1"  %>"/>
					<fr:property name="param(moveUp)" value="idInternal/oid"/>
					<fr:property name="key(moveUp)" value="link.moveUp"/>
					<fr:property name="bundle(moveUp)" value="RESEARCHER_RESOURCES"/>
					<fr:property name="order(moveUp)" value="2"/>
					<fr:property name="excludedFromFirst(moveUp)" value="true"/>
		
					<fr:property name="link(removeParticipation)" value="<%="/result/resultParticipationManagement.do?method=removeParticipation"+ 
					        												"&resultId=" + resultId +
																			"&resultType=" + resultType %>"/>
					<fr:property name="param(removeParticipation)" value="idInternal/participationId"/>
					<fr:property name="key(removeParticipation)" value="link.remove"/>
					<fr:property name="bundle(removeParticipation)" value="RESEARCHER_RESOURCES"/>
					<fr:property name="order(removeParticipation)" value="3"/>
					<fr:property name="visibleIf(removeParticipation)" value="isNotLastResultParticipation"/>
				</fr:layout>
		 	</fr:edit>
	 	</logic:greaterThan>
 	</logic:notEmpty>
 	
 	<%-- Create new Result Participation (internal person) --%>
	<logic:notPresent name="external">
		<h3>
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.addNewPerson"/>
			<html:link page="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithSimpleBean&external=false&resultId=" + resultId + "&resultType=" + resultType %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.internal" />
			</html:link>				
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.or" />
			<html:link page="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithSimpleBean&external=true&resultId=" + resultId + "&resultType=" + resultType %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.external" />
			</html:link>				
		</h3>
	</logic:notPresent>
 	<%-- Create new Result Participation (external person) --%>
	<logic:present name="external">
		<logic:equal name="external" value="false">
			<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.addNewInternalPerson"/></h3>
			<fr:edit 	id="simpleBean" name="simpleBean" schema="<%= newSchemaInternalPersonCreation %>"
						action="<%="/result/resultParticipationManagement.do?method=createParticipationInternalPerson&resultId=" + resultId + "&resultType=" + resultType %>">
				<fr:destination name="invalid" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithSimpleBean&external=false&resultId=" + resultId + "&resultType=" + resultType %>"/>	
				<fr:destination name="cancel" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + resultId + "&resultType=" + resultType %>"/>	
			</fr:edit>
		</logic:equal>
		<logic:equal name="external" value="true">
			<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.addNewExternalPerson"/></h3>
			<logic:present name="simpleBean">
				<fr:edit 	id="simpleBean" name="simpleBean" schema="<%= newSchemaExternalPersonSimpleCreation %>"
							action="<%= "/result/resultParticipationManagement.do?method=createParticipationExternalPerson&external=true&resultId=" + resultId + "&resultType=" + resultType %>">
					<fr:destination name="invalid" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithSimpleBean&external=true&resultId=" + resultId + "&resultType=" + resultType %>"/>	
					<fr:destination name="cancel" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + resultId + "&resultType=" + resultType %>"/>	
				</fr:edit>						
			</logic:present>
			<logic:present name="fullBean">
				<fr:edit 	id="fullBean" name="fullBean" schema="<%= newSchemaExternalPersonFullCreation %>"
							action="<%="/result/resultParticipationManagement.do?method=createParticipationExternalPerson&resultId=" + resultId + "&resultType=" + resultType %>">
					<fr:destination name="invalid" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipationWithFullBean&external=true&resultId=" + resultId + "&resultType=" + resultType %>"/>
					<fr:destination name="cancel" path="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + resultId + "&resultType=" + resultType %>"/>	
				</fr:edit>
			</logic:present>
		</logic:equal>
	</logic:present>
	<br/>
	
	<%-- Go to previous page --%>
	<html:link page="<%="/result/resultParticipationManagement.do?method=backToResult&resultId=" + resultId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>
<br/>