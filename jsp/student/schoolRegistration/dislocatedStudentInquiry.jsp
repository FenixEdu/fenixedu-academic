<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<p><strong>Página 4 de 6</strong></p>

<div style="width:70%; margin: 0 15%;">

<h2><bean:message key="label.title.dislocatedStudent.inquiry" bundle="STUDENT_RESOURCES" /></h2>

	<span class="error"><html:errors /></span> <html:form action="/dislocatedStudent">
	<br/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareResidenceCandidacy" />

<div class="infoop">
	<p><bean:message key="label.enrollment.dislocatedStudent.whereFrom"
		bundle="STUDENT_RESOURCES" /></p>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.countryID" property="countryID">
		<html:options collection="infoCountries" property="idInternal"
			labelProperty="name" />
	</html:select>

	<br />
	<br />

	<table>
		<tr>
			<td></td>
			<td><bean:message key="label.yes" bundle="STUDENT_RESOURCES"/></td>
			<td><bean:message key="label.no" bundle="STUDENT_RESOURCES"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.enrollment.dislocatedStudent.dislocated" bundle="STUDENT_RESOURCES" /></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dislocatedAnswer" property="dislocatedAnswer" value="true" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();" /></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dislocatedAnswer" property="dislocatedAnswer" value="false" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();" /></td>
		</tr>
	</table>
	<p><em><bean:message
		key="label.enrollment.dislocatedStudent.dislocated.explanation"
		bundle="STUDENT_RESOURCES" /></em></p>
	<br />



	<logic:present name="dislocated">
		<bean:message key="label.enrollment.dislocatedStudent.isDislocated"
			bundle="STUDENT_RESOURCES" />:<br />
		<table cellpadding="5">
			<tr>
				<td><bean:message key="label.enrollment.dislocatedStudent.country"
					bundle="STUDENT_RESOURCES" />:</td>
				<td><logic:present name="portugal">
					<bean:message key="label.enrollment.dislocatedStudent.district"
						bundle="STUDENT_RESOURCES" />:
		</logic:present></td>
			</tr>
			<tr>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.dislocatedCountryID" property="dislocatedCountryID"
					onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();">
					<html:option value="" />
					<html:options collection="infoCountries" property="idInternal"
						labelProperty="name" />
				</html:select></td>
				<td><logic:present name="portugal">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.districtID" property="districtID">
						<html:options collection="infoDistricts" property="idInternal"
							labelProperty="name" />
					</html:select>
				</logic:present></td>
			</tr>
		</table>
		<br />
	</logic:present>
</div>
<br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.continue" bundle="STUDENT_RESOURCES" />
	</html:submit>
</html:form>

</div>