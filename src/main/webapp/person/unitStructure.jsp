<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="string" %>

<logic:present name="currentUnit">
	<bean:define id="initialCurrentUnit" name="currentUnit" toScope="request"/>
	
	<logic:empty name="initialCurrentUnit" property="type">
		<bean:write name="initialCurrentUnit" property="name"/>	
		<br/>
	</logic:empty>	
	
	<logic:notEmpty name="initialCurrentUnit" property="type">
		<logic:notEqual name="initialCurrentUnit" property="type.name" value="AGGREGATE_UNIT">
			<bean:write name="initialCurrentUnit" property="name"/>
			<br/>
		</logic:notEqual>		
	</logic:notEmpty>
		
	<logic:iterate id="parentUnit" name="initialCurrentUnit" property="parentByOrganizationalStructureAccountabilityType">
		<logic:notEmpty name="parentUnit" property="parentByOrganizationalStructureAccountabilityType">
			<logic:iterate id="grandParentUnit" name="parentUnit" property="parentByOrganizationalStructureAccountabilityType">
				<logic:notEmpty name="grandParentUnit" property="parentByOrganizationalStructureAccountabilityType">
					<bean:define id="currentUnit" name="parentUnit" toScope="request"/>
					<jsp:include page="unitStructure.jsp"/>
				</logic:notEmpty>
			</logic:iterate>
		</logic:notEmpty>
	</logic:iterate>
</logic:present>
