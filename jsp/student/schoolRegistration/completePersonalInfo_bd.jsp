<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.Data" %>
<%@ page import="java.util.Date" %>
 <h2><bean:message key="label.person.title.personalConsult" /></h2>
 
<html:form action="/showStudentInquiry">
 <br/>
 <logic:present name="personalInfo">
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Dados Pessoais -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">1</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.personal.info" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Nome -->
          <tr>
            <td width="30%"><bean:message key="label.person.name" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="nome"/></td>
          </tr>
          <!-- Username -->
          <tr>
            <td width="30%"><bean:message key="label.person.username" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="username"/></td> 
          </tr>
          <!-- Sexo -->
          <tr>
            <td width="30%"><bean:message key="label.person.sex" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="sexo"/></td>
          </tr>
 	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentNumber" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentType" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="tipoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Local de Emissao do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Emissao do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentIssueDate" /></td>
			<td class="greytxt">
				<html:select property="DayOfEmissionDateOfDocumentId">
	                <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
                </html:select>
                <html:select property="MonthOfEmissionDateOfDocumentId">
	                <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
                </html:select>
                <html:select property="YearOfEmissionDateOfDocumentId">
	                <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
                </html:select>
			</td>            
          </tr>
          <!-- Data de Validade do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
            <td class="greytxt">
				<html:select property="DayOfExpirationDateOfDocumentId">
	                <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
                </html:select>
                <html:select property="MonthOfExpirationDateOfDocumentId">
	                <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
                </html:select>
                <html:select property="YearOfExpirationDateOfDocumentId">
	                <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
                </html:select>
			</td>   
          </tr>
          <!-- Numero de Contribuinte -->
          <tr>
            <td width="30%"><bean:message key="label.person.contributorNumber" /></td>
            <td class="greytxt"><html:text size="40" maxlength="50" property="contributorNumber"/></td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td width="30%"><bean:message key="label.person.occupation" /></td>
            <td class="greytxt"><html:text size="40" maxlength="50" property="occupation"/></td>
          </tr>
          <!-- Estado Civil -->
          <tr>
            <td width="30%"><bean:message key="label.person.maritalStatus" /></td>
            <td class="greytxt">
            	<html:select property="maritalStatus">
	                <html:options collection="<%= SessionConstants.MARITAL_STATUS_LIST_KEY %>" property="value" labelProperty="label"/>
                </html:select>
            </td>
          </tr>
       </table>
       <table width="100%" cellpadding="0" cellspacing="0">
          <!-- Filiação -->
          <tr>
          	<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
          	<td class="infoop"><strong><bean:message key="label.person.title.filiation" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Data de Nascimento -->
          <tr>
            <td width="30%"><bean:message key="label.person.birth" /></td>
            <logic:present name="personalInfo" property="nascimento" >
	            <bean:define id="date" name="personalInfo" property="nascimento" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Nacionalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.country" /></td>
		     	<td class="greytxt">
		     		<html:select property="nacionality">
	                	<html:options collection="<%= SessionConstants.NATIONALITY_LIST_KEY %>" property="value" labelProperty="label"/>
                	</html:select>
                </td>
          </tr>
          <!-- Freguesia de Naturalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceParish" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="parishOfBirth"/></td>
          </tr>
          <!-- Concelho de Naturalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceMunicipality" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="districtSubvisionOfBirth"/></td>
          </tr>
           <!-- Distrito de Naturalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceDistrict" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="districtOfBirth"/></td>
          </tr>
          <!-- Nome do Pai -->
          <tr>
            <td width="30%"><bean:message key="label.person.fatherName" /></td>
            <td class="greytxt"><html:text size="40" maxlength="56" property="nameOfFather"/></td>
          </tr>
          <!-- Nome da Mae -->
          <tr>
            <td width="30%"><bean:message key="label.person.motherName" /></td>
            <td class="greytxt"><html:text size="40" maxlength="60" property="nameOfMother"/></td>
          </tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Dados de Residencia -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">3</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.addressInfo" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Morada -->
          <tr>
            <td width="30%"><bean:message key="label.person.address" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="address"/></td>
          </tr>
          <!-- Codigo Postal -->
          <tr>
            <td width="30%"><bean:message key="label.person.postCode" /></td>
            <td class="greytxt">
            	<html:text size="4" maxlength="4" property="PrimaryAreaCode"/> - 
            	<html:text size="3" maxlength="3" property="SecondaryAreaCode"/>
          </tr>
          <!-- Area do Codigo Postal -->
          <tr>
            <td width="30%"><bean:message key="label.person.areaOfPostCode" /></td>
            <td class="greytxt"><html:text size="25" maxlength="25" property="areaOfAreaCode"/></td>
          </tr>
          <!-- Localidade de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.place" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="area"/></td>
          </tr>
          <!-- Freguesia de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.addressParish" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="parishOfResidence"/></td>
          </tr>
          <!-- Concelho de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.addressMunicipality" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="districtSubdivisionOfResidence"/></td>
          </tr>
          <!-- Distrito de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.addressDistrict" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="districtOfResidence"/></td>
          </tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Contactos -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">4</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.contactInfo" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Telefone -->
          <tr>
            <td width="30%"><bean:message key="label.person.telephone" /></td>
            <td class="greytxt"><html:text size="9" maxlength="9" property="phone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td width="30%"><bean:message key="label.person.mobilePhone" /></td>
            <td class="greytxt"><html:text size="9" maxlength="9" property="mobile"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td width="30%"><bean:message key="label.person.email" /></td>
            <td class="greytxt"><html:text size="40" maxlength="50" property="email"/></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td width="30%"><bean:message key="label.person.webSite" /></td>
            <td class="greytxt"><html:text size="40" maxlength="200" property="webAddress"/></td>
          </tr>
       </logic:present>
       	</table>
		<p align="center"><html:submit styleClass="inputbutton">Continuar</html:submit></p>
</html:form>