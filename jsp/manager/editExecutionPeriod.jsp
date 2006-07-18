<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.execution.periods"/></h2>
<br />

<fr:edit name="executionPeriod"
		type="net.sourceforge.fenixedu.domain.ExecutionPeriod"
		schema="net.sourceforge.fenixedu.domain.ExecutionPeriod.edit">
</fr:edit>
