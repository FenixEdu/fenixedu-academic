<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.details"/>
</h2>
	
<p>
	<span class="warning0">
		<html:messages id="message" property="message" message="true">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>

<style>
.changedCategories td {
font-weight: bold;
background: #ffd;
}
</style>

<fr:form action="/cardManagement.do?method=generatePdfCard">
	<fr:edit id="libraryCardSearch" name="libraryCardSearch" visible="false"/>
	<fr:edit id="libraryCardDTO" name="libraryCardDTO" visible="false"/>
	<fr:view name="libraryCardDTO" schema="library.card.generated">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright"/>
			<logic:notEqual name="libraryCardDTO" property="hasEqualPartyClassification" value="true">
				<fr:property name="rowClasses" value=",,,,,,,,changedCategories,changedCategories,,,,,"/>
			</logic:notEqual>
		</fr:layout>			
	</fr:view>

	<p>
		<html:submit property="modify"><bean:message key="button.modify" bundle="LIBRARY_RESOURCES"/></html:submit>
		<html:submit><bean:message key="button.generateCard" bundle="LIBRARY_RESOURCES"/></html:submit>
		<html:submit property="back" bundle="HTMLALT_RESOURCES" altKey="submit.back">
			<bean:message key="button.back" bundle="LIBRARY_RESOURCES"/>
		</html:submit>
	</p>
</fr:form>