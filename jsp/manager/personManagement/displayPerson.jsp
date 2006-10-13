<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.findPerson" /></h2>

<logic:notPresent name="personListFinded">
	<span class="errors"><bean:message bundle="MANAGER_RESOURCES" key="error.manager.implossible.findPerson" /></span>
</logic:notPresent>

<logic:present name="personListFinded">
	<bean:size id="numberFindedPersons" name="personListFinded"/>
	<logic:notEqual name="numberFindedPersons" value="1">
		<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.numberFindedPersons" arg0="<%= String.valueOf(numberFindedPersons) %>" /></b>	
	</logic:notEqual>
	
	<logic:equal name="numberFindedPersons" value="1">
		<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.findedOnePersons" arg0="<%= String.valueOf(numberFindedPersons) %>" /></b>
	</logic:equal>
	<br /><br />
	<logic:iterate id="personalInfo" name="personListFinded" indexId="personIndex">	    
		<bean:define id="personID" name="personalInfo" property="idInternal"/>
	  	<table width="100%" cellpadding="0" cellspacing="0">
		  <!-- Nome -->
		  <tr>
            	<td class="infoop" width="25"><span class="emphasis-box"><%= String.valueOf(personIndex.intValue() + 1) %></span></td>
		    	<td class="infoop"><strong><bean:write name="personalInfo" property="nome"/></strong></td>
          </tr>
	 	</table>
		<table width="100%">       
          <!-- Username -->
          <logic:notEmpty name="personalInfo" property="loginAlias">
	          <tr>
	            <td width="30%"><bean:message bundle="MANAGER_RESOURCES" key="label.person.username" /></td>
				<td class="greytxt">
					<bean:size name="personalInfo" property="loginAlias" id="aliasSize"/>
		          	<logic:iterate name="personalInfo" property="loginAlias" id="username" indexId="index">
			          	<bean:write name="username" property="alias"/>
			          	<logic:notEqual name="index" value="<%= String.valueOf(aliasSize.intValue() - 1) %>">
			          		;		          	
			          	</logic:notEqual>
		          	</logic:iterate>
	          	</td>          
	          </tr>       
          </logic:notEmpty>   
  	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message bundle="MANAGER_RESOURCES" key="label.person.identificationDocumentNumber" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td width="30%"><bean:message bundle="MANAGER_RESOURCES" key="label.person.identificationDocumentType" /></td>
            <td class="greytxt">
            	<bean:define id="idType" name="personalInfo" property="tipoDocumentoIdentificacao"/>
            	<bean:message bundle="MANAGER_RESOURCES" key='<%=idType.toString()%>' bundle="ENUMERATION_RESOURCES"/>
            </td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td width="30%"><bean:message bundle="MANAGER_RESOURCES" key="label.person.occupation" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="profissao"/></td>
          </tr>
          <!-- Telefone -->
          <tr>
            <td width="30%"><bean:message bundle="MANAGER_RESOURCES" key="label.person.telephone" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="telefone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td width="30%"><bean:message bundle="MANAGER_RESOURCES" key="label.person.mobilePhone" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="telemovel"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td width="30%"><bean:message bundle="MANAGER_RESOURCES" key="label.person.email" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="email"/></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td width="30%"><bean:message bundle="MANAGER_RESOURCES" key="label.person.webSite" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="enderecoWeb"/></td>
          </tr>
    	</table>
    	<br />
	</logic:iterate>
</logic:present>