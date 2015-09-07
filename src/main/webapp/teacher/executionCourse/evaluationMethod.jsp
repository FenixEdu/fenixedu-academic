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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/includeMathJax.jsp" />
<h2><bean:message key="link.evaluationMethod" /></h2>

<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>


<div class="infoop2">
	<bean:message key="label.evaluationMethod.explanation" />
</div>


<logic:present name="executionCourse">
	<bean:define id="professorship" name="executionCourse" property="professorshipForCurrentUser"/>
	<bean:define id="professorshipPermissions" name="professorship" property="permissions"/>
	
	<logic:equal name="professorshipPermissions" property="evaluationMethod" value="true">
	<ul class="mvert1">
		<li>
			<html:link page="/manageEvaluationMethod.do?method=prepareEditEvaluationMethod" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="button.edit"/>
			</html:link>
		</li>
		<li>
			<html:link page="/manageEvaluationMethod.do?method=prepareImportEvaluationMethod&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.import.evaluationMethod"/>
			</html:link>
		</li>
	</ul>
	</logic:equal>

    <c:forEach var="lang" items="${evaluationMethods.locales}">
        <h3 class="mtop15">
            ${lang.displayName}
        </h3>

        <blockquote>
            ${evaluationMethods.getContent(lang)}
        </blockquote>
    </c:forEach>

</logic:present>
