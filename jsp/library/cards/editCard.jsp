<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.generate"/>
</h2>
	
<span class="error0">
	<html:errors/>
	<html:messages id="error" property="error" message="true">
		<bean:write name="error" />
		<br />
	</html:messages>
</span>

<span class="warning0">
	<html:messages id="message" property="message" message="true">
		<bean:write name="message" />
		<br />
	</html:messages>
</span>

<bean:define id="personID" name="libraryCard" property="person.idInternal"/>

<fr:form action="/cardManagement.do?method=generateCard">

	<fr:edit id="libraryCard" name="libraryCard" schema="library.card.generate">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5"/>
			<fr:property name="columnClasses" value=",,tdclear error0"/>
		</fr:layout>			
		<fr:destination name="changeDateVisibility" path="/cardManagement.do?method=changeDateVisibility"/>
		<fr:destination name="input" path="<%= "/cardManagement.do?method=prepareGenerateCard&personID=" + personID.toString()%>"/>
	</fr:edit>

	<logic:present name="presentDate">
		<fr:edit id="validUntil" name="libraryCard" schema="library.card.generate.date">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5"/>
				<fr:property name="columnClasses" value=",,tdclear error0"/>
			</fr:layout>
			<fr:destination name="invalid" path="/cardManagement.do?method=invalidDate"/>	
			<fr:destination name="input" path="<%= "/cardManagement.do?method=prepareGenerateCard&personID=" + personID.toString() %>"/>
		</fr:edit>
	</logic:present>

	<html:submit ><bean:message key="button.confirm" bundle="LIBRARY_RESOURCES"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
		<bean:message key="button.back" bundle="LIBRARY_RESOURCES"/>
	</html:cancel>
</fr:form>