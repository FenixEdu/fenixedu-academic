<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="string" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<html:xhtml/>

<logic:present name="currentUnit">

	<bean:define id="initialCurrentUnit" name="currentUnit" toScope="request"/>	
	
	<logic:notEmpty name="initialCurrentUnit" property="type">
		<logic:notEqual name="initialCurrentUnit" property="type.name" value="AGGREGATE_UNIT">			
			<logic:notEmpty name="initialCurrentUnit" property="webAddress">
				<bean:define id="url" type="java.lang.String" name="initialCurrentUnit" property="webAddress"/>
				<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><html:link href="<%= url %>"><bean:write name="initialCurrentUnit" property="name"/></html:link>
			</logic:notEmpty>			
			<logic:empty name="initialCurrentUnit" property="webAddress">
				<bean:write name="initialCurrentUnit" property="name"/>
			</logic:empty>				
			<br/>
		</logic:notEqual>
	</logic:notEmpty>	
	
	<logic:empty name="initialCurrentUnit" property="type">
		<logic:notEmpty name="initialCurrentUnit" property="webAddress">
			<bean:define id="url" type="java.lang.String" name="initialCurrentUnit" property="webAddress"/>
			<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><html:link href="<%= url %>"><bean:write name="initialCurrentUnit" property="name"/></html:link>
		</logic:notEmpty>			
		<logic:empty name="initialCurrentUnit" property="webAddress">
			<bean:write name="initialCurrentUnit" property="name"/>
		</logic:empty>			
		<br/>
	</logic:empty>
			
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
