<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="SPACE_RESOURCES" key="space.list.changes.in.the.spaces.title"/></h2>

<logic:messagesPresent message="true">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<logic:notEmpty name="domainObjectActionLogs">
	
	<%
		String sortCriteria = request.getParameter("sortBy");
	
		if (sortCriteria == null) {
		    sortCriteria = "instant=descending";
		}
	%>

	<fr:view name="domainObjectActionLogs" schema="ListChangesInTheSpacesSchema">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle4"/>
			<fr:property name="columnClasses" value="nowrap,,nowrap,nowrap,aleft,aleft"/>
			<fr:property name="rowClasses" value="listClasses"/>			
			<fr:property name="sortUrl" value="<%= "/listChangesInTheSpaces.do?method=changesList" %>" />
			<fr:property name="sortParameter" value="sortBy"/>
			<fr:property name="sortBy" value="<%= sortCriteria %>"/>
		</fr:layout>
	</fr:view>		
</logic:notEmpty>
