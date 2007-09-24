<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
	
	<logic:equal name="libraryCardDTO" property="partyClassification" value="TEACHER">
		<fr:edit id="libraryCardEdit" name="libraryCardDTO" schema="library.card.edit.teacher">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1"/>
			</fr:layout>
			<fr:destination name="input" path="/cardManagement.do?method=generatePdfCard&modify=true"/>
		</fr:edit>
	</logic:equal>
	<logic:notEqual name="libraryCardDTO" property="partyClassification" value="TEACHER">
		
		<logic:notEqual name="libraryCardDTO" property="partyClassification" value="PERSON">
			<fr:edit id="libraryCardEdit" name="libraryCardDTO" schema="library.card.edit">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thright"/>
					<fr:property name="columnClasses" value=",,tderror1"/>
				</fr:layout>			
				<fr:destination name="input" path="/cardManagement.do?method=generatePdfCard&modify=true"/>
			</fr:edit>
		</logic:notEqual>
		
		<logic:equal name="libraryCardDTO" property="partyClassification" value="PERSON">
			<fr:edit id="libraryCardEdit" name="libraryCardDTO" schema="library.card.edit.person">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thright"/>
					<fr:property name="columnClasses" value=",,tderror1"/>
				</fr:layout>			
				<fr:destination name="input" path="/cardManagement.do?method=generatePdfCard&modify=true"/>
			</fr:edit>
		</logic:equal>
	</logic:notEqual>	
	
	<p>
		<html:submit><bean:message key="button.confirm" bundle="LIBRARY_RESOURCES"/></html:submit>		
		<html:cancel property="cancel" bundle="HTMLALT_RESOURCES" altKey="submit.back">
			<bean:message key="button.back" bundle="LIBRARY_RESOURCES"/>
		</html:cancel>
	</p>
</fr:form>