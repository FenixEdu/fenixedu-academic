<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<html:xhtml/>

<em><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.portal"/></em>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.board.announcements"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<logic:notPresent name="boardSearchBean" property="searchExecutionCourseBoards">
	<fr:form action="/announcements/boards.do?method=search">
		<fr:edit id="boardSearchBean.searchExecutionCourseBoards"
				name="boardSearchBean" schema="net.sourceforge.fenixedu.domain.messaging.BoardSearchBean.searchExecutionCourseBoards">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle thlight thright mtop05 mbottom1"/>
	  			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:destination name="search" path="/announcements/boards.do?method=search"/>
		   	</fr:layout>	    	
		</fr:edit>

		<div id="javascriptButtonID">
			<html:submit styleClass="inputbutton">
				<bean:message key="button.submit"/> 
			</html:submit>
			<html:cancel styleClass="inputbutton">
				<bean:message key="button.cancel"/> 
			</html:cancel>
		</div>		
	</fr:form>
</logic:notPresent>

<logic:present name="boardSearchBean" property="searchExecutionCourseBoards">

	<logic:equal name="boardSearchBean" property="searchExecutionCourseBoards" value="true">
		<fr:form action="/announcements/boards.do?method=search">
			<fr:edit id="boardSearchBean.executionCourse"
					name="boardSearchBean" schema="net.sourceforge.fenixedu.domain.messaging.BoardSearchBean.executionCourse">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thmiddle thlight thright mtop05 mbottom1"/>
		  			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		  			<fr:destination name="search" path="/announcements/boards.do?method=search"/>
			   	</fr:layout>
			</fr:edit>
			
			<div id="javascriptButtonID">
				<html:submit styleClass="inputbutton">
					<bean:message key="button.submit"/> 
				</html:submit>
				<html:cancel styleClass="inputbutton">
					<bean:message key="button.cancel"/> 
				</html:cancel>
			</div>
		</fr:form>
	</logic:equal>
	
	<logic:notEqual name="boardSearchBean" property="searchExecutionCourseBoards" value="true">
		<fr:form action="/announcements/boards.do?method=search">
			<fr:edit id="boardSearchBean.unit"
					name="boardSearchBean" schema="net.sourceforge.fenixedu.domain.messaging.BoardSearchBean.unit">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thmiddle thlight thright mtop05 mbottom1"/>
		  			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		  			<fr:destination name="search" path="/announcements/boards.do?method=search"/>
			   	</fr:layout>	    	
			</fr:edit>
		</fr:form>
	</logic:notEqual>

	<logic:present name="boards">
		<logic:notEmpty name="boards">
			<div class="mtop2">
				<jsp:include page="/messaging/announcements/listUnitBoards.jsp"/>
			</div>
		</logic:notEmpty>
	</logic:present>
	
</logic:present>
