<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<span class="error"><html:errors/></span>

<h2><bean:message key="label.manager.findPerson" /></h2>

<logic:notPresent name="personListFinded">
	<span class="errors"><bean:message key="error.manager.implossible.findPerson" /></span>
</logic:notPresent>

<logic:present name="personListFinded">
	<bean:size id="numberFindedPersons" name="personListFinded"/>
	<logic:notEqual name="numberFindedPersons" value="1">
		<b><bean:message key="label.manager.numberFindedPersons" arg0="<%= String.valueOf(numberFindedPersons) %>" /></b>	
	</logic:notEqual>
	
	<logic:equal name="numberFindedPersons" value="1">
		<b><bean:message key="label.manager.findedOnePersons" arg0="<%= String.valueOf(numberFindedPersons) %>" /></b>
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
          <!-- Telefone de Trabalho -->
          <logic:notEmpty name="personalInfo" property="workPhone">          
	          <tr>
	            <td width="30%"><bean:message key="label.person.workPhone" /></td>
	            <td class="greytxt"><bean:write name="personalInfo" property="workPhone"/></td>
	          </tr>
          </logic:notEmpty>
          <!-- E-Mail -->
          <logic:equal name="personalInfo" property="availableEmail" value="true">
	          <tr>
	            <td width="30%"><bean:message key="label.person.email" /></td>
	            <td class="greytxt"><bean:write name="personalInfo" property="email"/></td>
	          </tr>
          </logic:equal>
          <!-- WebPage -->
          <logic:equal name="personalInfo" property="availableWebSite" value="true">        
	          <tr>
	            <td width="30%"><bean:message key="label.person.webSite" /></td>
	            <td class="greytxt"><bean:write name="personalInfo" property="enderecoWeb"/></td>
	          </tr>
          </logic:equal>
    	</table>
    	<br />
	</logic:iterate>
</logic:present>