<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>

<body>
<span class="error"><html:errors /></span>
<table width="100%" cellspacing="0">
	<tr>
		<td class="infoop" width="50px"><span class="emphasis-box">1</span></td>
		<td class="infoop"><strong><bean:message
			key="label.person.title.personal.info" /></strong</td>
	</tr>
</table>
<br />
<html:form action="/changeApplicationInfoDispatchAction?method=change">
	<table>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.name" property="name" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.username" property="username" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidateID" property="candidateID" />
		
		<logic:present name="newPerson">
		
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentNumber" property="identificationDocumentNumber" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentType" property="identificationDocumentType" />
			
			<!-- Sexo -->
			<tr>
				<td width="150px"><bean:message key="label.person.sex" /></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.sex" property="sex">
					<html:options collection="<%= SessionConstants.SEX_LIST_KEY %>"
						property="value" labelProperty="label" />
				</html:select></td>
			</tr>
			<!-- Local de Emissao do Documento de Identificacao -->
			<tr>
				<td width="150px"><bean:message
					key="label.person.identificationDocumentIssuePlace" /></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.identificationDocumentIssuePlace" property="identificationDocumentIssuePlace" /></td>
			</tr>
			<!-- Data de Emissao do Documento de Identificacao -->
			<tr>
				<td width="150px"><bean:message
					key="label.person.identificationDocumentIssueDate" /></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.idIssueDateYear" property="idIssueDateYear">
					<html:options collection="<%= SessionConstants.YEARS_KEY %>"
					property="value" labelProperty="label" />
			</html:select> <html:select bundle="HTMLALT_RESOURCES" altKey="select.idIssueDateMonth" property="idIssueDateMonth">
				<html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>"
					property="value" labelProperty="label" />
			</html:select> <html:select bundle="HTMLALT_RESOURCES" altKey="select.idIssueDateDay" property="idIssueDateDay">
				<html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>"
						property="value" labelProperty="label" />
				</html:select></td>
			</tr>
			<!-- Data de Validade do Documento de Identificacao -->
			<tr>
				<td width="150px"><bean:message
					key="label.person.identificationDocumentExpirationDate" /></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.idExpirationDateYear" property="idExpirationDateYear">
					<html:options
						collection="<%= SessionConstants.EXPIRATION_YEARS_KEY %>"
					property="value" labelProperty="label" />
			</html:select> <html:select bundle="HTMLALT_RESOURCES" altKey="select.idExpirationDateMonth" property="idExpirationDateMonth">
				<html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>"
					property="value" labelProperty="label" />
			</html:select> <html:select bundle="HTMLALT_RESOURCES" altKey="select.idExpirationDateDay" property="idExpirationDateDay">
				<html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>"
						property="value" labelProperty="label" />
				</html:select></td>
			</tr>
			<!-- Numero de Contribuinte -->
			<tr>
				<td width="150px"><bean:message key="label.person.contributorNumber" /></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber" /></td>
			</tr>
			<!-- Profissao -->
			<tr>
				<td width="150px"><bean:message key="label.person.occupation" /></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.occupation" property="occupation" /></td>
			</tr>
			<!-- Estado Civil -->
				<tr>
					<td width="150px"><bean:message key="label.person.maritalStatus" /></td>
					<td><e:labelValues id="values"
						enumeration="net.sourceforge.fenixedu.domain.person.MaritalStatus" />
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.maritalStatus" property="maritalStatus">
						<html:option key="dropDown.Default" value="" />
						<html:options collection="values" property="value"
							labelProperty="label" />
					</html:select></td>
				</tr>
			</table>
			<br />
			<!--Filiação -->
			<table width="100%" cellspacing="0">
				<tr>
					<td class="infoop" width="50px"><span class="emphasis-box">2</span></td>
					<td class="infoop"><strong><bean:message
						key="label.person.title.filiation" /></strong></td>
				</tr>
			</table>
			<br />
			<table>
				<!-- Data de Nascimento -->
			<tr>
				<td width="150px"><bean:message key="label.person.birth" /></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.birthYear" property="birthYear">
					<html:options collection="<%= SessionConstants.YEARS_KEY %>"
					property="value" labelProperty="label" />
			</html:select> <html:select bundle="HTMLALT_RESOURCES" altKey="select.birthMonth" property="birthMonth">
				<html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>"
					property="value" labelProperty="label" />
			</html:select> <html:select bundle="HTMLALT_RESOURCES" altKey="select.birthDay" property="birthDay">
				<html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>"
						property="value" labelProperty="label" />
				</html:select></td>
			</tr>
			<!-- Nacionalidade -->
			<tr>
				<td width="150px"><bean:message key="label.person.nationality" /></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.nationality" property="nationality">
					<html:options
					collection="<%= SessionConstants.NATIONALITY_LIST_KEY %>"
							property="value" labelProperty="label" />
					</html:select></td>
				</tr>
				<!-- Freguesia de Naturalidade -->
				<tr>
					<td width="150px"><bean:message key="label.person.birthPlaceParish" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceParish" property="birthPlaceParish" /></td>
				</tr>
				<!-- Concelho de Naturalidade -->
				<tr>
					<td width="150px"><bean:message
						key="label.person.birthPlaceMunicipality" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceMunicipality" property="birthPlaceMunicipality" /></td>
				</tr>
				<!-- Distrito de Naturalidade -->
				<tr>
					<td width="150px"><bean:message key="label.person.birthPlaceDistrict" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceDistrict" property="birthPlaceDistrict" /></td>
				</tr>
				<!-- Nome do Pai -->
				<tr>
					<td width="150px"><bean:message key="label.person.fatherName" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.fatherName" property="fatherName" /></td>
				</tr>
				<!-- Nome da Mae -->
				<tr>
					<td width="150px"><bean:message key="label.person.motherName" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.motherName" property="motherName" /></td>
				</tr>
			
			</table>
			<br />
			<!-- Residência -->
			<table width="100%" cellspacing="0">
				<tr>
					<td class="infoop" width="50px"><span class="emphasis-box">3</span></td>
					<td class="infoop"><strong><bean:message
						key="label.person.title.addressInfo" /></strong></td>
				</tr>
			</table>
			<br />
			<table>
				<!-- Morada -->
				<tr>
					<td width="150px"><bean:message key="label.person.address" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.address" property="address" /></td>
				</tr>
				<!-- Localidade -->
				<tr>
					<td width="150px"><bean:message key="label.person.place" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.place" property="place" /></td>
				</tr>
				<!-- Codigo Postal -->
				<tr>
					<td width="150px"><bean:message key="label.person.postCode" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.postCode" property="postCode" /></td>
				</tr>
				<!-- Area do Codigo Postal -->
				<tr>
					<td width="150px"><bean:message key="label.person.areaOfPostCode" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" property="areaOfAreaCode" /></td>
				</tr>
				<!-- Freguesia de Morada -->
				<tr>
					<td width="150px"><bean:message key="label.person.addressParish" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressParish" property="addressParish" /></td>
				</tr>
				<!-- Concelho de Morada -->
				<tr>
					<td width="150px"><bean:message
						key="label.person.addressMunicipality" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressMunicipality" property="addressMunicipality" /></td>
				</tr>
				<!-- Distrito de Morada -->
				<tr>
					<td width="150px"><bean:message key="label.person.addressDistrict" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressDistrict" property="addressDistrict" /></td>
				</tr>
			</table>
			<br />
			<!--Contactos -->
			<table width="100%" cellspacing="0">
				<tr>
					<td class="infoop" width="50px"><span class="emphasis-box">4</span></td>
					<td class="infoop"><strong><bean:message
						key="label.person.title.contactInfo" /></strong></td>
				</tr>
			</table>
			<br />
			<table>
				<!-- telefone -->
				<tr>
					<td width="150px"><bean:message key="label.person.telephone" /></td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.telephone" property="telephone" /></td>
				</tr>

		</logic:present>
		
	<logic:notPresent name="newPerson">
	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.sex" property="sex" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentType" property="identificationDocumentType" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentNumber" property="identificationDocumentNumber" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentIssuePlace" property="identificationDocumentIssuePlace" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.maritalStatus" property="maritalStatus" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.nationality" property="nationality" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.fatherName" property="fatherName" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.motherName" property="motherName" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthPlaceParish" property="birthPlaceParish" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthPlaceMunicipality" property="birthPlaceMunicipality" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthPlaceDistrict" property="birthPlaceDistrict" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.address" property="address" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.place" property="place" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.postCode" property="postCode" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.addressParish" property="addressParish" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.addressMunicipality" property="addressMunicipality" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.addressDistrict" property="addressDistrict" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.telephone" property="telephone" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.contributorNumber" property="contributorNumber" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.occupation" property="occupation" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthDay" property="birthDay" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthMonth" property="birthMonth" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthYear" property="birthYear" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idIssueDateDay" property="idIssueDateDay" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idIssueDateMonth" property="idIssueDateMonth" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idIssueDateYear" property="idIssueDateYear" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idExpirationDateDay" property="idExpirationDateDay" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idExpirationDateMonth" property="idExpirationDateMonth" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idExpirationDateYear" property="idExpirationDateYear" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.areaOfAreaCode" property="areaOfAreaCode" />
	
	</logic:notPresent>
		
		<!-- Telemovel -->
		<tr>
			<td width="150px"><bean:message key="label.person.mobilePhone" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.mobilePhone" property="mobilePhone" /></td>
		</tr>
		<!-- E-Mail -->
		<tr>
			<td width="150px"><bean:message key="label.person.email" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" /></td>
		</tr>
		<!-- WebPage -->
		<tr>
			<td width="150px"><bean:message key="label.person.webSite" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.webSite" property="webSite" /></td>
		</tr>
		<!-- Major Degree -->
		<tr>
			<td width="150px"><bean:message key="label.candidate.majorDegree" />
			</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.majorDegree" property="majorDegree" /></td>
		</tr>
		<!-- Major Degree School -->
		<tr>
			<td width="150px"><bean:message
				key="label.candidate.majorDegreeSchool" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.majorDegreeSchool" property="majorDegreeSchool" /></td>
		</tr>
		<!-- Major Degree Year -->
		<tr>
			<td width="150px"><bean:message key="label.candidate.majorDegreeYear" />
			</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.majorDegreeYear" property="majorDegreeYear" /></td>
		</tr>
		<!-- Average -->
		<tr>
			<td><bean:message key="label.candidate.average" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.average" property="average" /></td>
		</tr>
		<!-- Specialization Area -->
		<tr>
			<td><bean:message key="label.candidate.specializationArea" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.specializationArea" property="specializationArea" /></td>
		</tr>
	</table>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Alterar" styleClass="inputbutton" property="ok" />
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton" />
</html:form>