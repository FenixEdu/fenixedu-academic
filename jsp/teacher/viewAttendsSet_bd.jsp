<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />

	<h2><bean:message key="title.attendsSetInformation"/></h2>

	<bean:define id="infoGrouping" name="component" property="infoGrouping"/>
	<bean:define id="attends" name="infoGrouping" property="infoAttends"/>
	<bean:define id="groupPropertiesCode" name="infoGrouping" property="idInternal"/>
	<bean:define id="groupingOID" name="infoGrouping" property="idInternal"/>

	<logic:empty name="attends">
		<div class="infoop2">
			<bean:message key="label.teacher.emptyAttendsSet.description" />
		</div>
	</logic:empty>	
	
	<logic:notEmpty name="attends">
		<div class="infoop2">
			<bean:message key="label.teacher.viewAttendsSet.description" />
		</div>
	</logic:notEmpty>		
	
<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>	



<logic:empty name="attends">

	<ul>
		<li>
			<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
		    <bean:message key="link.backToShiftsAndGroups"/>
		    </html:link>
	    </li>
    </ul>
		
	<p>
		<span class="warning0"><bean:message key="message.infoAttendsSet.not.available" /></span>
	</p>
	
	<p>
		<b><bean:message key="label.attendsSetManagement"/></b>
	</p>

	<p>
		<html:link page="<%="/editAttendsSetMembers.do?method=prepareEditAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    	<bean:message key="link.editAttendsSetMembers"/>
	    </html:link>
    </p>
 	
</logic:empty> 
	
	
	
<logic:notEmpty name="attends">
	<ul>
		<li>
			<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
			<bean:message key="link.backToShiftsAndGroups"/>
			</html:link>
		</li>

	 <% Map sendMailParameters = new TreeMap(request.getParameterMap());
        sendMailParameters.put("method","start");
        sendMailParameters.put("studentGroupCode", groupingOID.toString());
		request.setAttribute("sendMailParameters",sendMailParameters);%>
		<bean:define id="sendMailLinkParameters" type="java.util.Map" name="sendMailParameters"/>
		<li>
			<html:link page="/sendMailToWorkGroupStudents.do" name="sendMailLinkParameters">
				<bean:message key="link.sendEmailToAllStudents"/><br/><br/>
			</html:link>	
		</li>
	</ul>

	<p class="mbottom05">
		<b><bean:message key="label.attendsSetManagement"/></b>
	</p>
	<p class="mtop05">
		<html:link page="<%="/editAttendsSetMembers.do?method=prepareEditAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    	<bean:message key="link.editAttendsSetMembers"/>
	    </html:link> , 
		<html:link page="<%="/deleteAttendsSetMembersByExecutionCourse.do?method=deleteAttendsSetMembersByExecutionCourse&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    	<bean:message key="link.deleteAttendsSetMembersByExecutionCourse"/>
	    </html:link> , 
	    <html:link page="<%="/deleteAllAttendsSetMembers.do?method=deleteAllAttendsSetMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    	<bean:message key="link.deleteAllAttendsSetMembers"/>
	    </html:link>
    </p>


 	<bean:size id="count" name="attends"/>
 	<p class="mtop15 mbottom05">
 		<em>
			<bean:message key="label.teacher.NumberOfStudents" /><%= count %>
		</em>
	</p>

<table class="tstyle4">	
	<tr>
		<th width="16%"><bean:message key="label.numberWord" /></th>
		<th width="63%"><bean:message key="label.nameWord" /></th>
		<th width="26%"><bean:message key="label.emailWord" /></th>
	</tr>
			
	<logic:iterate id="infoFrequentaWithAll" name="attends">
	
		<bean:define id="infoStudent" name="infoFrequentaWithAll" property="aluno"/>
		<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>
		
		<tr>		
			<td><bean:write name="infoStudent" property="number"/></td>	
			<td><bean:write name="infoPerson" property="nome"/></td>		
			<td>
				<logic:present name="infoPerson" property="email">
					<bean:define id="mail" name="infoPerson" property="email"/>
					<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoPerson" property="email"/></html:link>
				</logic:present>
				<logic:notPresent name="infoPerson" property="email">
					&nbsp;
				</logic:notPresent>
			</td>
		</tr>				
	</logic:iterate>
</table>



</logic:notEmpty>

	 
</logic:present>

<logic:notPresent name="siteView" property="component">
<p>
	<span class="warning0"><bean:message key="message.infoAttendsSet.not.available" /></span>
</p>
</logic:notPresent>
