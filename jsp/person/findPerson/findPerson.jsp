<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<script type="text/javascript">
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

<table class="search mbottom0">
	<tr>
		<td class="leftcolumn width12em"><bean:message key="label.type"/>:</td>
		<td class="width46em">			<html:form action="/preparePerson" >
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="preparePerson" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.countPage" property="countPage" value="1"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.departmentId" property="departmentId" name="findPersonForm"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" name="findPersonForm"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewPhoto" property="viewPhoto" name="findPersonForm"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.name" property="name" name="findPersonForm"/>

				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.RoleType" bundle="ENUMERATION_RESOURCES" includedFields="STUDENT,TEACHER,GRANT_OWNER,EMPLOYEE" />
				<html:select bundle="HTMLALT_RESOURCES" property="roleType" onchange="this.form.submit()">
					<html:option value=""><!-- w3c complient --></html:option>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</html:form>
		</td>
	</tr>
	<logic:present name="degreeType">
		<tr>
			<td class="leftcolumn"><bean:message key="label.degree"/>:</td>
			<td>
				<html:form action="/preparePerson" >					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="preparePerson" />					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.countPage" property="countPage" value="1"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.departmentId" property="departmentId" name="findPersonForm"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" name="findPersonForm"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewPhoto" property="viewPhoto" name="findPersonForm"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.name" property="name" name="findPersonForm"/>					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.roleType" property="roleType" name="findPersonForm"/>					<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.DegreeType" bundle="ENUMERATION_RESOURCES"  />
					<html:select bundle="HTMLALT_RESOURCES" property="degreeType" onchange="this.form.submit()">
						<html:option value=""><!-- w3c complient --> </html:option>
						<html:options collection="values" property="value" labelProperty="label"/>				
					</html:select>
					<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>				</html:form>
			</td>
		</tr>
	</logic:present>
</table>
<html:form action="/findPerson" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="findPerson" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.startIndex" property="startIndex" value="1" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<bean:define id="roleType" name="findPersonForm" property="roleType" type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.roleType" property="roleType" value="<%= roleType %>"/>
	<bean:define id="degreeType" name="findPersonForm" property="degreeType" type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" value="<%= degreeType %>"/>

	<table class="search mtop0">
		<logic:present name="departments">
			<tr>
				<td class="leftcolumn width12em">
					<bean:message key="label.teacher.finalWork.department"/>:
				</td>
				<td class="width46em">
					<html:select bundle="HTMLALT_RESOURCES" property="departmentId">	
						<html:option value=""> <!-- w3c complient --> </html:option>
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
					<html:select bundle="HTMLALT_RESOURCES" property="degreeId">	
						<html:option value=""> <!-- w3c complient --> </html:option>
						<logic:iterate id="degree" name="nonMasterDegree" > 
						   	<bean:define id="degreeID" name="degree" property="idInternal"/>
							<html:option value="<%= degreeID.toString() %>">								<bean:write name="degree" property="nome"/> 							</html:option>
						</logic:iterate>
					</html:select>
				</td>
			</tr>
		</logic:present>
		<tr>
			<td class="leftcolumn width12em"><bean:message key="label.nameWord" />:</td>
			<td class="width46em">
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
	
	<logic:notEqual name="totalFindedPersons" value="1">
		<p><b><bean:message key="label.manager.numberFindedPersons" arg0="<%= pageContext.findAttribute("totalFindedPersons").toString() %>" /></b></p>	
	</logic:notEqual>
	<logic:equal name="totalFindedPersons" value="1">
		<p><b><bean:message key="label.manager.findedOnePersons" arg0="<%= pageContext.findAttribute("totalFindedPersons").toString() %>" /></b></p>
	</logic:equal>

	<bean:define id="url">/messaging/findPerson.do?method=findPerson&amp;name=<bean:write name="name"/>&amp;roleType=<bean:write name="roleType"/>&amp;degreeId=<bean:write name="degreeId"/>&amp;degreeType=<bean:write name="degreeType"/>&amp;departmentId=<bean:write name="departmentId"/>&amp;viewPhoto=<bean:write name="viewPhoto"/></bean:define>			
	<p><bean:message key="label.pages" />:					
		<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>			
	</p>
	
	<logic:iterate id="personalInfo" name="personListFinded" indexId="personIndex">	   
		<bean:define id="personID" name="personalInfo" property="idInternal"/>
	
		<div class="pp">
			<table class="ppid" cellpadding="0" cellspacing="0">
				<tr>
					<td width="70%">
						<strong>
							<bean:write name="personalInfo" property="name"/>
						</strong> (<bean:write name="personalInfo" property="username"/>)
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
						<bean:define id="id" value="<%= "id" + (personIndex.intValue() + 40)  %>" />						  <!--  <td width="30%" style="text-align: right;">-->
							<bean:define id="aa" value="<%= "aa" + personIndex %>" />
							<bean:define id="id" value="<%= "id" + (personIndex.intValue() + 40) %>" />
							<span class="switchInline">
								<input alt="input.input" type = button value="+"  id="<%= pageContext.findAttribute("id").toString()%>" onClick="check(document.getElementById('<%= pageContext.findAttribute("aa").toString() %>'),document.getElementById('<%= pageContext.findAttribute("id").toString() %>'));return false;"/>													
							</span>
  						<!-- </td>-->
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
						<logic:notEqual name="personalInfo" property="workPhone" value=""><bean:message key="label.person.workPhone.short" /></logic:notEqual> 
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
					
					<logic:present name="personalInfo" property="employee">						
						<logic:present name="personalInfo" property="employee.currentWorkingPlace" >
							<bean:define id="infoUnit" name="personalInfo" property="employee.currentWorkingPlace"/>	    			
							<tr>
								<td valign="top" class="ppleft2"><bean:message key="label.person.workPlace" /></td>
								<td class="ppright">
									<bean:write name="infoUnit" property="presentationNameWithParentsAndBreakLine" filter="false"/>									
								</td>
							</tr>
						</logic:present>
						
						<logic:present  name="personalInfo" property="employee.currentMailingPlace" >
							<tr>
								<td class="ppleft2"><bean:message key="label.person.mailingPlace" /></td>	     
								<bean:define id="costCenterNumber" name="personalInfo" property="employee.currentMailingPlace.costCenterCode"/>
								<bean:define id="unitName" name="personalInfo" property="employee.currentMailingPlace.name"/>
								<td class="ppright"><bean:write name="costCenterNumber"/> - <bean:write name="unitName"/></td>
							</tr>
						</logic:present>					
					</logic:present>
					
					<logic:present  name="personalInfo" property="teacher" >
						<logic:present  name="personalInfo" property="teacher.category" >
							<tr>
								<td class="ppleft2"><bean:message key="label.teacher.category" />:</td>
								<bean:define id="categoryCode" name="personalInfo" property="teacher.category.code"/>
								<bean:define id="categoryName" name="personalInfo" property="teacher.category.longName"/>
								<td class="ppright"><bean:write name="categoryCode"/> - <bean:write name="categoryName"/></td>
							</tr>
						</logic:present>
					</logic:present>
					
					<logic:equal name="personalInfo" property="availableWebSite" value="true">        
						<logic:notEmpty name="personalInfo" property="availableWebSite">
							<tr>
								<td class="ppleft2"><bean:message key="label.person.webSite" /></td>		            
								<td class="ppright">	            	
									<logic:present name="personalInfo" property="webAddress">
										<bean:define id="homepage" name="personalInfo" property="webAddress" />
										<html:link target="_blank" href="<%= pageContext.findAttribute("homepage").toString() %>"><bean:write name="personalInfo" property="webAddress"/></html:link>
									</logic:present>
								</td>
							</tr>
						</logic:notEmpty>
					</logic:equal>
					
					<logic:equal name="personalInfo" property="homePageAvailable" value="true">
						<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
						<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>				
						<bean:define id="homepageURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/homepage/<bean:write name="personalInfo" property="istUsername"/></bean:define>						
						<tr>
							<td class="ppleft2"><bean:message key="label.homepage"/></td>		            
							<td class="ppright">	            	
								<html:link href="<%= homepageURL %>" target="_blank"><bean:write name="homepageURL"/></html:link>
							</td>
						</tr>
					</logic:equal>					
					
					<logic:present name="personalInfo" property="student" >
						<logic:notEmpty name="personalInfo" property="student.registrations" >
	
							<logic:iterate id="registration" name="personalInfo" property="student.registrations">
								<tr>   
									<td class="ppleft2" style="vertical-align: top;"><bean:message key="label.degree.name" />:</td>  
									<td class="ppright"><bean:write name="registration" property="degree.presentationName"/></td>
								</tr>							
							</logic:iterate>											
						</logic:notEmpty>
					</logic:present>
				</table>			</div>
		</div>
	 </logic:iterate>


	<logic:notEqual name="numberOfPages" value="1">
		<p class="mtop15"><bean:message key="label.pages" />:			
			<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>			
		</p>
	</logic:notEqual>	 

	
</logic:present>


<script type="text/javascript">
	switchDisplay();
</script>
