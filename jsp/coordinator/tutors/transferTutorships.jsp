<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<bean:define id="teacherNumber" name="tutorshipManagementBean" property="teacherNumber" />
<bean:define id="executionDegreeId" name="tutorshipManagementBean" property="executionDegreeID" />
<bean:define id="degreeCurricularPlanID" name="tutorshipManagementBean" property="degreeCurricularPlanID" />
<bean:define id="parameters" value="<%= "teacherNumber=" + teacherNumber + "&executionDegreeId=" + executionDegreeId + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>" />

<h2><bean:message key="label.coordinator.tutor.transferTutorshipTitle" /></h2>

<div class="infoop2">
	<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	
		<!-- SELECTED TEACHER -->
		<p class="mvert025"><b><bean:message key="label.tutor" />:&nbsp;</b>
			<bean:write name="tutorshipManagementBean" property="teacher.person.name" />
			<bean:write name="tutorshipManagementBean" property="teacher.teacherNumber" />
		</p>
		<!-- CURRENT EXECUTION YEAR -->
		<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
		<p class="mvert025">
			<b><bean:message key="label.masterDegree.coordinator.executionYear"/></b>
			<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />
		</p>
	</logic:present>
</div>


<!-- AVISOS E ERROS -->
<span class="error"><!-- Error messages go here --> <html:errors />
</span>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
			<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<br />

<!-- TUTORSHIP INFORMATION -->
<b><bean:message key="label.coordinator.tutor.tutorshipInfo.tutorshipHistory" /></b>
<br />
<fr:view name="tutorshipManagementBean" schema="coordinator.tutor.tutorshipInfo">
	<fr:layout name="tabular">
   	    <fr:property name="classes" value="tstyle2 thlight thright mbottom2"/>
    </fr:layout>
</fr:view>

<logic:present name="tutorshipsToTransfer">
	<logic:iterate id="manageTutorshipBean" name="tutorshipsToTransfer">
		<logic:notEmpty name="manageTutorshipBean" property="studentsList" >
			<p class="mtop1"><b><bean:message key="label.studentsEntryYear" /></b>&nbsp;
				<bean:write name="manageTutorshipBean" property="executionYear.year" /></p>
			<bean:define id="students" name="manageTutorshipBean" property="studentsList" />
			<fr:view name="students" layout="tabular" schema="coordinator.tutor.tutorship" >
				<fr:layout>
					<fr:property name="classes" value="tstyle1 mtop0"/>
					<fr:property name="columnClasses" value="acenter,acenter width15em,acenter,acenter width6em"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:iterate>
</logic:present>

<br />

<!-- TRNSFER THE TUTORSHIP TO ANOTHER TUTOR FORM -->
<!-- Action closes the current tutorship (inactive) and creates a new one (active) -->
<b><bean:message key="label.coordinator.tutor.transferTutorship" /></b>
<br />
<p class="color888 mvert05"><bean:message key="label.coordinator.tutor.transferTutorship.help" /></p>
<fr:form action="/tutorshipManagement.do?method=transferTutorship">
	<fr:edit id="tutorshipsToTransferBean" name="tutorshipsToTransfer" visible="false" />
	<fr:edit id="tutorshipManagementBean" name="tutorshipManagementBean" visible="false" />
	<fr:edit id="targetTutorBean" name="targetTutorManagementBean" schema="coordinator.tutor.transferTutorship">
		<fr:destination name="invalid" path="<%= "/tutorshipManagement.do?method=manageTutorships&transfer=true&" +  parameters %>" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thleft thmiddle mtop0 mbottom0" />
			<fr:property name="columnClasses" value="width125px,width250px,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
	<table class="tstyle5 gluetop mtop0">
	<tr>
	<td class="width125px"></td>
	<td class="width250px">
		<html:submit><bean:message key="button.coordinator.tutor.transferTutorship"bundle="APPLICATION_RESOURCES" /></html:submit>
	</td>
	</tr>
	</table>
</fr:form>

<ul>
	<li><p class="mtop1 mbottom2">
		<html:link target="_blank" page="<%= "/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutorHistory&" + parameters %>">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.tutor.chooseTutorFromListLink" /></html:link></p>
	</li>
</ul>
	

