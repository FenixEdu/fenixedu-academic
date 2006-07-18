<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/dfaCandidacy.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="alterCandidacyData"/>

	<h2><strong><bean:message key="label.person.title.personal.info" /></strong></h2>
	<fr:edit id="personData" name="candidacy" property="person" schema="candidate.personalData-freeEdit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.filiation" /></strong></h2>
	<fr:edit id="personFiliation" name="candidacy" property="person" schema="candidate.filiation-freeEdit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.addressInfo" /></strong></h2>
	<fr:edit id="personAddress" name="candidacy" property="person" schema="candidate.address-freeEdit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.contactInfo" /></strong></h2>
	<fr:edit id="personContacts" name="candidacy" property="person" schema="candidate.contacts-freeEdit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ADMIN_OFFICE_RESOURCES" /></strong></h2>
	<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformation" schema="candidate.precedentDegreeInformation-freeEdit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>	

	<html:submit><bean:message key="button.submit" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>	
</html:form>