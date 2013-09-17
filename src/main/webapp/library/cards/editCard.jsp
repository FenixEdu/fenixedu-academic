<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.edit"/>
</h2>
	
<p>	
	<span class="error0">
		<html:errors/>
		<html:messages id="error" property="error" message="true">
			<bean:write name="error" />
		</html:messages>
	</span>
</p>
	
<fr:form action="/cardManagement.do?method=editCard">
	<fr:edit id="libraryCardSearch" name="libraryCardSearch" visible="false"/>	
	
	<bean:define id="personClassification" name="libraryCardDTO" property="person.partyClassification"/>

	<logic:equal name="personClassification" value="PERSON">
		<fr:edit id="libraryCardEdit" name="libraryCardDTO" schema="library.card.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>			
			<fr:destination name="input" path="/cardManagement.do?method=generatePdfCard&modify=true"/>
			<fr:destination name="partyClassificationPostBack" path="/cardManagement.do?method=postBack"/>
		</fr:edit>
	</logic:equal>

	<logic:notEqual name="personClassification" value="PERSON">
		<logic:equal name="libraryCardDTO" property="partyClassification" value="TEACHER">
			<fr:edit id="libraryCardEdit" name="libraryCardDTO" schema="library.card.edit.teacher">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
				<fr:destination name="input" path="/cardManagement.do?method=generatePdfCard&modify=true"/>
				<fr:destination name="partyClassificationPostBack" path="/cardManagement.do?method=postBack"/>
			</fr:edit>
		</logic:equal>
		
		<logic:notEqual name="libraryCardDTO" property="partyClassification" value="TEACHER">
			<logic:notEqual name="libraryCardDTO" property="partyClassification" value="PERSON">
				<fr:edit id="libraryCardEdit" name="libraryCardDTO" schema="library.card.edit">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>			
					<fr:destination name="input" path="/cardManagement.do?method=generatePdfCard&modify=true"/>
					<fr:destination name="partyClassificationPostBack" path="/cardManagement.do?method=postBack"/>
				</fr:edit>
			</logic:notEqual>

			<logic:equal name="libraryCardDTO" property="partyClassification" value="PERSON">
				<fr:edit id="libraryCardEdit" name="libraryCardDTO" schema="library.card.edit.person">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>			
					<fr:destination name="input" path="/cardManagement.do?method=generatePdfCard&modify=true"/>
					<fr:destination name="partyClassificationPostBack" path="/cardManagement.do?method=postBack"/>
				</fr:edit>
			</logic:equal>
		</logic:notEqual>	
	</logic:notEqual>
	<p>
		<html:submit><bean:message key="button.confirm" bundle="LIBRARY_RESOURCES"/></html:submit>		
		<html:cancel property="cancel" bundle="HTMLALT_RESOURCES" altKey="submit.back">
			<bean:message key="button.back" bundle="LIBRARY_RESOURCES"/>
		</html:cancel>
	</p>
</fr:form>