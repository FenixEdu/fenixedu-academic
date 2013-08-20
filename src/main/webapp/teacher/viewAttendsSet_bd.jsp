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
	<bean:define id="groupPropertiesCode" name="infoGrouping" property="externalId"/>
	<bean:define id="groupingOID" name="infoGrouping" property="externalId"/>

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
		<em><bean:message key="message.infoAttendsSet.not.available" /></em>
	</p>
	
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.attendsSetManagement"/></b>
	</p>

	<p class="mtop05">
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

		<bean:define id="sendMailLink">/sendMailToWorkGroupStudents.do?method=sendGroupingEmail&amp;groupingCode=<bean:write name="groupingOID"/>&amp;objectCode=<bean:write name="objectCode"/></bean:define>
		<li>
			<html:link page="<%= sendMailLink %>">
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
	
	<logic:present name="showPhotos">
		<html:link page="<%="/viewAttendsSet.do?method=viewAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
		    	<bean:message key="label.viewPhoto"/>
		</html:link>
	</logic:present>
	<logic:notPresent name="showPhotos">
		<html:link page="<%="/viewAttendsSet.do?method=viewAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
		    	<bean:message key="label.notViewPhoto"/>
		</html:link>
	</logic:notPresent>

<table class="tstyle4 mtop05">	
	<tr>
		<th>
			<bean:message key="label.numberWord" />
		</th>
		<logic:notPresent name="showPhotos">
			<th>
				<bean:message key="label.photo" />
			</th>
		</logic:notPresent>
		<th>
			<bean:message key="label.nameWord" />
		</th>
		<th>
			<bean:message key="label.emailWord" />
		</th>
	</tr>
			
	<logic:iterate id="infoFrequentaWithAll" name="attends">
	
		<bean:define id="infoStudent" name="infoFrequentaWithAll" property="aluno"/>
		<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>
		<bean:define id="person" name="infoPerson" property="person"/>
		
		<tr>		
			<td class="acenter">
				<bean:write name="infoStudent" property="number"/>
			</td>
			<logic:notPresent name="showPhotos">
				<td class="acenter">
					<bean:define id="personID" name="person" property="externalId"/>
					<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
				</td>
			</logic:notPresent>
			<td>
				<bean:write name="infoPerson" property="nome"/>
			</td>		
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
		<em><bean:message key="message.infoAttendsSet.not.available" /></em>
	</p>
</logic:notPresent>
