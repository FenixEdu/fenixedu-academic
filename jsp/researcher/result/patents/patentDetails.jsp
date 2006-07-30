<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true" bundle="RESEARCHER_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present role="RESEARCHER">
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.patentsManagement.title"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.patentDetailsUseCase.title"/></h3>

	<%-- Last Modification Date --%>
	<fr:view name="patent" schema="result.modifyedBy" layout="tabular"></fr:view>

	<%-- Participations --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h3>
	<fr:view name="patent" property="resultParticipations" schema="result.participations" layout="tabular">
		<fr:layout>
			<fr:property name="sortBy" value="personOrder"/>
		</fr:layout>
	</fr:view>
	<br/>
	
	<%-- Data --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></h3>
	<fr:view name="patent" schema="patent.viewEditData">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
 	<br/>
	
	<%-- Event Associations --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.eventsTitle"/></h3>
	<fr:view name="patent" property="resultEventAssociations" layout="tabular" schema="resultEventAssociation.summary"/>
	<logic:empty name="patent" property="resultEventAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.emptyList"/></em></p>
	</logic:empty>
 	<br/>
	
	<%-- Unit Associations --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.unitsTitle"/></h3>
	<fr:view name="patent" property="resultUnitAssociations" layout="tabular" schema="resultUnitAssociation.summary"/>
	<logic:empty name="patent" property="resultUnitAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.emptyList"/></em></p>
	</logic:empty>
	<br/>
	
	<html:link page="<%= "/patents/patentsManagement.do?method=listPatents" %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>
<br/>