<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
 
 <table width="100%">
 	<tr valign="bottom">
 		<td><h2><bean:message key="label.person.title.personalConsult" /></h2></td>
 		<td align="right">
 			<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
 		</td>
 	</tr>
 </table>
 <br />
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
            <td style="width: 20em;"><bean:message key="label.person.name" />:</td>
            <td class="greytxt"><bean:write name="personalInfo" property="nome"/></td>
          </tr>
          <!-- Username -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.username" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="username"/></td> 
          </tr>
          <!-- IST Username -->
          <logic:notEmpty name="personalInfo" property="istUsername">
	          <tr>
	            <td style="width: 20em;"><bean:message key="label.person.istUsername" /></td>
	            <td class="greytxt"><bean:write name="personalInfo" property="istUsername"/></td> 
	          </tr>
          </logic:notEmpty>
          <!-- Sexo -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.sex" /></td>
            <td class="greytxt">
            	<logic:present name="personalInfo" property="sexo">
	            	<bean:define id="gender" name="personalInfo" property="sexo"/>
    	        	<bean:message key='<%=gender.toString()%>' bundle="ENUMERATION_RESOURCES"/>
    	        </logic:present>
            </td>
          </tr>
 	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.identificationDocumentNumber" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.identificationDocumentType" /></td>
            <td class="greytxt">
            	<logic:present name="personalInfo" property="tipoDocumentoIdentificacao">
	            	<bean:define id="idType" name="personalInfo" property="tipoDocumentoIdentificacao"/>
    	        	<bean:message key='<%=idType.toString()%>'/>
    	        </logic:present>
            </td>
          </tr>
          <!-- Local de Emissao do Documento de Identificacao -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Emissao do Documento de Identificacao -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.identificationDocumentIssueDate" /></td>
            <logic:present name="personalInfo" property="dataEmissaoDocumentoIdentificacao" >
	            <bean:define id="date" name="personalInfo" property="dataEmissaoDocumentoIdentificacao" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Data de Validade do Documento de Identificacao -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
            <logic:present name="personalInfo" property="dataValidadeDocumentoIdentificacao" >
	            <bean:define id="date" name="personalInfo" property="dataValidadeDocumentoIdentificacao" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Numero de Contribuinte -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.contributorNumber" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="numContribuinte"/></td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.occupation" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="profissao"/></td>
          </tr>
          <!-- Estado Civil -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.maritalStatus" /></td>
            <logic:present name="personalInfo" property="maritalStatus">
	            <bean:define id="maritalStatus" name="personalInfo" property="maritalStatus"/>
    	        <td class="greytxt"><bean:message key='<%= maritalStatus.toString() %>'/></td>
    	    </logic:present>
          </tr>
		</table>
		<br />
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
            <td style="width: 20em;"><bean:message key="label.person.birth" /></td>
            <logic:present name="personalInfo" property="nascimento" >
	            <bean:define id="date" name="personalInfo" property="nascimento" />
				<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>            
			</logic:present>
          </tr>
          <!-- Nacionalidade -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.country" /></td>
            <logic:present name="personalInfo"  property="infoPais">
		     	<td class="greytxt"><bean:write name="personalInfo" property="infoPais.nationality"/></td>
	        </logic:present>
	     	<logic:notPresent name="personalInfo"  property="infoPais">
		     	<td class="greytxt"></td>
	        </logic:notPresent>
          </tr>   
          <!-- Freguesia de Naturalidade -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.birthPlaceParish" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="freguesiaNaturalidade"/></td>
          </tr>
          <!-- Concelho de Naturalidade -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.birthPlaceMunicipality" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="concelhoNaturalidade"/></td>
          </tr>
          <!-- Distrito de Naturalidade -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.birthPlaceDistrict" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="distritoNaturalidade"/></td>
          </tr>
          <!-- Nome do Pai -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.fatherName" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="nomePai"/></td>
          </tr>
          <!-- Nome da Mae -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.motherName" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="nomeMae"/></td>
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
            <td style="width: 20em;"><bean:message key="label.person.address" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="morada"/></td>
          </tr>
          <!-- Codigo Postal -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.postCode" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="codigoPostal"/></td>
          </tr>
          <!-- Area do Codigo Postal -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.areaOfPostCode" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localidadeCodigoPostal"/></td>
          </tr>
          <!-- Localidade de Residencia -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.place" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localidade"/></td>
          </tr>
          <!-- Freguesia de Residencia -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.addressParish" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="freguesiaMorada"/></td>
          </tr>
          <!-- Concelho de Residencia -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.addressMunicipality" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="concelhoMorada"/></td>
          </tr>
          <!-- Distrito de Residencia -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.addressDistrict" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="distritoMorada"/></td>
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
            <td style="width: 20em;"><bean:message key="label.person.telephone" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="telefone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.mobilePhone" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="telemovel"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.email" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="email"/></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td style="width: 20em;"><bean:message key="label.person.webSite" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="enderecoWeb"/></td>
          </tr>
        </logic:present>
    </table>