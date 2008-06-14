<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<h2><bean:message key="title.student.enrolment.specialSeason" bundle="DEGREE_ADM_OFFICE"/></h2>

<logic:messagesPresent message="true">
	<ul>
	<html:messages id="messages" message="true">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
	</ul>
</logic:messagesPresent>
<html:errors/>

<br/>
<br/>
<fr:edit action="/specialSeasonEnrolment.do?method=viewSpecialSeasonEnrolments" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment.SpecialSeasonEnrolmentBean" 
		 schema="specialSeason.choose.student">
		 <fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		 </fr:layout> 
</fr:edit>