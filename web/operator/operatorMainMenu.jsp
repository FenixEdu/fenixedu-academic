<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul>
<li>
	<html:link page="/findPerson.do?method=prepareFindPerson&page=0"><bean:message key="link.operator.newPassword" /></html:link>
</li>
<li>
	<html:link page="/generateUserUID.do?method=prepareSearchPerson">
		<bean:message key="generate.userUID.title" bundle="MANAGER_RESOURCES"/>
	</html:link>
</li>
<li>
	<html:link page="/submitPhoto.do?method=preparePhotoUpload&page=0"><bean:message key="link.operator.submitPhoto" /></html:link>
</li>
<li>
	<html:link page="/generatePasswordsForCandidacies.do?method=prepareChooseExecutionDegree">
		<bean:message key="link.operator.candidacy.passwords" />
	</html:link>
</li>
</ul>