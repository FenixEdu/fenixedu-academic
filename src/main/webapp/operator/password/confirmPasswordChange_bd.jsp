<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<em><bean:message key="operator" /></em>
<h2><bean:message key="link.operator.newPassword" /></h2>

<logic:notPresent name="personListFinded">
	<p>
		<span class="errors"><bean:message key="error.manager.implossible.findPerson" /></span>
	</p>
</logic:notPresent>

<logic:present name="personListFinded">
	
	<bean:define id="totalFindedPersons" name="totalFindedPersons" />	
	<logic:notEqual name="totalFindedPersons" value="1">
		<p><bean:message key="label.manager.numberFindedPersons" arg0="<%= String.valueOf(totalFindedPersons) %>" /></p>	
	</logic:notEqual>
	
	<logic:equal name="totalFindedPersons" value="1">
		<p><bean:message key="label.manager.findedOnePersons" arg0="<%= String.valueOf(totalFindedPersons) %>" /></p>
	</logic:equal>

		
	<bean:define id="url">/operator/findPerson.do?method=findPerson&username=<bean:write name="username"/>&documentIdNumber=<bean:write name="documentIdNumber"/></bean:define>				
	<bean:message key="label.collectionPager.page" bundle="MANAGER_RESOURCES"/>:
	<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>

	
	<logic:iterate id="personalInfo" name="personListFinded" indexId="personIndex">	    
		<bean:define id="personID" name="personalInfo" property="idInternal"/>
		<bean:define id="username" name="personalInfo" property="username" />
	
	  	<table width="98%" cellpadding="0" cellspacing="0" class="mvert15">
		  <!-- Nome -->
		  <tr>
            	<td class="infoop" width="25"><span class="emphasis-box"><%= String.valueOf(personIndex.intValue() + 1) %></span></td>
		    	<td class="infoop"><strong><bean:write name="personalInfo" property="name"/></strong></td>
          </tr>
	 	</table>
	 	
		<table>
		  <!-- Role -->
		  <tr>
	      	<td colspan="2">
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
			<td>
				<bean:message key="label.identificationDocumentNumber" />:
			</td>
			<td>
				<bean:write name="personalInfo" property="documentIdNumber" />
			</td>
		  </tr>
		  <tr>
			<td>
				<bean:message key="label.identificationDocumentType" />:
			</td>
			<td>
				<bean:define id="idType" name="personalInfo" property="idDocumentType"/>
				<bean:message key='<%=idType.toString()%>'/>
			</td>
		  </tr>		  
          <!-- Telefone de Trabalho -->                    
	      <tr>
	      	<td><bean:message key="label.person.workPhone" /></td>
	        <td><bean:write name="personalInfo" property="workPhone"/></td>
	      </tr>                    
       	  <!-- E-Mail -->
          <tr>
            <td><bean:message key="label.person.email" /></td>
            <td><bean:write name="personalInfo" property="email"/></td>
          </tr>         
          <!-- CellPhone -->
          <tr>
            <td><bean:message key="label.person.mobilePhone" /></td>
            <td><bean:write name="personalInfo" property="mobile"/></td>
          </tr> 
          <!-- Address -->
          <tr>
            <td><bean:message key="label.person.address" /></td>
            <td><bean:write name="personalInfo" property="address"/></td>
          </tr>     
          <!-- Area Code -->
          <tr>
            <td><bean:message key="label.person.postCode" /></td>
            <td><bean:write name="personalInfo" property="areaCode"/></td>
          </tr>
          <!-- Area of Area Code -->
          <tr>
            <td><bean:message key="label.person.areaOfPostCode" /></td>
            <td><bean:write name="personalInfo" property="areaOfAreaCode"/></td>
          </tr>
          <!-- Area -->
          <tr>
            <td><bean:message key="label.person.place" /></td>
            <td><bean:write name="personalInfo" property="area"/></td>
          </tr>
               
    	</table>

	<p class="mtop15">
		<strong>	
			<html:link page="<%= "/generateNewPassword.do?method=generatePassword&page=0&personID="
						+ pageContext.findAttribute("personID")
						+ "&username="
						+ pageContext.findAttribute("username")
				%>"  target="_blank">
				<bean:message key="link.operator.changePassword" />
			</html:link>
		</strong>
	</p>    	
	</logic:iterate>
	
	<logic:notEqual name="numberOfPages" value="1">
		<br/><br/>
		<bean:message key="label.collectionPager.page" bundle="MANAGER_RESOURCES"/>:		
		<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>				
	</logic:notEqual>
	
</logic:present>



