<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message" filter="false" />
	</span>
</html:messages>

<logic:present name="teacherServiceComment">
	<bean:define id="teacherService" name="teacherServiceComment" property="teacherService"/>
</logic:present>

<h3><bean:message key="label.notes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<jsp:include page="../teacherCreditsStyles.jsp"/>

<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="teacherService" property="teacher.person.username"/></bean:define>
<table class="headerTable"><tr>	
<td><img src="<%= request.getContextPath() + url %>" /></td>
<td >

<fr:view name="teacherService">
	<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.TeacherService">
		<fr:slot name="teacher.person.presentationName" key="label.name"/>
		<fr:slot name="executionPeriod" key="label.period" layout="format">
			<fr:property name="format" value="${executionYear.year}" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
   		<fr:property name="classes" value="creditsStyle"/>
	</fr:layout>
</fr:view>

</td></tr></table>


<bean:define id="executionYearOid" name="teacherService" property="executionPeriod.executionYear.externalId"/>
<bean:define id="teacherOid" name="teacherService" property="teacher.externalId"/>

<logic:present name="teacherServiceComment">
	<fr:edit id="teacherServiceComment" name="teacherServiceComment" action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>">
		<fr:schema type="net.sourceforge.fenixedu.domain.teacher.TeacherServiceComment" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
			<fr:slot name="content" key="label.comment" layout="longText">
				<fr:property name="columns" value="60" />
				<fr:property name="rows" value="5" />
			</fr:slot>
		</fr:schema>
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>"/>
	</fr:edit>
</logic:present>

<logic:notPresent name="teacherServiceComment">
	<fr:create action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>" type="net.sourceforge.fenixedu.domain.teacher.TeacherServiceComment"
	schema="create.teacherServiceComment">
		<fr:hidden slot="teacherService" name="teacherService"/>
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
		</fr:layout>
	</fr:create>
</logic:notPresent>
