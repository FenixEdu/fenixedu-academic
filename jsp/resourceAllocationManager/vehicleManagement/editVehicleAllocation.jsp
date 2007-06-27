<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message bundle="SOP_RESOURCES" key="title.resourceAllocationManager.management"/></em>

<logic:present role="RESOURCE_ALLOCATION_MANAGER">
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
		
	<%-- Create Vehicle Allocation --%>
	
	<logic:empty name="vehicleAllocation">
		<logic:notEmpty name="allocationBean">			
			<h2><bean:message key="label.create.vehicle.allocation" bundle="SOP_RESOURCES"/></h2>				
			<fr:edit id="createVehicleAllocationBeanID" name="allocationBean" action="/vehicleManagement.do?method=createAllocation" schema="CreateVehicleAllocation">
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle1"/>		        
				</fr:layout>
				<fr:destination name="invalid" path="/vehicleManagement.do?method=prepareCreate"/>	
				<fr:destination name="cancel" path="/vehicleManagement.do?method=prepare"/>						
			</fr:edit>					
		</logic:notEmpty>
	</logic:empty>
		
	
	<%-- Edit Vehicle Allocation --%>
			
	<logic:notEmpty name="vehicleAllocation">
		<h2><bean:message key="label.edit.vehicle.allocation" bundle="SOP_RESOURCES"/></h2>		
		<fr:edit id="vehicleAllocationEditID" name="vehicleAllocation" schema="EditVehicleAllocation" action="/vehicleManagement.do?method=prepare">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>		        
			</fr:layout>
			<fr:destination name="cancel" path="/vehicleManagement.do?method=prepare"/>			
		</fr:edit>		
	</logic:notEmpty>
	
</logic:present>	