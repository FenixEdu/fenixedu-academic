<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="shiftsList">

<logic:empty name="shiftsList">
<h4>
<bean:message key="message.infoSiteShiftList.not.available" />
</h4>
</logic:empty>


<html:form action="/groupEnrolment" method="get">

<h2><bean:message key="title.enrolmentGroup.insertNewGroup"/></h2>
<b><bean:message key="label.enrolmentGroup.chooseShiftAndStudents"/></b>
<br>

<h2><span class="error"><html:errors/></span></h2>		 
		<table width="50%" cellpadding="0" border="0">
		<tr>
			<td><bean:message key="label.shiftWord"/></td>
		
			<td>
				<html:select property="shift" size="1">
    			<html:options collection="shiftsList" property="value" labelProperty="label"/>
    			</html:select>
    		</td>
			
		</tr>	
		</table>
		<br>
		<br>

<logic:present name="infoStudents"> 
	<logic:empty name="infoStudents">
	<h4>
	<bean:message key="message.infoStudents.not.available" />
	</h4>
	</logic:empty>
	
	<logic:notEmpty name="infoStudents">
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
<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />

<table>
<tr>
	<td><html:submit styleClass="inputbutton"><bean:message key="button.finalize.enrolment"/>                    		         	
		</html:submit>       
		<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>  

		</html:form>
	</td>
	<td>
		<html:form action="/viewProjectStudentGroups" method="get">
	
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	
		<html:hidden property="method" value="execute"/>
		<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>		
	
	</html:form>
	</td>
	
</tr>
</table>
			
</logic:present>
<logic:notPresent name="shiftsList">
<h4>
<bean:message key="message.infoSiteShiftList.not.available" />
</h4>
</logic:notPresent>