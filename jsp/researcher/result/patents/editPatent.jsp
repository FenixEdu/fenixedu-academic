<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="patentId" name="patent" property="idInternal"/>
	<bean:define id="resultType" name="patent" property="class.simpleName"/>
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.editPatentUseCase.title"/></h2>
	
	<%-- Last Modification Date --%>
	<fr:view name="patent" schema="result.modifyedBy" layout="tabular"></fr:view>

	<%-- Warnings--%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<%-- Participations --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h3>

	<logic:notEmpty name="patent" property="resultParticipations">
		<fr:view name="patent" property="resultParticipations" schema="result.participations" layout="tabular">
			<fr:layout><fr:property name="sortBy" value="personOrder"/></fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="patent" property="resultParticipations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.emptyList"/></em></p>
	</logic:empty>
	
	<html:link page="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + patentId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.editParticipations" />
	</html:link>
	<br/>	

	<%-- Data --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></h3>
	<fr:view name="patent" schema="patent.viewEditData">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<html:link page="<%="/patents/patentsManagement.do?method=prepareEditPatentData&resultId=" + patentId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.editResultData" />
	</html:link>
	<br/>
	
	<%-- Event Associations --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.eventsTitle"/></h3>
	<fr:view name="patent" property="resultEventAssociations" layout="tabular" schema="resultEventAssociation.summary"/>
	<logic:empty name="patent" property="resultEventAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.emptyList"/></em></p>
	</logic:empty>
	<html:link page="<%="/result/resultAssociationsManagement.do?method=prepareEditEventAssociations&resultId=" + patentId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.editEventsLink" />
	</html:link>
	<br/>
	
	<%-- Unit Associations --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.unitsTitle"/></h3>
	<fr:view name="patent" property="resultUnitAssociations" layout="tabular" schema="resultUnitAssociation.summary"/>
	<logic:empty name="patent" property="resultUnitAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.emptyList"/></em></p>
	</logic:empty>
	<html:link page="<%="/result/resultAssociationsManagement.do?method=prepareEditUnitAssociations&resultId=" + patentId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.editUnitsLink" />
	</html:link>
	<br/>
	<br/>
	
	<%-- Delete Result Patent --%>
	<h3><html:link page="<%= "/patents/patentsManagement.do?method=prepareDeletePatent&resultId=" + patentId + "&from=edit" %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.deletePatentUseCase.title" />
	</html:link></h3>
	<br/>
	<br/>
	
	<%-- Go back --%>
	<html:link page="<%= "/patents/patentsManagement.do?method=listPatents" %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>
