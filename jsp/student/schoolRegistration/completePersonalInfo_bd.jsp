<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
 <strong>Página 2 de 6</strong>
 <br/>
 <h2><bean:message key="label.person.title.personalConsult" bundle="DEFAULT" /></h2>


<logic:present name="incompleteData" scope="request">
	<p align="center"><span class="error"><%= request.getAttribute("incompleteData") %></span></p>
</logic:present>
<html:form action="/showStudentInquiry?method=viewInquiryQuestions">
 <html:hidden property="page" value="3"/>
 <br/>
 <logic:present name="personalInfo">
 <bean:define id="infoPersonId" name="personalInfo" property="idInternal"/>
 <html:hidden property="idInternal" value="<%= infoPersonId.toString()%>"/>

		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Dados Pessoais -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">1</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.personal.info" bundle="DEFAULT"/></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Nome -->
          <tr>
            <td width="30%"><bean:message key="label.person.name" bundle="DEFAULT"/></td>
            <td class="greytxt"><bean:write name="personalInfo" property="nome"/></td>
          </tr>
          <!-- Username -->
          <tr>
            <td width="30%"><bean:message key="label.person.username" bundle="DEFAULT"/></td>
            <td class="greytxt"><bean:write name="personalInfo" property="username"/></td> 
          </tr>
          <!-- Sexo -->
          <tr>
            <td width="30%"><bean:message key="label.person.sex" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="sexo"/></td>
          </tr>
 	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentNumber" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentType" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="tipoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Local de Emissao do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentIssuePlace" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Emissao do Documento de Identificacao -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="dayOfEmissionDateOfDocumentId"/></span>
				<span class="error"><html:errors property="monthOfEmissionDateOfDocumentId"/></span>
				<span class="error"><html:errors property="yearOfEmissionDateOfDocumentId"/></span>
			</td>
		  </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentIssueDate" bundle="DEFAULT" /></td>
			<td class="greytxt">
				<html:text size="2" maxlength="2" property="dayOfEmissionDateOfDocumentId"/> /
				<html:text size="2" maxlength="2" property="monthOfEmissionDateOfDocumentId"/> /
				<html:text size="4" maxlength="4" property="yearOfEmissionDateOfDocumentId"/> (dd/mm/aaaa)
			</td>            
          </tr>
          <!-- Data de Validade do Documento de Identificacao -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="dayOfExpirationDateOfDocumentId"/></span>
				<span class="error"><html:errors property="monthOfExpirationDateOfDocumentId"/></span>
				<span class="error"><html:errors property="yearOfExpirationDateOfDocumentId"/></span>
			</td>
		  </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentExpirationDate" bundle="DEFAULT" /></td>
            <td class="greytxt">
				<html:text size="2" maxlength="2" property="dayOfExpirationDateOfDocumentId"/> /
				<html:text size="2" maxlength="2" property="monthOfExpirationDateOfDocumentId"/> /
				<html:text size="4" maxlength="4" property="yearOfExpirationDateOfDocumentId"/> (dd/mm/aaaa)								            
			</td>   
          </tr>
          <!-- Numero de Contribuinte -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="contributorNumber"/></span></td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.contributorNumber" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="50" property="contributorNumber"/></td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td width="30%"><bean:message key="label.person.occupation" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="50" property="occupation"/></td>
          </tr>
          <!-- Estado Civil -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="maritalStatus"/></span><td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.maritalStatus" bundle="DEFAULT" /></td>
            <td class="greytxt">
            	<html:select property="maritalStatus">
	                <html:options collection="<%= SessionConstants.MARITAL_STATUS_LIST_KEY %>" property="value" labelProperty="label"/>
                </html:select>
            </td>
          </tr>
       </table>
       <br/>
       <table width="100%" cellpadding="0" cellspacing="0">
          <!-- Filiação -->
          <tr>
          	<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
          	<td class="infoop"><strong><bean:message key="label.person.title.filiation" bundle="DEFAULT" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Data de Nascimento -->
          <tr>
            <td width="30%"><bean:message key="label.person.birth" bundle="DEFAULT" /></td>
            <logic:present name="personalInfo" property="nascimento" >
	            <bean:define id="date" name="personalInfo" property="nascimento" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Nacionalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.country" bundle="DEFAULT" /></td>
		     	<td class="greytxt">
		     		<html:select property="nacionality">
	                	<html:options collection="<%= SessionConstants.NATIONALITY_LIST_KEY %>" property="value" labelProperty="label"/>
                	</html:select>
                </td>
          </tr>
          <!-- Freguesia de Naturalidade -->
          <tr>
          	<td></td>
          	<td></span><span class="error"><html:errors property="parishOfBirth"/></span></td>
	      </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceParish" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="parishOfBirth"/></td>
          </tr>
          <!-- Concelho de Naturalidade -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="districtSubvisionOfBirth"/></span></td>
		  </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceMunicipality" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="districtSubvisionOfBirth"/></td>
          </tr>
           <!-- Distrito de Naturalidade -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="districtOfBirth"/></span></td> 
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceDistrict" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="districtOfBirth"/></td>
          </tr>
          <!-- Nome do Pai -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="nameOfFather"/></span></td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.fatherName" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="56" property="nameOfFather"/></td>
          </tr>
          <!-- Nome da Mae -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="nameOfMother" /></td>
		  </tr>		
          <tr>
            <td width="30%"><bean:message key="label.person.motherName" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="60" property="nameOfMother"/></td>
          </tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Dados de Residencia -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">3</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.addressInfo" bundle="DEFAULT" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%" >
          <!-- Morada -->
          <tr>
          	<td></td
          	<td><span class="error"><html:errors property="address"/></span></td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.address" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="address"/></td>
          </tr>
          <!-- Codigo Postal -->
          <tr>
          	<td></td>
          	<td><span class="error">
          			<html:errors property="primaryAreaCode"/>
          			<html:errors property="secondaryAreaCode"/>
          		</span>
          	</td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.postCode" bundle="DEFAULT" /></td>
            <td class="greytxt">
            	<html:text size="4" maxlength="4" property="primaryAreaCode"/> - 
            	<html:text size="3" maxlength="3" property="secondaryAreaCode"/> (XXXX-XXX)
            </td>
          </tr>
          <!-- Area do Codigo Postal -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="areaOfAreaCode"/></span></td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.areaOfPostCode" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="25" maxlength="25" property="areaOfAreaCode"/></td>
          </tr>
          <!-- Localidade de Residencia -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="area"/></span></td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.place" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="25" maxlength="25" property="area"/></td>
          </tr>
          <!-- Freguesia de Residencia -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="parishOfResidence"/></span></td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.addressParish" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="parishOfResidence"/></td>
          </tr>
          <!-- Concelho de Residencia -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="districtSubdivisionOfResidence"/></span></td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.addressMunicipality" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="districtSubdivisionOfResidence"/></td>
          </tr>
          <!-- Distrito de Residencia -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="districtOfResidence"/></span></td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.addressDistrict" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="40" property="districtOfResidence"/></td>
          </tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Contactos -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">4</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.contactInfo" bundle="DEFAULT" /></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%">
          <!-- Telefone -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="phone"/></span><td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.telephone" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="9" maxlength="9" property="phone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="mobile"/></span><td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.mobilePhone" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="9" maxlength="9" property="mobile"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
          	<td></td>
          	<td><span class="error"><html:errors property="email"/></span></td>
          </tr>
          <tr>
            <td width="30%"><bean:message key="label.person.email" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="50" property="email"/> 
            					<bean:message key="label.person.availableEmail" bundle="DEFAULT" />
            					<html:checkbox property="availableEmail"/></td>            
          </tr>
          <!-- WebPage -->
          <tr>
            <td width="30%"><bean:message key="label.person.webSite" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text size="40" maxlength="200" property="webAddress"/> 
					            <bean:message key="label.person.availableWebSite" bundle="DEFAULT" />
					            <html:checkbox property="availableWebAdress"/></td>
          </tr>
          <!-- Photo -->
          <tr>
            <td width="30%"><bean:message key="label.person.photo" bundle="DEFAULT" /></td>
          	<td class="greytxt">
          		<!-- <html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+infoPersonId.toString()%>"/> -->
          		<bean:message key="label.person.availablePhoto" bundle="DEFAULT" />
          		<html:checkbox property="availablePhoto"/>
          	</td>
          </tr>
       </logic:present>
       	</table>
		<p align="center"><html:submit styleClass="inputbutton">Continuar</html:submit></p>
</html:form>