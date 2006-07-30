<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>

<style>
.leftcell {
width: 20em;
}
</style>

<p><strong>Página 2 de 6</strong></p>

 <br/>
 <h2><bean:message key="label.person.title.personalConsult" bundle="DEFAULT" /></h2>

<logic:present name="incompleteData" scope="request">
	<p align="center"><span class="error"><!-- Error messages go here --><%= request.getAttribute("incompleteData") %></span></p>
</logic:present>
<html:form action="/studentPersonalDataAuthorization?method=preparePersonalDataUseInquiry">
 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="3"/>
 <br/>
 <logic:present name="infoPerson">
 <bean:define id="infoPersonId" name="infoPerson" property="idInternal"/>
 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal" value="<%= infoPersonId.toString()%>"/>

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
            <td class="leftcell"><bean:message key="label.person.name" bundle="DEFAULT"/></td>
            <td class="greytxt"><bean:write name="infoPerson" property="nome"/></td>
          </tr>
          <!-- Username -->
          <tr>
            <td class="leftcell"><bean:message key="label.person.username" bundle="DEFAULT"/></td>
            <td class="greytxt"><bean:write name="infoPerson" property="username"/></td> 
          </tr>
          <!-- Sexo -->
          <tr>
            <td class="leftcell"><bean:message key="label.person.sex" bundle="DEFAULT" /></td>
            <td class="greytxt">
            <bean:define id="sex" name="infoPerson" property="sexo"/>
            <bean:message key='<%= sex.toString() %>' bundle="ENUMERATION_RESOURCES"/></td>
          </tr>
 	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td class="leftcell"><bean:message key="label.person.identificationDocumentNumber" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="numeroDocumentoIdentificacao"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td class="leftcell"><bean:message key="label.person.identificationDocumentType" bundle="DEFAULT" /></td>
            <td class="greytxt">
	            <e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType" bundle="ENUMERATION_RESOURCES"/>
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.identificationDocumentType" property="identificationDocumentType">
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
            </td>
          </tr>
          <!-- Local de Emissao do Documento de Identificacao -->
          <tr>
            <td class="leftcell"><bean:message key="label.person.identificationDocumentIssuePlace" bundle="DEFAULT" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="localEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Emissao do Documento de Identificacao -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="dayOfEmissionDateOfDocumentId"/></span>
				<span class="error"><!-- Error messages go here --><html:errors property="monthOfEmissionDateOfDocumentId"/></span>
				<span class="error"><!-- Error messages go here --><html:errors property="yearOfEmissionDateOfDocumentId"/></span>
			</td>
		  </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.identificationDocumentIssueDate" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
			<td class="greytxt">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dayOfEmissionDateOfDocumentId" size="2" maxlength="2" property="dayOfEmissionDateOfDocumentId"/> /
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.monthOfEmissionDateOfDocumentId" size="2" maxlength="2" property="monthOfEmissionDateOfDocumentId"/> /
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.yearOfEmissionDateOfDocumentId" size="4" maxlength="4" property="yearOfEmissionDateOfDocumentId"/> (dd/mm/aaaa)
			</td>            
          </tr>
          <!-- Data de Validade do Documento de Identificacao -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="dayOfExpirationDateOfDocumentId"/></span>
				<span class="error"><!-- Error messages go here --><html:errors property="monthOfExpirationDateOfDocumentId"/></span>
				<span class="error"><!-- Error messages go here --><html:errors property="yearOfExpirationDateOfDocumentId"/></span>
			</td>
		  </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.identificationDocumentExpirationDate" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dayOfExpirationDateOfDocumentId" size="2" maxlength="2" property="dayOfExpirationDateOfDocumentId"/> /
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.monthOfExpirationDateOfDocumentId" size="2" maxlength="2" property="monthOfExpirationDateOfDocumentId"/> /
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.yearOfExpirationDateOfDocumentId" size="4" maxlength="4" property="yearOfExpirationDateOfDocumentId"/> (dd/mm/aaaa)								            
			</td>   
          </tr>
          <!-- Numero de Contribuinte -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="contributorNumber"/></span></td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.contributorNumber" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" size="9" maxlength="9" property="contributorNumber"/></td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td class="leftcell"><bean:message key="label.person.occupation" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.occupation" size="40" maxlength="40" property="occupation"/></td>
          </tr>
          <!-- Estado Civil -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="maritalStatus"/></span><td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.maritalStatus" bundle="DEFAULT" /></td>
            <td class="greytxt">
            	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.MaritalStatus" bundle="DEFAULT"/>
                <html:select bundle="HTMLALT_RESOURCES" altKey="select.maritalStatus" property="maritalStatus">
                    <html:options collection="values" property="value" labelProperty="label"/>
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
            <td class="leftcell"><bean:message key="label.person.birth" bundle="DEFAULT" /></td>
            <logic:present name="infoPerson" property="nascimento" >
	            <bean:define id="date" name="infoPerson" property="nascimento" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Nacionalidade -->
          <tr>
            <td class="leftcell"><bean:message key="label.person.country" bundle="DEFAULT" /></td>
		     	<td class="greytxt">
		     		<html:select bundle="HTMLALT_RESOURCES" altKey="select.nacionality" property="nacionality">
	                	<html:options collection="nationalityList" property="value" labelProperty="label"/>
                	</html:select>
                </td>
          </tr>
          <!-- Freguesia de Naturalidade -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="parishOfBirth"/></span></td>
	      </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.birthPlaceParish" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.parishOfBirth" size="30" maxlength="30" property="parishOfBirth"/></td>
          </tr>
          <!-- Concelho de Naturalidade -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="districtSubvisionOfBirth"/></span></td>
		  </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.birthPlaceMunicipality" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtSubvisionOfBirth" size="30" maxlength="30" property="districtSubvisionOfBirth"/></td>
          </tr>
           <!-- Distrito de Naturalidade -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="districtOfBirth"/></span></td> 
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.birthPlaceDistrict" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtOfBirth" size="30" maxlength="30" property="districtOfBirth"/></td>
          </tr>
          <!-- Nome do Pai -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="nameOfFather"/></span></td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.fatherName" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.nameOfFather" size="50" maxlength="56" property="nameOfFather"/></td>
          </tr>
          <!-- Nome da Mae -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="nameOfMother" /></td>
		  </tr>		
          <tr>
            <td class="leftcell"><bean:message key="label.person.motherName" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.nameOfMother" size="50" maxlength="60" property="nameOfMother"/></td>
          </tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Dados de Residencia -->
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">3</span></td>
            <td class="infoop"><strong><bean:message key="label.person.title.addressInfo" bundle="DEFAULT" /> <span class="redtxt">*</span></strong></td>
          </tr>
		</table>
		<br />
		<table width="100%" >
          <!-- Morada -->
          <tr>
          	<td></td
          	<td><span class="error"><!-- Error messages go here --><html:errors property="address"/></span></td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.address" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.address" size="50" maxlength="40" property="address"/></td>
          </tr>
          <!-- Codigo Postal -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here -->
          			<html:errors property="primaryAreaCode"/>
          			<html:errors property="secondaryAreaCode"/>
          		</span>
          	</td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.postCode" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt">
            	<html:text bundle="HTMLALT_RESOURCES" altKey="text.primaryAreaCode" size="4" maxlength="4" property="primaryAreaCode"/>
            	 - 
            	<html:text bundle="HTMLALT_RESOURCES" altKey="text.secondaryAreaCode" size="3" maxlength="3" property="secondaryAreaCode"/> (XXXX-XXX)
            </td>
          </tr>
          <!-- Area do Codigo Postal -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="areaOfAreaCode"/></span></td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.areaOfPostCode" bundle="DEFAULT" />  <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" size="25" maxlength="25" property="areaOfAreaCode"/></td>
          </tr>
          <!-- Localidade de Residencia -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="area"/></span></td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.place" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.area" size="25" maxlength="25" property="area"/></td>
          </tr>
          <!-- Freguesia de Residencia -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="parishOfResidence"/></span></td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.addressParish" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.parishOfResidence" size="30" maxlength="30" property="parishOfResidence"/></td>
          </tr>
          <!-- Concelho de Residencia -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="districtSubdivisionOfResidence"/></span></td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.addressMunicipality" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtSubdivisionOfResidence" size="30" maxlength="30" property="districtSubdivisionOfResidence"/></td>
          </tr>
          <!-- Distrito de Residencia -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="districtOfResidence"/></span></td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.addressDistrict" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtOfResidence" size="30" maxlength="30" property="districtOfResidence"/></td>
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
          	<td><span class="error"><!-- Error messages go here --><html:errors property="phone"/></span><td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.telephone" bundle="DEFAULT" /> <span class="redtxt">*</span></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.phone" size="9" maxlength="9" property="phone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="mobile"/></span><td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.mobilePhone" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.mobile" size="9" maxlength="9" property="mobile"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
          	<td></td>
          	<td><span class="error"><!-- Error messages go here --><html:errors property="email"/></span></td>
          </tr>
          <tr>
            <td class="leftcell"><bean:message key="label.person.email" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.email" size="40" maxlength="50" property="email"/> 
            					<bean:message key="label.person.availableEmail" bundle="DEFAULT" />
            					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.availableEmail" property="availableEmail"/></td>            
          </tr>
          <!-- WebPage -->
          <tr>
            <td class="leftcell"><bean:message key="label.person.webSite" bundle="DEFAULT" /></td>
            <td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.webAddress" size="40" maxlength="200" property="webAddress"/> 
					            <bean:message key="label.person.availableWebSite" bundle="DEFAULT" />
					            <html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.availableWebAdress" property="availableWebAdress"/></td>
          </tr>
          <!-- Photo -->
          <tr>
            <td class="leftcell"><bean:message key="label.person.photo" bundle="DEFAULT" /></td>
          	<td class="greytxt">          		
          		<bean:message key="label.person.availablePhoto" bundle="DEFAULT" />
          		<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.availablePhoto" property="availablePhoto"/>
          	</td>
          </tr>
       </logic:present>
       	</table>

       	<br/>
		<p align="center"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">Continuar</html:submit></p>
</html:form>