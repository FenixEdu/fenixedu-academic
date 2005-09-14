<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<li><html:link page="/findPerson.do?method=prepareFindPerson&page=0"><bean:message key="link.operator.newPassword" /></html:link></li>
<li><html:link page="/submitPhoto.do?method=preparePhotoUpload&page=0"><bean:message key="link.operator.submitPhoto" /></html:link></li>
<li><html:link page="/generateNewStudentsPasswords.do?method=prepareGeneratePasswords&page=0"><bean:message key="link.newPasswordForStudentRegistration" bundle="MANAGER_RESOURCES"/></html:link></li>