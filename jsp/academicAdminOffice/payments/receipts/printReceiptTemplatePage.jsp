<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<table border="0" style="width: 100%; height: 100%;">
	<tr style="height: 30;">
		<td>
		<table border="0" style="width: 100%; valign: top">
			<tr>
				<td colspan="2">
				<table border="0" align="center"	cellpadding="0" cellspacing="0" style="width: 100%; height: 104;">
					<tr>
						<td style="width: 1px;"><img
							src="<%= request.getContextPath() %>/images/LogoIST.gif"
							alt="<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="LogoIST" bundle="IMAGE_RESOURCES" />"
							border="0" style="width: 50; height: 104"/></td>
						<td>&nbsp;</td>
						<td>
						<table border="0" style="width: 100%; height: 100%;">
							<tr align="left">
								<td>&nbsp;<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.institutionName.upper.case"/></b><br />
								&nbsp;<b><bean:write name="currentUnit" property="name"/></b><br/>
                      			&nbsp;<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.costCenter"/> <bean:write name="currentUnit" property="costCenterCode"/></b>
								<hr size="1"/>
								</td>
							</tr>
							<tr>
								<td align="right" style="vertical-align: top;"><b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.receipt.receiptNumber"/> </b> <bean:write
									name="receipt" property="number" />/<bean:write name="receipt"
									property="year" /><br />
								<logic:greaterEqual name="receipt" property="receiptsVersionsCount"
									value="2">
									<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.receipt.secondPrintVersion"/></em>
								</logic:greaterEqual></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr style="vertical-align: top;">
		<td>
		<table border="0" style="width: 100%;">
			<tr>
				<td>
				<table border="0" style="width: 100%">
					<tr>
						<td width="20%"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.processFrom"/>:</strong></td>
						<td width="80%">&nbsp;</td>
					</tr>
					<tr>
					  <td> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.net.sourceforge.fenixedu.domain.Person.name" bundle="APPLICATION_RESOURCES" /> </td>
					  <td> <bean:write name="receipt" property="person.name"/> </td>
					</tr>
					<tr>
					  <td> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.net.sourceforge.fenixedu.domain.Person.idDocumentType" bundle="APPLICATION_RESOURCES"/> </td>
					  <td> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" name="receipt" property="person.idDocumentType.name" bundle="ENUMERATION_RESOURCES"/> </td>
					</tr>
					<tr>
					  <td> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.net.sourceforge.fenixedu.domain.Person.documentIdNumber" bundle="APPLICATION_RESOURCES" /> </td>
					  <td> <bean:write name="receipt" property="person.documentIdNumber"/> </td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>

					<tr>
						<td width="30%"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.receipt.contributor"/>:</strong></td>
						<td width="70%">&nbsp;</td>
					</tr>

					<tr>
						<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" bundle="ACADEMIC_OFFICE_RESOURCES" 
							key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.name" bundle="APPLICATION_RESOURCES" />:</td>
						<td><bean:write name="receipt"
							property="contributorParty.name" /></td>
					</tr>
					<tr>
						<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" bundle="ACADEMIC_OFFICE_RESOURCES" 
							key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.socialSecurityNumber" bundle="APPLICATION_RESOURCES" />:</td>
						<td><bean:write name="receipt"
							property="contributorParty.socialSecurityNumber" /></td>
					</tr>
					<tr>
						<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.address" bundle="APPLICATION_RESOURCES" />:</td>
						<td><bean:write name="receipt" property="contributorParty.address" /></td>
					</tr>
					<logic:notEmpty name="receipt" property="contributorParty.areaCode">
					<tr>
						<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.areaCode" bundle="APPLICATION_RESOURCES" /></td>
						<td><bean:write name="receipt" property="contributorParty.areaCode" /> - <bean:write name="receipt" property="contributorParty.areaOfAreaCode" /></td>
					</tr>
					</logic:notEmpty>
				</table>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td>
		<table align="right">
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
			<logic:iterate id="entry" name="sortedEntries">
				<tr>
					<td>
						<span>
							<app:labelFormatter name="entry" property="description">
		      					<app:property name="enum" value="ENUMERATION_RESOURCES"/>
	        					<app:property name="application" value="APPLICATION_RESOURCES"/>
								<app:property name="default" value="APPLICATION_RESOURCES"/>
							</app:labelFormatter>
						</span>
					</td>
					<td>.........................................&nbsp;</td>
					<td><bean:define id="amount" name="entry" property="originalAmount"
						type="Money" /> <%=amount.toPlainString()%> &nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" bundle="ACADEMIC_OFFICE_RESOURCES" 
						key="label.currencySymbol" /></td>
				</tr>
			</logic:iterate>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.totalAmountToPay"/> </strong></td>
				<td>_____________________&nbsp;</td>
				<td><strong><bean:define id="totalAmount" name="receipt"
					property="totalAmount" type="Money" /><%=totalAmount.toPlainString()%>&nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" bundle="ACADEMIC_OFFICE_RESOURCES" 
					key="label.currencySymbol" /></strong></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr style="vertical-align: bottom;">
		<td>
		<table border="0" style="vertical-align: bottom; width: 100%;">
			<tr>
				<td style="width: 50%;">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.city"/>, <%= new java.text.SimpleDateFormat("dd MMMM yyyy", request.getLocale()).format(new java.util.Date()) %>
				</td>
			</tr>
			<tr >
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td style="vertical-align: bottom;">&nbsp;
					<div align="center">&nbsp;</div>
					<div align="center">&nbsp;</div>
					<div align="center"><b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.theEmployee"/></b> <br/>
					<br/>
					<br/>
					</div>
					<hr align="center" style="width: 300; size: 1;"/>
				</td>
				<td width="20%">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
						<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
						<jsp:include page="/academicAdminOffice/payments/commons/footer.jsp" flush="true" />
				</td>
			</tr>


</table>
</logic:present>
