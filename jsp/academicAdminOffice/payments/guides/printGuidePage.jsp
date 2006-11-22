<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<%-- The Original Payment-Guide --%>
	<jsp:include page="./printGuideTemplatePage.jsp" flush="true" />

	<%-- Copy of Payment-Guide --%>
	<jsp:include page="./printGuideTemplatePage.jsp" flush="true" />

</logic:present>
	