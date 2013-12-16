<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>

<logic:present name="infoSiteStudentGroup">

	<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>
	<bean:define id="studentGroupCode" name="infoStudentGroup" property="externalId"/>
	<bean:define id="infoAttends" name="infoStudentGroup" property="infoAttends"/>
	<bean:define id="infoGrouping" name="infoStudentGroup" property="infoGrouping"/>
	<bean:define id="groupPropertiesCode" name="infoGrouping" property="externalId"/>

	<em><bean:message key="title.student.portalTitle"/></em>
	<h2><bean:message key="title.StudentGroupInformation"/> <bean:write name="infoStudentGroup" property="groupNumber"/></h2>

	<p><strong><bean:message key="label.projectTable.project"/>:</strong> <bean:write name="infoGrouping" property="name"/></p>

		<p>
		<html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
   		<bean:message key="link.backToShiftsAndGroups"/></html:link> - <bean:message key="link.backToShiftsAndGroups.description"/>
		</p>

<logic:empty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">

	<div class="infoop2">
		<ul>
			<li><bean:message key="label.student.StudentGroupInformation.description"/></li>
		</ul>
	</div>

		
		<div class="inobullet">
		<!-- Error messages go here --><html:errors />
		</div>
	
		<bean:define id="nrOfElements" name="infoSiteStudentGroup" property="nrOfElements"/>
		<b><bean:message key="label.nrOfElements"/> </b><bean:write name="nrOfElements"/>
		<br/>
		<h2 style="color: #a00;"><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
		
	<p>	
		<b><bean:message key="label.groupOption"/></b>&nbsp;
		
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.externalId"/>
		<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&amp;executionCourseCode=" + request.getParameter("executionCourseCode") +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString() +"&amp;shiftCode=" +shiftCode.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
	    	<bean:message key="link.enrolment"/>
	    </html:link>
		</logic:lessEqual>
			
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&amp;executionCourseCode=" + request.getParameter("executionCourseCode") +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
	    	<bean:message key="link.enrolment"/>
	    </html:link>
		</logic:greaterEqual>
	</p>
</logic:empty>


<logic:notEmpty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">

	
	<div class="inobullet mvert15">
	<!-- Error messages go here --><html:errors />
	</div>

<div class="infoop2">
	<ul>
		<li><bean:message key="label.student.StudentGroupInformation.description"/></li>
	</ul>
</div>

	<bean:define id="nrOfElements" name="infoSiteStudentGroup" property="nrOfElements"/>
	<p><bean:message key="label.nrOfElements"/> <bean:write name="nrOfElements"/></p>
	
<p class="mtop1 mbottom05">
	<span>
		<b><bean:message key="label.groupOperations"/></b>&nbsp;
		
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.externalId"/>
		<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+"&amp;shiftCode=" + request.getParameter("shiftCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
		<bean:message key="link.enrolment"/>
		</html:link> &nbsp;|&nbsp;
		</logic:lessEqual>	
		
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
	    <bean:message key="link.enrolment"/>
		</html:link> &nbsp;|&nbsp;
		</logic:greaterEqual>	
	
	
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.externalId"/>
		<html:link page="<%="/removeGroupEnrolment.do?method=prepareRemove&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+"&amp;shiftCode=" + request.getParameter("shiftCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
		<bean:message key="link.removeEnrolment"/>
		</html:link> &nbsp;|&nbsp;
		</logic:lessEqual>
			    
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%="/removeGroupEnrolment.do?method=prepareRemove&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
	   	<bean:message key="link.removeEnrolment"/>
		</html:link> &nbsp;|&nbsp;
		</logic:greaterEqual>	    
	  
	
		<logic:equal name="ShiftType" value="1">
	    <bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.externalId"/>
		<html:link page="<%="/editStudentGroupShift.do?method=prepareEdit&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>"  paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
	    <bean:message key="link.editStudentGroupShift"/>
		</html:link>
		</logic:equal>
	
	
		<logic:equal name="ShiftType" value="3">
		<html:link page="<%="/enrollStudentGroupShift.do?method=prepareEnrollStudentGroupShift&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>"  paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
	    <bean:message key="link.enrollStudentGroupInShift"/>
	    </html:link>
		</logic:equal>
	
	  	<logic:equal name="ShiftType" value="2">
	    <html:link page="<%="/unEnrollStudentGroupShift.do?method=unEnrollStudentGroupShift&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>"  paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
	    <bean:message key="link.unEnrollStudentGroupShift"/>
	    </html:link>
		</logic:equal>
	</span>
</p>


<table class="tstyle4">
	<tbody>   
	<tr>
		<th><bean:message key="label.numberWord" />
		</th>
		<th><bean:message key="label.nameWord" />
		</th>
		<th><bean:message key="label.emailWord" />
		</th>
	</tr>
	
		
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentGroup" property="infoSiteStudentInformationList">			
 		<bean:define id="username" name="USER_SESSION_ATTRIBUTE" property="user.username" type="java.lang.String"/>
		<logic:equal name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr class="highlight">
				<td>
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>
				<td>
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>		
				<td>
					<logic:present name="infoSiteStudentInformation" property="email">
						<bean:define id="mail" name="infoSiteStudentInformation" property="email"/>
						<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoSiteStudentInformation" property="email"/></html:link>
					</logic:present>
					<logic:notPresent name="infoSiteStudentInformation" property="email">
						&nbsp;
					</logic:notPresent>
				</td>
			</tr>
		</logic:equal>

		<logic:notEqual name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr>
				<td><bean:write name="infoSiteStudentInformation" property="number"/>
				</td>
				<td><bean:write name="infoSiteStudentInformation" property="name"/>
				</td>		
				<td>
					<logic:present name="infoSiteStudentInformation" property="email">
						<bean:define id="mail" name="infoSiteStudentInformation" property="email"/>
						<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoSiteStudentInformation" property="email"/></html:link>
					</logic:present>
					<logic:notPresent name="infoSiteStudentInformation" property="email">
						&nbsp;
					</logic:notPresent>
				</td>
			</tr>
		</logic:notEqual>
	</logic:iterate>
	</tbody>
</table>

</logic:notEmpty>

</logic:present>

<logic:notPresent name="infoSiteStudentGroup">

	<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
	
</logic:notPresent>












