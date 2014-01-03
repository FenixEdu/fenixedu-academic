<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message bundle="SOP_RESOURCES" key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="label.see.vehicle.allocation" bundle="SOP_RESOURCES"/></h2>

<logic:present role="role(RESOURCE_ALLOCATION_MANAGER)">
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
	
	<ul class="mvert15">
		<li>
			<html:link page="/vehicleManagement.do?method=prepare">		
				<bean:message bundle="SOP_RESOURCES" key="label.back"/>
			</html:link>
		</li>		
	</ul>

	<logic:notEmpty name="vehicleAllocation">
		<fr:view schema="SeeVehicleAllocationWithReason" name="vehicleAllocation">	
			<fr:layout name="tabular">			      			
				<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
				<fr:property name="rowClasses" value="bold,,,,,,,bold" />			   				  
		   	</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
</logic:present>	