<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>
<!-- Import new CSS for this section: #navlateral  -->
<ul>
	<%-- 
	<li><html:link page="/assiduousnessRecords.do?method=showEmployeeInfo">
		<bean:message key="link.employeeInfo" />
	</html:link></li>
	--%>
	<%-- Não descomentar este link
	<li><html:link
		page="/assiduousnessRecords.do?method=prepareChooseDate&amp;action=showWorkSheet">
		<bean:message key="link.workSheet" />
	</html:link></li>
	--%>
	<%-- 
	<li><html:link
		page="/assiduousnessRecords.do?method=prepareChooseDate&amp;action=showClockings">
		<bean:message key="link.clockings" />
	</html:link></li>
	<li><html:link
		page="/assiduousnessRecords.do?method=prepareChooseDate&amp;action=showJustifications">
		<bean:message key="link.justifications" />
	</html:link></li>
	--%>
</ul>
