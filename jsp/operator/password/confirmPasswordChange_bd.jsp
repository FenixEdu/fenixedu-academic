<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<br />

<logic:notPresent name="personListFinded">
	<span class="errors"><bean:message key="error.manager.implossible.findPerson" /></span>
</logic:notPresent>

<logic:present name="personListFinded">
	
	<bean:define id="totalFindedPersons" name="totalFindedPersons" />	
	<logic:notEqual name="totalFindedPersons" value="1">
		<b><bean:message key="label.manager.numberFindedPersons" arg0="<%= String.valueOf(totalFindedPersons) %>" /></b>	
	</logic:notEqual>
	
	<logic:equal name="totalFindedPersons" value="1">
		<b><bean:message key="label.manager.findedOnePersons" arg0="<%= String.valueOf(totalFindedPersons) %>" /></b>
	</logic:equal>
	<br /><br />
		
	<bean:message key="label.collectionPager.page" bundle="MANAGER_RESOURCES"/>:	
	<bean:define id="url">/operator/findPerson.do?method=findPerson&username=<bean:write name="username"/>&documentIdNumber=<bean:write name="documentIdNumber"/></bean:define>				
	<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>			
	<br /><br />	
	
	<logic:iterate id="personalInfo" name="personListFinded" indexId="personIndex">	    
		<bean:define id="personID" name="personalInfo" property="idInternal"/>
		<bean:define id="username" name="personalInfo" property="username" />
	
	  	<table width="100%" cellpadding="0" cellspacing="0">
		  <!-- Nome -->
		  <tr>
            	<td class="infoop" width="25"><span class="emphasis-box"><%= String.valueOf(personIndex.intValue() + 1) %></span></td>
		    	<td class="infoop"><strong><bean:write name="personalInfo" property="nome"/></strong></td>
          </tr>
	 	</table>
		<table width="100%">
		  <!-- Role -->
		  <tr>
	      	<td width="30%" colspan="2">
	      		<logic:match name="personalInfo" property="username" value="D">
	      			<b><bean:message key="teacher"/></b>
	      		</logic:match>
	      		<logic:match name="personalInfo" property="username" value="F">
	      			<b><bean:message key="employee"/></b>
	      		</logic:match>
	      		<logic:match name="personalInfo" property="username" value="L">
	      			<b><bean:message key="student"/></b>
	      		</logic:match>	      	
	      	</td>
	      </tr>
	      <!-- Document Id -->
		  <tr>
			<td width="30%">
				<bean:message key="label.identificationDocumentNumber" />
			</td>
			<td class="greytxt">
				<bean:write name="personalInfo" property="numeroDocumentoIdentificacao" />
			</td>
		  </tr>
		  <tr>
			<td width="30%">
				<bean:message key="label.identificationDocumentType" />
			</td>
			<td class="greytxt">
				<bean:define id="idType" name="personalInfo" property="tipoDocumentoIdentificacao"/>
				<bean:message key='<%=idType.toString()%>'/>
			</td>
		  </tr>		  
          <!-- Telefone de Trabalho -->                    
	      <tr>
	      	<td width="30%"><bean:message key="label.person.workPhone" /></td>
	        <td class="greytxt"><bean:write name="personalInfo" property="workPhone"/></td>
	      </tr>                    
       	  <!-- E-Mail -->
          <tr>
            <td width="30%"><bean:message key="label.person.email" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="email"/></td>
          </tr>         
          <!-- CellPhone -->
          <tr>
            <td width="30%"><bean:message key="label.person.mobilePhone" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="telemovel"/></td>
          </tr> 
          <!-- Address -->
          <tr>
            <td width="30%"><bean:message key="label.person.address" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="morada"/></td>
          </tr>     
          <!-- Area Code -->
          <tr>
            <td width="30%"><bean:message key="label.person.postCode" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="codigoPostal"/></td>
          </tr>
          <!-- Area of Area Code -->
          <tr>
            <td width="30%"><bean:message key="label.person.areaOfPostCode" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localidadeCodigoPostal"/></td>
          </tr>
          <!-- Area -->
          <tr>
            <td width="30%"><bean:message key="label.person.place" /></td>
            <td class="greytxt"><bean:write name="personalInfo" property="localidade"/></td>
          </tr>
               
    	</table>
    	<br />
	<br />
	<h2>	
	<html:link page="<%= "/generateNewPassword.do?method=generatePassword&page=0&personID="
					+ pageContext.findAttribute("personID")
					+ "&username="
					+ pageContext.findAttribute("username")
			%>"  target="_blank">
			<bean:message key="link.operator.changePassword" />
		</html:link>
	</h2>    	
	</logic:iterate>
</logic:present>



