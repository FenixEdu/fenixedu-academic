<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<h2><bean:message key="label.title.dislocatedStudent.inquiry"/></h2>

<div class="infoop2">
	<p><bean:message key="label.info.dislocatedStudent.inquiry"/>:</p>
</div>

<html:form action="/dislocatedStudent.do">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="registerDislocatedStudentInquiryAnswers"/>
	

<table class="tstyle5 thlight thright">
<tr>
	<th>
		<bean:message key="label.enrollment.dislocatedStudent.whereFrom"/>
	</th>
	<td>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.countryID" property="countryID">
			<html:options collection="infoCountries" property="idInternal" labelProperty="name"/>
		</html:select>
	</td>
</tr>
<tr>
	<th><bean:message key="label.enrollment.dislocatedStudent.dislocated"/></th>
	<td>
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dislocatedAnswer" property="dislocatedAnswer" value="true" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();" />
		Sim
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dislocatedAnswer" property="dislocatedAnswer" value="false" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();" />
		Não
	</td>
</tr>
</table>

<p>
	<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
</p>



<logic:present name="dislocated">
	<p class="mbottom05"><bean:message key="label.enrollment.dislocatedStudent.isDislocated"/>:<br/></p>
	<table class="tstyle5 thlight thright mtop05">
	<tr>
		<th>
			<p><bean:message key="label.enrollment.dislocatedStudent.country"/>:</p>
		</th>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.dislocatedCountryID" property="dislocatedCountryID" onchange="this.form.method.value='prepareDislocatedStudentInquiry';this.form.submit();">
				<html:option value=""/>
				<html:options collection="infoCountries" property="idInternal" labelProperty="name"/>
			</html:select>
			<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.submit"/>
			</html:submit>
		</td>
	</tr>
	<logic:present name="portugal">
		<tr>
			<th>
				<bean:message key="label.enrollment.dislocatedStudent.district"/>:
			</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.districtID" property="districtID">
					<html:options collection="infoDistricts" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
	</logic:present>
	</table>
</logic:present>	

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
</p>


<p class="mtop2"><em><bean:message key="label.enrollment.dislocatedStudent.dislocated.explanation"/></em></p>

</html:form>