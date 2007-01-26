<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">


<div style="font-family: Arial; padding: 0 1em;">


<table style="width: 95%;">
<tr>
	<td rowspan="2" style="width: 100px;">
		<img src="<%= request.getContextPath() %>/images/LogoIST.gif" alt="<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="LogoIST" bundle="IMAGE_RESOURCES" />"/>
	</td>
	<td style="padding-left: 1em;">
		<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.institutionName.upper.case"/></h3>
		<b><bean:write name="currentUnit" property="name"/></b><br/>
		<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.costCenter"/> <bean:write name="currentUnit" property="costCenterCode"/></b>
		<hr size="1"/>
	</td>
</tr>
<tr>
	<td style="text-align: right;">
		<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.guide"/></h3>
	</td>
</tr>
</table>




<p style="margin-bottom: 0.5em; margin-top: 2em;"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.processFrom"/></strong></p>

<table style="margin-top: 0.5em; margin-bottom: 6em;">
	<tr>
		<td style="width: 300px"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.net.sourceforge.fenixedu.domain.Person.name" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:write name="paymentsManagementDTO" property="person.name"/></td>
	</tr>
	<tr>
		<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.net.sourceforge.fenixedu.domain.Person.idDocumentType" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" name="paymentsManagementDTO" property="person.idDocumentType.name" bundle="ENUMERATION_RESOURCES"/></td>
	</tr>
	<tr>
		<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.net.sourceforge.fenixedu.domain.Person.documentIdNumber" bundle="APPLICATION_RESOURCES" />:</td>
		<td><bean:write name="paymentsManagementDTO" property="person.documentIdNumber"/></td>
	</tr>
</table>



	<logic:iterate id="entryDTO" name="paymentsManagementDTO" property="selectedEntries" >
		<table style="width: 95%;">
			<tr>
				<td style="text-align: right;">
					<app:labelFormatter name="entryDTO" property="description">
						<app:property name="enum" value="ENUMERATION_RESOURCES"/>
						<app:property name="application" value="APPLICATION_RESOURCES"/>
						<app:property name="default" value="APPLICATION_RESOURCES"/>	
					</app:labelFormatter>
				</td>
				<td style="text-align: right; width: 190px;">
					_______________
					<bean:define id="amountToPay" name="entryDTO" property="amountToPay" type="Money" /> <%= amountToPay.toPlainString() %><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.currencySymbol"/>
				</td>
			</tr>
		</table>
	</logic:iterate>
	


	<table style="width: 95%; padding-top: 1em;">
	<tr>
		<td style="text-align: right;">
			<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.totalAmountToPay"/></strong>
		</td>
		<td style="text-align: right; width: 190px;">
			<strong>_______________ <bean:define id="totalAmountToPay" name="paymentsManagementDTO" property="totalAmountToPay" type="Money"/><%= totalAmountToPay.toPlainString() %><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.currencySymbol"/></strong>
		</td>
	</tr>
	</table>






	<p style="text-align: left; margin-top: 12em; font-size: 10pt;">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.city"/>, <%= new java.text.SimpleDateFormat("dd MMMM yyyy", request.getLocale()).format(new java.util.Date()) %>
	</p>

	<p style="text-align: right; margin-top: 2em; padding-right: 15em; font-size: 10pt;">
		<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.theEmployee"/></b>
	</p>
	<p style="text-align: right; padding-right: 10em; font-size: 10pt;">
		_____________________________
	</p>

	
	

	<div style="margin-top: 10em;">
		<jsp:include page="/academicAdminOffice/payments/commons/footer.jsp" flush="true" />
	</div>


</div>


<div class="breakafter"></div>


</logic:present>
