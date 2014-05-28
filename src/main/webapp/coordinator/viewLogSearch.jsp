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
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<html:xhtml/>
<!-- aqui comeca o viewLogSearch -->

<jsp:include page="/coordinator/context.jsp" />

<h2> 
	<bean:message key= "log.coordinator.title"/>
	<logic:present name="executionCourse">
		<bean:write name="executionCourse" property="name"/>
	</logic:present>
</h2>

<div class="infoop4">
	<bean:message key="log.coordinator.message" bundle="APPLICATION_RESOURCES"/>
</div>

<logic:present name="searchBean">
	<fr:form action="<%="/searchDLog.do?method=search&amp;degreeCurricularPlanID=" + request.getParameter("degreeCurricularPlanID") %>">
		<fr:edit id="searchBean" name="searchBean">
			<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.coordinator.SearchDegreeLogBean" bundle="MESSAGING_RESOURCES">
				<fr:slot name="degreeLogTypes" layout="option-select" bundle="ENUMERATION_RESOURCES" key="DegreeLogTypes">
	        		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DegreeLogTypesProvider" />
	        		<fr:property name="classes" value="nobullet noindent"/>
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
	
	<logic:notEmpty name="searchBean" property="degreeLogs">
		<bean:size id="size" name="searchBean" property="degreeLogs"/>
		<h3> <bean:write name="size"/>
			<bean:message bundle="APPLICATION_RESOURCES" key="log.total.entries"/>
		</h3>
		<bean:define id="bean" name="searchBean" property="searchElementsAsParameters"/>
			<cp:collectionPages
				url="<%="/coordinator/searchDLog.do?method=prepare&amp;degreeCurricularPlanID=" + request.getParameter("degreeCurricularPlanID") + bean %>" 
				pageNumberAttributeName="pageNumber"
				numberOfPagesAttributeName="numberOfPages"/>

			<fr:view name="logPagesBean" property="degreeLogs">
				<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.coordinator.SearchDegreeLogBean" bundle="APPLICATION_RESOURCES">
					<fr:slot name="person" layout="view-as-image">
						<fr:property name="photoCellClasses" value="personalcard_photo"/>
				   		<fr:property name="imageFormat" value="<%=request.getContextPath()+ "/person/retrievePersonalPhoto.do?method=retrieveByUUID&uuid=${istUsername}"%>"/>
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
	    			<fr:slot name="degreeLogType" bundle="ENUMERATION_RESOURCES" key="DegreeLogTypes">
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

			<cp:collectionPages
				url="<%="/searchDLog.do?method=prepare&amp;degreeCurricularPlanID=" + request.getParameter("degreeCurricularPlanID") + bean %>" 
				pageNumberAttributeName="pageNumber"
				numberOfPagesAttributeName="numberOfPages"/>
	
	</logic:notEmpty>
	<logic:empty name="searchBean" property="degreeLogs">
		<h3><bean:message bundle="APPLICATION_RESOURCES" key="log.label.noResults"/></h3>
	</logic:empty>
</logic:present>
<!-- aqui acaba o viewLogSearch -->