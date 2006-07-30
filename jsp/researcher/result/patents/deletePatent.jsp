<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.patentsManagement.title"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.deletePatentUseCase.title"/></h3>
	
	<%-- Last Modification Date --%>
	<fr:view name="patent" schema="result.modifyedBy" layout="tabular"></fr:view>
	<br/>
	
	<%-- Action Messages --%>
	<p><span class="error"><!-- Error messages go here -->
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.deleteResult.warning"/>
	</span></p>
	
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
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.emptyList"/></em>
	</logic:empty>
	<br/>
	
	<%-- Unit Associations --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.unitsTitle"/></h3>
	<fr:view name="patent" property="resultUnitAssociations" layout="tabular" schema="resultUnitAssociation.summary"/>
	<logic:empty name="patent" property="resultUnitAssociations">
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.emptyList"/></em>
	</logic:empty>
	<br/>
	<br/>
		
	<html:form action="/patents/patentsManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deletePatent"/>
		<bean:define id="patentId" name="patent" property="idInternal" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.resultId" property="resultId" value="<%= patentId.toString() %>"/>
		<bean:define id="backTo" value="<%= "this.form.method.value='listPatents'" %>"/>

		<logic:present name="from">
			<bean:define id="backTo" value="<%= "this.form.method.value='prepareEditPatent'" %>" />
		</logic:present>
					
		<table>
			<tr>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputButton">
						<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
					</html:submit>
				</td>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputButton" onclick='<%= backTo %>'>
						<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
					</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
</logic:present>