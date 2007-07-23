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

<h2><bean:message key="label.coordinator.tutor.changeEndDates.title" /></h2>

<div class="infoop2">
	<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	
		<!-- SELECTED TEACHER -->
		<p class="mvert025"><b><bean:message key="label.tutor"/>:&nbsp;</b>
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

<ul>
	<li>
		<p class="mtop2 mbottom1"><html:link page="<%= "/tutorManagement.do?method=prepare&forwardTo=readTutor&" +  parameters %>">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.back" /></html:link></p>
	</li>
</ul>

<!-- AVISOS E ERROS -->
<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
			<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>
<br />

<!-- ALL TUTORSHIPS -->
<logic:present name="allTutorshipsByEntryYearBeans">
	<logic:notEmpty name="allTutorshipsByEntryYearBeans">

		<!-- CHANGE ALL TUTORSHIPS END DATES -->
		<b><bean:message key="label.coordinator.tutor.changeEndDates.allTutorships" /></b>
		<br />
		<p class="color888 mvert05"><bean:message key="label.coordinator.tutor.changeEndDates.allTutorships.help" /></p>
		<fr:form action="/changeTutorship.do?method=changeAllTutorshipsEndDates">
			<fr:edit id="allTutorshipsByEntryYearBeans" name="allTutorshipsByEntryYearBeans" visible="false"/>
			<fr:edit id="tutorshipManagementBean" name="tutorshipManagementBean" schema="coordinator.tutor.endDate">
				<fr:destination name="invalid" path="<%= "/changeTutorship.do?method=prepareChangeTutorshipsEndDates&" +  parameters %>" />
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom0" />
					<fr:property name="columnClasses" value="width110px,width160px,tdclear tderror1" />
				</fr:layout>
			</fr:edit>
			<table class="tstyle5 gluetop mtop0">
				<tr>
					<td class="width110px"></td>
					<td class="width160px">
						<html:submit><bean:message key="label.update"bundle="APPLICATION_RESOURCES" /></html:submit>
						<html:submit property="cancel"><bean:message key="button.coordinator.tutor.cancel" /></html:submit>
					</td>
				</tr>
			</table>
		</fr:form>
	
		<br />

		<!-- CHANGE EACH ACTIVE TUTORSHIP END DATE -->
		<!-- IT SHOWS ALL ACTIVE TUTORSHIPS, ORDERED BY STUDENTS ENTRY YEAR -->
		<logic:present name="activeTutorshipsByEntryYearBeans">
			<logic:notEmpty name="activeTutorshipsByEntryYearBeans">
				<p><b><bean:message key="label.coordinator.tutor.changeEndDates.activeTutorships" /></b></p>
				<p class="color888 mvert05"><bean:message key="label.coordinator.tutor.changeEndDates.activeTutorships.help" /></p>
				<fr:form action="/changeTutorship.do?method=changeTutorshipsEndDates">
					<fr:edit id="tutorshipManagementBean" name="tutorshipManagementBean" visible="false"/>
					<logic:iterate id="changeActiveTutorshipByEntryYearBean" name="activeTutorshipsByEntryYearBeans" indexId="i">
						<ul>
							<li>
								<p class="mtop1 mbottom0">
									<bean:message key="label.studentsEntryYear" />
									<span class="highlight1"><bean:write name="changeActiveTutorshipByEntryYearBean" property="executionYear.year" /></span>
								</p>
							</li>
						</ul>
						<bean:define id="changeActiveTutorshipsBeansList" name="changeActiveTutorshipByEntryYearBean" property="changeTutorshipsBeans" />
						<fr:edit id="<%= "changeActiveTutorshipBean" + i %>" name="changeActiveTutorshipsBeansList" schema="coordinator.tutor.changeTutorshipsEndDates">
							<fr:destination name="invalid" path="<%= "/changeTutorship.do?method=prepareChangeTutorshipsEndDates&" +  parameters %>" />
							<fr:layout name="tabular-row">
								<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0 mbottom1"/>
						  	    <fr:property name="headerClasses" value="acenter"/>
								<fr:property name="columnClasses" value=",width300px aleft,,,,"/>
							</fr:layout>
						</fr:edit>
					</logic:iterate>
					<html:submit ><bean:message key="label.update" /></html:submit>
					<html:submit property="cancel"><bean:message key="button.coordinator.tutor.cancel" /></html:submit>
				</fr:form>
			</logic:notEmpty>
		</logic:present>
			
		<!-- CHANGE EACH PAST TUTORSHIP END DATE -->
		<!-- IT SHOWS ALL PAST TUTORSHIPS, ORDERED BY STUDENTS ENTRY YEAR -->
		<logic:present name="pastTutorshipsByEntryYearBeans">
			<logic:notEmpty name="pastTutorshipsByEntryYearBeans">
				<p class="mtop3"><b><bean:message key="label.coordinator.tutor.changeEndDates.pastTutorships" /></b></p>
				<p class="color888 mvert05"><bean:message key="label.coordinator.tutor.changeEndDates.pastTutorships.help" /></p>
				<fr:form action="/changeTutorship.do?method=changeTutorshipsEndDates">
					<fr:edit id="tutorshipManagementBean" name="tutorshipManagementBean" visible="false"/>
					<logic:iterate id="changePastTutorshipByEntryYearBean" name="pastTutorshipsByEntryYearBeans" indexId="i">
						<ul>
							<li>
								<p class="mtop1 mbottom0">
									<bean:message key="label.studentsEntryYear" />
									<span class="highlight1"><bean:write name="changePastTutorshipByEntryYearBean" property="executionYear.year" /></span>
								</p>
							</li>
						</ul>
						<bean:define id="changePastTutorshipsBeansList" name="changePastTutorshipByEntryYearBean" property="changeTutorshipsBeans" />
						<fr:edit id="<%= "changePastTutorshipBean" + i %>" name="changePastTutorshipsBeansList" schema="coordinator.tutor.changeTutorshipsEndDates">
							<fr:destination name="invalid" path="<%= "/changeTutorship.do?method=prepareChangeTutorshipsEndDates&" +  parameters %>" />
							<fr:layout name="tabular-row">
								<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0 mbottom1"/>
						  	    <fr:property name="headerClasses" value="acenter"/>
								<fr:property name="columnClasses" value=",width300px aleft,,,,"/>
							</fr:layout>
						</fr:edit>
					</logic:iterate>
					<html:submit ><bean:message key="label.update" /></html:submit>
					<html:submit property="cancel"><bean:message key="button.coordinator.tutor.cancel" /></html:submit>
				</fr:form>
			</logic:notEmpty>
		</logic:present>
	</logic:notEmpty>
	<logic:empty name="allTutorshipsByEntryYearBeans">
		<p class="mtop1"><span class="error"><bean:message key="label.coordinator.tutor.emptyStudentsList" /></span></p>
	</logic:empty>
</logic:present>

