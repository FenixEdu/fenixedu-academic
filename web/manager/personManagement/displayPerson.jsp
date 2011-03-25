<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

<span class="error"><!-- Error messages go here --><html:errors /></span>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.findPerson" /></h2>

<logic:notPresent name="personListFinded">
	<p><span class="errors"><bean:message bundle="MANAGER_RESOURCES" key="error.manager.implossible.findPerson" /></span></p>
</logic:notPresent>

<logic:notEmpty name="personListFinded">
	
	<bean:define id="totalFindedPersons" name="totalFindedPersons" />
	<logic:notEqual name="totalFindedPersons" value="1">
		<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.numberFindedPersons" arg0="<%= String.valueOf(totalFindedPersons) %>" /></b>	
	</logic:notEqual>
	
	<logic:equal name="totalFindedPersons" value="1">
		<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.findedOnePersons" arg0="<%= String.valueOf(totalFindedPersons) %>" /></b>
	</logic:equal>
	<br /><br />
		
	&nbsp;&nbsp;&nbsp;
	
	
	<bean:define id="url">/<%= request.getAttribute("modulePrefix") %>/findPerson.do?method=findPerson&name=<bean:write name="name"/>&email=<bean:write name="email"/>&username=<bean:write name="username"/>&documentIdNumber=<bean:write name="documentIdNumber"/></bean:define>			
	<bean:message key="label.collectionPager.page" bundle="MANAGER_RESOURCES"/>:	
	<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>			
	<br /><br />
		
	<logic:iterate id="personalInfo" name="personListFinded" indexId="personIndex">	   
		<bean:define id="personID" name="personalInfo" property="externalId"/>
	
		<div class="pp">
			<table class="ppid" cellpadding="0" cellspacing="0">
				<tr>
					<td width="70%">
						<strong>
						 	<html:link action="<%= "/findPerson.do?method=viewPerson&personID="+personID %>" > <bean:write name="personalInfo" property="name"/> </html:link>					
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
						<bean:define id="id" value="<%= "id" + (personIndex.intValue() + 40)  %>" />
						  <!--  <td width="30%" style="text-align: right;">-->
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
		  		<bean:define id="personID" name="personalInfo" property="externalId"/>	  	    		  	  	
	  			<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
		   	</logic:equal>

			<table class="ppdetails">
		  		<tr class="highlight">
    		  		<td class="ppleft" valign="top">
						<bean:message key="label.person.workPhone.short" /> 
					</td>
		  			<td class="ppright" valign="top" style="width: 18em;">
                        <fr:view name="personalInfo" property="phones">
                            <fr:layout name="contact-list">
                                <fr:property name="classes" value="nobullet list6" />
                            </fr:layout>
                        </fr:view>
					</td>
                    <td class="ppleft2" valign="top" style="text-align: right;">
                        <bean:message key="label.person.email" />
                    </td>
                    <td class="ppright" valign="top">
                        <fr:view name="personalInfo" property="emailAddresses">
                            <fr:layout name="contact-list">
                                <fr:property name="classes" value="nobullet list6" />
                            </fr:layout>
                        </fr:view>
                    </td>
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
					   
					<bean:define id="personSpaces" name="personalInfo" property="activePersonSpaces"></bean:define>
					<logic:notEmpty name="personSpaces">
						<tr>
							<td class="ppleft2"><bean:message key="label.person.rooms"/>:</td>	   						
							<td>
								<fr:view name="personSpaces">
									<fr:layout name="list">
										<fr:property name="classes" value="mvert05 ulindent0 nobullet" />
										<fr:property name="eachSchema" value="FindPersonSpaceSchema" />
										<fr:property name="eachLayout" value="values" />																				
									</fr:layout>																			
								</fr:view>
							</td>																		
						</tr>			
					</logic:notEmpty>
					
					<logic:notEmpty name="personalInfo" property="teacher" >
						<logic:notEmpty  name="personalInfo" property="teacher.currentCategory" >
							<tr>
								<td class="ppleft2"><bean:message key="label.teacher.category" />:</td>
								<bean:define id="categoryName" name="personalInfo" property="teacher.currentCategory.name"/>
								<td class="ppright"><bean:write name="personalInfo" property="teacher.currentCategory.name"/></td>
							</tr>
						</logic:notEmpty>
					</logic:notEmpty>
					
					<logic:notEmpty name="personalInfo" property="employee" >
						<logic:notEmpty  name="personalInfo" property="employee.category" >
							<tr>
								<td class="ppleft2"><bean:message key="label.employee.category" />:</td>
								<td class="ppright"><bean:write name="personalInfo" property="employee.category.name.content"/></td>
							</tr>
						</logic:notEmpty>
					</logic:notEmpty>
					
                    <fr:view name="personalInfo" property="webAddresses">
                        <fr:layout name="contact-table">
                            <fr:property name="types" value="WORK" />
                            <fr:property name="bundle" value="APPLICATION_RESOURCES" />
                            <fr:property name="label" value="label.person.webSite" />
                            <fr:property name="defaultLabel" value="label.partyContacts.defaultContact" />
                            <fr:property name="leftColumnClasses" value="ppleft2" />
                            <fr:property name="rightColumnClasses" value="ppright" />
                        </fr:layout>
                    </fr:view>
                    
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
									<td class="ppright"><bean:write name="registration" property="degreeName"/></td>
								</tr>							
							</logic:iterate>											
						</logic:notEmpty>
					</logic:present>
				</table>
			</div>
		</div>
	 </logic:iterate>
	
	
	<logic:notEqual name="numberOfPages" value="1">
		<bean:message key="label.collectionPager.page" bundle="MANAGER_RESOURCES"/>:	
		<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>			
	</logic:notEqual>
	
	<script type="text/javascript">
		switchDisplay();
	</script>
	
</logic:notEmpty>