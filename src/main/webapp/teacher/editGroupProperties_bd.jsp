<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<h2><bean:message key="title.editGroupProperties"/></h2>

<bean:define id="groupProperties" name="infoGrouping" />

<div class="dinline forminline">

<html:form action="/studentGroupManagement">
	<fr:context>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editGroupProperties"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
			
	<div class="infoop2">
		<bean:message key="label.teacher.editGroupProperties.description" />
	</div>
	
	<logic:present name="notPosibleToRevertChoice">
		<span class="infoop2">
			<bean:message key="label.teacher.editGroupProperties.notPosibleToRevert" />
		</span>
	</logic:present>
	<p>
		<span class="error0"><!-- Error messages go here --><html:errors /></span>
	</p>
	
	<br/>

	<bean:define id="isAutomaticEnrolment" value="false" />
	<bean:define id="isDifferentiatedCapacity" value="false" />
	
	<logic:equal name="groupProperties" property="automaticEnrolment" value="true">
		<bean:define id="isAutomaticEnrolment" value="true"/>
	</logic:equal>
	
	<logic:equal name="groupProperties" property="differentiatedCapacity" value="true">
		<bean:define id="isDifferentiatedCapacity" value="true" />
	</logic:equal>
	<logic:equal name="groupProperties" property="differentiatedCapacity" value="false">
		<bean:define id="isDifferentiatedCapacity" value="false" />
	</logic:equal>

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
		<tr>
			<th><bean:message key="message.groupPropertiesAutomaticEnrolment"/>:</th>
			<bean:define id="disableAutomaticEnrolment" value="false"/>
			<logic:present name="automaticEnrolmentDisable">
				<bean:define id="disableAutomaticEnrolment" value="true"/>
			</logic:present>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.automaticEnrolment" name="groupProperties" disabled="<%= Boolean.valueOf(disableAutomaticEnrolment) %>"
					property="automaticEnrolment" onclick="this.form.method.value='editGroupPropertiesPostBack';this.form.page.value='0';this.form.submit();"/>
			</td>
		</tr>		
		<bean:define id="enrolmentPolicyValue" name="enrolmentPolicyValue"/>
		<tr>
			<th><bean:message key="message.groupPropertiesEnrolmentPolicy"/>:</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.enrolmentPolicy" 
					property="enrolmentPolicy" disabled="<%= Boolean.valueOf(isAutomaticEnrolment) %>">
					<html:option value="<%= enrolmentPolicyValue.toString() %>"><bean:write name="enrolmentPolicyName"/></html:option>
					<html:options name="enrolmentPolicyValues" labelName="enrolmentPolicyNames"/>
				</html:select>
			</td>			
		</tr>	
		<tr>
			<th><bean:message key="message.groupPropertiesShiftType" />:</th>
			<td><html:select bundle="HTMLALT_RESOURCES"
				property="shiftType"
				disabled="<%= Boolean.valueOf(isAutomaticEnrolment) %>">
				<html:options collection="shiftTypeValues" property="value"
					labelProperty="label" />
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message key="message.groupPropertiesDifferentiatedCapacity"/>:</th>
			<bean:define id="disableDifferentiatedCapacity" value="false"/>
			<logic:present name="differentiatedCapacityDisable">
				<bean:define id="disableDifferentiatedCapacity" value="true"/>
			</logic:present>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.differentiatedCapacity" name="groupProperties" disabled="<%= Boolean.valueOf(disableDifferentiatedCapacity) | Boolean.valueOf(isAutomaticEnrolment)%>"
					property="differentiatedCapacity" onclick="this.form.method.value='editGroupCapacityPropertiesPostBack';this.form.page.value='0';this.form.submit();"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="message.groupPropertiesMaximumCapacity"/>:
				<br/><bean:message key="label.teacher.insertGroupProperties.MaximumCapacityDescription"/>
			</th>
			<td>
				<logic:empty name="groupProperties" property="maximumCapacity">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumCapacity" size="5" 
							property="maximumCapacity" readonly="<%= Boolean.valueOf(isAutomaticEnrolment) %>"/>
				</logic:empty>
				<logic:notEmpty name="groupProperties" property="maximumCapacity">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumCapacity" size="5" name="groupProperties" 
						property="maximumCapacity" readonly="<%= Boolean.valueOf(isAutomaticEnrolment) %>" />
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
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCapacity" size="5" 
						property="minimumCapacity" readonly="<%= Boolean.valueOf(isAutomaticEnrolment) %>"/>
				</logic:empty>
				<logic:notEmpty name="groupProperties" property="minimumCapacity">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCapacity" size="5" name="groupProperties" 
						property="minimumCapacity" readonly="<%= Boolean.valueOf(isAutomaticEnrolment) %>" />
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
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.idealCapacity" size="5" 
						property="idealCapacity" readonly="<%= Boolean.valueOf(isAutomaticEnrolment) %>"/>
				</logic:empty>
				<logic:notEmpty name="groupProperties" property="idealCapacity">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.idealCapacity" size="5" name="groupProperties" 
						property="idealCapacity" readonly="<%= Boolean.valueOf(isAutomaticEnrolment) %>" />
				</logic:notEmpty>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="message.groupPropertiesGroupMaximumNumber"/>:
			</th>
			<td>
				<logic:empty name="groupProperties" property="groupMaximumNumber">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.groupMaximumNumber" size="5" 
						property="groupMaximumNumber" 
						readonly="<%= Boolean.valueOf(isAutomaticEnrolment) %>"
						disabled="<%= Boolean.valueOf(isDifferentiatedCapacity) %>"/>
				</logic:empty>
				<logic:notEmpty name="groupProperties" property="groupMaximumNumber">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.groupMaximumNumber" size="5" name="groupProperties" 
						property="groupMaximumNumber" readonly="<%= Boolean.valueOf(isAutomaticEnrolment) %>"/>
				</logic:notEmpty>
			</td>
		</tr>
	</table>
	
	<logic:equal name="isDifferentiatedCapacity" value="true">
		<fr:edit id="shiftsTable" name="shiftsList" visible="false" />

		<fr:edit id="shiftsTable-edit" name="shiftsList">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thmiddle" />
				<fr:property name="columnClasses"
					value="acenter,,aright,aright,acenter" />
			</fr:layout>
			<fr:schema
				type="net.sourceforge.fenixedu.dataTransferObject.InfoShift"
				bundle="APPLICATION_RESOURCES">
				<fr:slot name="nome" key="label.shifts" readOnly="true" />
				<fr:slot name="lessons" key="label.shifts" readOnly="true" />
				<fr:slot name="lotacao" key="property.capacity" readOnly="true" />
				<fr:slot name="ocupation"
					key="property.number.students.attending.course" readOnly="true" />
				<fr:slot name="groupCapacity"
					key="message.groupPropertiesGroupMaximumNumber" />
			</fr:schema>
		</fr:edit>
	</logic:equal>
	
	<br/><br/>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="setAutomaticValues(this.form);">
		<bean:message key="button.save"/>                    		         	
	</html:submit>

	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset> 	
	</fr:context>
</html:form>

	
	
	<html:form action="/studentGroupManagement" >
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewShiftsAndGroups"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

</div>

<script language="javascript">
	function setAutomaticValues(form) {
		if(form.automaticEnrolment.checked){
			form.enrolmentPolicy.disabled=false;
			form.shiftType.disabled=false;
		}
	}
</script>