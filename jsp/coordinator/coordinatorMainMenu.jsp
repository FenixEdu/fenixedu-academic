<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
	<bean:define id="infoExecutionDegreeId" name="infoExecutionDegree" property="idInternal"/>

	<%-- Start of Master Degree Coordinator Options --%>
	<logic:equal name="infoExecutionDegree" 
		property="infoDegreeCurricularPlan.infoDegree.tipoCurso" 
		value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
			<p>
			<strong>&raquo; <bean:message key="link.coordinator.candidate"/></strong>
			<ul>
	        	<li>
		        	<html:link page="/candidateOperation.do?method=getCandidates&action=visualize&page=0">
		        	<bean:message key="link.coordinator.visualizeCandidate" /></html:link>
		        	<br/>
					<br/>
		        </li>
	        	<li>
	        		<bean:define id="link">/prepareCandidateApproval.do?method=chooseExecutionDegree&page=0&executionDegreeID=
	        		</bean:define>
		        	<bean:define id="prepareCandidateApprovalLink">
    					<bean:write name="link"/><bean:write name="infoExecutionDegreeId"/>
    				</bean:define> 	
		        	
		        	<html:link page='<%= pageContext.findAttribute("prepareCandidateApprovalLink").toString() %>'>
		        	<bean:message key="link.coordinator.approveCandidates" /></html:link>
		        	<br/>
					<br/>		        	
		        </li>
				<li>
					<bean:define id="link2">/displayCandidateListToMakeStudyPlan.do?method=prepareSelectCandidates&amp;page=0&executionDegreeID=
	        		</bean:define>
		        	<bean:define id="displayCandidateListToMakeStudyPlanLink">
    					<bean:write name="link2"/><bean:write name="infoExecutionDegreeId"/>
    				</bean:define> 	
					<html:link page="<%= pageContext.findAttribute("displayCandidateListToMakeStudyPlanLink").toString() %>">
					<bean:message key="link.masterDegree.administrativeOffice.makeStudyPlan" /></html:link>
		        	<br/>
					<br/>
				</li>
			</ul>
			</p>
			<p><strong>&raquo; <bean:message key="link.coordinator.student"/></strong>
			<ul>
				<li>
					<html:link page="/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0">
					<bean:message key="link.coordinator.studentListByDegree" /></html:link><br>
		        	<br/>
					<br/>
				</li>
				<li>
					<html:link page="/studentListByDegree.do?method=getCurricularCourses&jspTitle=title.studentListByCourse&page=0">
				<bean:message key="link.studentListByCourse" /></html:link>
		        	<br/>
					<br/>
				</li>
		</ul>
		</p>	
	</logic:equal>
	
	<%-- Start of Degree Coordinator Options --%>
	<logic:notEqual name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
		<ul>
			<%-- <html:link forward="equivalenceForCoordinator">
			<bean:message key="link.coordinator.equivalence"/></html:link><br/>
		  	--%>
			<li>
				<html:link page="/executionCoursesInformation.do?method=prepareChoice">
				<bean:message key="link.coordinator.executionCoursesInformation"/></html:link>
				<br/>
				<br/>
			</li>
			<li>
				<html:link page="/teachersInformation.do" paramId="executionDegreeId" paramName="infoExecutionDegree" paramProperty="idInternal">
					<bean:message key="link.coordinator.teachersInformation"/>
				</html:link>
				<br/>
				<br/>
			</li>
			
			<li>
				<html:link page="/tutorManagement.do?method=prepareChooseTutor" paramId="executionDegreeId" paramName="infoExecutionDegree" paramProperty="idInternal">
					<bean:message key="label.coordinator.tutors"/>
				</html:link>
				<br/>
				<br/>
			</li>
			<li>
				<html:link page="/studentEnrollementSection.do" paramId="executionDegreeId" paramName="infoExecutionDegree" paramProperty="idInternal">
					<bean:message key="label.coordinator.studentInformation"/>
				</html:link>
				<br/>
				<br/>
			</li>
			
		</ul>
	</logic:notEqual> 
	
	<%-- Start of Common Options --%>
	<ul>
		<li>
			<bean:define id="infoExecutionDegreeCode" name="infoExecutionDegree" property="idInternal"/>
			<html:link page="<%="/degreeCurricularPlanManagement.do?method=showActiveCurricularCourses&amp;infoExecutionDegreeCode=" + infoExecutionDegreeCode %>">
				<bean:message key="link.coordinator.degreeCurricularPlan.management"/>
			</html:link> 
			<br/>
			<br/>
		</li>

		<li>
			<html:link page="<%= "/viewCoordinationTeam.do?method=viewTeam&infoExecutionDegreeId="+ 
			infoExecutionDegreeId.toString()  %>" >
				Equipa de Coordenação
			</html:link> 
			<br/>
			<br/>
		</li>
	
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=subMenu&amp;infoExecutionDegreeId=" + infoExecutionDegreeId.toString()%>">
				<bean:message key="link.coordinator.degreeSite.management"/>		
			</html:link> 
			<br/>
			<br/>
		</li>
	</ul>
</logic:present>
