<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.editGroupProperties"/></h2>

<logic:present name="siteView"> 
<bean:define id="infoSiteGroupProperties" name="siteView" property="component"/>
<bean:define id="groupProperties" name="infoSiteGroupProperties" property="infoGroupProperties"/>

<html:form action="/editGroupProperties">
<html:hidden property="page" value="1"/>
<span class="error"><html:errors/></span>
<br />

<table>
		<tr>
			<td><bean:message key="message.groupPropertiesName"/></td>
			<td><html:text size="40" name="groupProperties" property="name" /></td>
			<td><span class="error"><html:errors property="name"/></span></td>
		</tr>
	    
	    
    	<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentBeginDay"/><bean:message key="message.dateFormat"/></td>
			<td><html:text size="10" name="groupProperties" property="enrolmentBeginDayFormatted" /></td>
			<td><span class="error"><html:errors property="enrolmentBeginDay"/></span></td>
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentEndDay"/><bean:message key="message.dateFormat"/></td>
			<td><html:text size="10" name="groupProperties" property="enrolmentEndDayFormatted" /></td>
			<td><span class="error"><html:errors property="enrolmentEndDay"/></span></td>
		</tr>
		
				
		<bean:define id="enrolmentPolicyValue" name="enrolmentPolicyValue"/>
		<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentPolicy"/></td>
			<td><html:select property="enrolmentPolicy">
			<html:option value="<%= enrolmentPolicyValue.toString() %>"><bean:write name="enrolmentPolicyName"/></html:option>
			<html:options name="enrolmentPolicyValues" labelName="enrolmentPolicyNames"/>
		</html:select></td>
			
			<td><span class="error"><html:errors property="enrolmentPolicy"/></span></td>
		</tr>
		
		
		<bean:define id="shiftTypeValue" name="shiftTypeValue"/>
		<tr>
			<td><bean:message key="message.groupPropertiesShiftType"/></td>
			<td><html:select property="shiftType">
			<html:option value="<%= shiftTypeValue.toString() %>"><bean:write name="shiftTypeName"/></html:option>
			<html:options name="shiftTypeValues" labelName="shiftTypeNames"/>
		</html:select></td>
			
			<td><span class="error"><html:errors property="shiftType"/></span></td>
		</tr>

		<tr>
			<td><bean:message key="message.groupPropertiesMaximumCapacity"/></td>
			<td><html:text size="5" name="groupProperties" property="maximumCapacity" /></td>
			<td><span class="error"><html:errors property="maximumCapacity"/></span></td>
		</tr>	
    	<tr>
			<td><bean:message key="message.groupPropertiesMinimumCapacity"/></td>
			<td><html:text size="5" name="groupProperties" property="minimumCapacity" /></td>
			<td><span class="error"><html:errors property="minimumCapacity"/></span></td>
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesIdealCapacity"/></td>
			<td><html:text size="5" name="groupProperties" property="idealCapacity" /></td>
			<td><span class="error"><html:errors property="idealCapacity"/></span></td>
		</tr>

		<tr>
			<td><bean:message key="message.groupPropertiesGroupMaximumNumber"/></td>
			<td><html:text size="5" name="groupProperties" property="groupMaximumNumber" /></td>
			<td><span class="error"><html:errors property="groupMaximumNumber"/></span></td>
		</tr>	

</table>
<br />
<br />
<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
</html:submit>       
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  

<html:hidden property="method" value="editGroupProperties"/>	
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden  property="groupProperties" value="<%= request.getParameter("groupProperties") %>" />
</html:form>
</logic:present>