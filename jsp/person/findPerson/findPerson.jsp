<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<style>
table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}

div.pp {
border: 1px solid #ccc;
padding-bottom: 0;
margin: 0.5em 0;
width: 100%;
float: left;
}

table.ppid {
padding: 0.5em;
width: 100%;
background-color: #eee;
}
div.pp img {
float: right;
margin: 4px;
border: 1px solid #ccc;
background-color: #eee;
padding: 2px;
}
table.ppdetails {
background-color: #fff;
border-collapse: collapse;
margin: 0.5em 1em;
}
table.pphigh {
background-color: #ffa;
}
table.ppdetails tr td {
padding: 0.25em;
}
table.ppdetails td.ppleft {
text-align: right;
width: 10em;
}
table.ppdetails td.ppright {
}
table.ppdetails td.ppright2 {
width: 10em;
}
table.ppdetails td.ppleft_mail {
text-align: right;
width: 10em;
}
table.ppdetails tr.highlight {
}
table.ppdetails td.highlight {
background-color: #ffffea;
}
</style>



<script language="JavaScript">
function check(e,v)
{
if (e.style.display == "none")
  {
  e.style.display = "";
  v.value = "-";
  }
else
  {e.style.display = "none";
  v.value = "+";
}
}
</script>




<h2><bean:message key="label.manager.findPerson"/></h2>
<span class="error"><html:errors/></span>

<!-- <table><tr><td colspan="2" class="infoop"><bean:message key="info.person.findPerson"/></td></tr></table> -->


<html:form action="/preparePerson" >
	<html:hidden property="method" value="preparePerson" />
	<html:hidden property="countPage" value="1"/>
	<html:hidden property="departmentId" name="findPersonForm"/>
	<html:hidden property="degreeId" name="findPersonForm"/>
	<html:hidden property="viewPhoto" name="findPersonForm"/>
	<html:hidden property="name" name="findPersonForm"/>
	
	<table class="search">
			
		<tr>
			<td class="leftcolumn"><bean:message key="label.type"/>:</td>
			<td><e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.RoleType" bundle="ENUMERATION_RESOURCES" includedFields="STUDENT,TEACHER,GRANT_OWNER,EMPLOYEE" />
				<html:select property="roleType" onchange="this.form.submit()">
				<html:option value=""/>
				<html:options collection="values" property="value" labelProperty="label"/>
				<html:hidden property="roleType" name="findPersonForm"/>
				
				</html:select>
			</td>
		</tr>
	
		<logic:present name="degreeType">
			<tr>
			<td class="leftcolumn"><bean:message key="label.degree"/>:</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.DegreeType" bundle="ENUMERATION_RESOURCES"  />
				<html:select property="degreeType" onchange="this.form.submit()">
				<html:option value=""/>
				<html:options collection="values" property="value" labelProperty="label"/>				
				<html:hidden property="degreeType" name="findPersonForm"/>
				</html:select>
			</td>
			</tr>
		</logic:present>
 
</html:form>


<html:form action="/findPerson" >
<html:hidden property="method" value="findPerson" />
<html:hidden property="startIndex" value="1" />
<html:hidden property="page" value="1" />
<bean:define id="roleType" name="findPersonForm" property="roleType" type="java.lang.String"/>
<html:hidden property="roleType" value="<%= roleType %>"/>
<bean:define id="degreeType" name="findPersonForm" property="degreeType" type="java.lang.String"/>
<html:hidden property="degreeType" value="<%= degreeType %>"/>

<logic:present name="departments">
		<tr>
		<td class="leftcolumn">
			<bean:message key="label.teacher.finalWork.department"/>:
		</td>
		<td>
			<html:select property="departmentId">	
			<html:option value=""/>
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
		<td class="leftcolumn">
			<bean:message key="label.degree.name"/>:
		</td>
		<td>
			<html:select property="degreeId">	
				<html:option value=""/>
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
		<td class="leftcolumn"><bean:message key="label.nameWord" />:</td>
		<td>
			<html:text name="findPersonForm" property="name" size="50"/>
			<html:hidden property="name" name="findPersonForm"/>
		</td>		
	</tr>
	<tr>
		<td class="leftcolumn">
			<bean:message key="label.viewPhoto" />:
		</td>
		<td>
			<html:checkbox  property="viewPhoto" />
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<html:submit styleClass="inputbutton">
			<bean:message key="button.search"/>
			</html:submit>
			<html:reset  styleClass="inputbutton">
			<bean:message key="label.clear"/>
			</html:reset>
		</td>
	</tr>
</table>
</html:form>



<logic:present name="personListFinded" >

<bean:size id="numberFindedPersons" name="personListFinded"/>
	<logic:notEqual name="numberFindedPersons" value="1">
		<p><b><bean:message key="label.manager.numberFindedPersons" arg0="<%= pageContext.findAttribute("totalFindedPersons").toString() %>" /></b></p>	
	</logic:notEqual>
	<logic:equal name="numberFindedPersons" value="1">
		<p><b><bean:message key="label.manager.findedOnePersons" arg0="<%= pageContext.findAttribute("totalFindedPersons").toString() %>" /></b></p>
	</logic:equal>

<logic:present name="pages" >
<p>Páginas:
	<logic:iterate id="pages" name="pages" indexId="pageIndex">	
		<bean:define id="indexPageId" value="<%= String.valueOf(pageIndex.intValue() + 1) %>" />		
		<bean:define id="actualPage" value="<%= pageContext.findAttribute("startIndex").toString()%>"/>
		<logic:equal name="actualPage" value="<%= pageContext.findAttribute("indexPageId").toString()%>" >
			<bean:write name="indexPageId"/>
		</logic:equal>
		<logic:notEqual name="actualPage" value="<%= pageContext.findAttribute("indexPageId").toString()%>" >
			<html:link page="<%= "/findPerson.do?method=findPerson&amp;name=" + pageContext.findAttribute("name") + "&amp;startIndex=" + pageContext.findAttribute("indexPageId").toString() + "&amp;roleType=" + pageContext.findAttribute("roleType")+ "&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;degreeType=" + pageContext.findAttribute("degreeType")+ "&amp;departmentId=" + pageContext.findAttribute("departmentId") +"&amp;viewPhoto=" + pageContext.findAttribute("viewPhoto")%>"><%= pageIndex.intValue() + 1 %></html:link>
		</logic:notEqual>	
	</logic:iterate>
</p>
</logic:present>


<bean:size id="numberFindedPersons" name="personListFinded"/>
	<logic:iterate id="personalInfo" name="personListFinded" indexId="personIndex">	   
	<bean:define id="personID" name="personalInfo" property="idInternal"/>
	
	<div class="pp">
		<table class="ppid" cellpadding="0" cellspacing="0">
		<!-- Nome -->
			<tr>
			<td width="70%"> 
				<strong><bean:write name="personalInfo" property="nome"/> (<bean:write name="personalInfo" property="username"/>)</strong>
				<bean:size id="mainRolesSize" name="personalInfo" property="mainRoles"></bean:size> 
				<logic:greaterThan name="mainRolesSize" value="0">
					<logic:iterate id="role" name="personalInfo" property="mainRoles" indexId="i">
					<em><bean:write name="role"/><logic:notEqual name="mainRolesSize" value="<%= String.valueOf(i.intValue() + 1) %>">, </logic:notEqual></em>
					</logic:iterate>
				</logic:greaterThan>
				<logic:equal name="mainRolesSize" value="0"></logic:equal>
			</td>

			<td width="30%" style="text-align: right;">
				<bean:define id="aa" value="<%= "aa" + personIndex %>" />
				<bean:define id="id" value="<%= "id" + personIndex %>" />
				<input type = button value="+"  id="<%= pageContext.findAttribute("id").toString()%>" indexed="true" onClick="check(document.getElementById('<%= pageContext.findAttribute("aa").toString() %>'),document.getElementById('<%= pageContext.findAttribute("id").toString() %>'));return false;" >													
			</td>
		</tr>
		</table>
	       
       	<logic:equal name="viewPhoto" value="true">
		  	<bean:define id="personID" name="personalInfo" property="idInternal"/>	  	    		  	  	
  			<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
	   	</logic:equal>

	      
		<table class="ppdetails">
		<!-- Telefone de Trabalho -->    
	  		<tr class="highlight">
	  			<td class="ppleft">
				<logic:notEqual name="personalInfo" property="workPhone" value=""><bean:message key="label.person.workPhone" /></logic:notEqual> 
				</td>
				<td class="ppright2">
				<logic:notEqual name="personalInfo" property="workPhone" value=""><bean:write name="personalInfo" property="workPhone"/></logic:notEqual>  
				</td>
		<!-- E-Mail -->
			<logic:equal name="show" value="true">
			<logic:present name="personalInfo" property="email">
				<logic:notEqual name="personalInfo" property="email" value=""> 
					<td class="ppleft_mail"><bean:message key="label.person.email" /></td>
					<td>
						<bean:define id="eMail" name="personalInfo" property="email" />
						<html:link href="<%= "mailto:" + pageContext.findAttribute("eMail").toString() %>"><bean:write name="personalInfo" property="email"/></html:link>		            
					</td>		
			</logic:notEqual>  
			</logic:present>  
	        </logic:equal>
	        
			<logic:equal name="show" value="false">
			<logic:equal name="personalInfo" property="availableEmail" value="true">
				<logic:present name="personalInfo" property="email">
					<td class="ppleft_mail"><bean:message key="label.person.email" /></td>
					<td>
						<bean:define id="eMail" name="personalInfo" property="email" />
						<html:link href="<%= "mailto:" + pageContext.findAttribute("eMail").toString() %>"><bean:write name="personalInfo" property="email"/></html:link>		            
					</td>
				</logic:present>
			</logic:equal>
			</logic:equal>	        
			</tr>
		</table>
	  
		<table class="ppdetails" id="<%= pageContext.findAttribute("aa").toString() %>" style="display:none">
		<logic:present  name="personalInfo" property="infoEmployee" >
			<!-- Local de Trabalho -->                    
			<logic:present name="personalInfo" property="infoEmployee.workingUnit" >
			<bean:define id="infoUnit" name="personalInfo" property="infoEmployee.workingUnit"/>	    			
				<tr>
				<td class="ppleft2"><bean:message key="label.person.workPlace" /></td>
					<bean:size id="numberOfElem" name="infoUnit" property="superiorUnitsNames"/>     			      	
					<logic:iterate id="superiorUnit" name="infoUnit" property="superiorUnitsNames" indexId="elem">
				<td class="ppright"><bean:write name="superiorUnit"/></td>
					<logic:notEqual name="numberOfElem" value="<%= String.valueOf(elem.intValue()+1) %>">
					<tr><td></td>
					</logic:notEqual>
					</logic:iterate>					
				</tr>
			</logic:present>

			<logic:present  name="personalInfo" property="infoEmployee.mailingUnit" >
			<tr>
				<td class="ppleft2"><bean:message key="label.person.mailingPlace" /></td>	     
				<bean:define id="costCenterNumber" name="personalInfo" property="infoEmployee.mailingUnit.costCenterCode"/>
				<bean:define id="unitName" name="personalInfo" property="infoEmployee.mailingUnit.name"/>
				<td class="ppright"><bean:write name="costCenterNumber"/> - <bean:write name="unitName"/></td>
			</tr>
			</logic:present>
		</logic:present>
		
		<logic:present  name="personalInfo" property="infoTeacher" >
			<logic:present  name="personalInfo" property="infoTeacher.infoCategory" >
			<tr>
				<td class="ppleft2"><bean:message key="label.teacher.category" />:</td>
				<bean:define id="categoryCode" name="personalInfo" property="infoTeacher.infoCategory.code"/>
				<bean:define id="categoryName" name="personalInfo" property="infoTeacher.infoCategory.longName"/>
				<td class="ppright"><bean:write name="categoryCode"/> - <bean:write name="categoryName"/></td>
			</tr>
			</logic:present>
		</logic:present>
		<!-- WebPage -->
		<logic:equal name="personalInfo" property="availableWebSite" value="true">        
		<logic:present name="personalInfo" property="availableWebSite">
			<tr>
				<td class="ppleft2"><bean:message key="label.person.webSite" /></td>		            
				<td class="ppright">	            	
				<logic:present name="personalInfo" property="enderecoWeb">
					<bean:define id="homepage" name="personalInfo" property="enderecoWeb" />
						<html:link href="<%= pageContext.findAttribute("homepage").toString() %>"><bean:write name="personalInfo" property="enderecoWeb"/></html:link>
				</logic:present>
				</td>
			</tr>
		</logic:present>
		</logic:equal>

		<logic:present  name="personalInfo" property="infoStudentCurricularPlanList" >
		<!-- Local de Trabalho -->   
			<tr>   
				<td class="ppleft2"><bean:message key="label.degree.name" />:</td>  
				<logic:iterate id="infoStudent" name="personalInfo" property="infoStudentCurricularPlanList">		
				<bean:define id="degreeName" name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				<td class="ppright"> 
					<logic:match name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" location="start" value="DEGREE"> 
					<bean:message key="link.degree"/> <bean:write name="degreeName" /><br/>
					</logic:match>
					<logic:match name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" location="start" value="MASTER_DEGREE"> 
					<bean:message key="link.master"/> <bean:write name="degreeName" />
					</logic:match>
				</td>
			</tr>
			</logic:iterate>
		</logic:present>
	</table>
</div>
		
	  </logic:iterate>
</logic:present>