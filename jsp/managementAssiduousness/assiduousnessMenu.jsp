<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
    <li><bean:message key="link.consult" />
    	<ul>
			<li><html:link page="/workByEmployee.do?method=prepareInputs"><bean:message key="link.work.employee.sheet" /></html:link></li>
    	</ul>    
    </li>
    <li><bean:message key="link.extraWork" />
    	<ul>
    		<li><html:link page="/managementMoneyCostCenter.do?method=prepareYear"><bean:message key="link.extraWork.money.cost.center" /></html:link></li>
    		<li><html:link page="/requestsExtraWork.do?method=prepareRequests"><bean:message key="link.extraWork.requests" /></html:link></li>
			<li><html:link page="/extraWorkByEmployee.do?method=prepareInputs"><bean:message key="link.extraWork.employee" /></html:link></li>
    	</ul>    
    </li>
</ul>   