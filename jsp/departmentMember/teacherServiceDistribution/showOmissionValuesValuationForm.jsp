<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<style>
table.vtsbc {
margin-bottom: 1em;
border: 2px solid #aaa;
text-align: center;
border-collapse: collapse;
}
table.vtsbc th {
padding: 0.2em 0.2em;
border: 1px solid #bbb;
border-bottom: 1px solid #aaa;
background-color: #f4f4f8;
font-weight: bold;
text-align: left;
}
table.vtsbc td {
background-color: #eaeaea;
border: none;
border: 1px solid #ccc;
padding: 0.25em 0.5em;
}
table.vtsbc td.error {
color: red; 
padding: 0.25em 0.25em;
}
</style>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
<%--		(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>º<bean:message key="label.common.courseSemester"/>--%>
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.omissionValuesValuation"/>
</h3>

<fr:edit name="currentValuationPhase" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase" schema="omissionValuesValuation.details">
	<fr:layout name="tabular">
        <fr:property name="classes" value="vtsbc"/>
        <fr:property name="columnClasses" value=",,error"/>
    </fr:layout>
</fr:edit>

<br/>
<br/>

<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
