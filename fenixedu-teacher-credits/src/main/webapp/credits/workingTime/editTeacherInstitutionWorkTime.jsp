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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="institutionWorkTime">
	<bean:define id="teacherService" name="institutionWorkTime" property="teacherService"/>
</logic:present>

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

<p class="mtop2 mbottom05">
	<strong>
	<logic:present name="toCreate">
		<bean:message key="label.teacher-institution-working-time.create"/>
	</logic:present>
	<logic:notPresent name="toCreate">
		<bean:message key="label.teacher-institution-working-time.edit"/>			
	</logic:notPresent>
	</strong>
</p>

<bean:define id="executionYearOid" name="teacherService" property="executionPeriod.executionYear.externalId"/>
<bean:define id="teacherOid" name="teacherService" property="teacher.externalId"/>

<html:messages id="message" message="true">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message" filter="false" />
	</span>
</html:messages>

<fr:hasMessages><fr:messages type="CONVERSION"><p><span class="error0"><fr:message/></span></p></fr:messages></fr:hasMessages>

<logic:present name="institutionWorkTime">
	<fr:edit id="institutionWorkTime" name="institutionWorkTime" action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>"
	schema="edit.institutionWorkTime">
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</logic:present>

<logic:notPresent name="institutionWorkTime">
	<fr:create action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>" type="org.fenixedu.academic.domain.teacher.InstitutionWorkTime"
	schema="create.institutionWorkTime">
		<fr:hidden slot="teacherService" name="teacherService"/>
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:create>
</logic:notPresent>
