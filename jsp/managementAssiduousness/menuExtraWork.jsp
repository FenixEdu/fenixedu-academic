<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
    <li><html:link page="/extraWork.do"><bean:message key="link.extraWork" /></html:link>
    	<ul>
    		<li><html:link page="/managementMoneyCostCenter.do?method=prepareYear"><bean:message key="link.extraWork.money.cost.center" /></html:link></li>
    		<li><html:link page="/requestsExtraWork.do?method=prepareInputs"><bean:message key="link.extraWork.requests" /></html:link></li>
			<li><html:link page="/extraWorkByEmployee.do?method=prepareInputs"><bean:message key="link.extraWork.employee" /></html:link></li>
    	</ul>    
    </li>
</ul>   