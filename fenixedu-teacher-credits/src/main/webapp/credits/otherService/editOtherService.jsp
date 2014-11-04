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

<logic:present role="role(SCIENTIFIC_COUNCIL)">

<h3><bean:message key="label.teacherCreditsSheet.otherTypeCreditLines" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<jsp:include page="../teacherCreditsStyles.jsp"/>
<logic:present name="otherService">
	<bean:define id="teacherService" name="otherService" property="teacherService"/>
</logic:present>


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

<fr:hasMessages><p><span class="error0"><fr:messages><fr:message/></fr:messages></span></p></fr:hasMessages>

<bean:define id="executionYearOid" name="teacherService" property="executionPeriod.executionYear.externalId"/>
<bean:define id="teacherOid" name="teacherService" property="teacher.externalId"/>

	<logic:present name="otherService">
		<fr:edit id="otherService" name="otherService" action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>" schema="edit.otherService">
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
			</fr:layout>
		</fr:edit>
	</logic:present>
	<logic:notPresent name="otherService">
		<fr:create action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>" type="org.fenixedu.academic.domain.teacher.OtherService"
		schema="create.otherService">
		<fr:hidden slot="teacherService" name="teacherService"/>
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
		</fr:layout>
	</fr:create>
	</logic:notPresent>	

</logic:present>