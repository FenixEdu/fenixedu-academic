<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
    <li><html:link page="/extraWork.do"><bean:message key="link.extraWork" /></html:link>
    	<ul>
    		<li><html:link page="/managementMoneyCostCenter.do?method=prepareYear"><bean:message key="link.extraWork.money.cost.center" /></html:link>
    		<li><html:link page="/consultSheetExtraWork.do?method=prepare"><bean:message key="link.extraWork.sheet" /></html:link>
    	</ul>    
    </li>
</ul>   