<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="infoExecutionDegreesNamesList" name="<%= SessionConstants.DEGREE_LIST %>" scope="request"/>
<bean:define id="yearsList" name="<%= SessionConstants.ENROLMENT_YEAR_LIST_KEY %>" scope="request"/>
<bean:define id="semestersList" name="<%= SessionConstants.ENROLMENT_SEMESTER_LIST_KEY %>"  scope="request"/>
<h2><bean:message key="title.student.enrolment.without.rules" bundle="DEGREE_ADM_OFFICE"/></h2>
<span class="error"><html:errors/></span>
<br />
<html:form action="/courseEnrolmentWithoutRulesManagerDA">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readCoursesToEnroll"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.userType" property="userType"/>

	<table border="0">
		<!-- Degrees -->
		<tr>
			<td colspan="2" align="left"><strong><bean:message key="label.choose.degree"/></strong></td>
		</tr>
		<tr>
			<td colspan="2" align="left">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegree" property="executionDegree" size="1">
					<html:options collection="infoExecutionDegreesNamesList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr>
			<td align="left"><strong><bean:message key="label.choose.year"/></strong></td>
			<td align="left"><strong><bean:message key="label.choose.semester"/></strong></td>
		</tr>
		<tr>
			<td valign="top">
				<!-- Years -->
				<table border="0">
					<logic:iterate id="year" name="yearsList">
						<tr>
							<td>
								<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularYears" property="curricularYears">
									<bean:write name="year"/>
								</html:multibox>
							</td>
							<td>
								<bean:write name="year"/>&nbsp;<bean:message key="label.number.year" />
							</td>
						</tr>
					</logic:iterate>		
				</table>
			</td>
			<td valign="top">
				<!-- Semesters -->
				<table border="0">
					<logic:iterate id="semester" name="semestersList">
						<tr>
							<td>
								<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularSemesters" property="curricularSemesters">
									<bean:write name="semester"/>
								</html:multibox>
							</td>
							<td>
								<bean:write name="semester"/>&nbsp;<bean:message key="label.number.semester" />
							</td>
						</tr>
					</logic:iterate>		
				</table>
			</td>			
		</tr>
	</table>	
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.submit.degree.type.and.student"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="button.clean"/>
	</html:reset>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='readEnrollments';this.form.submit();">
		<bean:message key="button.cancel"/>
	</html:cancel>
</html:form>