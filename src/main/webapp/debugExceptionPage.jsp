<%@page import="net.sourceforge.fenixedu.presentationTier.util.ExceptionInformation"%>
<%@page language="java" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page import="org.apache.struts.Globals"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<html>

<head>
	<title>
		<bean:message key="debug.page.title" bundle="GLOBAL_RESOURCES"/>
	</title>
   	<link rel="stylesheet"  href="<%= request.getContextPath() %>/CSS/debug.css">
	<meta charset="UTF-8">
</head>

<body>
	<bean:define id="exception" name="debugExceptionInfo" property="exception"/>
	<bean:define id="exceptionClass" name="exception" property="class"/>
	
	<div id="debugPage">
		<h1 class="debugTitleException"><bean:write name="exceptionClass" property="name"/></h1>
		<div class="debugTitleCause">
		<logic:notEmpty name="exception" property="message">
			<bean:write name="exception" property="message"/>
		</logic:notEmpty>
		<logic:empty name="exception" property="message">
			&nbsp;
		</logic:empty>
		
		</div>
		
	</div>
				
	<!-- HACK: I'm using comments to prevent extra whitespace! Please bear the horror and forgive me! -->
	<div id="errorInfo">
   		<table>
			<tr>
				<td>Exception Description:</td>
				<td style="font-size: 15px;"><!--
					--><span class="traceException"><!--
						--><span class="traceExceptionClassLabel"><!--
							--><span class="traceExceptionClassPackage"><bean:write name="exceptionClass" property="package.name"/>.</span><!--
							--><span class="traceExceptionClassName"><bean:write name="exceptionClass" property="simpleName"/></span><!--
						--></span><!-- 
						--><span class="traceExceptionMessage"><bean:write name="exception" property="localizedMessage"/></span><!--
					--></span><!--
				--></td>
			</tr>
			<tr>
				<td>Thrown at:</td>
				<td style="font-size: 15px;"><!--
				--><logic:present name="debugExceptionInfo" property="actionMapping"><!-- 
					--><span class="traceLine level0"><!-- 
						--><span class="traceLocation"><!--
							--><span class="javaLocation"><!--
								--><span class="classPackage"><bean:write name="debugExceptionInfo" property="actionErrorClass.package.name"/></span><!--
								-->.<!--
								--><span class="className"><bean:write name="debugExceptionInfo" property="actionErrorClass.simpleName"/></span><!--
								-->.<!--
								--><span class="methodName"><bean:write name="debugExceptionInfo" property="actionErrorMethod"/></span><!--
							--></span><!--
							-->(<!--
							--><span class="fileLocation"><!--
								--><span class="fileName"><bean:write name="debugExceptionInfo" property="actionErrorFile"/></span><!--
								-->:<!--
								--><span class="lineNumber"><bean:write name="debugExceptionInfo" property="actionErrorLine"/></span><!--
							--></span><!--
							-->)<!--
						--></span><!--
					--></span><!--
				--></logic:present>
				<logic:notPresent name="debugExceptionInfo" property="actionMapping">
					<bean:message key="debug.page.message.uncaughtException" bundle="GLOBAL_RESOURCES"/>
				</logic:notPresent>
				</td>
			</tr>
		</table>
    </div>

    <div id="userInfo">
		<table border="0" cellspacing="0">
			<tr>
				<th>User info</th>
			</tr>
			<logic:present name="debugExceptionInfo" property="userName">
			<tr>
			<td><span class="label">User id:</span></td>
				<td><bean:write name="debugExceptionInfo" property="userName"/></td>
			</tr>
			<tr>
				<td><span class="label">User roles:</span></td>
					<td><logic:iterate id="roleType" name="debugExceptionInfo" property="userRoles"><!--
						--><div class="userRole"><bean:write name="roleType"/></div><!--
					--></logic:iterate>
					</td>
			</tr>
			</logic:present>
			<logic:notPresent name="debugExceptionInfo" property="userName">
				<tr><td>No user</td></tr>
			</logic:notPresent>
			
		</table>
    </div>
    
    <div id="requestInfo">
    	<table id="requestInfoTable" class="super-table">
			<tr>
				<th colspan="2">Request description</th>
			</tr>
			<tr>
				<td><span class="label">Method:</span></td>
				<td><bean:write name="debugExceptionInfo" property="requestMethod"/></td>
			</tr>
			<tr>
				<td><span class="label">URL:</span></td>
				<td><bean:write name="debugExceptionInfo" property="requestFullUrl"/></td>
			</tr>
			<tr>
				<td><span class="label">Path:</span></td>
				<td><bean:write name="debugExceptionInfo" property="requestURL"/></td>
			</tr>
			<tr>
				<td><span class="label">Query Path:</span></td>
				<td><bean:write name="debugExceptionInfo" property="queryString"/></td>
			</tr>
			<tr>
				<td><span class="label">Request Parameters:</span></td>
			</tr>

		</table>
		<table class="parameters-table">
			<bean:define id="queryParameters"  name="debugExceptionInfo" property = "queryParameters"/>
 			<logic:iterate id="element" name="queryParameters">
				<tr>
 					<td><span class="label key"><bean:write name="element" property="key"/></span></td>
 					<td class="value"><bean:write name="element" property="value"/></td>
 				</tr>
    		</logic:iterate>
		</table>
    </div>
    
    
    <div id="mappingInfo">
    	<table id="mappingInfoTable" class="super-table">
			<tr>
				<th colspan="2">Mapping Description</th>
			</tr>
			<logic:present name="debugExceptionInfo" property="actionMapping">
				<bean:define id="mapping" name="debugExceptionInfo" property="actionMapping"/>
				<bean:define id="module" name="mapping" property="moduleConfig"/>
				<tr>
					<td><span class="label">Module Prefix:</span></td>
					<td><bean:write name="module" property="prefix"/></td>
				</tr>
				<tr>
					<td><span class="label">Mapping Path:</span></td>
					<td><bean:write name="mapping" property="path"/></td>
				</tr>
				<logic:present name="mapping" property="type">
				<tr>
					<td><span class="label">Mapping Type:</span></td>
					<td><bean:write name="mapping" property="type"/></td>
				</tr>
				</logic:present>
				<logic:present name="mapping" property="forward">
				<tr>
					<td><span class="label">Mapping Forward:</span></td>
					<td><bean:write name="mapping" property="forward"/></td>
				</tr>
				</logic:present>
				<logic:present name="mapping" property="include">
				<tr>
					<td><span class="label">Mapping Include:</span></td>
					<td><bean:write name="mapping" property="include"/></td>
				</tr>
				</logic:present>
				<tr>
					<td><span class="label">Mapping Name:</span></td>
					<td><bean:write name="mapping" property="name"/></td>
				</tr>
				<tr>
					<td><span class="label">Mapping Scope:</span></td>
					<td><bean:write name="mapping" property="scope"/></td>
				</tr>
				<tr>
					<td><span class="label">Mapping Attribute:</span></td>
					<td><bean:write name="mapping" property="attribute"/></td>
				</tr>
				<tr>
					<td><span class="label">Mapping Suffix:</span></td>
					<td><bean:write name="mapping" property="suffix"/></td>
				</tr>
				<tr>
					<td><span class="label">Mapping Roles:</span></td>
					<td><bean:write name="mapping" property="roles"/></td>
				</tr>
				<tr>
					<td><span class="label">Multipart Class:</span></td>
					<td><bean:write name="mapping" property="multipartClass"/></td>
				</tr>
			</logic:present>
			<logic:notPresent name="debugExceptionInfo" property="actionMapping">
				<tr><td>UncaughtException</td></tr>
			</logic:notPresent>
			
		</table>
    </div>
    
    <div id="requestContextInfo" class="super-table">
    	<bean:define id="contextEntries"  name="debugExceptionInfo" property = "requestContextEntries"/>
    	<table class="super-table">
			<tr>
				<th colspan="2">Request Context</th>
			</tr>
    	</table>
    	<table class="parameters-table">			
   			<logic:iterate name="contextEntries" id="entry">
   				<tr>
   					<td><span class="label key"><bean:write name="entry" property="key"/></span></td>
   					<td><span class="value"><bean:write name="entry"  property="value"/></span></td>
   				</tr>
    		</logic:iterate>
    	</table>
    </div>
    
    <div id="sessionContextInfo">
    	<bean:define id="sessionEntries"  name="debugExceptionInfo" property = "sessionContextEntries"/>
   		<table class="super-table">
			<tr>
				<th colspan="2">Session Context</th>
			</tr>
		</table>
		<table class="parameters-table">
   			<logic:iterate name="sessionEntries" id="entry">
   				<tr>
   					<td><span class="label key"><bean:write name="entry" property="key"/></span></td>
   					<td><span class="value"><bean:write name="entry" property="value"/></span></td>
   				</tr>
    		</logic:iterate>
    	</table>
    </div>
    
	<logic:present name="debugExceptionInfo" property="extraInfo">
    <div id="extraInfo">
    	<table>
    		<tr><th>Extra Information</th></tr>
			<logic:iterate name="debugExceptionInfo" property="extraInfo" id="object">
				<tr>
					<td>
						<logic:present name="object">		
							<bean:write name="object"/>
						</logic:present>
						<logic:notPresent name="object">
							NULL
						</logic:notPresent>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</div>
	</logic:present>

	<!-- TODO: 	Hide uninteresting parts of the stack trace.
				Like java, remove all lines that are repeated in causes. -->
	<div id="stackTrace">
		<table>
			<tr>
				<th>Stack Trace</th>
			</tr>
			<tr>
			<td>
			<bean:define id="exceptionInfoStack" name="debugExceptionInfo" property="flatExceptionStack"/>
			<logic:iterate name="exceptionInfoStack" id="exceptionInfo">
				<bean:define id="cause" name="exceptionInfo" property="cause"/>
				<bean:define id="suppressed" name="exceptionInfo" property="suppressed"/>
				<bean:define id="level" name="exceptionInfo" property="level"/>
<%-- 				<bean:define id="exception" name="exceptionInfo" property="subject"/> --%>
				<bean:define id="stackTrace" name="exceptionInfo" property="subjectInfo"/>
				<bean:define id="exceptionClass" name="exceptionInfo" property="subject.class"/>
				
				<span class="traceFirstLine level<bean:write name="level"/>"><!--
					--><span class="traceLineRole"><!--
						--><logic:equal name="cause" value="true"><!--
							--><bean:define id="role" value="cause"/><!--
							--><span class="causeText">Caused by</span><!--
						--></logic:equal><!--
						--><logic:equal name="suppressed" value="true"><!--
							--><bean:define id="role" value="suppressed"/><!--
							--><span class="suppressedText">Suppressed</span><!--
						--></logic:equal><!--
						--><logic:notEqual name="cause" value="true"><!--
							--><logic:notEqual name="suppressed" value="true"><!--
								--><bean:define id="role" value="normal"/><!--
								--><span class="exceptionText"><!--
									-->Exception in thread "<!--
									--><span class="traceThread"><bean:write name="debugExceptionInfo" property="threadName"/></span><!-- 
									-->" -<!--
								--></span><!--
							--></logic:notEqual><!--
						--></logic:notEqual><!--
					--></span><!--
					--><span class="traceException"><!--
						--><span class="traceExceptionClassLabel"><!--
							--><span class="traceExceptionClassPackage"><bean:write name="exceptionClass" property="package.name"/><!--
							-->.<!--
							--><span class="traceExceptionClass"><bean:write name="exceptionClass" property="simpleName"/></span><!--
							-->:<!--
						--></span><!-- 
						--><span class="traceExceptionMessage"><bean:write name="exception" property="localizedMessage"/></span><!--
					--></span><!--
				--></span>
				<br/>
		
				<logic:iterate name="stackTrace" id="element">
				
					<span class="traceLine level<bean:write name="level"/>"><!--
					
						--><logic:equal name="element" property="externalClass" value="false"><!--
							--><span class="externalException"><!--
						--></logic:equal><!--
					
						--><span class="traceLineRole"><!--
							--><span class="locationText <bean:write name="role"/>Indent">at</span><!--
							-->:<!--
						--></span><!-- 

						--><span class="traceLocation"><!--
							--><bean:define id="line" name="element" property="line"/><!--
							--><bean:define id="isNative" name="element" property="native"/><!--
							
							--><span class="javaLocation"><!--
								--><span class="classPackage"><bean:write name="element" property="packageName"/></span><!--
								-->.<!--
								--><span class="className"><bean:write name="element" property="simpleClassName"/></span><!--
								-->.<!--
								--><span class="methodName"><bean:write name="element" property="methodName"/></span><!--
							--></span><!--
							
							-->(<!--
							--><span class="fileLocation"><!--
								--><logic:notEmpty name="element" property="fileName"><!--
									--><span class="fileName"><bean:write name="element" property="fileName"/></span><!--
									--><logic:greaterThan name="line" value="0"><!--
										-->:<!--
										--><span class="lineNumber"><bean:write name="line"/></span><!--
									--></logic:greaterThan><!--
								--></logic:notEmpty><!--
								--><logic:empty name="element" property="fileName"><!--
									--><logic:equal name="isNative" value="true"><!--
										--><span class="nativeMethodText">Native Method</span><!--
									--></logic:equal><!--
									--><logic:notEqual name="isNative" value="true"><!--
										--><span class="unknownSourceText">Unknown Source</span><!--
									--></logic:notEqual><!--
								--></logic:empty><!--
							--></span><!--
							-->)<!--
							
						--></span><!--
						--><logic:equal name="element" property="externalClass" value="true"><!--
							--></span><!--
						--></logic:equal><!--
					--></span>
					<br/>
				</logic:iterate>
			</logic:iterate>
			</td>
			</tr>
    	</table>
    </div>
    
</body>

</html>
