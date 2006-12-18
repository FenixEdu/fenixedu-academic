<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="selectedId" name="teacherServiceDistribution" property="idInternal"/>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + selectedId %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
<%--		(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>�<bean:message key="label.common.courseSemester"/>--%>
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.omissionValuesValuation"/>
</h3>


<ul>
<li><html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + selectedId %>'>
	<bean:message key="link.back"/>
</html:link>
</li>
<li>
<html:link page="<%= "/valuationPhasesManagement.do?method=prepareForOmissionValuesValuation&edit=yes&teacherServiceDistribution=" +  selectedId %>">
	<bean:message key="link.edit"/>
</html:link>
</li>
</ul>

<bean:define id="edit" value="<%= String.valueOf(request.getParameter("edit")) %>"/>

<logic:notEqual name="edit" value="yes">
<fr:view name="currentValuationPhase" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase" schema="omissionValuesValuation.details">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle1"/>
        <fr:property name="columnClasses" value=",,error"/>
    </fr:layout>
</fr:view>
</logic:notEqual>

<logic:equal name="edit" value="yes">
<fr:form action="<%= "/valuationPhasesManagement.do?method=prepareForOmissionValuesValuation&edit=no&teacherServiceDistribution=" + selectedId %>" >
<fr:edit name="currentValuationPhase" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase" schema="omissionValuesValuation.details">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle1"/>
        <fr:property name="columnClasses" value=",,error"/>
    </fr:layout>
</fr:edit>
<html:submit> 
<bean:message key="button.submit"/>
</html:submit>
</fr:form>
</logic:equal>
