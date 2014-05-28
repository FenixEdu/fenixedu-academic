<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml />

<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

<p class="separator2 mtop2"><b><bean:message key="title.inquiries.studentResults" bundle="INQUIRIES_RESOURCES"/></b></p>
<bean:define id="courseResult" name="studentInquiriesCourseResult" type="net.sourceforge.fenixedu.dataTransferObject.oldInquiries.StudentInquiriesCourseResultBean"/>
    <table>
        <tr>
            <td valign="top">
            	<bean:message key="link.teachingInquiries.cuResults" bundle="INQUIRIES_RESOURCES"/> - 
            </td>
            <td valign="top">
                <html:link href="<%= request.getContextPath() + "/teacher/teachingInquiry.do?method=showInquiryCourseResult&resultId=" + courseResult.getStudentInquiriesCourseResult().getExternalId() %>" target="_blank">            
            		<strong><bean:write name="courseResult" property="studentInquiriesCourseResult.executionCourse.nome" /> -
            		<bean:write name="courseResult" property="studentInquiriesCourseResult.executionDegree.degreeCurricularPlan.name" /></strong>
            	</html:link>
                - <c:out value="${executionCourse.executionPeriod.qualifiedName}" />
                <br/>
                <bean:define id="executionCourseLink"><c:out value="${pageContext.request.contextPath}" /><c:out value="${executionCourse.site.reversePath}" />/pagina-inicial</bean:define>
                <!-- NO_CHECKSUM --><html:link href="<%= executionCourseLink %>" target="_blank">
                    <em><bean:message key="link.curricularUnit.website" bundle="INQUIRIES_RESOURCES"/></em>
                </html:link>
            
            </td>
        </tr>
    </table>

<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
	<ul>
		<logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult">
			<li>
                <html:link href="<%= request.getContextPath() + "/teacher/teachingInquiry.do?method=showInquiryTeachingResult&resultId=" + teachingResult.getExternalId() %>" target="_blank">            
					<bean:write name="teachingResult" property="professorship.person.name" />
					&nbsp;(<bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>)<br/>
                </html:link>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>

<p class="separator2 mtop25"><b><bean:message key="title.teachingInquiries.resultsToImprove" bundle="INQUIRIES_RESOURCES"/></b></p>

<table class="tstyle1 thlight thleft tdcenter">
    <tr class="top">
        <th class="aright">Organização da UC <a href="#" class="help">[?] <span>Resultados a melhorar se mais de 25% alunos (no mínimo de 10 respostas) classifica como abaixo ou igual a 3 (Discordo) 2 das 4 questões do grupo. - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
        <th class="aright">Avaliação da UC <a href="#" class="help">[?] <span>Resultados a melhorar se mais 25% alunos (no mínimo de 10 respostas) classifica como abaixo ou igual a 3 (Discordo) a questão e/ou taxa de avaliação <50% e/ou taxa de aprovação <50%. - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
        <th class="aright">Passível de Auditoria <a href="#" class="help">[?] <span>Se os grupos da organização e avaliação da UC apresentarem ambos resultados a melhorar e, pelo menos, metade do corpo docnete (pares docente/tipo de aulas) apresentar resultados a melhorar no mínimo de dois grupos - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
    </tr>
    <tr>
        <td><bean:message key="<%= "label.colored." + courseResult.getStudentInquiriesCourseResult().getUnsatisfactoryResultsCUOrganization().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
        <td><bean:message key="<%= "label.colored." + courseResult.getStudentInquiriesCourseResult().getUnsatisfactoryResultsCUEvaluation().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
        <td><bean:message key="<%= "label.colored." + courseResult.getStudentInquiriesCourseResult().getAuditCU().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
    </tr>
</table>

<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
    <table class="tstyle1 thlight tdcenter">
        <tr>        
            <th class="nowrap"><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/></th>
            <th><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/></th>
            <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsAssiduity" bundle="INQUIRIES_RESOURCES"/> <a href="#" class="help">[?] <span>Resultados a melhorar se mais de 25% classifica como abaixo ou igual a 3 (De vez em quando) - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsPresencialLearning" bundle="INQUIRIES_RESOURCES"/> <a href="#" class="help">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade às aulas, no mínimo de 10), mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 3 questões do grupo. - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsPedagogicalCapacity" bundle="INQUIRIES_RESOURCES"/> <a href="#" class="help">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade às aulas, no mínimo de 10), mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 3 questões do grupo. - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsStudentInteraction" bundle="INQUIRIES_RESOURCES"/> <a href="#" class="help">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade às aulas, no mínimo de 10), mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 3 questões do grupo. - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <c:if test="${!isToImproove}">
                <th><bean:message key="label.teachingInquiries.unsatisfactoryResultsAuditable" bundle="INQUIRIES_RESOURCES"/></th>
            </c:if>
        </tr>
        <logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult">
            <tr>        
                <td class="aleft nowrap"><c:out value="${teachingResult.professorship.person.name}" /></td>
                <td><bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/></td>
                <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsAssiduity().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsPresencialLearning().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsPedagogicalCapacity().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsStudentInteraction().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                <c:if test="${!isToImproove}">
                    <td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsAuditable().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
                </c:if>
            </tr>       
        </logic:iterate>
    </table>
</logic:notEmpty>

<p class="separator2 mtop25"><b><bean:message key="title.teachingInquiries.excellentResults" bundle="INQUIRIES_RESOURCES"/></b></p>

<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
    <table class="tstyle1 thlight tdcenter">
        <tr>        
            <th class="nowrap"><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/></th>
            <th><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/></th>
            <th class="aright">Assiduidade dos alunos <a href="#" class="help">[?] <span>Resultados excelentes se mais de 75% classifica como acima ou igual a 3 (De vez em quando) - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th class="aright">Proveito da aprendizagem presencial <a href="#" class="help">[?] <span>Resultados excelentes se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade às aulas, no mínimo de 10), mais de 75% classifica como acima ou igual a 7 (Concordo) todas as questões do grupo (excepto a questão da assiduidade) e a média de respostas nos outros grupos (Capacidade Pedagógica e Interacção com os alunos) superior a 7 (Concordo). - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th class="aright">Capacidade pedagógica <a href="#" class="help">[?] <span>Resultados excelentes se, entre os alunos que frequentaram as aulas, mais de 75% classifica como acima ou igual a 7 (Concordo) todas as questões do grupo e a média de respostas nos outros grupos (Proveito da aprendizagem presencial (excepto a questão da assiduidade dos alunos) e Interacção com os alunos) superior a 7 (Concordo). - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
            <th class="aright">Interacção com os alunos <a href="#" class="help">[?] <span>Resultados excelentes se, entre os alunos que frequentaram as aulas, mais de 75% classifica como acima ou igual a 7 (Concordo) todas as questões do grupo e a média de respostas nos outros grupos (Proveito da aprendizagem presencial (excepto a questão da assiduidade dos alunos) e Capacidade pedagógica) superior a 7 (Concordo). - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
        </tr>
        </tr>
        <logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult">
            <tr>
                <td class="aleft nowrap"><c:out value="${teachingResult.professorship.person.name}" /></td>
                <td><bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/></td>
                <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${teachingResult.valuesMap['Res_excelentes_assiduidade']}" /></td>
                <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${teachingResult.valuesMap['Res_excelentes_prov_aprend_pres']}" /></td>
                <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${teachingResult.valuesMap['Res_excelentes_cap_pedag']}" /></td>
                <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${teachingResult.valuesMap['Res_excelentes_int_alunos']}" /></td>
            </tr>
        </logic:iterate>
    </table>
</logic:notEmpty>


<p class="separator2 mtop25"><b><bean:message key="title.inquiries.teachingReports" bundle="INQUIRIES_RESOURCES"/></b></p>

<ul>
	<logic:iterate id="professorship" name="executionCourse" property="professorships" >
		<li>
			<logic:notEmpty name="professorship" property="teachingInquiry">
				<bean:define id="teachingInquiryID" name="professorship" property="teachingInquiry.externalId" />
				<html:link page="<%= "/viewInquiriesResults.do?method=showFilledTeachingInquiry&filledTeachingInquiryId=" + teachingInquiryID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")  %>" target="_blank">
					<bean:write name="professorship" property="person.name"/>
				</html:link>
			</logic:notEmpty>
			<logic:empty name="professorship" property="teachingInquiry">
				<bean:write name="professorship" property="person.name"/>
			</logic:empty>
            <bean:define id="emailAddress" name="professorship" property="person.institutionalOrDefaultEmailAddressValue" />
            <html:link href="<%= "mailto:" + emailAddress %>" target="_blank" style="border: none" titleKey="link.email" bundle="INQUIRIES_RESOURCES">
                <img src="<%=request.getContextPath()%>/images/icon_email.gif"/>
            </html:link>
            
            <logic:present role="role(PEDAGOGICAL_COUNCIL)">
	            <bean:define id="professorshipID" name="professorship" property="oid"/>
		    	<html:link page="<%= "/viewInquiriesResults.do?method=showOthersTeacherCourses&professorshipID=" +  professorshipID%>">
		        	(<bean:message key="label.inquiries.courses.other" bundle="INQUIRIES_RESOURCES"/>)
		        </html:link>
		    </logic:present>
            
		</li>
	</logic:iterate>
</ul>

<div class="infoop8 mtop15">
	<p class="mvert025 color777">
		<em>
			Nota: <bean:message key="message.inquiries.teachers.inquiries.instructions1" bundle="INQUIRIES_RESOURCES"/>
		</em>
	</p>
	<p class="mtop025 color777">
		<em>
			Nota: <bean:message key="message.inquiries.teachers.inquiries.instructions2" bundle="INQUIRIES_RESOURCES"/>
		</em>
	</p>
</div>

<p class="separator2 mtop25"><b><bean:message key="title.inquiries.delegateReports" bundle="INQUIRIES_RESOURCES"/></b></p>
<ul>
    <logic:iterate id="delegateInquiry" name="executionCourse" property="yearDelegateCourseInquiries">
        <li>
            <bean:define id="delegateInquiryID" name="delegateInquiry" property="externalId" />
            <html:link page="<%= "/viewInquiriesResults.do?method=showFilledYearDelegateInquiry&filledYearDelegateInquiryId=" + delegateInquiryID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")  %>" target="_blank">
                <bean:write name="delegateInquiry" property="delegate.registration.student.person.name"/>
            </html:link>
            <bean:define id="emailAddress" name="delegateInquiry" property="delegate.registration.student.person.institutionalOrDefaultEmailAddressValue" />
            <html:link href="<%= "mailto:" + emailAddress %>" target="_blank" style="border: none" titleKey="link.email" bundle="INQUIRIES_RESOURCES">
                <img src="<%=request.getContextPath()%>/images/icon_email.gif"/>
            </html:link>
        </li>
    </logic:iterate>
</ul>

<p class="separator2 mtop25"><b><bean:message key="title.inquiries.courseResults.coordinatorComments" bundle="INQUIRIES_RESOURCES"/></b></p>
<logic:empty name="courseResultsCoordinatorCommentEdit">
    <fr:view name="courseResult" property="studentInquiriesCourseResult" schema="studentInquiriesCourseResult.courseResultsCoordinatorComment" >
        <fr:layout name="tabular">
            <fr:property name="labelTerminator" value=""/>
        </fr:layout>
    </fr:view>
    
    <br/><br/>
    <logic:equal name="canComment" value="true">
        <html:form action="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&courseResultsCoordinatorCommentEdit=true&degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
            <html:hidden property="executionCourseID"/>
            <html:hidden property="executionDegreeID"/>
            <html:submit><bean:message key="label.inquiries.courseResults.coordinatorComments.edit" bundle="INQUIRIES_RESOURCES"/></html:submit>
        </html:form>
    </logic:equal>
</logic:empty>
<logic:notEmpty name="courseResultsCoordinatorCommentEdit">
    <fr:edit name="courseResult" property="studentInquiriesCourseResult" schema="studentInquiriesCourseResult.courseResultsCoordinatorComment" >
        <fr:layout>
            <fr:property name="labelTerminator" value=""/>
        </fr:layout>
    </fr:edit>
</logic:notEmpty>
<br/>
<html:form action="<%= "/viewInquiriesResults.do?method=selectexecutionSemester&courseResultsCoordinatorCommentEdit=true&degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
    <html:hidden property="degreeCurricularPlanID"/>
    <bean:define id="executionSemesterID" name="executionCourse" property="executionPeriod.oid" type="java.lang.Long" />
    <html:hidden property="executionSemesterID" value="<%= executionSemesterID.toString() %>" />
    <html:submit><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
</html:form>