<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<script language="JavaScript">
function getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}

function switchDisplay(){
	var a = getElementsByClass("switchNone", null, null);
	for (i = 0; i < a.length; i++) {
		a[i].className = "dnone";	
	}
	
	var a = getElementsByClass("switchInline", null, null);
	for (i = 0; i < a.length; i++) {
		a[i].className = "dinline";	
	}
}

function check(e,v){
	if (e.className == "dnone")
  	{
	  e.className = "dblock";
	  v.value = "-";
	}
	else {
	  e.className = "dnone";
  	  v.value = "+";
	}
}
</script>

<h2><bean:message key="label.manager.findPerson"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<table class="search">
	<tr>
		<td class="leftcolumn"><bean:message key="label.type"/>:</td>
		
		<td>			<html:form action="/preparePerson" >				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="preparePerson" />				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.countPage" property="countPage" value="1"/>				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.departmentId" property="departmentId" name="findPersonForm"/>				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" name="findPersonForm"/>				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewPhoto" property="viewPhoto" name="findPersonForm"/>				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.name" property="name" name="findPersonForm"/>				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.RoleType" bundle="ENUMERATION_RESOURCES" includedFields="STUDENT,TEACHER,GRANT_OWNER,EMPLOYEE" />
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.roleType" property="roleType" onchange="this.form.submit()">
					<html:option value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>			</html:form>
		</td>
	</tr>
	<logic:present name="degreeType">
		<tr>
			<td class="leftcolumn"><bean:message key="label.degree"/>:</td>
			<td>
				<html:form action="/preparePerson" >					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="preparePerson" />					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.countPage" property="countPage" value="1"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.departmentId" property="departmentId" name="findPersonForm"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" name="findPersonForm"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewPhoto" property="viewPhoto" name="findPersonForm"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.name" property="name" name="findPersonForm"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.roleType" property="roleType" name="findPersonForm"/>					<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.DegreeType" bundle="ENUMERATION_RESOURCES"  />
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeType" property="degreeType" onchange="this.form.submit()">
						<html:option value=""/>
						<html:options collection="values" property="value" labelProperty="label"/>				
					</html:select>
					<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>				</html:form>
			</td>
		</tr>
	</logic:present>
<html:form action="/findPerson" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="findPerson" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.startIndex" property="startIndex" value="1" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<bean:define id="roleType" name="findPersonForm" property="roleType" type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.roleType" property="roleType" value="<%= roleType %>"/>
	<bean:define id="degreeType" name="findPersonForm" property="degreeType" type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" value="<%= degreeType %>"/>

		<logic:present name="departments">
			<tr>
				<td class="leftcolumn">
					<bean:message key="label.teacher.finalWork.department"/>:
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.departmentId" property="departmentId">	
						<html:option value=""/>
						<logic:iterate id="department" name="departments"> 
						   	<bean:define id="departmentID" name="department" property="idInternal"/>
							<html:option value="<%= departmentID.toString() %>">								<bean:write name="department" property="realName"/> 							</html:option>
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
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeId" property="degreeId">	
						<html:option value=""/>
						<logic:iterate id="degree" name="nonMasterDegree" > 
						   	<bean:define id="degreeID" name="degree" property="idInternal"/>
							<html:option value="<%= degreeID.toString() %>">								<bean:write name="degree" property="nome"/> 							</html:option>
						</logic:iterate>
					</html:select>
				</td>
			</tr>
		</logic:present>
		<tr>
			<td class="leftcolumn"><bean:message key="label.nameWord" />:</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" name="findPersonForm" property="name" size="50"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.name" property="name" name="findPersonForm"/>
			</td>		
		</tr>
		<tr>
			<td class="leftcolumn">
				<bean:message key="label.viewPhoto" />:
			</td>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewPhoto"  property="viewPhoto" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td>				<logic:notPresent name="nonMasterDegree">					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value=""/>				</logic:notPresent>				<logic:notPresent name="departments">					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.departmentId" property="departmentId" value=""/>				</logic:notPresent>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.search"/>
				</html:submit>
				<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
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
				<tr>
					<td width="70%"> 
						<strong><bean:write name="personalInfo" property="nome"/> (<bean:write name="personalInfo" property="username"/><logic:notEmpty name="personalInfo" property="istUsername"> | <bean:write name="personalInfo" property="istUsername"/></logic:notEmpty>)</strong>
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
						<bean:define id="id" value="<%= "id" + (personIndex.intValue() + 40)  %>" />						<td width="30%" style="text-align: right;">
							<bean:define id="aa" value="<%= "aa" + personIndex %>" />
							<bean:define id="id" value="<%= "id" + (personIndex.intValue() + 40) %>" />
							<span class="switchInline">
								<input alt="input.input" type = button value="+"  id="<%= pageContext.findAttribute("id").toString()%>" indexed="true" onClick="check(document.getElementById('<%= pageContext.findAttribute("aa").toString() %>'),document.getElementById('<%= pageContext.findAttribute("id").toString() %>'));return false;" >													
							</span>
						</td>
					</td>
				</tr>
			</table>

			<logic:equal name="viewPhoto" value="true">
		  		<bean:define id="personID" name="personalInfo" property="idInternal"/>	  	    		  	  	
	  			<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
		   	</logic:equal>
			<table class="ppdetails">
		  		<tr class="highlight">
		  			<td class="ppleft">
						<logic:notEqual name="personalInfo" property="workPhone" value=""><bean:message key="label.person.workPhone" /></logic:notEqual> 
					</td>
					<td class="ppright2">
						<logic:notEqual name="personalInfo" property="workPhone" value=""><bean:write name="personalInfo" property="workPhone"/></logic:notEqual>  
					</td>
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
						<div id="<%= pageContext.findAttribute("aa").toString() %>" class="switchNone">
				<table class="ppdetails" >
					<logic:present  name="personalInfo" property="infoEmployee" >
						<logic:present name="personalInfo" property="infoEmployee.workingUnit" >
							<bean:define id="infoUnit" name="personalInfo" property="infoEmployee.workingUnit"/>	    			
							<tr>
								<td valign="top" class="ppleft2"><bean:message key="label.person.workPlace" /></td>
								<td class="ppright">
									<bean:write name="infoUnit" property="superiorUnitsNames" filter="false"/><br/>
									<bean:write name="infoUnit" property="costCenterCode"/> - <bean:write name="infoUnit" property="name"/>
								</td>
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
					<logic:present name="personalInfo" property="infoStudentCurricularPlanList" >
						<logic:notEmpty name="personalInfo" property="infoStudentCurricularPlanList" >
							<tr>   
								<td class="ppleft2" style="vertical-align: top;"><bean:message key="label.degree.name" />:</td>  
								<td class="ppright">
									<logic:iterate id="infoStudent" name="personalInfo" property="infoStudentCurricularPlanList">		
										<bean:define id="degreeName" name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.nome"/>									
										<bean:message name="infoStudent" property="infoDegreeCurricularPlan.infoDegree.tipoCurso.name" bundle="ENUMERATION_RESOURCES" /> <bean:message key="label.in"/> <bean:write name="degreeName" /><br/>																		
									</logic:iterate>
								</td>
							</tr>
						</logic:notEmpty>
					</logic:present>
				</table>			</div>
		</div>
	 </logic:iterate>
</logic:present>

<script>
	switchDisplay();
</script>