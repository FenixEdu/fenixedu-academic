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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h1 class="mbottom03 cnone">
	<fr:view name="researchUnit" property="nameWithAcronym"/>
</h1>

<bean:define id="siteID" name="site" property="externalId"/>

<p>
	<html:link href="<%= request.getContextPath() + "/publico/researchSite/viewResearchUnitSite.do?siteID=" + siteID + "&method=organization"%>" ><bean:message key="label.organization" bundle="SITE_RESOURCES"/></html:link> | <bean:message key="label.members" bundle="SITE_RESOURCES"/>
</p>

<h2><bean:message key="label.members" bundle="SITE_RESOURCES"/></h2>
	<bean:define id="researchUnit" name="researchUnit" toScope="request"/>
	<jsp:include flush="true" page="viewMembersFromUnit.jsp"></jsp:include>


<logic:notEmpty name="researchUnit" property="allCurrentActiveSubUnits"> 
<bean:define id="unitName" name="researchUnit" property="name" type="java.lang.String"/>
	<logic:iterate id="unit" name="researchUnit" property="allCurrentActiveSubUnitsOrdered"> 
		<logic:equal name="unit" property="researchUnit" value="true">
			<fr:view name="unit">
				<fr:layout name="unit-link">
					<fr:property name="style" value="font-weight: bold; font-size: 12px; color: #333;"/>
					<fr:property name="unitLayout" value="values"/>
					<fr:property name="unitSchema" value="unit.name"/>
					<fr:property name="targetBlank" value="true"/>
					<fr:property name="parenteShown" value="false"/>
				</fr:layout>
			</fr:view>
			<logic:equal name="unit" property="siteAvailable" value="true">
				<bean:define id="researchUnit" name="unit" toScope="request"/>
				<jsp:include flush="true" page="viewMembersFromUnit.jsp"></jsp:include>
			</logic:equal>
			<logic:equal name="unit" property="siteAvailable" value="false">
				<p><em><bean:message key="label.noMembersDefined" bundle="SITE_RESOURCES"/></em></p>
			</logic:equal>	
		</logic:equal>
	</logic:iterate>

</logic:notEmpty>


