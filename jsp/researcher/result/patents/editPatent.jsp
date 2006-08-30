<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="patentId" name="patent" property="idInternal"/>
	<bean:define id="resultType" name="patent" property="class.simpleName"/>
	<bean:define id="participations" name="patent" property="resultParticipations"/>
	<bean:define id="documents" name="patent" property="resultDocumentFiles"/>	
	<bean:define id="eventAssociations" name="patent" property="resultEventAssociations"/>
	<bean:define id="unitAssociations" name="patent" property="resultUnitAssociations"/>
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.superUseCase.title"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.edit.useCase.title"/></h2>
	
	<%-- Last Modification Date --%>
	<fr:view name="patent" schema="result.modifyedBy">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
		</fr:layout>
	</fr:view>

	<%-- Warnings--%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<%-- Participations --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h3>
	<logic:empty name="participations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultParticipation.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="participations">
		<fr:view name="participations" schema="result.participations">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="columnClasses" value=",,,acenter"/>
				<fr:property name="sortBy" value="personOrder"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<html:link page="<%="/resultParticipations/prepareEdit.do?resultId=" + patentId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.participations.link" />
	</html:link>
	<br/>	

	<%-- Data --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></h3>
	<fr:view name="patent" schema="patent.viewEditData">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle4"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<html:link page="<%="/resultPatents/prepareEditData.do?resultId=" + patentId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.edit.data" />
	</html:link>
	<br/>
	
	<%-- Documents --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></h3>
	<logic:empty name="documents">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="documents">
		<fr:view name="documents" schema="resultDocumentFile.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="columnClasses" value=",,,acenter"/>
				<fr:property name="sortBy" value="uploadTime=desc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<html:link page="<%="/resultDocumentFiles/prepareEdit.do?resultId=" + patentId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.documents.link" />
	</html:link>
	<br/>
	
	<%-- Event Associations --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></h3>
	<logic:empty name="eventAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="eventAssociations">
		<fr:view name="eventAssociations" schema="resultEventAssociation.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="columnClasses" value=",,,acenter"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<html:link page="<%="/resultAssociations/prepareEditEventAssociations.do?resultId=" + patentId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.eventAssociations.link" />
	</html:link>
	<br/>
	
	<%-- Unit Associations --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></h3>
	<logic:empty name="unitAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="unitAssociations">
		<fr:view name="unitAssociations" schema="resultUnitAssociation.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="columnClasses" value=",,,acenter"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<html:link page="<%="/resultAssociations/prepareEditUnitAssociations.do?resultId=" + patentId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.unitAssociations.link" />
	</html:link>
	<br/>
	<br/>
	
	<%-- Delete Result Patent --%>
	<h3><html:link page="<%= "/resultPatents/prepareDelete.do?resultId=" + patentId + "&from=edit" %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.delete.useCase.title" />
	</html:link></h3>
	<br/>
	<br/>
	
	<%-- Go back --%>
	<html:link page="/resultPatents/management.do">
		<bean:message bundle="RESEARCHER_RESOURCES" key="link.goBackToView"/>
	</html:link>
</logic:present>
