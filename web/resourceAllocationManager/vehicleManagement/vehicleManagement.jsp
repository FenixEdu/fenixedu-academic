<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message bundle="SOP_RESOURCES" key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="label.vehicle.management" bundle="SOP_RESOURCES"/></h2>

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
	
	<ul class="mvert15">
		<li>
			<html:link page="/vehicleManagement.do?method=prepareCreate">		
				<bean:message bundle="SOP_RESOURCES" key="label.create.vehicle.allocation"/>
			</html:link>
		</li>
		<li>
			<html:link page="/vehicleManagement.do?method=seeVehicleAllocationHistory">		
				<bean:message bundle="SOP_RESOURCES" key="link.vehicle.allocation.history"/>
			</html:link>
		</li>
	</ul>
		
	<p class="mtop2"><strong><bean:message key="label.active.vehicle.allocations" bundle="SOP_RESOURCES"/></strong></p>		
	<logic:notEmpty name="activeAllocations">
		<fr:view schema="SeeVehicleAllocation" name="activeAllocations">	
			<fr:layout name="tabular">
			      			
				<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
	   
	   			<fr:property name="link(view)" value="/vehicleManagement.do?method=seeVehicleAllocation"/>
	            <fr:property name="param(view)" value="idInternal/allocationID"/>
		        <fr:property name="key(view)" value="link.see"/>
	            <fr:property name="bundle(view)" value="SOP_RESOURCES"/>
	            <fr:property name="order(view)" value="0"/>
	            
	   			<fr:property name="link(edit)" value="/vehicleManagement.do?method=prepareEditAllocation"/>
	            <fr:property name="param(edit)" value="idInternal/allocationID"/>
		        <fr:property name="key(edit)" value="link.edit"/>
	            <fr:property name="bundle(edit)" value="SOP_RESOURCES"/>
	            <fr:property name="order(edit)" value="1"/>
	            
	            <fr:property name="link(delete)" value="/vehicleManagement.do?method=deleteAllocation"/>
	            <fr:property name="param(delete)" value="idInternal/allocationID"/>
		        <fr:property name="key(delete)" value="link.delete"/>
	            <fr:property name="bundle(delete)" value="SOP_RESOURCES"/>
	            <fr:property name="order(delete)" value="2"/> 
				<fr:property name="confirmationKey(delete)" value="label.delete.vehicle.allocation.confirmation"/>
				<fr:property name="confirmationBundle(delete)" value="SOP_RESOURCES"/>	                                                      
	            
	    	</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="activeAllocations">
		<p><em><bean:message key="label.empty.active.vehicle.allocations" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>		
			
	<p class="mtop15"><strong><bean:message key="label.other.vehicle.allocations" bundle="SOP_RESOURCES"/></strong></p>
	<logic:notEmpty name="futureAllocations">
		<fr:view schema="SeeVehicleAllocation" name="futureAllocations">
			<fr:layout name="tabular">
			      			
				<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
	   
	      		<fr:property name="link(view)" value="/vehicleManagement.do?method=seeVehicleAllocation"/>
	            <fr:property name="param(view)" value="idInternal/allocationID"/>
		        <fr:property name="key(view)" value="link.see"/>
	            <fr:property name="bundle(view)" value="SOP_RESOURCES"/>
	            <fr:property name="order(view)" value="0"/>                                           	            
	   
	   			<fr:property name="link(edit)" value="/vehicleManagement.do?method=prepareEditAllocation"/>
	            <fr:property name="param(edit)" value="idInternal/allocationID"/>
		        <fr:property name="key(edit)" value="link.edit"/>
	            <fr:property name="bundle(edit)" value="SOP_RESOURCES"/>
	            <fr:property name="order(edit)" value="1"/>
	            
	            <fr:property name="link(delete)" value="/vehicleManagement.do?method=deleteAllocation"/>
	            <fr:property name="param(delete)" value="idInternal/allocationID"/>
		        <fr:property name="key(delete)" value="link.delete"/>
	            <fr:property name="bundle(delete)" value="SOP_RESOURCES"/>
	            <fr:property name="order(delete)" value="2"/>                                           
	            
	    	</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="futureAllocations">
		<p><em><bean:message key="label.empty.future.vehicle.allocations" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>	
		
</logic:present>	