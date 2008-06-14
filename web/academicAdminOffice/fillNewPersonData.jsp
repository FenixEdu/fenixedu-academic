<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.student.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:form action="/createStudent.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareShowCreateStudentConfirmation"/>
	<fr:edit id="executionDegree" name="executionDegreeBean" visible="false" />
	<fr:edit id="person" name="personBean" visible="false" />	
	<fr:edit id="chooseIngression" name="ingressionInformationBean" visible="false" />	
	
	<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.personal.info" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personData" name="personBean" schema="student.personalData-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.identification" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personIdentification" name="personBean" schema="student.documentId-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.filiation" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personFiliation" name="personBean" schema="student.filiation-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.addressInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personAddress" name="personBean" schema="student.address-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.contactInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personContacts" name="personBean" schema="student.contacts-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit name="precedentDegreeInformationBean" id="precedentDegreeInformation" type="net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean" schema="student.precedentDegreeInformation-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>	
	<p>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>	
	</p>
</fr:form>