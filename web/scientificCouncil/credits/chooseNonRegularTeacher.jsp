<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error0">
	<html:messages id="message" message="true">
		<bean:write name="message" />
	</html:messages>
</span>
		
<logic:present name="nonRegularTeacherBean">
	<fr:edit id="nonRegularTeacherBean" name="nonRegularTeacherBean" action="/manageNonRegularTeachingService.do?method=showNonRegularTeachingService">
		<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.teacher.credits.NonRegularTeacherBean">
			<fr:slot name="username"/>
		</fr:schema>
	</fr:edit>

</logic:present>