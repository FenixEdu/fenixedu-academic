<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
				<fr:property name="checkboxValue" value="externalId" />		
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