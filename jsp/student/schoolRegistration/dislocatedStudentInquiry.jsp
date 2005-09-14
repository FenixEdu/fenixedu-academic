<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong>Página 4 de 7</strong>
<h2><bean:message key="label.title.dislocatedStudent.inquiry" bundle="STUDENT_RESOURCES"/></h2>

<br />
<span class="error"><html:errors/></span>
<html:form action="/dislocatedStudent">
<html:hidden property="method" value="prepareResidenceCandidacy"/>

<p><bean:message key="label.enrollment.dislocatedStudent.whereFrom" bundle="STUDENT_RESOURCES"/></p>
<html:select property="countryID">
	<html:options collection="infoCountries" property="idInternal" labelProperty="name"/>
</html:select>

<br/>
<br/>

<table>
	<tr>
		<td></td>
		<td>Sim</td>
		<td>Não</td>
	</tr>
	<tr>
		<td><bean:message key="label.enrollment.dislocatedStudent.dislocated" bundle="STUDENT_RESOURCES"/></td>
		<td><html:radio property="dislocatedAnswer" value="true" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();" /></td>
		<td><html:radio property="dislocatedAnswer" value="false" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();" /></td>
	</tr>
</table>
<p><em><bean:message key="label.enrollment.dislocatedStudent.dislocated.explanation" bundle="STUDENT_RESOURCES"/></em></p>
<br />



<logic:present name="dislocated">
<bean:message key="label.enrollment.dislocatedStudent.isDislocated" bundle="STUDENT_RESOURCES"/>:<br/>
<table cellpadding="5">
<tr>
	<td><bean:message key="label.enrollment.dislocatedStudent.country" bundle="STUDENT_RESOURCES"/>:</td>
	<td>
		<logic:present name="portugal">
			<bean:message key="label.enrollment.dislocatedStudent.district" bundle="STUDENT_RESOURCES"/>:
		</logic:present>
	</td>
</tr>
<tr>
	<td>
		<html:select property="dislocatedCountryID" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();">
			<html:option value=""/>
			<html:options collection="infoCountries" property="idInternal" labelProperty="name"/>
		</html:select>
	</td>
	<td>
		<logic:present name="portugal">
			<html:select property="districtID">
				<html:options collection="infoDistricts" property="idInternal" labelProperty="name"/>
			</html:select>
		</logic:present>
	</td>
</tr>
</table>
<br/>
</logic:present>	

<html:submit styleClass="inputbutton"><bean:message key="button.continue" bundle="STUDENT_RESOURCES"/></html:submit>
</html:form>