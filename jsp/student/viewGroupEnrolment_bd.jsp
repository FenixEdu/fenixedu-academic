<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>





<h2><bean:message key="title.enrolmentGroup.insertNewGroup"/></h2>

	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.student.viewGroupEnrolment.description" />
			</td>
		</tr>
	</table>
	<br>

<html:form action="/groupEnrolment" method="get">

<br>

<h2><span class="error"><html:errors/></span></h2>		 

<bean:define id="groupNumber" name="groupNumber"/>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<b><bean:message key="label.GroupNumber"/> </b><bean:write name="groupNumber"/>
		</td>
		
	</tr>

</table>

<br/>
<logic:present name="infoStudents"> 
	<logic:empty name="infoStudents">
	<h2>
	<bean:message key="message.infoStudents.not.available" />
	</h2>
	</logic:empty>
	
	<logic:notEmpty name="infoStudents">
	<b><bean:message key="label.infoStudents.studentsWithoutGroup" /></b>
	<br>
	<br>
	<table width="50%" cellpadding="0" border="0">	
		<tr>
		<td class="listClasses-header">
		</td>
		<td class="listClasses-header"><bean:message key="label.numberWord" />
		</td>
		<td class="listClasses-header"><bean:message key="label.nameWord" />
		</td>
		<td class="listClasses-header"><bean:message key="label.emailWord" />
		</td>
		</tr>
	
		<logic:iterate id="infoStudent" name="infoStudents">			
			<tr>	
				<td class="listClasses">
				<html:multibox property="studentsNotEnroled">
				<bean:write name="infoStudent" property="idInternal"/>
				</html:multibox>
				</td>	
				<td class="listClasses"><bean:write name="infoStudent" property="number"/>
				</td>	
				<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>		
				<td class="listClasses"><bean:write name="infoPerson" property="nome"/>
				</td>
				<td class="listClasses"><bean:write name="infoPerson" property="email"/>
				</td>
	 		</tr>	
	 	</logic:iterate>
	 

		</table>
		<br>
	</logic:notEmpty>
	</logic:present>

<html:hidden property="method" value="enrolment"/>
<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<logic:present name="shiftCode"> 
<html:hidden  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>
<html:hidden  property="groupNumber" value="<%= groupNumber.toString() %>" />

<table>
<tr>
	<td><html:submit styleClass="inputbutton"><bean:message key="button.finalize.enrolment"/>                    		         	
		</html:submit>       
		<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>  

		</html:form>
	</td>
	<td>
		<html:form action="/viewShiftsAndGroups" method="get">
	
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	
		<html:hidden property="method" value="execute"/>
		<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
		<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>		

	</html:form>
	</td>
	
</tr>
</table>
			

