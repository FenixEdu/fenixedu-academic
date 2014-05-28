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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<p>
	<bean:message key="label.organization" bundle="SITE_RESOURCES"/> | <html:link href="<%= request.getContextPath() + "/publico/researchSite/viewResearchUnitSite.do?siteID=" + site.getExternalId() + "&method=showResearchers"%>"><bean:message key="label.members" bundle="SITE_RESOURCES"/></html:link>
</p>

<h2>
    <bean:message key="label.organization" bundle="SITE_RESOURCES"/> 
</h2>

<div>
	<fr:view name="site" property="unit" layout="organigram-with-card">
		<fr:layout>
			<fr:property name="classes" value="ostructure1"/>
			<fr:property name="rootUnitClasses" value="osrootunit"/>
			<fr:property name="unitClasses" value="osunit"/>
			<fr:property name="levelClasses" value="oslevel"/>
			<fr:property name="employeesSectionClasses" value="osemployees"/>
			<fr:property name="employeesClasses" value="osperson osworker"/>
			<fr:property name="functionsClasses" value="osfunction"/>
			<fr:property name="implicitFunctionsClasses" value="osfunction osimplicit"/>
			<fr:property name="membersClasses" value="osperson osmember nobullet"/>
			
			
			<fr:property name="showDates" value="false"/>
			<fr:property name="unitShown" value="false"/>
			<fr:property name="cardProperty(subLayout)" value="values-as-list"/>
			<fr:property name="cardProperty(subSchema)" value="present.research.member"/>
		</fr:layout>	
	</fr:view>
</div>