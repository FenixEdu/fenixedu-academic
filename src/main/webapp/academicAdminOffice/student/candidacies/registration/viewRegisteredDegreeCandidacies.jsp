<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.registeredDegreeCandidacies.first.time.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<jsp:include page="viewRegisteredDegreeCandidacies_bd.jsp" />
