<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul>
	<li class="navheader">
		<%-- POR NO RESOURCES --%>
		<bean:message key="label.gep.portal.tilte"  bundle="GEP_RESOURCES" />
	</li>
	<li>
		<html:link page="/searchCoursesInformation.do?method=doBeforeSearch">
			<bean:message key="link.gep.executionCoursesInformation"
						  bundle="GEP_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link page="/searchTeachersInformation.do?method=doBeforeSearch">
			<bean:message key="link.gep.teachersInformation"
						  bundle="GEP_RESOURCES"/>
		</html:link>
	</li>
	<li>
	  	<html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare">
	  		<bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES" />
	  	</html:link>
	</li>
	<li>
	  	<html:link page="/reportsByDegreeType.do?method=selectDegreeType">
	  		<bean:message key="link.reports.by.degree.type" bundle="GEP_RESOURCES" />
	  	</html:link>
	</li>
    <li>
        <html:link page="/manageEctsComparabilityTables.do?method=index">
            <bean:message key="link.ects.management" bundle="GEP_RESOURCES" />
        </html:link>
    </li>
	<li>
        <html:link page="/a3es.do?method=prepare">
            <bean:message key="link.gep.a3es" bundle="GEP_RESOURCES" />
        </html:link>
    </li>
    
    
    <li class="navheader">
		<bean:message key="label.listings" bundle="GEP_RESOURCES"/>
	</li>
    <li>
        <html:link page="/studentListByDegree.do?method=prepareByDegree">
            <bean:message key="link.lists.by.degree" bundle="GEP_RESOURCES" />
        </html:link>
    </li>
    <li>
        <html:link page="/studentsListByCurricularCourse.do?method=prepareByCurricularCourse">
            <bean:message key="link.lists.by.curricular.course" bundle="GEP_RESOURCES" />
        </html:link>
    </li>

	<li class="navheader">
		<bean:message key="label.inquiries" bundle="INQUIRIES_RESOURCES"/>
	</li>
	<li>
		<html:link page="/defineResponsePeriods.do?method=prepare">		
			<bean:message key="link.inquiries.define.response.period" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>
	<li>
		<html:link page="/executionCourseInquiries.do?method=search">
			<bean:message key="link.inquiries.execution.course.define.available.for.evaluation" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>
	<!-- li>
		<html:link page="/sendEmailReminder.do?method=prepare">		
			<bean:message key="link.inquiries.email.reminder" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li-->
	<li>
		<html:link page="/teachingStaff.do?method=selectExecutionYear">
			<bean:message key="link.inquiries.teachingStaff" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>
	<li>
		<html:link page="/uploadInquiriesResults.do?method=prepare">
			<bean:message key="link.inquiries.uploadResults" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>
	<li>
		<html:link page="/deleteInquiryResults.do?method=prepare">
			<bean:message key="link.inquiries.deleteResults" bundle="INQUIRIES_RESOURCES"/>
		</html:link>				
	</li>	
    <li>
        <html:link page="/viewInquiriesResults.do?method=chooseDegreeCurricularPlan">
            <bean:message key="title.inquiries.results" bundle="INQUIRIES_RESOURCES"/>
        </html:link>
    </li>
    
	<li class="navheader">
		<bean:message key="label.protocols.navigation.header" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</li>
	<li>
		<html:link page="/protocols.do?method=searchProtocols&amp;showAllNationalityTypes">
	  		<bean:message key="link.protocols.search" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	  	</html:link>
	</li>	
	<li class="navheader">
		<bean:message key="label.alumni" bundle="GEP_RESOURCES"/>
	</li>
	<li>
		<html:link page="/alumni.do?method=showAlumniStatistics">
	  		<bean:message key="label.alumni.statistics" bundle="GEP_RESOURCES"/>
	  	</html:link>
	</li>
	<li>
		<html:link page="/alumni.do?method=searchAlumni">
	  		<bean:message key="label.alumni.search" bundle="GEP_RESOURCES"/>
	  	</html:link>
	</li>
	<li>
		<html:link page="/alumni.do?method=manageRecipients">
	  		<bean:message key="link.alumni.recipients.manage" bundle="GEP_RESOURCES"/>
	  	</html:link>
	</li>	

	<li class="navheader">
		<bean:message key="label.registeredDegreeCandidacies.first.time.student.registration" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</li>
	<li>
		<html:link page="/registeredDegreeCandidacies.do?method=view">
			<bean:message key="label.registeredDegreeCandidacies.first.time.list" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</li>
	
	<logic:present role="MANAGER">
	<li class="navheader">
		<bean:message key="title.personal.ingression.data.viewer" bundle="GEP_RESOURCES" />
	</li>
	<li>
		<html:link page="/personalIngressionDataViewer.do?method=chooseStudent">
			<bean:message key="link.personal.ingression.data.viewer" bundle="GEP_RESOURCES" />
		</html:link>
	</li>
	</logic:present>
	
</ul>
<%--
Devido ï¿?s inscriï¿?ï¿?es, as funcionalidades do portal GEP estï¿?o temporariamente desactivadas para não causar sobrecarga no servidor.
As nossas desculpas pelo incï¿?modo.
--%>