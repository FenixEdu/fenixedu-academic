<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<span class="error"><html:errors/></span>

<h2><bean:message key="label.manager.findPerson" /></h2>

<logic:notPresent name="personListFinded">
	<span class="errors"><bean:message key="error.manager.implossible.findPerson" /></span>
</logic:notPresent>

<logic:present name="personListFinded">
	<bean:size id="numberFindedPersons" name="personListFinded"/>
	<logic:notEqual name="numberFindedPersons" value="1">
		<b><bean:message key="label.manager.numberFindedPersons" arg0="<%= pageContext.findAttribute("totalFindedPersons").toString() %>" /></b>	
	</logic:notEqual>
	
	<logic:equal name="numberFindedPersons" value="1">
		<b><bean:message key="label.manager.findedOnePersons" arg0="<%= pageContext.findAttribute("totalFindedPersons").toString() %>" /></b>
	</logic:equal>
	<br />
	
	<logic:greaterThan name="previousStartIndex" value="0">
		<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("previousStartIndex") %>"> anteriores </html:link>			
	</logic:greaterThan>
	<bean:define id="limitFindedPersons">
		<%= SessionConstants.LIMIT_FINDED_PERSONS %>
	</bean:define>
	<logic:equal name="numberFindedPersons" value="<%= limitFindedPersons %>">
		<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("startIndex") %>"> próximos </html:link>			
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
		  <!-- Role -->
		  <tr>
	      	<td width="30%" colspan="2">
	      		<logic:match name="personalInfo" property="username" value="D">
	      			<b><bean:message key="teacher.docente"/></b>
	      		</logic:match>
	      		<logic:match name="personalInfo" property="username" value="F">
	      			<b><bean:message key="employee"/></b>
	      		</logic:match>
	      		<logic:match name="personalInfo" property="username" value="L">
	      			<b><bean:message key="student"/></b>
	      		</logic:match>	     
	      		<logic:match name="personalInfo" property="username" value="M">
	      			<b><bean:message key="studentMasterDegree"/></b>
	      		</logic:match>
	      		<logic:match name="personalInfo" property="username" value="B">
	      			<b><bean:message key="grantOwner"/></b>
	      		</logic:match> 	
	      	</td>
	      </tr>
		  
		  
          <!-- Telefone de Trabalho -->                    
	      <tr>
	      	<td width="30%"><bean:message key="label.person.workPhone" /></td>
	        <td class="greytxt"><bean:write name="personalInfo" property="workPhone"/></td>
	      </tr>
                    
          <logic:equal name="show" value="true">
          	  <!-- E-Mail -->
	          <tr>
	            <td width="30%"><bean:message key="label.person.email" /></td>
	            <td class="greytxt">
	     			<logic:present name="personalInfo" property="email">
						<bean:define id="eMail" name="personalInfo" property="email" />
		    	        <html:link href="<%= "mailto:" + pageContext.findAttribute("eMail").toString() %>"><bean:write name="personalInfo" property="email"/></html:link>		            
					</logic:present>
	            </td>		         
	          </tr>  
	          <!-- WebPage --> 	          
	          <tr>
	            <td width="30%"><bean:message key="label.person.webSite" /></td>
	            <td class="greytxt">	            	
					<logic:present name="personalInfo" property="enderecoWeb">
						<bean:define id="homepage" name="personalInfo" property="enderecoWeb" />
			           	<html:link href="<%= pageContext.findAttribute("homepage").toString() %>"><bean:write name="personalInfo" property="enderecoWeb"/></html:link>
					</logic:present>
	            </td>
	          </tr>	                 
          </logic:equal>          
                    
          <logic:equal name="show" value="false">
	          <logic:equal name="personalInfo" property="availableEmail" value="true">
		          <tr>
		            <td width="30%"><bean:message key="label.person.email" /></td>
		            <td class="greytxt">
		     			<logic:present name="personalInfo" property="email">
							<bean:define id="eMail" name="personalInfo" property="email" />
			    	        <html:link href="<%= "mailto:" + pageContext.findAttribute("eMail").toString() %>"><bean:write name="personalInfo" property="email"/></html:link>		            
						</logic:present>
		          </tr>
	          </logic:equal>
	          <!-- WebPage -->
	          <logic:equal name="personalInfo" property="availableWebSite" value="true">        
		          <tr>
		            <td width="30%"><bean:message key="label.person.webSite" /></td>		            
		            <td class="greytxt">	            	
						<logic:present name="personalInfo" property="enderecoWeb">
							<bean:define id="homepage" name="personalInfo" property="enderecoWeb" />
				           	<html:link href="<%= pageContext.findAttribute("homepage").toString() %>"><bean:write name="personalInfo" property="enderecoWeb"/></html:link>
						</logic:present>
		            </td>
		          </tr>
	          </logic:equal>
          </logic:equal> 
    	</table>
    	<br />
	</logic:iterate>	
	<logic:greaterThan name="previousStartIndex" value="0">
		<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("previousStartIndex") %>"> anteriores </html:link>			
	</logic:greaterThan>
	<logic:equal name="numberFindedPersons" value="<%= limitFindedPersons %>">
		<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("startIndex") %>"> próximos </html:link>			
	</logic:equal>
</logic:present>