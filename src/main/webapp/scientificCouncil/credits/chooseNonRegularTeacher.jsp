<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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