<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoCurricularCourseScope" %>

<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>

<br />
<br />
<span class="error"><html:errors/></span>
<br />
<br />

<logic:present name="infoExecutionDegree" >
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" /><br />
	<bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>:<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" /><br />
</logic:present>

<logic:present name="curricularCourses">
	<h2><bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourseToStudyPlan" /></h2>

	<html:form action="/displayCourseListToStudyPlan?method=chooseCurricularCourses">
   	  	<html:hidden property="page" value="1"/> 
   	  	<html:hidden property="candidateID"/> 
		<!-- CurricularCourse -->
		<table>
			<tr>
				<td>
					<bean:message key="label.givenCredits"/>
				</td>
				<td>
					<html:text property="attributedCredits" size="2"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.givenCreditsRemarks"/>
				</td>
				<td>
					<html:textarea property="givenCreditsRemarks" cols="30" rows="5" />
				</td>
			</tr>
		</table>
		
		<table>
			<logic:present name="candidateEnrolments">		
				<h2><bean:message key="label.masterDegree.alreadyChosenCourses"/></h2>

				<logic:iterate id="candidateEnrolment" name="candidateEnrolments" indexId="index">
					<tr>
						<td>
<%--							<html:multibox property='<%= "selection[" + index.intValue() + "]" %>'>
--%>						<html:multibox property="selection" >	

								<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.idInternal"/>
							</html:multibox>
						</td>
						<td>
							<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
						</td>
						<td>
							<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoBranch.name"/> <br/>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>
		</table>


		<h2><bean:message key="label.masterDegree.chooseNewCourses"/></h2>
		
		<table>
			<logic:iterate id="curricularCourseElem" name="curricularCourses"  indexId="index">
			   	<bean:define id="curricularCourse" name="curricularCourseElem" property="name"/>
				<tr>
					<td>
					<bean:define id="offset" value="0"/>
						<bean:size id="ccsSize" name="curricularCourseElem" property="infoScopes" />
						<logic:iterate id="curricularCourseScope" name="curricularCourseElem"  indexId="scopeID" property="infoScopes" 
								  offset="0" length="1">
	        						<logic:equal name="curricularCourseScope" property="infoBranch.name"  value='<%= new String("") %>'>
	        							<bean:define id="offset" value="1"/>
	            						<html:multibox property="selection">
	                						<bean:write name="curricularCourseScope" property="idInternal"/>
	            						</html:multibox>
	        						</logic:equal>
	        						<logic:notEqual name="curricularCourseScope" property="infoBranch.name"  value='<%= new String("") %>'>
	        							<bean:define id="offset" value="0"/>
	        						</logic:notEqual>
	        						<strong><bean:write name="curricularCourseElem" property="name"/></strong><br />
						</logic:iterate>
						
					    <blockquote>
	            			<logic:iterate id="curricularCourseScope" name="curricularCourseElem"  indexId="scopeID" property="infoScopes" 
	            						    offset="<%= new String(offset) %>" length="<%= String.valueOf(ccsSize.intValue() - Integer.parseInt(offset)) %>">
	                						<html:multibox property="selection">
	                    						<bean:write name="curricularCourseScope" property="idInternal"/>
	                						</html:multibox>
	               							<bean:write name="curricularCourseScope" property="infoBranch.name"/> <br/>
							</logic:iterate>	
					    </blockquote>
					</td>
				</tr>
			</logic:iterate>
		</table>		
<br />
		
<html:submit value="Submeter" styleClass="inputbutton" property="ok"/>
</html:form>
</logic:present>
