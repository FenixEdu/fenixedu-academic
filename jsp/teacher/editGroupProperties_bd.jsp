<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>




<logic:present name="siteView" property="component"> 

<h2><bean:message key="title.editGroupProperties"/></h2>

<bean:define id="infoSiteGroupProperties" name="siteView" property="component"/>
<bean:define id="groupProperties" name="infoSiteGroupProperties" property="infoGroupProperties"/>

<html:form action="/editGroupProperties">
<html:hidden property="page" value="1"/>

<h2><span class="error"><html:errors/></span></h2>
<br>
<table>
		<tr>
			<td><bean:message key="message.groupPropertiesName"/></td>
			<td><html:text size="40" name="groupProperties" property="name" /></td>
			
		</tr>
		
		<tr>
			<td><bean:message key="message.groupPropertiesProjectDescription"/></td>
			<td><html:textarea name="groupProperties" property="projectDescription" cols="30" rows="4"/></td>
			
		</tr>
		
	    <tr>
			<td><bean:message key="message.groupPropertiesEnrolmentBeginDay"/></td>
			<td>
			<logic:empty name="groupProperties" property="enrolmentBeginDayFormatted">
			<html:text size="16" property="enrolmentBeginDayFormatted"/>
			</logic:empty>
			
			<logic:notEmpty name="groupProperties" property="enrolmentBeginDayFormatted">
			<html:text size="16" name="groupProperties" property="enrolmentBeginDayFormatted" />
			</logic:notEmpty>
			</td>
			
		</tr>
	    
 		 </tr>
	    	<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentEndDay"/></td>
			<td>
			<logic:empty name="groupProperties" property="enrolmentEndDayFormatted">
			<html:text size="16" property="enrolmentEndDayFormatted"/>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="enrolmentEndDayFormatted">
			<html:text size="16" name="groupProperties" property="enrolmentEndDayFormatted" />
			</logic:notEmpty>
			</td>
			
		</tr>
			
		<bean:define id="enrolmentPolicyValue" name="enrolmentPolicyValue"/>
		<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentPolicy"/></td>
			<td><html:select property="enrolmentPolicy">
			<html:option value="<%= enrolmentPolicyValue.toString() %>"><bean:write name="enrolmentPolicyName"/></html:option>
			<html:options name="enrolmentPolicyValues" labelName="enrolmentPolicyNames"/>
			</html:select></td>
			
		</tr>
		
		
		<bean:define id="shiftTypeValue" name="shiftTypeValue"/>
		<tr>
			<td><bean:message key="message.groupPropertiesShiftType"/></td>
			<td><html:select property="shiftType">
			<html:option value="<%= shiftTypeValue.toString() %>"><bean:write name="shiftTypeName"/></html:option>
			<html:options name="shiftTypeValues" labelName="shiftTypeNames"/>
			</html:select></td>
			
			
		</tr>

		<tr>
			<td><bean:message key="message.groupPropertiesMaximumCapacity"/></td>
			<td>
			<logic:empty name="groupProperties" property="maximumCapacity">
			<html:text size="5" property="maximumCapacity"/>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="maximumCapacity">
			<html:text size="5" name="groupProperties" property="maximumCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>	
    	<tr>
			<td><bean:message key="message.groupPropertiesMinimumCapacity"/></td>
			<td>
			<logic:empty name="groupProperties" property="minimumCapacity">
			<html:text size="5" property="minimumCapacity"/>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="minimumCapacity">
			<html:text size="5" name="groupProperties" property="minimumCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesIdealCapacity"/></td>
			<td>
			<logic:empty name="groupProperties" property="idealCapacity">
			<html:text size="5" property="idealCapacity"/>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="idealCapacity">
			<html:text size="5" name="groupProperties" property="idealCapacity" />
			</logic:notEmpty>
			</td>
			
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesGroupMaximumNumber"/></td>
			<td>
			<logic:empty name="groupProperties" property="groupMaximumNumber">
			<html:text size="5" property="groupMaximumNumber"/>
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="groupMaximumNumber">
			<html:text size="5" name="groupProperties" property="groupMaximumNumber" />
			</logic:notEmpty>
			</td>
			
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
<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
</html:form>
</logic:present>

<logic:notPresent name="siteView" property="component"> 
<h4>
<bean:message key="message.infoGroupProperties.not.available" />
</h4>
</logic:notPresent>