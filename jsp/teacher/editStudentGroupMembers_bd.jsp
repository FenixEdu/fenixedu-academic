<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.editStudentGroupMembers"/></h2>


<logic:present name="siteView" property="component"> 
<bean:define id="component" name="siteView" property="component" />

<div class="infoop2">
	<bean:message key="label.teacher.EditStudentGroupMembers.description" />
</div>

<p>	
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:present name="shiftCode">
	<ul>
		<li>
			<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")+ "&amp;studentGroupCode=" + request.getParameter("studentGroupCode")%>">
		    	<bean:message key="link.backToGroup"/>
		    </html:link>
	    </li>
    </ul>
</logic:present>
<logic:notPresent name="shiftCode"> 
	<ul>
		<li>
			<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;studentGroupCode=" + request.getParameter("studentGroupCode")%>">
		    	<bean:message key="link.backToGroup"/>
		    </html:link>
	    </li>
    </ul>
</logic:notPresent> 

<logic:empty name="component" property="infoSiteStudentInformationList">
<p>
	<span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span>
</p>
</logic:empty> 	

<logic:notEmpty name="component" property="infoSiteStudentInformationList">
<html:form action="/deleteStudentGroupMembers" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<p class="mbottom05">
	<bean:message key="message.editStudentGroupMembers.RemoveMembers"/>
</p>

<table class="tstyle4 mtop05">
	<tr>
		<th>
		</th>
		<th><bean:message key="label.teacher.StudentNumber" />
		</th>
		<th><bean:message key="label.teacher.StudentName" />
		</th>
		<th><bean:message key="label.teacher.StudentEmail" />
		</th>
		
	</tr>
	
	<logic:iterate id="infoSiteStudentInformation" name="component" property="infoSiteStudentInformationList">			
		<tr>	
			<td>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentsToRemove" property="studentsToRemove">
			<bean:write name="infoSiteStudentInformation" property="username" />
			</html:multibox>
			</td>	
			<td class="acenter"><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			</td>	
			<td><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>
			</td>	
			<td><bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
		
	 	</tr>	
	 </logic:iterate>
 
</table>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.remove"/>                    		         	
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>
</p>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteStudentGroupMembers"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
<logic:present name="shiftCode">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>

</html:form>
</logic:notEmpty> 	

</logic:present>

<logic:notPresent name="siteView" property="component">
<p class="mtop15">
	<span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span>
</p>
</logic:notPresent>


<logic:present name="infoStudentList"> 
		
<html:form action="/insertStudentGroupMembers" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<logic:empty name="infoStudentList"> 
<p>
	<span class="warning0"><bean:message key="message.editStudentGroupMembers.NoMembersToAdd" /></span>
</p>
</logic:empty>

<logic:notEmpty name="infoStudentList"> 

<p class="mbottom05">
	<bean:message key="message.editStudentGroupMembers.InsertMembers"/>
</p>

<table class="tstyle4 mtop05">
	<tr>
		<th>
		</th>
		<th><bean:message key="label.teacher.StudentNumber" />
		</th>
		<th><bean:message key="label.teacher.StudentName" />
		</th>
		<th><bean:message key="label.teacher.StudentEmail" />
		</th>
	</tr>


	<logic:iterate id="infoStudent" name="infoStudentList">			
		<tr>	
			<td>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentsToInsert" property="studentsToInsert">
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson" />
			<bean:write name="infoPerson" property="username"/>
			</html:multibox>
			</td>	
			<td class="acenter"><bean:write name="infoStudent" property="number"/>
			</td>	
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>		
			<td><bean:write name="infoPerson" property="nome"/>
			</td>
			<td><bean:write name="infoPerson" property="email"/>
			</td>
	 	</tr>	
	 </logic:iterate>
</table>


<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertStudentGroupMembers"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
<logic:present name="shiftCode">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.insert"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  

</logic:notEmpty>

</html:form>


</logic:present>


<logic:notPresent name="infoStudentList">
<p class="mtop15">
	<span class="warning0"><bean:message key="message.editStudentGroupMembers.NoMembersToAdd" /></span>
</p>
</logic:notPresent>