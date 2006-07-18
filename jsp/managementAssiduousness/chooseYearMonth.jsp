<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<br />
<h2><bean:message key="link.exportWorkSheets" /></h2>
<br />
<p><span class="error0"><html:errors /></span></p>

<fr:form action="/exportAssiduousness.do?method=exportToPDFWorkDaySheet">
	<fr:edit name="yearMonth" schema="choose.date">
		<fr:layout>
			<fr:property name="classes" value="thlight thright" />
		</fr:layout>
	</fr:edit>
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
		<bean:message key="button.export" />
	</html:submit></p>
</fr:form>
