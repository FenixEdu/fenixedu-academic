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
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<logic:present name="siteView" >
	<bean:define id="component" name="siteView" property="commonComponent" />
	<bean:define id="curricularCourses" name="component" property="associatedDegrees" />
	<div id="associated-degrees">
		<bean:define id="curricularCoursesFiltered" value="" />		
		<logic:iterate id="curricularCourse" name="curricularCourses">
			<bean:define id="curricularCourseSigla" name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla" />
			<logic:notMatch name="curricularCoursesFiltered" value="<%= curricularCourseSigla.toString() %>" >	
				<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.degree.presentationName" /><br />			
				<bean:define id="curricularCoursesFiltered" value="<%= curricularCoursesFiltered.toString().concat(curricularCourseSigla.toString()) %>" />
			</logic:notMatch>
		</logic:iterate>
	</div>
</logic:present>
