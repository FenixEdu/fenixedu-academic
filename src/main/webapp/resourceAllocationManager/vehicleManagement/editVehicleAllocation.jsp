<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
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

			<logic:empty name="allocationBean" property="amountCharged">
				<fr:edit id="viewVehicleAllocationBeanID" name="allocationBean" action="/vehicleManagement.do?method=prepareConfirmCreation" schema="PrepareCreateVehicleAllocation">
					<fr:layout name="tabular" >
						<fr:property name="classes" value="tstyle5 thlight thright"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
					<fr:destination name="invalid" path="/vehicleManagement.do?method=prepareCreate"/>	
					<fr:destination name="cancel" path="/vehicleManagement.do?method=prepare"/>						
				</fr:edit>
			</logic:empty>
			
			<logic:notEmpty name="allocationBean" property="amountCharged">
				
				<fr:view name="allocationBean" schema="ViewVehicleAllocation">
					<fr:layout name="tabular" >
						<fr:property name="classes" value="tstyle5 thlight thright"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						<fr:property name="rowClasses" value="bold,,,,," />	
					</fr:layout>
				</fr:view>
			
				<fr:edit id="createVehicleAllocationBeanID" name="allocationBean" action="/vehicleManagement.do?method=createAllocation" schema="CreateVehicleAllocation">
					<fr:layout name="tabular" >
						<fr:property name="classes" value="tstyle5 thlight thright"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
					<fr:destination name="invalid" path="/vehicleManagement.do?method=prepareCreate"/>	
					<fr:destination name="cancel" path="/vehicleManagement.do?method=prepare"/>						
				</fr:edit>			
			</logic:notEmpty>			
								
		</logic:notEmpty>
	</logic:empty>
		
	
	<%-- Edit Vehicle Allocation --%>
			
	<logic:notEmpty name="vehicleAllocation">
		<h2><bean:message key="label.edit.vehicle.allocation" bundle="SOP_RESOURCES"/></h2>		
		<fr:edit id="vehicleAllocationEditID" name="vehicleAllocation" schema="EditVehicleAllocation" action="/vehicleManagement.do?method=prepare">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="cancel" path="/vehicleManagement.do?method=prepare"/>			
		</fr:edit>		
	</logic:notEmpty>
	
</logic:present>	