<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

	<%-- The Original Payment-Receipt --%>
	<jsp:include page="./printReceiptTemplatePage.jsp" flush="true" />

	<%-- Copy of Payment-Receipt --%>
	<jsp:include page="./printReceiptTemplatePage.jsp" flush="true" />
