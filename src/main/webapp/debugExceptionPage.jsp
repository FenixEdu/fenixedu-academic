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
<%@page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<html>

<head>
	<title>
		<bean:message key="debug.page.title" bundle="GLOBAL_RESOURCES"/>
	</title>
   	<link rel="stylesheet"  href="<%= request.getContextPath() %>/CSS/debug.css">
   	<script src="<%= request.getContextPath() %>/javaScript/jquery/jquery.js"></script>
	<meta charset="UTF-8">
</head>

<body>
	<bean:define id="exception" name="debugExceptionInfo" property="exception"/>
	<bean:define id="exceptionClass" name="exception" property="class"/>
	
	<div id="debugPage">
		<h1 class="debugTitleException"><bean:write name="exceptionClass" property="name"/></h1>
		<div class="debugTitleCause">
		<logic:notPresent name="debugExceptionInfo" property="jspExceptionMessage">
			<logic:notEmpty name="exception" property="message">
				<bean:write name="exception" property="message"/>
			</logic:notEmpty>
			<logic:empty name="exception" property="message">
				&nbsp;
			</logic:empty>
		</logic:notPresent>
		<logic:present name="debugExceptionInfo" property="jspExceptionMessage">
			<bean:write name="debugExceptionInfo" property="jspExceptionMessage"/>
		</logic:present>
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
						--><span class="traceExceptionMessage">
					<logic:present name="debugExceptionInfo" property="jspExceptionMessage">
						<bean:write name="debugExceptionInfo" property="jspExceptionMessage"/>
					</logic:present>
					<logic:notPresent name="debugExceptionInfo" property="jspExceptionMessage">
						<bean:write name="exception" property="localizedMessage"/>
					</logic:notPresent>
						</span><!--
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
							--><logic:present name="debugExceptionInfo" property="actionErrorFile"><!--
							-->(<!--
							--><span class="fileLocation"><!--
								--><span class="fileName"><bean:write name="debugExceptionInfo" property="actionErrorFile"/></span><!--
								--><logic:notEqual name="debugExceptionInfo" property="actionErrorLine" value="0"><!--
								-->:<!--
								--><span class="lineNumber"><bean:write name="debugExceptionInfo" property="actionErrorLine"/></span><!--
								--></logic:notEqual><!--
							--></span><!--
							-->)<!--
							--></logic:present>
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
    
   	<logic:present name="debugExceptionInfo" property="jspExceptionMessage">
		<h2>Template</h2>
		
		<table class="source cut-top cut-bottom">
   
   <bean:define id="lines"  name="debugExceptionInfo" property = "jspExceptionSourceBefore"/>
			<logic:iterate id="l" name="lines">
      <tr><th><bean:write name="l" property="lineNumber" /></th>
      <td>    <bean:write name="l" property="line"/>
</td></tr>
			</logic:iterate>
	<bean:define id="l"  name="debugExceptionInfo" property = "jspExceptionSourceLine"/>
      <tr class="error"><th><bean:write name="l" property="lineNumber" /></th>
      <td>    <bean:write name="l" property="line"/>
</td></tr>
   <bean:define id="lines"  name="debugExceptionInfo" property = "jspExceptionSourceAfter"/>
			<logic:iterate id="l" name="lines">
      <tr><th><bean:write name="l" property="lineNumber" /></th>
      <td>    <bean:write name="l" property="line"/>
</td></tr>
			</logic:iterate>
   
   
   </tbody></table>

	</logic:present>

    <div id="userInfo">
    	<h2>User Information</h2>
		<table border="0" cellspacing="0">
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
    	<h2>Request description</h2>
		<bean:define id="requestParameters"  name="debugExceptionInfo" property = "requestParameters"/>
    	<table id="requestInfoTable" class="super-table">
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
				<logic:empty name="requestParameters"><td><i>Unavailable</i></td></logic:empty>
			</tr>
		</table>
		<logic:notEmpty name="requestParameters">
			<table class="parameters-table">
	 			<logic:iterate id="element" name="requestParameters">
					<tr>
	 					<td><span class="label key"><bean:write name="element" property="key"/></span></td>
	 					<td class="value"><bean:write name="element" property="value"/></td>
	 				</tr>
	    		</logic:iterate>
			</table>
		</logic:notEmpty>
    </div>
    
    
    <div id="mappingInfo">
    	<h2>Mapping Description</h2>
    	<table id="mappingInfoTable" class="super-table">
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
				<tr><td><i>Unavailable</i></td></tr>
			</logic:notPresent>
			
		</table>
    </div>
    
    <div id="requestContextInfo" class="super-table">
    	<h2>Request Context</h2>
    	<bean:define id="contextEntries"  name="debugExceptionInfo" property = "requestContextEntries"/>
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
    	<h2>Session Context</h2>
    	<bean:define id="sessionEntries"  name="debugExceptionInfo" property = "sessionContextEntries"/>
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
	<h2>Extra Information</h2>
    <div id="extraInfo">
    	<table>
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
		<h2>Stack Trace</h2>
		<table>
			<tr>
			<td>
			<bean:define id="exceptionInfoStack" name="debugExceptionInfo" property="flatExceptionStack"/>
			<script>
				$(function(){
					$("#stackTrace span.traceExceptionMessage").map(function(i,x){
						$(x).html($(x).html().replace(/\n/g, "<br />"))
					})
				})
			</script>
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
									-->": <br/><!--
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
