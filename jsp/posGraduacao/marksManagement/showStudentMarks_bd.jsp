<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.students.listMarks"/></h2>

<logic:present name="infoSiteEnrolmentEvaluation">
	<bean:define id="studentmMarksListComponent" name="infoSiteEnrolmentEvaluation"  />
	<table width="100%">
		<logic:iterate id="enrolment" name="studentmMarksListComponent" length="1">	
			<logic:iterate id="enrollmentEvaluationElem" name="enrolment" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation" length="1">	
				<tr>
					<td class="infoselected">
						<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>:</b>
						<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome" />
						<br />
						<b><bean:message key="label.curricularPlan"/>:</b>
						<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.infoDegreeCurricularPlan.name" />
						<br />
						<b><bean:message key="label.curricularCourse"/>:</b>
						<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.name" />
						<br />
						<b><bean:message key="label.student" />:</b>
						<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number" />&nbsp;-&nbsp;
						<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.infoPerson.nome" />
					</td>
				</tr>
			</logic:iterate>
		</logic:iterate>
	</table>
	<br />
	<span class="error"><html:errors/></span>
	<table>   
		<tr>
			<td class="listClasses-header"><bean:message key="label.mark"  /></td>
			<td class="listClasses-header"><bean:message key="label.examDate"  /></td>
			<td class="listClasses-header"><bean:message key="label.gradeAvailableDate"  /></td>
			<td class="listClasses-header"><bean:message key="label.enrolmentEvaluationType"  /></td>
			<td class="listClasses-header"><bean:message key="label.teacherName"  /></td>
			<logic:present name="showMarks" >
			 	<td class="listClasses-header"><bean:message key="label.employee"  /></td>
		    	<td class="listClasses-header"><bean:message key="label.when"  /></td>
	    	</logic:present>
			<td class="listClasses-header"><bean:message key="label.observation"  /></td>
		</tr>
		<logic:iterate id="enrolment" name="studentmMarksListComponent" >	
			<logic:iterate id="enrolmentEvaluation" name="enrolment" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation" indexId="evaluationId" >		
    				 <bean:define id="enrolmentEvaluationCode" name="enrolmentEvaluation" property="idInternal"/>   		
    	  	   <logic:notEmpty name="enrolmentEvaluation" property="infoPersonResponsibleForGrade" >
    				 <bean:define id="teacherName" name="enrolmentEvaluation" property="infoPersonResponsibleForGrade" /> 
       		   </logic:notEmpty> 
       		   <logic:empty name="enrolmentEvaluation" property="infoPersonResponsibleForGrade" >
    				 <bean:define id="teacherCode" value="&nbsp;"/> 
       		   </logic:empty> 
    
       		<bean:define id="studentCode" name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.idInternal" /> 
    			<tr>
	    			<td class="listClasses" >	
		    			<bean:write name="enrolmentEvaluation" property="grade"/>
		    		</td>
		    		<td class="listClasses" >
		    			<logic:present name="enrolmentEvaluation" property="examDate" >	
		    				<dt:format pattern="dd-MM-yyyy">
								<bean:write name="enrolmentEvaluation" property="examDate.time"/>
							</dt:format>
						</logic:present>
					</td>
					<td  class="listClasses" >
						<logic:present name="enrolmentEvaluation" property="gradeAvailableDate" >
							<dt:format pattern="dd-MM-yyyy">
								<bean:write name="enrolmentEvaluation" property="gradeAvailableDate.time"/>
							</dt:format>
						</logic:present>
					</td>
					<td  class="listClasses" >
						<bean:write name="enrolmentEvaluation" property="enrolmentEvaluationType" />
					</td>
					<logic:notEmpty name="enrolmentEvaluation" property="infoPersonResponsibleForGrade" >
						<td  class="listClasses" >
							<bean:write name="teacherName" property="nome" />
						</td>
					</logic:notEmpty>
					<logic:empty name="enrolmentEvaluation" property="infoPersonResponsibleForGrade" >
						<td  class="listClasses" >
						&nbsp;
						</td>
					</logic:empty>
					<logic:present name="showMarks" >
						<logic:empty name="enrolmentEvaluation" property="infoEmployee" >	
							<td  class="listClasses" >&nbsp;</td> 
							<td  class="listClasses" >&nbsp;</td> 
						</logic:empty>
						<logic:notEmpty name="enrolmentEvaluation" property="infoEmployee" >	
							<td  class="listClasses" >
								<bean:write name="enrolmentEvaluation" property="infoEmployee.nome"/>	
							</td> 
							<td  class="listClasses" >
								<dt:format pattern="dd-MM-yyyy">
									<bean:write name="enrolmentEvaluation" property="when.time"/>
								</dt:format>
							</td> 
						</logic:notEmpty>
					</logic:present >
					<logic:notEmpty name="enrolmentEvaluation" property="observation">
					<td  class="listClasses" >
						<bean:write name="enrolmentEvaluation" property="observation"/>	
					</td>						
					</logic:notEmpty>
					<logic:empty name="enrolmentEvaluation" property="observation">
						<td  class="listClasses" >&nbsp;</td>						
					</logic:empty>
				</tr>	
			</logic:iterate>	
		</logic:iterate> 
   	</table>    
	<logic:notPresent name="showMarks" >
		<html:form action="/changeMarkDispatchAction?method=studentMarkChanged" >
	 		<bean:define id="teacherCode" name="lastEnrolmentEavluation" property="idInternal"/>
	 		<html:hidden property="studentNumber"/>	
			<html:hidden property="teacherCode" value="<%= pageContext.findAttribute("teacherCode").toString() %>" />
	 		<html:hidden property="courseId" value="<%= pageContext.findAttribute("courseId").toString() %>" />
	 		<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	 		<html:hidden property="degreeId" value="<%= pageContext.findAttribute("degreeId").toString() %>" />
			<table>
	 			<tr>									
					<td align="left">
						<bean:message key="label.mark" />:
					</td>
					<td align="left">
						<html:text property="grade" size="4"  />
					</td>
				</tr>
				<%-- Data de Exame --%>
				<tr>								
					<td align="left">
						<bean:message key="label.examDate" />:
					</td>
             		<td>
             			<html:select property="examDateYear">
                    		<html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
                 		</html:select>
                 		<html:select property="examDateMonth">
                    		<html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
                 		</html:select>
                 		<html:select property="examDateDay">
                    		<html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
                 		</html:select>    
             		</td>          
   				</tr>
				<%-- Data de Avaliaçao --%>
   				<tr>							
					<td align="left">
						<bean:message key="label.gradeAvailableDate" />:
					</td>
	             	<td>
	             		<html:select property="gradeAvailableDateYear">
	                    	<html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
	                 	</html:select>
	                 	<html:select property="gradeAvailableDateMonth">
	                    	<html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
	                 	</html:select>
	                 	<html:select property="gradeAvailableDateDay">
	                    	<html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
	                 	</html:select>    
	             	</td>          
   				</tr>
   				<tr>									
					<td align="left">
						<bean:message key="label.enrolmentEvaluationType" />:
					</td>
					<td align="left">
						<html:select property="enrolmentEvaluationType">
		               		<html:options collection="<%= SessionConstants.ENROLMENT_EVALUATION_TYPE_LIST %>"  property="value" labelProperty="label"/>
	             		</html:select>   
					</td>
  				</tr>
  				<tr>									
					<td align="left">
						<bean:message key="label.masterDegree.administrativeOffice.responsibleTeacher" />:
					</td>
					<td align="left">	
						<html:text property="teacherNumber" size="4" />	
					</td>
				</tr>
				<tr>									
					<td align="left">
						<bean:message key="label.observation" />:
					</td>
					<td align="left">
						<html:text  property="observation" size="20" />
					</td>
				</tr>
	 		</table>	
	 		<html:submit styleClass="inputbutton">
	 			<bean:message key="button.save"/>
	 		</html:submit>
	 	</html:form>
	</logic:notPresent>
</logic:present>
