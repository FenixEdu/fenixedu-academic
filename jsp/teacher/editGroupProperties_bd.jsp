<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView" property="component"> 

<h2><bean:message key="title.editGroupProperties"/></h2>

<bean:define id="infoSiteGroupProperties" name="siteView" property="component"/>
<bean:define id="groupProperties" name="infoSiteGroupProperties" property="infoGrouping"/>

<div class="dinline forminline">

<html:form action="/editGroupProperties">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<div class="infoop2">
	<bean:message key="label.teacher.editGroupProperties.description" />
</div>


<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<br/>

<table class="tstyle5 thlight thright dinline">
		<tr>
			<th><bean:message key="message.groupPropertiesName"/>:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="40" name="groupProperties" property="name" /></td>
		</tr>
		
		<tr>
			<th><bean:message key="message.groupPropertiesProjectDescription"/>:</th>
			<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.projectDescription" name="groupProperties" property="projectDescription" cols="50" rows="6"/></td>
		</tr>
		
	    <tr>
			<th><bean:message key="message.groupPropertiesEnrolmentBeginDay"/>:</th>
			<td>
				<logic:empty name="groupProperties" property="enrolmentBeginDayFormatted">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentBeginDayFormatted" size="10" property="enrolmentBeginDayFormatted"/>
				<i><bean:message key="label.at" /></i>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentBeginHourFormatted" size="5" property="enrolmentBeginHourFormatted"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />
			</logic:empty>
			
			<logic:notEmpty name="groupProperties" property="enrolmentBeginDayFormatted">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentBeginDayFormatted" size="10" name="groupProperties" property="enrolmentBeginDayFormatted" />
				<i><bean:message key="label.at" /></i>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentBeginHourFormatted" size="5" name="groupProperties" property="enrolmentBeginHourFormatted"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />
			</logic:notEmpty>
			</td>
		</tr>
	    
    	<tr>
			<th><bean:message key="message.groupPropertiesEnrolmentEndDay"/>:</th>
			<td>
			<logic:empty name="groupProperties" property="enrolmentEndDayFormatted">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentEndDayFormatted" size="10" property="enrolmentEndDayFormatted"/>
				<i><bean:message key="label.at" /></i>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentEndHourFormatted" size="5" property="enrolmentEndHourFormatted"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />				
			</logic:empty>
			<logic:notEmpty name="groupProperties" property="enrolmentEndDayFormatted">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentEndDayFormatted" size="10" name="groupProperties" property="enrolmentEndDayFormatted" />
				<i><bean:message key="label.at" /></i>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentEndHourFormatted" size="5" name="groupProperties" property="enrolmentEndHourFormatted"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />			
			</logic:notEmpty>
			</td>
		</tr>
			
		<bean:define id="enrolmentPolicyValue" name="enrolmentPolicyValue"/>
		<tr>
			<th><bean:message key="message.groupPropertiesEnrolmentPolicy"/>:</th>
			<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.enrolmentPolicy" property="enrolmentPolicy">
			<html:option value="<%= enrolmentPolicyValue.toString() %>"><bean:write name="enrolmentPolicyName"/></html:option>
			<html:options name="enrolmentPolicyValues" labelName="enrolmentPolicyNames"/>
			</html:select></td>			
		</tr>
		
		<bean:define id="shiftTypeValue" type="java.lang.Object" value="SEM TURNO"/>
	    <logic:present 	name="groupProperties" property="shiftType">
		 	<bean:define id="shiftTypeValue" name="groupProperties" property="shiftType"/>
		</logic:present>
		
		<tr>
			<th><bean:message key="message.groupPropertiesShiftType"/>:</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.shiftType" property="shiftType" >
					<html:option value="<%= shiftTypeValue.toString() %>"><bean:write name="shiftTypeValue"/></html:option>
				<html:options collection="shiftTypeValues" property="value" labelProperty="label"/>
				</html:select>
			</td>		
		</tr>

		<tr>
			<th>
				<bean:message key="message.groupPropertiesMaximumCapacity"/>:
				<br/><bean:message key="label.teacher.insertGroupProperties.MaximumCapacityDescription"/>
			</th>
			
			<td>
				<logic:empty name="groupProperties" property="maximumCapacity">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumCapacity" size="5" property="maximumCapacity"/>
				</logic:empty>
				<logic:notEmpty name="groupProperties" property="maximumCapacity">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumCapacity" size="5" name="groupProperties" property="maximumCapacity" />
				</logic:notEmpty>
			</td>
			
		</tr>	
    	<tr>
			<th>
				<bean:message key="message.groupPropertiesMinimumCapacity"/>:
				<br/><bean:message key="label.teacher.insertGroupProperties.MinimumCapacityDescription"/>
			</th>
			<td>
				<logic:empty name="groupProperties" property="minimumCapacity">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCapacity" size="5" property="minimumCapacity"/>
				</logic:empty>
				<logic:notEmpty name="groupProperties" property="minimumCapacity">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCapacity" size="5" name="groupProperties" property="minimumCapacity" />
				</logic:notEmpty>
			</td>
		</tr>

		<tr>
			<th>
				<bean:message key="message.groupPropertiesIdealCapacity"/>:
				<br/><bean:message key="label.teacher.insertGroupProperties.IdealCapacityDescription"/>
			</th>
			<td>
				<logic:empty name="groupProperties" property="idealCapacity">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.idealCapacity" size="5" property="idealCapacity"/>
				</logic:empty>
				<logic:notEmpty name="groupProperties" property="idealCapacity">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.idealCapacity" size="5" name="groupProperties" property="idealCapacity" />
				</logic:notEmpty>
			</td>
		</tr>

		<tr>
			<th>
				<bean:message key="message.groupPropertiesGroupMaximumNumber"/>:
			</th>
			<td>
				<logic:empty name="groupProperties" property="groupMaximumNumber">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.groupMaximumNumber" size="5" property="groupMaximumNumber"/>
				</logic:empty>
				<logic:notEmpty name="groupProperties" property="groupMaximumNumber">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.groupMaximumNumber" size="5" name="groupProperties" property="groupMaximumNumber" />
				</logic:notEmpty>
			</td>
		</tr>
	</table>

	<br/><br/>
	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
		</html:submit>
		

		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>  

		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editGroupProperties"/>	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

	
	
	<html:form action="/viewShiftsAndGroups" method="get">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewShiftsAndGroups"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

</div>

</logic:present>



<logic:notPresent name="siteView" property="component"> 
<p>
	<span class="warning0"><bean:message key="message.infoGroupProperties.not.available" /></span>
</p>
</logic:notPresent>