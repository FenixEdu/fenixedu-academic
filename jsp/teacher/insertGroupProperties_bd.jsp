<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.insertGroupProperties"/></h2>

<logic:present name="siteView"> 


<html:form action="/createGroupProperties">
<html:hidden property="page" value="1"/>
<u><bean:message key="message.insertGroupPropertiesData"/></u>
<br />
<br />


<table>
		<tr>
			<td><bean:message key="message.groupPropertiesName"/></td>
			<td><html:text size="40" property="name" /></td>
			<td><span class="error"><html:errors property="name"/></span></td>
		</tr>
	    

    	<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentBeginDay"/></td>
			<td><html:text size="10" property="enrolmentBeginDay" /></td>
			<td><span class="error"><html:errors property="enrolmentBeginDay"/></span></td>
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentEndDay"/></td>
			<td><html:text size="10" property="enrolmentEndDay" /></td>
			<td><span class="error"><html:errors property="enrolmentEndDay"/></span></td>
		</tr>
    
    	<tr>
			<td><bean:message key="message.groupPropertiesEnrolmentPolicy"/></td>
			<td><html:select property="enrolmentPolicy">
	    		<html:option key="option.groupProperties.enrolmentPolicy.atomic" value="true"/>
	    		<html:option key="option.groupProperties.enrolmentPolicy.individual" value="false"/>
	    		</html:select>
	    	</td>
			<td><span class="error"><html:errors property="enrolmentPolicy"/></span></td>
		</tr>
    
    	<tr>
			<td><bean:message key="message.groupPropertiesShiftType"/></td>
			<td><html:select property="shiftType">
				<html:options name="shiftTypeValues" labelName="shiftTypeNames"/>
				</html:select>
			</td>
			<td><span class="error"><html:errors property="shiftType"/></span></td>
		</tr>

	    <tr>
			<td><bean:message key="message.groupPropertiesMaximumCapacity"/></td>
			<td><html:text size="5" property="maximumCapacity" /></td>
			<td><span class="error"><html:errors property="maximumCapacity"/></span></td>
		</tr>	
    	<tr>
			<td><bean:message key="message.groupPropertiesMinimumCapacity"/></td>
			<td><html:text size="5" property="minimumCapacity" /></td>
			<td><span class="error"><html:errors property="minimumCapacity"/></span></td>
		</tr>
		<tr>
			<td><bean:message key="message.groupPropertiesIdealCapacity"/></td>
			<td><html:text size="5" property="idealCapacity" /></td>
			<td><span class="error"><html:errors property="idealCapacity"/></span></td>
		</tr>

		<tr>
			<td><bean:message key="message.groupPropertiesGroupMaximumNumber"/></td>
			<td><html:text size="5" property="groupMaximumNumber" /></td>
			<td><span class="error"><html:errors property="groupMaximumNumber"/></span></td>
		</tr>	


</table>
<br />
<br />
<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
</html:submit>       
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  

<html:hidden property="method" value="createGroupProperties"/>	
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

</html:form>
</logic:present>