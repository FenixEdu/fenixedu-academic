<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="infoTeacher" name="teacherCreditsSheet" property="infoTeacher"/>
<bean:define id="infoExecutionPeriod" name="teacherCreditsSheet" property="infoExecutionPeriod"/>

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" /></b> <bean:write name="infoExecutionPeriod" property="name"/>
</p>
<p class="infoop">
<span class="emphasis-box">1</span><bean:message key="label.teacherCreditsSheet.degreeFinalProjectStudents"/>
</p>
<p class="infoop">
<span class="emphasis-box">2</span><bean:message key="label.teacherCreditsSheet.institutionWorkingTime"/>
</p>
<p class="infoop">
<span class="emphasis-box">3</span><bean:message key="label.teacherCreditsSheet.masterDegreeProfessorships"/>
</p>
<p class="infoop">
<span class="emphasis-box">4</span><bean:message key="label.teacherCreditsSheet.supportLessons"/>
</p>
<p class="infoop">
<span class="emphasis-box">5</span><bean:message key="label.teacherCreditsSheet.shiftProfessorships"/>
</p>
