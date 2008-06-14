<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda"%>


<logic:present role="STUDENT">
    <h2><bean:message key="label.enrollment.courses" bundle="STUDENT_RESOURCES"/></h2>
    
	<logic:messagesPresent message="true">
		<ul class="mtop15 mbottom1 nobullet list2">
			<html:messages id="messages" message="true" bundle="STUDENT_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
    
   
</logic:present>

