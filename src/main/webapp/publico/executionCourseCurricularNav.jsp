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
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="curricularCourses" name="component" property="associatedDegrees" />

<h2 class="brown"><bean:message key="label.curricular.information"/></h2>
<ul>
	<logic:iterate id="curricularCourse" name="curricularCourses">
		<bean:define id="curricularCourseId" name="curricularCourse" property="externalId" />
		<bean:define id="degreeID" name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.externalId" />
		<bean:define id="degreeCurricularPlanID" name="curricularCourse" property="infoDegreeCurricularPlan.externalId" />

		<bean:define id="initialDate" name="curricularCourse" property="infoDegreeCurricularPlan.initialDate" />
		
		<li><html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseId") + "&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  pageContext.getAttribute("degreeID") %>"> 	
			<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>	
			<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
		</html:link></li>
	</logic:iterate>
</ul>

