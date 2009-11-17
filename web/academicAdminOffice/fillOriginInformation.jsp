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
	<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformationBean" visible="false" />
	
	<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.originInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="originInformation" name="originInformationBean" schema="student.originInformationBean-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	        <fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		<fr:destination name="invalid" path="/createStudent.do?method=prepareShowFillOriginInformation"/>
	</fr:edit>
	
	<p>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>	
	</p>
</fr:form>