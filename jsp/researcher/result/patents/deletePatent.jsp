<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.management.title"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.delete.useCase.title"/></h3>
	
	<%-- Action Messages --%>
	<p><span class="error"><!-- Error messages go here -->
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.delete.warning"/>
	</span></p>
	
	<%-- Last Modification Date --%>
	<fr:view name="patent" schema="result.modifyedBy">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
		</fr:layout>
	</fr:view>

	<%-- Participations --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h3>
	<fr:view name="patent" property="resultParticipations" schema="result.participations" layout="tabular">
		<fr:layout>
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,,acenter" />
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
	
	<%-- Documents --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></h3>
	<logic:empty name="patent" property="resultDocumentFiles">
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.emptyList"/></em>
	</logic:empty>
	<logic:notEmpty name="patent" property="resultDocumentFiles">
		<fr:view name="patent" property="resultDocumentFiles" schema="resultDocumentFile.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="columnClasses" value=",,,acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<br/>
	
	<%-- Event Associations --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></h3>
	<logic:empty name="patent" property="resultEventAssociations">
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.emptyList"/></em>
	</logic:empty>
	<logic:notEmpty name="patent" property="resultEventAssociations">
		<fr:view name="patent" property="resultEventAssociations" schema="resultEventAssociation.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="columnClasses" value=",,,acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<br/>
	
	<%-- Unit Associations --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></h3>
	<logic:empty name="patent" property="resultUnitAssociations">
		<em><bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.ResultUnitAssociation.emptyList" /></em>
	</logic:empty>
	<logic:notEmpty name="patent" property="resultUnitAssociations">
		<fr:view name="patent" property="resultUnitAssociations" schema="resultUnitAssociation.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="columnClasses" value=",,,acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<br/>
	<br/>
		
	<html:form action="/patents/management">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="delete"/>
		<bean:define id="patentId" name="patent" property="idInternal" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.resultId" property="resultId" value="<%= patentId.toString() %>"/>
		<bean:define id="backTo" value="<%= "this.form.method.value='management'" %>"/>

		<logic:present name="from">
			<bean:define id="backTo" value="<%= "this.form.method.value='prepareEdit'" %>" />
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