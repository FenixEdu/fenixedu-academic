<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
	<li class="navheader"><bean:message key="link.employeeConsult" /></li>
	<li><html:link page="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showWorkSheet"><bean:message key="link.workSheet" /></html:link></li>
	<li><html:link page="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showSchedule"><bean:message key="label.schedule" /></html:link></li>
	<li><html:link page="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showClockings"><bean:message key="link.clockings" /></html:link></li>
	<li><html:link page="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showJustifications"><bean:message key="link.justifications" /></html:link></li>
	<li class="navheader"><bean:message key="link.consult" /></li>
	<li><html:link page="/viewAssiduousness.do?method=showSchedules"><bean:message key="link.schedules" /></html:link></li>
	<li><html:link page="/viewAssiduousness.do?method=showJustificationMotives"><bean:message key="link.justifications" /></html:link></li>
	<li><html:link page="/viewAssiduousness.do?method=showRegularizationMotives"><bean:message key="link.regularizations" /></html:link></li>
	<li class="navheader"/>
    <li><html:link page="/exportAssiduousness.do?method=chooseYearMonth"><bean:message key="link.exportWorkSheets" /></html:link></li>
    <%-- 
    <li><bean:message key="link.consult" />
    	<ul>
			<li><html:link page="/workByEmployee.do?method=prepareInputs"><bean:message key="link.work.employee.sheet" /></html:link></li>
    	</ul>    
    </li>
    <li><bean:message key="link.extraWork" />
    	<ul>
    		<li><html:link page="/managementMoneyCostCenter.do?method=prepareYear"><bean:message key="link.extraWork.money.cost.center" /></html:link></li>
    		<li><html:link page="/requestsExtraWork.do?method=prepareInputs"><bean:message key="link.extraWork.requests" /></html:link></li>
			<li><html:link page="/extraWorkByEmployee.do?method=prepareInputs"><bean:message key="link.extraWork.employee" /></html:link></li>
    	</ul>    
    </li>
    --%>
</ul>   