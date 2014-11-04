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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message" filter="false" />
	</span>
</html:messages>

<logic:present name="reductionService">
	<bean:define id="teacherService" name="reductionService" property="teacherService"/>
</logic:present>

<h3><bean:message key="label.credits.creditsReduction.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<jsp:include page="../teacherCreditsStyles.jsp"/>
<bean:define id="url" type="java.lang.String">/user/photo/<bean:write name="teacherService" property="teacher.person.username"/></bean:define>
<table class="headerTable"><tr>	
<td><img src="<%= request.getContextPath() + url %>" /></td>
<td >

<fr:view name="teacherService">
	<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.teacher.TeacherService">
		<fr:slot name="teacher.person.presentationName" key="label.name"/>
		<fr:slot name="executionPeriod" key="label.period" layout="format">
			<fr:property name="format" value="${name}  ${executionYear.year}" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
   		<fr:property name="classes" value="creditsStyle"/>
	</fr:layout>
</fr:view>

</td></tr></table>

<bean:define id="executionYearOid" name="teacherService" property="executionPeriod.executionYear.externalId"/>
<bean:define id="teacherOid" name="teacherService" property="teacher.externalId"/>
	
<logic:present name="reductionService">
	<br/><br/>
	<fr:edit id="reductionService" name="reductionService" action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>">
		<fr:schema type="org.fenixedu.academic.domain.teacher.ReductionService" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
			<fr:slot name="requestCreditsReduction" key="label.requestCreditsReduction"  layout="radio" required="true">
				<fr:property name="classes" value="nobullet"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="flow">
			<fr:property name="labelTerminator" value=""/>
			<fr:property name="labelStyle" value="font-weight: bold;" />
		</fr:layout>
	</fr:edit>
</logic:present>

<logic:notPresent name="reductionService">
	<br/><br/>
	<fr:create action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>" type="org.fenixedu.academic.domain.teacher.ReductionService"
	schema="create.reductionService">
		<fr:hidden slot="teacherService" name="teacherService"/>
		<fr:layout name="flow">
			<fr:property name="labelTerminator" value=""/>
			<fr:property name="labelStyle" value="font-weight: bold;" />
		</fr:layout>
	</fr:create>
</logic:notPresent>
