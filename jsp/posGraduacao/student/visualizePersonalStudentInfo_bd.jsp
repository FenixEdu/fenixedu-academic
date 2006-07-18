<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
 <h2><bean:message key="label.person.title.personalConsult" /></h2>
 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
 <br />
        <logic:present name="infoPerson">
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
            <td class="greytxt"><bean:write name="infoPerson" property="nome"/></td>
          </tr>
          <!-- Username -->
          <tr>
            <td width="30%"><bean:message key="label.person.username" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="username"/></td> 
          </tr>
          <!-- Sexo -->
          <tr>
            <td width="30%"><bean:message key="label.person.sex" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="sexo"/></td>
          </tr>
 	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentNumber" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="numeroDocumentoIdentificacao"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentType" /></td>
            <td class="greytxt">
            	<logic:present name="infoPerson" property="tipoDocumentoIdentificacao" >
	            	<bean:define id="idType" name="infoPerson" property="tipoDocumentoIdentificacao"/>
	            	<bean:message key='<%=idType.toString()%>'/>
            	</logic:present>
            </td>
          </tr>
          <!-- Local de Emissao do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="localEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Emissao do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentIssueDate" /></td>
            <logic:present name="infoPerson" property="dataEmissaoDocumentoIdentificacao" >
	            <bean:define id="date" name="infoPerson" property="dataEmissaoDocumentoIdentificacao" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Data de Validade do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
            <logic:present name="infoPerson" property="dataValidadeDocumentoIdentificacao" >
	            <bean:define id="date" name="infoPerson" property="dataValidadeDocumentoIdentificacao" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Numero de Contribuinte -->
          <tr>
            <td width="30%"><bean:message key="label.person.contributorNumber" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="numContribuinte"/></td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td width="30%"><bean:message key="label.person.occupation" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="profissao"/></td>
          </tr>
          <!-- Estado Civil -->
          <tr>
            <td width="30%"><bean:message key="label.person.maritalStatus" /></td>
            <bean:define id="maritalStatus" name="infoPerson" property="maritalStatus"/>
            <td class="greytxt"><bean:message key='<%= maritalStatus.toString() %>'/></td>
          </tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Filia��o -->
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
            <logic:present name="infoPerson" property="nascimento" >
	            <bean:define id="date" name="infoPerson" property="nascimento" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Nacionalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.country" /></td>
			<bean:define id="nationality" name="infoPerson" property="infoPais.nationality" type="java.lang.String"/>
            <td class="greytxt">
				<%
					if(nationality.startsWith("PORTUGUESA")){
				%>
					<bean:message key="label.person.portugueseNationality" />
				<% }else{ %>
					<bean:write name="infoPerson" property="infoPais.nationality"/>
				<% } %>
				
			</td>
          </tr>   
          <!-- Freguesia de Naturalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceParish" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="freguesiaNaturalidade"/></td>
          </tr>
          <!-- Concelho de Naturalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceMunicipality" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="concelhoNaturalidade"/></td>
          </tr>
          <!-- Distrito de Naturalidade -->
          <tr>
            <td width="30%"><bean:message key="label.person.birthPlaceDistrict" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="distritoNaturalidade"/></td>
          </tr>
          <!-- Nome do Pai -->
          <tr>
            <td width="30%"><bean:message key="label.person.fatherName" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="nomePai"/></td>
          </tr>
          <!-- Nome da Mae -->
          <tr>
            <td width="30%"><bean:message key="label.person.motherName" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="nomeMae"/></td>
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
            <td class="greytxt"><bean:write name="infoPerson" property="morada"/></td>
          </tr>
          <!-- Codigo Postal -->
          <tr>
            <td width="30%"><bean:message key="label.person.postCode" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="codigoPostal"/></td>
          </tr>
          <!-- Area do Codigo Postal -->
          <tr>
            <td width="30%"><bean:message key="label.person.areaOfPostCode" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="localidadeCodigoPostal"/></td>
          </tr>
          <!-- Localidade de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.place" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="localidade"/></td>
          </tr>
          <!-- Freguesia de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.addressParish" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="freguesiaMorada"/></td>
          </tr>
          <!-- Concelho de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.addressMunicipality" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="concelhoMorada"/></td>
          </tr>
          <!-- Distrito de Residencia -->
          <tr>
            <td width="30%"><bean:message key="label.person.addressDistrict" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="distritoMorada"/></td>
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
            <td class="greytxt"><bean:write name="infoPerson" property="telefone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td width="30%"><bean:message key="label.person.mobilePhone" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="telemovel"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td width="30%"><bean:message key="label.person.email" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="email"/></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td width="30%"><bean:message key="label.person.webSite" /></td>
            <td class="greytxt"><bean:write name="infoPerson" property="enderecoWeb"/></td>
          </tr>
        </logic:present>
    </table>
    <bean:define id="link">/editStudentToVisualizeInformations.do?method=execute&page=0&studentNumber=<bean:write name="studentNumber" /></bean:define>
    
    <html:link page='<%= pageContext.findAttribute("link").toString() %>'>
		<bean:message key="label.masterDegree.administrativeOffice.editStudentData"/>
	</html:link>