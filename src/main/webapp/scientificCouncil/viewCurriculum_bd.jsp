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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<logic:present name="component" property="infoCurriculum">
<bean:define id="infoCurriculum" name="component" property="infoCurriculum"/>
<logic:notEmpty name="infoCurriculum" property="program">
<h2><bean:message key="label.program"/></h2>
<bean:write name="infoCurriculum" property="program" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="programEn">
<h2><bean:message key="label.program.eng"/></h2>
<bean:write name="infoCurriculum" property="programEn" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="operacionalObjectives">
<h2><bean:message key="label.operacionalObjectives"/></h2>
<bean:write name="infoCurriculum" property="operacionalObjectives" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="operacionalObjectivesEn">
<h2><bean:message key="label.operacionalObjectives.eng"/></h2>
<bean:write name="infoCurriculum" property="operacionalObjectivesEn" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="generalObjectives">
<h2><bean:message key="label.generalObjectives"/></h2>
<bean:write name="infoCurriculum" property="generalObjectives" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="generalObjectivesEn">
<h2><bean:message key="label.generalObjectives.eng"/></h2>
<bean:write name="infoCurriculum" property="generalObjectivesEn" filter="false"/>
</logic:notEmpty>

<logic:equal name="infoCurriculum" property="infoCurricularCourse.basic" value="true">
<bean:define id="curriculumId" name="infoCurriculum" property="externalId"/>
<br/>
<br/>
<br/>
<div class="gen-button">
	<html:link page="<%= "/curricularCourseManager.do?method=prepareEditCurriculum&index=" + pageContext.findAttribute("curriculumId")%>">
		<bean:message key="button.edit"/>
	</html:link>
</div>
</logic:equal>
</logic:present>
<logic:notPresent name="component" property="infoCurriculum">
<bean:define id="infoCurricularCourse" name="component" property="infoCurricularCourse"/>
<bean:define id="curricularCourseId" name="infoCurricularCourse" property="externalId"/>
<bean:message key="message.curriculum.notAvailable"/>
<br/>
<br/>
<br/>
<logic:equal name="infoCurricularCourse" property="basic" value="true">
<div class="gen-button">
	<html:link page="<%= "/curriculumManager.do?method=prepareInsertCurriculum&index=" + pageContext.findAttribute("curricularCourseId")%>">
		<bean:message key="button.insert"/>
	</html:link>
</div>
</logic:equal>
</logic:notPresent>