<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.listErrors"/></h2>

<h3><bean:message bundle="MANAGER_RESOURCES" key="label.errorList.selectDay"/></h3>

<fr:form action="/errorReport.do?method=list">
	<fr:edit name="bean" id="bean" type="net.sourceforge.fenixedu.presentationTier.Action.manager.ErrorLogDispatchAction$RequestLogDayBean" schema="YearMonthDay">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thmiddle mbottom1 mtop05"/>
		</fr:layout>
	</fr:edit>
</fr:form>