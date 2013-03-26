<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><!-- Error messages go here --><html:errors /></span>


<br />
<html:form action="/generatePasswordsForCandidacies.do?method=generatePasswords" target="_blank">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

	<h3><bean:message key="label.operator.candidacy.passwords.chooseCandidacies" /></h3>

	<logic:notEmpty name="studentCandidacies">
		<fr:view name="studentCandidacies" schema="StudentCandidacy.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thcenter" />
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="candidacyIdsToProcess" />
				<fr:property name="checkboxValue" value="idInternal" />		
				<fr:property name="sortBy" value="number=asc"/>
				<fr:property name="selectAllShown" value="true"/>
				<fr:property name="selectAllLocation" value="both"/>
			</fr:layout>
		</fr:view>
		<br /><br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.OK" styleClass="inputbutton" property="OK"><bean:message  key="button.operator.candidacy.passwords.generatePasswords" /> </html:submit>
	</logic:notEmpty>
	<logic:empty name="studentCandidacies">
		<span class="error0"><bean:message key="label.operator.candidacy.passwords.noCandidacies"/></span>
	</logic:empty>


</html:form> 