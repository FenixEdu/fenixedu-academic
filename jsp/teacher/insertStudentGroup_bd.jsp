<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView">
<h2><bean:message key="title.insertStudentGroup"/></h2>

<div class="dinline forminline">
<html:form action="/insertStudentGroup" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<div class="infoop2">
	<bean:message key="label.teacher.CreateStudentGroup.description" />
</div>

	
<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<p class="mtop15 mbottom05">
	<bean:message key="message.insertStudentGroupData"/>:
</p>


		 
<table class="tstyle5 thlight thmiddle">
	<tr>
		<th>
			<bean:message key="label.GroupNumber"/>
		</th>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.nrOfElements" size="21" name="infoSiteStudentGroup" property="nrOfElements" />
		</td>
	</tr>
</table>


<logic:empty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
<p>
	<em class="warning0"><bean:message key="message.editStudentGroupMembers.NoMembersToAdd" /></em>
</p>
</logic:empty>

<logic:notEmpty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
<table class="tstyle5 thlight dinline">	
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
	
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentGroup" property="infoSiteStudentInformationList">				
		<tr>	
			<td>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentCodes" property="studentCodes">
			<bean:write name="infoSiteStudentInformation" property="username"/>
			</html:multibox>
			</td>	
			<td><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			<td><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>
			<td><bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
	 	</tr>	
	 </logic:iterate>
</table>
</logic:notEmpty>


<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createStudentGroup"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />

<logic:present name="shiftCode">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>


<br/>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.insert"/>                    		         	
		</html:submit>       
	
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>  

	</html:form>

	<html:form action="/viewShiftsAndGroups" method="get">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewShiftsAndGroups"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

</div>

</logic:present>