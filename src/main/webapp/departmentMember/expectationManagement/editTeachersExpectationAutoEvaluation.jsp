<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.departmentMember" bundle="DEPARTMENT_MEMBER_RESOURCES"/></em>
<h2><bean:message key="label.autoEvaluation"/></h2>

<logic:present role="DEPARTMENT_MEMBER">

	<logic:notEmpty name="expectation">
		
		<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="expectation" property="executionYear.year"/></em></p>
				
		<bean:define id="executionYearId" name="expectation" property="executionYear.idInternal"/>
		<fr:edit name="expectation" slot="autoEvaluation" layout="rich-text">
			<fr:layout name="rich-text">
				<fr:property name="config" value="intermediateWithBreakLineInsteadOfParagraph"/>
				<fr:property name="columns" value="75"/>
				<fr:property name="rows" value="16"/>
			</fr:layout>
			<fr:destination name="success" path="<%= "/teacherExpectationAutoAvaliation.do?method=show&amp;executionYearId=" + executionYearId %>"/>
			<fr:destination name="cancel" path="<%= "/teacherExpectationAutoAvaliation.do?method=show&amp;executionYearId=" + executionYearId %>"/>
		</fr:edit>
		
	</logic:notEmpty>
	
</logic:present>

