<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2 class="mtop15 mbottom025"><bean:message key="label.candidacy" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
<br/>
<jsp:include flush="true" page="/caseHandling/listActivities.jsp" />
