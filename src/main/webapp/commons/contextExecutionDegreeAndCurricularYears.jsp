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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<jsp:include page="contextExecutionDegree.jsp"/>

<logic:present name="<%= PresentationConstants.CURRICULAR_YEARS_LIST %>" scope="request">
	<logic:iterate id="year" name="<%= PresentationConstants.CURRICULAR_YEARS_LIST %>" scope="request">
		<logic:equal name="year" value="1">
			<bean:define id="curricularYears_1"
						 value="1"
						 toScope="request"/>
		</logic:equal>
		<logic:equal name="year" value="2">
			<bean:define id="curricularYears_2"
						 value="2"
						 toScope="request"/>
		</logic:equal>
		<logic:equal name="year" value="3">
			<bean:define id="curricularYears_3"
						 value="3"
						 toScope="request"/>
		</logic:equal>
		<logic:equal name="year" value="4">
			<bean:define id="curricularYears_4"
						 value="4"
						 toScope="request"/>
		</logic:equal>
		<logic:equal name="year" value="5">
			<bean:define id="curricularYears_5"
						 value="5"
						 toScope="request"/>
		</logic:equal>
	</logic:iterate>
</logic:present>