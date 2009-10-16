<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.listErrors"/></h2>

<h3><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.selectDay"/></h3>

<fr:form action="/errorReport.do?method=list">
	<fr:edit name="bean" id="bean" type="net.sourceforge.fenixedu.presentationTier.Action.manager.ErrorLogDispatchAction$RequestLogDayBean" schema="YearMonthDay">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thmiddle mbottom1 mtop05"/>
		</fr:layout>
	</fr:edit>
</fr:form>