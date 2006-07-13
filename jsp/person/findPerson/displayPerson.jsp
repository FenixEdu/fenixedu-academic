<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

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
	
	<logic:greaterThan name="previousStartIndex" value="-1">
		<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("previousStartIndex") + "&amp;roleType=" + pageContext.findAttribute("roleType")+ "&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;degreeType=" + pageContext.findAttribute("degreeType")+ "&amp;departmentId=" + pageContext.findAttribute("departmentId")%>"> anteriores </html:link>			
	</logic:greaterThan>
	<bean:define id="limitFindedPersons">
		<%= SessionConstants.LIMIT_FINDED_PERSONS %>
	</bean:define>
	<logic:lessThan name="startIndex" value="<%= pageContext.findAttribute("totalFindedPersons").toString()%>">
		<logic:equal name="numberFindedPersons" value="<%= limitFindedPersons %>">    
				<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("startIndex")+ "&amp;roleType=" + pageContext.findAttribute("roleType") + "&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;degreeType=" + pageContext.findAttribute("degreeType")+ "&amp;departmentId=" + pageContext.findAttribute("departmentId")%>"> próximos </html:link>			
		</logic:equal>
	</logic:lessThan>
	
	
	<br /><br />
	<logic:iterate id="personalInfo" name="personListFinded" indexId="personIndex">	    
		<bean:define id="personID" name="personalInfo" property="idInternal"/>
		
	  	<table width="100%" cellpadding="0" cellspacing="0">
		  <!-- Nome -->
		  <tr>
            	<td class="infoop" width="25"><span class="emphasis-box"><%= String.valueOf(personIndex.intValue() + 1) %></span></td>
		    	<td class="infoop"><strong><bean:write name="personalInfo" property="username"/>-<bean:write name="personalInfo" property="nome"/></strong></td>
          </tr>
	 	</table>
		<table width="100%" >
		  <tr>	
		  	<logic:equal name="viewPhoto" value="true">
			  	<logic:equal name="personalInfo" property="availablePhoto" value="true">
					<td rowspan="4" width="100">	  	    		  	  	
	      				<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
	      			</td>
	      		</logic:equal>
	      	</logic:equal>
	      
		  <!-- Role -->
		  
	      	<td width="30%" colspan="2">
	      		<bean:size id="mainRolesSize" name="personalInfo" property="mainRoles"></bean:size> 
		      	<logic:iterate id="role" name="personalInfo" property="mainRoles" indexId="i">
	
			      	<b><bean:write name="role"/>
	      			<logic:notEqual name="mainRolesSize" value="<%= String.valueOf(i.intValue() + 1) %>">
		      			,&nbsp;
		      		</logic:notEqual>	      				
		      		</b>
	      		</logic:iterate> 	
	      		&nbsp;
	      	</td>
	      </tr>
	      
	      <logic:notEqual name="mainRolesSize" value="0">
	      <tr>
	      		<td>&nbsp;</td>
	      		<td>&nbsp;</td>
	      </tr>
	      </logic:notEqual>
		  		  
          <!-- Telefone de Trabalho -->                    
	      <tr>
	      	<td width="30%"><bean:message key="label.person.workPhone" /></td>
	        <td class="greytxt"><bean:write name="personalInfo" property="workPhone"/></td>
	      </tr>
	      <logic:present  name="personalInfo" property="infoEmployee" >
		      
		      <!-- Local de Trabalho -->                    
		      <logic:present name="personalInfo" property="infoEmployee.workingUnit" >
		      
		      <bean:define id="infoUnit" name="personalInfo" property="infoEmployee.workingUnit"/>	    			
		     
			      <tr>
			      	<td width="30%"><bean:message key="label.person.workPlace" /></td>
				  	      			      	
		   			<logic:iterate id="superiorUnit" name="infoUnit" property="superiorUnitsNames">
						<td class="greytxt"><bean:write name="superiorUnit"/></td></tr><tr><td>&nbsp;</td>		      	
					</logic:iterate>					
			      </tr>
		 
	         </logic:present>
	         <logic:present  name="personalInfo" property="infoEmployee.mailingUnit" >
	         <tr>
	      		<td width="30%"><bean:message key="label.person.mailingPlace" /></td>	     
		      	<bean:define id="costCenterNumber" name="personalInfo" property="infoEmployee.mailingUnit.costCenterCode"/>
		      	<bean:define id="unitName" name="personalInfo" property="infoEmployee.mailingUnit.name"/>
		      	<td class="greytxt"><bean:write name="costCenterNumber"/> - <bean:write name="unitName"/></td>
	      	</tr>
	        </logic:present>
          </logic:present>
          <logic:present  name="personalInfo" property="infoTeacher" >
		                        
		      <tr>
		      	<td width="30%"><bean:message key="label.teacher.category" />:</td>
		      	
			      	<bean:define id="categoryCode" name="personalInfo" property="infoTeacher.infoCategory.code"/>
			      	<bean:define id="categoryName" name="personalInfo" property="infoTeacher.infoCategory.longName"/>
			      	<td class="greytxt"><bean:write name="categoryCode"/> - <bean:write name="categoryName"/></td>
		      </tr>
          </logic:present>
          
          <logic:equal name="show" value="true">
          	  <!-- E-Mail -->
	          <tr>
	            <td width="30%"><bean:message key="label.person.email" /></td>
	            <td class="greytxt">
	     			<logic:present name="personalInfo" property="email">
						<bean:define id="eMail" name="personalInfo" property="email" />
		    	        <html:link href="<%= "mailto:" + pageContext.findAttribute("eMail").toString() %>"><bean:write name="personalInfo" property="email"/></html:link>		            
					</logic:present>
					&nbsp;
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
					&nbsp;
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
					</td>
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
		       <logic:equal name="personalInfo" property="availableWebSite" value="false">
		       	<tr>
		       		<td width="30%">&nbsp;</td>
		       		<td>&nbsp;</td>
		       	</tr>
	          </logic:equal>
          </logic:equal> 
          <logic:present  name="personalInfo" property="infoStudentCurricularPlanList" >
		      <!-- Locale de Trabalho -->   
		      <tr>   
		      <td width="30%"><bean:message key="label.degree" />:</td>  
		      
		      <logic:iterate id="infoStudent" name="personalInfo" property="infoStudentCurricularPlanList">		
			       <bean:define id="degreeName" name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.nome"/>
			       <logic:match name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" location="start" value="DEGREE"> 
			        <td class="greytxt"> <bean:message key="link.degree"/> <bean:write name="degreeName" /></td>
			       </logic:match>
			       <logic:match name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" location="start" value="MASTER_DEGREE"> 
			        <td class="greytxt"> <bean:message key="link.master"/> <bean:write name="degreeName" /></td>
			       </logic:match>
		      </tr><tr><td>&nbsp;</td>
		      
		      </logic:iterate>
          </logic:present>
    	</table>
    	
    	<br />
	</logic:iterate>	
	<logic:greaterThan name="previousStartIndex" value="-1">
		<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("previousStartIndex") + "&amp;roleType=" + pageContext.findAttribute("roleType")+ "&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;degreeType=" + pageContext.findAttribute("degreeType")+ "&amp;departmentId=" + pageContext.findAttribute("departmentId")%>"> anteriores </html:link>			
	</logic:greaterThan>
	<logic:lessThan name="startIndex" value="<%= pageContext.findAttribute("totalFindedPersons").toString()%>">	
		<logic:equal name="numberFindedPersons" value="<%= limitFindedPersons %>">
			<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("startIndex")+ "&amp;roleType=" + pageContext.findAttribute("roleType") + "&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;degreeType=" + pageContext.findAttribute("degreeType")+ "&amp;departmentId=" + pageContext.findAttribute("departmentId")%>"> próximos </html:link>	
		</logic:equal>
	</logic:lessThan>

</logic:present>