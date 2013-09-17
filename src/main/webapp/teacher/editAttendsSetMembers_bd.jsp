<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message key="title.editAttendsSetMembers"/></h2>

<logic:present name="siteView" property="component"> 
<bean:define id="component" name="siteView" property="component" />
<bean:define id="infoGrouping" name="component" property="infoGrouping" />


<div class="infoop2">
	<bean:message key="label.teacher.EditAttendsSetMembers.description" />
</div>


<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<ul class="mvert15">
	<li>
		<html:link page="<%="/viewAttendsSet.do?method=viewAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>">
		<bean:message key="link.backToAttendsSet"/>
		</html:link>
	</li>
</ul>
  

<logic:empty name="infoGrouping" property="infoAttends">
	<p>
		<em>
			<bean:message key="message.infoAttendsSet.not.available" />
		</em>
	</p>
</logic:empty> 	


<logic:notEmpty name="infoGrouping" property="infoAttends">
	<html:form action="/deleteAttendsSetMembers" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<p class="mbottom05">
		<bean:message key="message.editAttendsSetMembers.RemoveMembers"/>
	</p>

	<logic:present name="showPhotos">
		<html:link page="<%="/editAttendsSetMembers.do?method=prepareEditAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + request.getParameter("groupingOID")%>">
		    	<bean:message key="label.viewPhoto"/>
		</html:link>
	</logic:present>
	<logic:notPresent name="showPhotos">
		<html:link page="<%="/editAttendsSetMembers.do?method=prepareEditAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + request.getParameter("groupingOID")%>">
		    	<bean:message key="label.notViewPhoto"/>
		</html:link>
	</logic:notPresent>
	
	<table class="tstyle4 mtop05">
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="label.teacher.StudentNumber" />
			</th>
			<logic:notPresent name="showPhotos">
				<th>
					<bean:message key="label.photo" />
				</th>
			</logic:notPresent>
			<th>
				<bean:message key="label.teacher.StudentName" /></th>
			<th>
				<bean:message key="label.teacher.StudentEmail" />
			</th>		
		</tr>	
		<logic:iterate id="infoAttend" name="infoGrouping" property="infoAttends">
			<bean:define id="infoStudent" name="infoAttend" property="aluno" />
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson" />	
			<tr>	
				<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentsToRemove" property="studentsToRemove">
					<bean:write name="infoPerson" property="username" />
					</html:multibox>
				</td>
				<td>
					<bean:write name="infoStudent" property="number" />
				</td>
				<logic:notPresent name="showPhotos">
					<td class="acenter">
						<bean:define id="personID" name="infoPerson" property="person.externalId"/>
						<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
					</td>
				</logic:notPresent>
				<td>
					<bean:write name="infoPerson" property="nome" />
				</td>	
				<td>
					<bean:write name="infoPerson" property="email" />
				</td>		
		 	</tr>	
		</logic:iterate>
	</table>

	<p> 
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.removeAluno"/>               		         	
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>
	</p>

		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteAttendsSetMembers"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode" property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>
</logic:notEmpty> 	

</logic:present>

<logic:notPresent name="siteView" property="component">
	<p class="mvert15">
		<span class="warning0"><bean:message key="message.infoAttendsSet.not.available" /></span>
	</p>
</logic:notPresent>


<logic:present name="infoStudentList"> 
		
<html:form action="/insertAttendsSetMembers" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<logic:empty name="infoStudentList"> 
	<p class="mvert15">
		<span class="warning0"><bean:message key="message.editAttendsSetMembers.NoMembersToAdd" /></span>
	</p>
</logic:empty>

<logic:notEmpty name="infoStudentList"> 

<p class="mtop15 mbottom05">
	<bean:message key="message.editAttendsSetMembers.InsertMembers"/>
</p>

	<logic:present name="showPhotos">
		<html:link page="<%="/editAttendsSetMembers.do?method=prepareEditAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + request.getParameter("groupingOID")%>">
		    	<bean:message key="label.viewPhoto"/>
		</html:link>
	</logic:present>
	<logic:notPresent name="showPhotos">
		<html:link page="<%="/editAttendsSetMembers.do?method=prepareEditAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + request.getParameter("groupingOID")%>">
		    	<bean:message key="label.notViewPhoto"/>
		</html:link>
	</logic:notPresent>

<table class="tstyle4 mtop05">
	<tr>
		<th>
		</th>
		<th>
			<bean:message key="label.teacher.StudentNumber" />
		</th>
		<logic:notPresent name="showPhotos">
			<th>
				<bean:message key="label.photo" />
			</th>
		</logic:notPresent>
		<th>
			<bean:message key="label.teacher.StudentName" />
		</th>
		<th>
			<bean:message key="label.teacher.StudentEmail" />
		</th>
	</tr>
	<logic:iterate id="infoStudent" name="infoStudentList">			
		<tr>	
			<td>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentCodesToInsert" property="studentCodesToInsert">
					<bean:write name="infoStudent" property="externalId"/>
				</html:multibox>
			</td>	
			<td>
				<bean:write name="infoStudent" property="number"/>
			</td>
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>	
			<logic:notPresent name="showPhotos">
				<td class="acenter">
					<bean:define id="person" name="infoPerson" property="person"/>
					<bean:define id="personID" name="person" property="externalId"/>
					<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
				</td>
			</logic:notPresent>		
			<td>
				<bean:write name="infoPerson" property="nome"/>
			</td>
			<td>
				<bean:write name="infoPerson" property="email"/>
			</td>
	 	</tr>	
	 </logic:iterate>
</table>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertAttendsSetMembers"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.insertAluno"/>                    		         	
	</html:submit>       
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>  
</p>

</logic:notEmpty>

</html:form>

</logic:present>


<logic:notPresent name="infoStudentList">
	<p class="mvert15">
		<span class="warning0"><bean:message key="message.editAttendsSetMembers.NoMembersToAdd" /></span>
	</p>
</logic:notPresent>