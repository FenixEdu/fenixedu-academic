<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.generate"/>
</h2>

<p>	
	<span class="error0">
		<html:errors/>
		<html:messages id="error" property="error" message="true">
			<bean:write name="error" />
		</html:messages>
	</span>
</p>

<p>
	<span class="warning0">
		<html:messages id="message" property="message" message="true">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>

<bean:define id="personID" name="libraryCardDTO" property="person.externalId"/>

<fr:form action="/cardManagement.do?method=createCard">
	<fr:edit id="libraryCardSearch" name="libraryCardSearch" visible="false"/>
	
	<logic:equal name="libraryCardDTO" property="partyClassification" value="TEACHER">
		<fr:edit id="libraryCardToCreate" name="libraryCardDTO" schema="library.card.generate">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>			
			<fr:destination name="changeDateVisibility" path="/cardManagement.do?method=changeDateVisibility"/>
			<fr:destination name="input" path="<%= "/cardManagement.do?method=prepareGenerateCard&personID=" + personID.toString()%>"/>
		</fr:edit>
	</logic:equal>
	<logic:notEqual name="libraryCardDTO" property="partyClassification" value="TEACHER">
	
		<logic:equal name="libraryCardDTO" property="partyClassification" value="PERSON">
			<fr:edit id="libraryCardToCreate" name="libraryCardDTO" schema="library.card.generate.person">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>			
				<fr:destination name="changeDateVisibility" path="/cardManagement.do?method=changeDateVisibility"/>
				<fr:destination name="input" path="<%= "/cardManagement.do?method=prepareGenerateCard&personID=" + personID.toString()%>"/>
			</fr:edit>
		</logic:equal>
		
		<logic:notEqual name="libraryCardDTO" property="partyClassification" value="PERSON">
			<fr:edit id="libraryCardToCreate" name="libraryCardDTO" schema="library.card.generate.employee">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>			
				<fr:destination name="changeDateVisibility" path="/cardManagement.do?method=changeDateVisibility"/>
				<fr:destination name="input" path="<%= "/cardManagement.do?method=prepareGenerateCard&personID=" + personID.toString()%>"/>
			</fr:edit>
		</logic:notEqual>
	</logic:notEqual>

	<logic:present name="presentDate">
		<fr:edit id="validUntil" name="libraryCardDTO" schema="library.card.generate.date">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/cardManagement.do?method=invalidDate"/>	
			<fr:destination name="input" path="<%= "/cardManagement.do?method=prepareGenerateCard&personID=" + personID.toString() %>"/>
		</fr:edit>
	</logic:present>
	<logic:notPresent name="presentDate">
		<fr:edit id="validUntil" name="libraryCardDTO" visible="false"/>
	</logic:notPresent>

	<p>
		<html:submit ><bean:message key="button.confirm" bundle="LIBRARY_RESOURCES"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
			<bean:message key="button.back" bundle="LIBRARY_RESOURCES"/>
		</html:cancel>
	</p>
</fr:form>