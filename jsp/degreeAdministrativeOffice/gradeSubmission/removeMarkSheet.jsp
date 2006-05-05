<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:form action="/markSheetManagement.do?method=deleteMarkSheet">
	
	<html:hidden property="epID" />
	<html:hidden property="dID" />
	<html:hidden property="dcpID" />
	<html:hidden property="ccID"  />	
	<html:hidden property="msID" />
	<html:hidden property="tn" />
	<html:hidden property="ed"/>
	<html:hidden property="mss" />
	<html:hidden property="mst" />
	
	<fr:view name="markSheet" schema="markSheet.view" layout="tabular" />
	<br/><br/>
	<bean:message key="label.markSheet.removeMarkSheet"/>
	<br/><br/>	
	<html:submit> <bean:message key="label.markSheet.yes"/> </html:submit>
	<html:cancel> <bean:message key="label.markSheet.no"/> </html:cancel>
	
</html:form>