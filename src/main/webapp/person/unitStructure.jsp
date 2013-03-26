<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="string" %>

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
