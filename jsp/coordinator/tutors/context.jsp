<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<p>
				<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
					<strong><bean:message key="label.masterDegree.coordinator.selectedDegree"/></strong> 
					<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
					<br />
					<strong><bean:message key="label.masterDegree.coordinator.executionYear"/></strong>
					<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />					
					<br />
				</logic:present>
				
				<!-- Tutor's name and number -->
				<logic:present name="studentsOfTutor">
				<logic:notEmpty name="studentsOfTutor">
					<strong><bean:message key="label.tutor"/>:&nbsp;</strong>
					<logic:iterate id="teacher" name="studentsOfTutor" length="1">
						<bean:write name="teacher" property="infoTeacher.infoPerson.nome" />
						&nbsp;-&nbsp;
						<bean:write name="teacher" property="infoTeacher.teacherNumber" />
					</logic:iterate>
				</logic:notEmpty>
				</logic:present>
			</p>
		</td>
	</tr>
</table>