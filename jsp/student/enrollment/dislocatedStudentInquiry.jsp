<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<h2><bean:message key="label.title.dislocatedStudent.inquiry"/></h2>

<div class="infoop">
<p><bean:message key="label.info.dislocatedStudent.inquiry"/>:</p>
</div>

<br />

<html:form action="/dislocatedStudent.do">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="registerDislocatedStudentInquiryAnswers"/>

<p><bean:message key="label.enrollment.dislocatedStudent.whereFrom"/></p>
<html:select bundle="HTMLALT_RESOURCES" altKey="select.countryID" property="countryID">
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
		<td><bean:message key="label.enrollment.dislocatedStudent.dislocated"/></td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dislocatedAnswer" property="dislocatedAnswer" value="true" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();" />
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dislocatedAnswer" property="dislocatedAnswer" value="false" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();" />
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.submit"/>
			</html:submit>
		</td>
	</tr>
</table>
<p><em><bean:message key="label.enrollment.dislocatedStudent.dislocated.explanation"/></em></p>
<br />



<logic:present name="dislocated">
<bean:message key="label.enrollment.dislocatedStudent.isDislocated"/>:<br/>
<table cellpadding="5">
<tr>
	<td><bean:message key="label.enrollment.dislocatedStudent.country"/>:</td>
	<td>
		<logic:present name="portugal">
			<bean:message key="label.enrollment.dislocatedStudent.district"/>:
		</logic:present>
	</td>
</tr>
<tr>
	<td>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.dislocatedCountryID" property="dislocatedCountryID" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();">
			<html:option value=""/>
			<html:options collection="infoCountries" property="idInternal" labelProperty="name"/>
		</html:select>
		<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
	</td>
	<td>
		<logic:present name="portugal">
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.districtID" property="districtID">
				<html:options collection="infoDistricts" property="idInternal" labelProperty="name"/>
			</html:select>
		</logic:present>
	</td>
</tr>
</table>
<br/>
</logic:present>	

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
</html:form>