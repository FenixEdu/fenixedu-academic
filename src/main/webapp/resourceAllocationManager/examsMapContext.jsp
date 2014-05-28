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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>	
<logic:present name="<%= PresentationConstants.EXECUTION_DEGREE %>"  >
	<bean:define id="infoDegree" name="<%= PresentationConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree"scope="request" />
   	<bean:define id="infoExecutionPeriod" name="<%= PresentationConstants.EXECUTION_PERIOD %>" scope="request"/>
	<jsp:getProperty name="infoDegree" property="degree.presentationName" />
	<br/>
	<jsp:getProperty name="infoExecutionPeriod" property="name"/> -
	<bean:write name="infoExecutionPeriod" property="infoExecutionYear.year"/>
</logic:present>