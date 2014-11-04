<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.messaging.MessagingApplication$OrganizationalStructurePage" />

<f:view>

<script type="text/javascript">
function check(e,v)
{

	if (e.style.display == "none")
	  {
	  e.style.display = "";
	  v.src = "<%= request.getContextPath() %>/images/toggle_minus10.gif";
	  }
	else
	  {
	  e.style.display = "none";
	  v.src = "<%= request.getContextPath() %>/images/toggle_plus10.gif";
	  }
}
</script>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<f:loadBundle basename="resources/DegreeAdministrativeOfficeResources" var="bundleDegreeAdministrativeOffice"/>

	<h:outputText value="<h2>#{organizationalStructure.instituitionName}</h2>" escape="false"/>
	<h:outputText value="#{organizationalStructure.units}<br/>" escape="false"/>

</f:view>
