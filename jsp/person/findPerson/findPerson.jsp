<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>



<h2><bean:message key="label.manager.findPerson" /></h2>
<br />
<span class="error"><html:errors/></span>
  
<html:form action="/preparePerson" >
	<html:hidden property="method" value="preparePerson" />
	<html:hidden property="startIndex" value="0" />
	<html:hidden property="pagesIndex" value="1"/>
	<html:hidden property="countPage" value="1"/>
	<html:hidden property="name" name="findPersonForm"/>
	<html:hidden property="departmentId" name="findPersonForm"/>
	<html:hidden property="degreeId" name="findPersonForm"/>
	<html:hidden property="viewPhoto" name="findPersonForm"/>
	
	

	
	<table>
		<tr>
			<td>
				<bean:message key="label.type"/>:
			</td>
			<td>	
			   	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.RoleType" bundle="ENUMERATION_RESOURCES" includedFields="STUDENT,TEACHER,GRANT_OWNER,EMPLOYEE" />
				
				<html:select property="roleType" onchange="this.form.submit()">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				<html:hidden property="roleType" name="findPersonForm"/>
				</html:select>
				
			</td>
		</tr>
		<logic:present name="degreeType">
			<tr>
			<td>
				<bean:message key="label.degree"/>:
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.DegreeType" bundle="ENUMERATION_RESOURCES"  />
					<html:select property="degreeType" onchange="this.form.submit()">
						<html:option key="dropDown.Default" value=""/>
						<html:options collection="values" property="value" labelProperty="label"/>				
					</html:select>
			</td>
			</tr>
		</logic:present>
		
 </table>
</html:form>

<SCRIPT LANGUAGE="JavaScript">

function check(e)
{
if (e.style.display == "none")
  {
  e.style.display = "";
  }
else
  {e.style.display = "none";}
}
</script>

<html:form action="/findPerson" >
<html:hidden property="method" value="findPerson" />
<html:hidden property="startIndex" value="0" />
<html:hidden property="page" value="1" />
<bean:define id="roleType" name="findPersonForm" property="roleType" type="java.lang.String"/>
<html:hidden property="roleType" value="<%= roleType %>"/>
<bean:define id="degreeType" name="findPersonForm" property="degreeType" type="java.lang.String"/>
<html:hidden property="degreeType" value="<%= degreeType %>"/>
<html:hidden property="pagesIndex" value="1"/>






<table>

<logic:present name="departments">
		<tr>
		<td>
			<bean:message key="label.teacher.finalWork.department"/>:
		</td>
		<td>
			<html:select property="departmentId">	
			<html:option key="dropDown.Default" value=""/>
				<logic:iterate id="department" name="departments" > 
			   	<bean:define id="departmentID" name="department" property="idInternal"/>
					<html:option value="<%= departmentID.toString() %>"> <bean:write name="department" 
						property="realName"/> 
					</html:option>
					
			  </logic:iterate>
			</html:select>
		</td>
		</tr>
</logic:present>
<logic:present name="nonMasterDegree">
		<tr>
		<td>
			<bean:message key="link.degree.nonMaster"/>:
		</td>
		<td>
			<html:select property="degreeId">	
				<html:option key="dropDown.Default" value=""/>
				<logic:iterate id="degree" name="nonMasterDegree" > 
			   	<bean:define id="degreeID" name="degree" property="idInternal"/>
			   		
					<html:option value="<%= degreeID.toString() %>"> <bean:write name="degree" 
						property="nome"/> 
					</html:option>
					
			  </logic:iterate>
			</html:select>
		</td>
		</tr>
</logic:present>
<logic:notPresent name="nonMasterDegree">
<html:hidden property="degreeId" value=""/>
</logic:notPresent>
<logic:notPresent name="departments">
<html:hidden property="departmentId" value=""/>
</logic:notPresent>
	<tr>
		<td colspan="2" class="infoop">
			<bean:message key="info.person.findPerson"/>
		</td>		
	</tr>
	
	
	
		
	<tr>
		<td>
			<bean:message key="label.viewPhoto" />
		</td>
		<td>
			<html:checkbox  property="viewPhoto" />
		</td>
	</tr>
	
	
	<tr>
		<td>
			<bean:message key="label.nameWord" />
		</td>
		<td>
			<html:text name="findPersonForm" property="name" size="50"/>
		</td>		
	</tr>
	
</table>

<html:submit styleClass="inputbutton">
	<bean:message key="button.search"/>
</html:submit>
<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>	
</html:form>

<logic:present name="personListFinded" >



<bean:size id="numberFindedPersons" name="personListFinded"/>
	<logic:notEqual name="numberFindedPersons" value="1">
		<b><bean:message key="label.manager.numberFindedPersons" arg0="<%= pageContext.findAttribute("totalFindedPersons").toString() %>" /></b>	
	</logic:notEqual>
	<logic:equal name="numberFindedPersons" value="1">
		<b><bean:message key="label.manager.findedOnePersons" arg0="<%= pageContext.findAttribute("totalFindedPersons").toString() %>" /></b>
	</logic:equal>
<br />

<logic:present name="pages" >
Páginas: 
	<logic:iterate id="pages" name="pages" indexId="pageIndex">	
		<bean:define id="indexPageId" value="<%= String.valueOf(pageIndex.intValue() + 1) %>" />
		<logic:equal name="pagesIndex" value="<%= pageContext.findAttribute("indexPageId").toString()%>" >
			<bean:write name="pagesIndex"/>
		</logic:equal>
		<logic:notEqual name="pagesIndex" value="<%= pageContext.findAttribute("indexPageId").toString()%>" >
			<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("pages").toString() + "&amp;roleType=" + pageContext.findAttribute("roleType")+ "&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;degreeType=" + pageContext.findAttribute("degreeType")+ "&amp;departmentId=" + pageContext.findAttribute("departmentId") + "&amp;pagesIndex=" + pageContext.findAttribute("indexPageId")%>"><%= pageIndex.intValue() + 1 %></html:link>
		</logic:notEqual>	
	</logic:iterate>
</logic:present>


<bean:size id="numberFindedPersons" name="personListFinded"/>

	
<br /><br />

	<logic:iterate id="personalInfo" name="personListFinded" indexId="personIndex">	   

		<bean:define id="personID" name="personalInfo" property="idInternal"/>
	
	  	<table width="100%" cellpadding="0" cellspacing="0">
		  <!-- Nome -->
		  <tr>
            	<td class="infoop" width="75%"><span class="emphasis-box"><%= String.valueOf(personIndex.intValue()+1) %></span>
		    		<strong><bean:write name="personalInfo" property="nome"/> (<bean:write name="personalInfo" property="username"/>)</strong>
        		
        		 
			  	<logic:equal name="viewPhoto" value="true">
				  	<logic:equal name="personalInfo" property="availablePhoto" value="true">
				  	<bean:define id="personID" name="personalInfo" property="idInternal"/>
						<td rowspan="4" width="100">	  	    		  	  	
		      				<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>"/>
		      			</td>
		      		</logic:equal>
		      	</logic:equal>
        		
	      		<bean:size id="mainRolesSize" name="personalInfo" property="mainRoles"></bean:size> 
		      	<logic:greaterThan name="mainRolesSize" value="0">
		      	<td class="infoop" width="25%">
			      	<logic:iterate id="role" name="personalInfo" property="mainRoles" indexId="i">
			      	
				      	<b><bean:write name="role"/>
		      			<logic:notEqual name="mainRolesSize" value="<%= String.valueOf(i.intValue() + 1) %>">
			      			,&nbsp;
			      		</logic:notEqual>	      				
			      		</b>
		      		</logic:iterate>
		      	</td> 
		      	</logic:greaterThan>	
	           <logic:equal name="mainRolesSize" value="0">
	      		<td class="infoop" width="30%">&nbsp;</td>
	    	   </logic:equal>
	    	   <td class="infoop">
	    	   <bean:define id="aa" value="<%= "aa" + personIndex %>" />
	    	      	 
	    	   <input type = button value=">"  indexed="true" onClick="check(document.getElementById('<%= pageContext.findAttribute("aa").toString() %>'));return false;" >
	    	   																
	    	   </td>
	      </tr>
	       </table >

	      <table  width="50%" cellpadding="0" cellspacing="0">
	       <!-- Telefone de Trabalho -->    
	                    
	      <tr>
	      <logic:notEqual name="personalInfo" property="workPhone" value="">  
	      	<td ><bean:message key="label.person.workPhone" /></td>
	        <td class="greytxt"><bean:write name="personalInfo" property="workPhone"/></td>
	      </logic:notEqual>  
	      
	      </tr>
	         <!-- E-Mail -->
	        <logic:equal name="show" value="true">
	        <tr>
          	  <logic:present name="personalInfo" property="email">
          	 	 <logic:notEqual name="personalInfo" property="email" value=""> 
		            <td class="greytxt"><bean:message key="label.person.email" /></td>
		            <td  >
							<bean:define id="eMail" name="personalInfo" property="email" />
			    	        <html:link href="<%= "mailto:" + pageContext.findAttribute("eMail").toString() %>"><bean:write name="personalInfo" property="email"/></html:link>		            
		            </td>		
		            </logic:notEqual>  
	         </logic:present>  
	       
	        </logic:equal>
	        <logic:equal name="show" value="false">
	       
	          <logic:equal name="personalInfo" property="availableEmail" value="true">
	          	<logic:present name="personalInfo" property="email">
		            <td class="greytxt"><bean:message key="label.person.email" /></td>
		            <td >
							<bean:define id="eMail" name="personalInfo" property="email" />
			    	        <html:link href="<%= "mailto:" + pageContext.findAttribute("eMail").toString() %>"><bean:write name="personalInfo" property="email"/></html:link>		            
					</td>
			 	</logic:present>
	          </logic:equal>
	        </logic:equal>	        
	      </tr>
	      </table>
	   
	    

	     <table  width="50%"  id="<%= pageContext.findAttribute("aa").toString() %>" style="display:none" cellpadding="0" cellspacing="0">
		 <logic:present  name="personalInfo" property="infoEmployee" >
		      
		      <!-- Local de Trabalho -->                    
		      <logic:present name="personalInfo" property="infoEmployee.workingUnit" >
		      
		      <bean:define id="infoUnit" name="personalInfo" property="infoEmployee.workingUnit"/>	    			
		     
			      <tr>
			      	<td ><bean:message key="label.person.workPlace" /></td>
				  	      			      	
		   			<logic:iterate id="superiorUnit" name="infoUnit" property="superiorUnitsNames">
						<td class="greytxt"><bean:write name="superiorUnit"/></td></tr><tr><td>&nbsp;</td>		      	
					</logic:iterate>					
			      </tr>
		 
	         </logic:present>
	         <logic:present  name="personalInfo" property="infoEmployee.mailingUnit" >
	         <tr>
	      		<td ><bean:message key="label.person.mailingPlace" /></td>	     
		      	<bean:define id="costCenterNumber" name="personalInfo" property="infoEmployee.mailingUnit.costCenterCode"/>
		      	<bean:define id="unitName" name="personalInfo" property="infoEmployee.mailingUnit.name"/>
		      	<td class="greytxt"><bean:write name="costCenterNumber"/> - <bean:write name="unitName"/></td>
	      	</tr>
	        </logic:present>
          </logic:present>
          <logic:present  name="personalInfo" property="infoTeacher" >
		                        
		      <tr>
		      	<td ><bean:message key="label.teacher.category" />:</td>
		      	
			      	<bean:define id="categoryCode" name="personalInfo" property="infoTeacher.infoCategory.code"/>
			      	<bean:define id="categoryName" name="personalInfo" property="infoTeacher.infoCategory.longName"/>
			      	<td class="greytxt"><bean:write name="categoryCode"/> - <bean:write name="categoryName"/></td>
		      </tr>
          </logic:present>
	      	 <!-- WebPage -->
	          <logic:equal name="personalInfo" property="availableWebSite" value="true">        
		         <logic:present name="personalInfo" property="availableWebSite">
		          <tr>
		            <td ><bean:message key="label.person.webSite" /></td>		            
		            <td class="greytxt" >	            	
						<logic:present name="personalInfo" property="enderecoWeb">
							<bean:define id="homepage" name="personalInfo" property="enderecoWeb" />
				           	<html:link href="<%= pageContext.findAttribute("homepage").toString() %>"><bean:write name="personalInfo" property="enderecoWeb"/></html:link>
						</logic:present>
		            </td>
		          </tr>
		          </logic:present>
		       </logic:equal>
		       
            
          <logic:present  name="personalInfo" property="infoStudentCurricularPlanList" >
		      <!-- Locale de Trabalho -->   
		      <tr>   
		      <td ><bean:message key="label.degree" />:</td>  
		      
		      <logic:iterate id="infoStudent" name="personalInfo" property="infoStudentCurricularPlanList">		
			       <bean:define id="degreeName" name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.nome"/>
			       <logic:match name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" location="start" value="DEGREE"> 
			        <td class="greytxt" > <bean:message key="link.degree"/> <bean:write name="degreeName" /></td>
			       </logic:match>
			       <logic:match name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" location="start" value="MASTER_DEGREE"> 
			        <td class="greytxt"> <bean:message key="link.master"/> <bean:write name="degreeName" /></td>
			       </logic:match>
		      </tr><tr><td width="30%">&nbsp;</td>
		      
		      </logic:iterate>
          </logic:present>
          </table>
         
    	
    	<br />
	  </logic:iterate>

</logic:present>