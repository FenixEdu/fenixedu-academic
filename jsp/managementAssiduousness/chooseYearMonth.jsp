<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.exportWorkSheets" /></h2>
<p class="mtop2"><span class="error0"><html:errors /></span></p>

<fr:form action="/exportAssiduousness.do?method=exportToPDFWorkDaySheet">
	<fr:edit id="yearMonth" name="yearMonth" schema="choose.date">
		<fr:layout>
			<fr:property name="classes" value="thlight thright" />
		</fr:layout>
	</fr:edit>
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
		<bean:message key="button.export" />
	</html:submit></p>
</fr:form>
<br/><br/><br/>
<h2><bean:message key="link.exportWorkSheets" /></h2>
<span class="error0 mtop0"><html:messages id="message" message="true">
	<bean:write name="message" />
	<br />
</html:messages></span>
<fr:form action="/exportAssiduousness.do?method=exportToPDFChoosedWorkDaySheet">
	<fr:edit id="workDaySheetToExportSearch" name="workDaySheetToExportSearch" schema="choose.workDaySheetToExportSearch">
		<fr:layout>
			<fr:property name="classes" value="thlight thright" />
		</fr:layout>
	</fr:edit>
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
		<bean:message key="button.export" />
	</html:submit></p>
</fr:form>
