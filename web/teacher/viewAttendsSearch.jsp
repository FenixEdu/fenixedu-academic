<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>


<logic:present name="searchBean">
	<h2>Alunos de <bean:write name="searchBean" property="executionCourse.nome" /></h2>
	
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<div class="infoop2">
		<bean:message key="message.students.explanation"/>
	</div>
	<fr:form action="/searchECAttends.do?method=search">
		<fr:edit id="searchBean" name="searchBean" layout="search-execution-course-attends">
			<fr:layout>
				<fr:property name="mailURL" value="/mailSender.do"/>
				<fr:property name="spreadSheetURL" value="/getTabSeparatedStudentList.do"/>
				<fr:property name="searchTableClasses" value="tstyle4 mtop15 tdtop inobullet"/>
				<fr:property name="attendsListTableClasses" value="tstyle4 tdcenter"/>
				<fr:property name="summaryClasses" value="tstyle1 tdcenter mtop05"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
</logic:present>


