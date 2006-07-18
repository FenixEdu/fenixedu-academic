<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li class="navheader"><bean:message key="link.consult" /></li>
	<li><html:link page="/viewAssiduousness.do?method=chooseEmployee&amp;action=showWorkSheet"><bean:message key="link.showEmployeeWorkSheet" /></html:link></li>
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