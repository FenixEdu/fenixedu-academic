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
<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="teacherService" property="teacher.person.username"/></bean:define>
<table class="headerTable"><tr>	
<td><img src="<%= request.getContextPath() + url %>" /></td>
<td >

<fr:view name="teacherService">
	<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.TeacherService">
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
		<fr:schema type="net.sourceforge.fenixedu.domain.teacher.ReductionService" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
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
	<fr:create action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>" type="net.sourceforge.fenixedu.domain.teacher.ReductionService"
	schema="create.reductionService">
		<fr:hidden slot="teacherService" name="teacherService"/>
		<fr:layout name="flow">
			<fr:property name="labelTerminator" value=""/>
			<fr:property name="labelStyle" value="font-weight: bold;" />
		</fr:layout>
	</fr:create>
</logic:notPresent>
