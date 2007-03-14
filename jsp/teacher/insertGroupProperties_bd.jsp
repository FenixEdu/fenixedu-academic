<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<logic:present name="siteView"> 

<h2><bean:message key="title.insertGroupProperties"/></h2>


<div class="infoop2">
	<bean:message key="label.teacher.insertGroupProperties.description" />
</div>
			
<p>		
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>


<div class="dinline forminline">

<html:form action="/createGroupProperties" styleClass="dinline" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>


<p class="mtop15"><b><bean:message key="message.insertGroupPropertiesData"/>:</b></p>

<table class="tstyle5 thright thlight dinline">
		<tr>
			<th><bean:message key="message.groupPropertiesName"/>:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="40" property="name" /></td>
		</tr>
	    
		<tr>
			<th><bean:message key="message.groupPropertiesProjectDescription"/>:</th>
			<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.projectDescription" bundle="HTMLALT_RESOURCES" property="projectDescription" cols="50" rows="5"/></td>
		</tr>
		
    	<tr>
			<th><bean:message key="message.groupPropertiesEnrolmentBeginDay"/>:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentBeginDay" size="10" property="enrolmentBeginDay" />
				<i><bean:message key="label.at" /></i>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentBeginHour" size="5" property="enrolmentBeginHour"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />						
			</td>			
		</tr>
		<tr>
			<th><bean:message key="message.groupPropertiesEnrolmentEndDay"/>:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentEndDay" size="10" property="enrolmentEndDay" />
				<i><bean:message key="label.at" /></i>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentEndHour" size="5" property="enrolmentEndHour"/>
				<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />	
			</td>		
			<%--<td><span class="error"><!-- Error messages go here --><html:errors property="enrolmentEndDay"/></span></td>--%>
		</tr>
    
    	<tr>
			<th><bean:message key="message.groupPropertiesEnrolmentPolicy"/>:</th>
			<td><html:select bundle="HTMLALT_RESOURCES" property="enrolmentPolicy">
	    		<html:option key="option.groupProperties.enrolmentPolicy.atomic" value="true"/>
	    		<html:option key="option.groupProperties.enrolmentPolicy.individual" value="false"/>
	    		</html:select>
	    	</td>
			
		</tr>

		<tr>
			<th><bean:message key="message.groupPropertiesShiftType"/>:</th>
			<td>
			<html:select bundle="HTMLALT_RESOURCES" property="shiftType" >
				<html:options collection="shiftTypeValues" property="value" labelProperty="label"/>
			</html:select>
			</td>		
		</tr>

	    <tr>
			<th>
				<bean:message key="message.groupPropertiesMaximumCapacity"/>:<br/>
				<bean:message key="label.teacher.insertGroupProperties.MaximumCapacityDescription"/>
			</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumCapacity" size="5" property="maximumCapacity" /></td>
		</tr>	
    	<tr>
			<th>
				<bean:message key="message.groupPropertiesMinimumCapacity"/>:
				<br/><bean:message key="label.teacher.insertGroupProperties.MinimumCapacityDescription"/>
			</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCapacity" size="5" property="minimumCapacity" /></td>
		</tr>
		<tr>
			<th>
				<bean:message key="message.groupPropertiesIdealCapacity"/>:
				<br/><bean:message key="label.teacher.insertGroupProperties.IdealCapacityDescription"/>
			</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.idealCapacity" size="5" property="idealCapacity" /></td>
		</tr>
		<tr>
			<th><bean:message key="message.groupPropertiesGroupMaximumNumber"/>:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.groupMaximumNumber" size="5" property="groupMaximumNumber" /></td>
		</tr>	
</table>

<br/><br/>

<p class="mtop15 dinline">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.save"/></html:submit>       
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>  
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createGroupProperties"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
</p>	

</html:form>


<html:form action="/viewExecutionCourseProjects" method="get">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareViewExecutionCourseProjects"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />	
</html:form>

</div>



</logic:present>

<logic:notPresent name="siteView">
<p class="mvert15">
	<em><bean:message key="message.insert.infoGroupProperties.not.available" /></em>
</p> 
</logic:notPresent> 