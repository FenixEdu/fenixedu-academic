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
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseLogBean"%>
<%@page import="net.sourceforge.fenixedu.util.Month"%>
<%@page import="net.sourceforge.fenixedu.domain.Professorship"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<html:xhtml/>
<!-- aqui comeca o viewLogSearch -->

<h2> 
	<bean:message key= "log.title"/>
	<logic:present name="executionCourse">
		<bean:write name="executionCourse" property="name"/>
	</logic:present>
</h2>

<div class="infoop4">
	<bean:message key="log.message.explanation"/>
</div>

<logic:present name="searchBean">
	<fr:form action="/searchECLog.do?method=search&executionCourseID=${executionCourseID}">
		<fr:edit id="searchBean" name="searchBean">
			<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseLogBean" bundle="MESSAGING_RESOURCES">
				<fr:slot name="executionCourseLogTypes" layout="option-select" bundle="ENUMERATION_RESOURCES" key="ExecutionCourseLogTypes">
	        		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionCourseLogTypesProvider" />
	        		<fr:property name="classes" value="nobullet noindent"/>
	        		<fr:property name="selectAllShown" value="true"/>
	    		</fr:slot>
	    		<fr:slot name="months" layout="option-select" bundle="ENUMERATION_RESOURCES" key="MONTHS">
	        		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionCourseLogMonthProvider" />
	        		<fr:property name="classes" value="nobullet noindent"/>
	        		<fr:property name="selectAllShown" value="true"/>
	    		</fr:slot>	
	    		<fr:slot name="professorships" layout="option-select" bundle="APPLICATION_RESOURCES" key="title.teachers">
	        		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionCourseLogProfessorshipProvider" />
	        		<fr:property name="classes" value="nobullet noindent"/>
	        		<fr:property name="eachSchema" value="executionCourseLog.professorship"/>
	        		<fr:property name="eachLayout" value="values-dash"/>        		
	        		<fr:property name="sortBy" value="person.istUsername"/>
	        		<fr:property name="selectAllShown" value="true"/>
	    		</fr:slot>
	    		<%-- <fr:slot name="viewPhoto" layout="option-select" bundle="APPLICATION_RESOURCES" key="label.viewPhoto"/>
	    		--%> 		
			</fr:schema>
			<fr:layout name="tabular-row">
				<fr:property name="classes" value="tstyle2 mtop15 tdtop inobullet"/>
			</fr:layout>
		</fr:edit>
		<html:submit >
			<bean:message key="submit.submit" bundle="HTMLALT_RESOURCES" />
		</html:submit>
	</fr:form>

	<logic:notEmpty name="searchBean" property="executionCourseLogs">
		<bean:size id="size" name="searchBean" property="executionCourseLogs"/>
		<h3> <bean:write name="size"/> /
			 <bean:write name="executionCourse" property="executionCourseLogsCount"/>
			<bean:message key="log.total.entries"/>

		</h3>
		<bean:define id="bean" name="searchBean" property="searchElementsAsParameters"/>
			<div style="word-wrap: break-word">
				<cp:collectionPages
					url="<%="/teacher/searchECLog.do?method=prepare&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean %>" 
					pageNumberAttributeName="pageNumber"
					numberOfPagesAttributeName="numberOfPages"/>
			</div>
			<fr:view name="logPagesBean" property="executionCourseLogs">
				<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseLogBean" bundle="APPLICATION_RESOURCES">
					<fr:slot name="person" layout="view-as-image">
						<fr:property name="photoCellClasses" value="personalcard_photo"/>
				   		<fr:property name="imageFormat" value="<%=request.getContextPath()+ "/person/retrievePersonalPhoto.do?method=retrieveByID&personCode=${externalId}"%>"/>
	    			</fr:slot>
					<fr:slot name="person.istUsername" key="label.istid" >
		        		<fr:property name="classes" value="nobullet noindent"/>   
	    			</fr:slot>
	    			<fr:slot name="person.name" key="label.name">
		        		<fr:property name="classes" value="nobullet noindent"/>   
	    			</fr:slot>
	    			<fr:slot name="whenDateTime" bundle="ENUMERATION_RESOURCES" key="DATE">
		        		<fr:property name="classes" value="nobullet noindent"/>   
	    			</fr:slot>
	    			<fr:slot name="executionCourseLogType" bundle="ENUMERATION_RESOURCES" key="ExecutionCourseLogTypes">
		        		<fr:property name="classes" value="nobullet noindent"/>   
	    			</fr:slot>
	    			<fr:slot name="description">
	        			<fr:property name="classes" value="nobullet noindent"/>   
		    		</fr:slot>	
	    		</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 mtop15 tdcenter inobullet"/>
				</fr:layout>
			</fr:view>
			<div style="word-wrap: break-word">
				<cp:collectionPages
					url="<%="/teacher/searchECLog.do?method=prepare&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean %>" 
					pageNumberAttributeName="pageNumber"
					numberOfPagesAttributeName="numberOfPages"/>
			</div>
	
	</logic:notEmpty>
	<logic:empty name="searchBean" property="executionCourseLogs">
		<h3><bean:message key="log.label.noResults"/></h3>
	</logic:empty>
</logic:present>
<!-- aqui acaba o viewLogSearch -->