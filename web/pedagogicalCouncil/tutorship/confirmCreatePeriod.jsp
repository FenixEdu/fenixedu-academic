<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="label.tutorshipSummaryPeriod" bundle="APPLICATION_RESOURCES" /></h2>

<div class="infoop2">
<p><bean:message key="message.tutorshipSummaryPeriod.confirm" bundle="PEDAGOGICAL_COUNCIL" /></p>
<p>
<html:link page="/tutorshipSummary.do?method=searchTeacher">[voltar]
</html:link></p>
</div>