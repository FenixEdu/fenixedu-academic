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
<h1><bean:message key="debug.page.title" bundle="GLOBAL_RESOURCES"/></h1>

				
	<!-- HACK: I'm using comments to prevent extra whitespace! Please bear the horror and forgive me! -->
	<div id="errorInfo">
   		<table>
			<tr>
				<th colspan="2"><bean:message key="debug.page.header.error" bundle="GLOBAL_RESOURCES"/></th>
			</tr>
			<tr>
				<bean:define id="exception" name="debugExceptionInfo" property="exception"/>
				<bean:define id="exceptionClass" name="exception" property="class"/>
				<td><bean:message key="debug.page.label.error.description" bundle="GLOBAL_RESOURCES"/>:</td>
				<td><!--
					--><span class="traceException"><!--
						--><span class="traceExceptionClassLabel"><!--
							--><span class="traceExceptionClass"><bean:write name="exceptionClass" property="name"/></span><!--
							-->:<!--
						--></span><!-- 
						--><span class="traceExceptionMessage"><bean:write name="exception" property="localizedMessage"/></span><!--
					--></span><!--
				--></td>
			</tr>
			<tr>
				<td><bean:message key="debug.page.label.error.location" bundle="GLOBAL_RESOURCES"/>:</td>
				<td><!--
				--><logic:present name="debugExceptionInfo" property="actionMapping"><!-- 
					--><span class="traceLine level0"><!-- 
						--><span class="traceLocation"><!--
							--><span class="javaLocation"><!--
								--><span class="className"><bean:write name="debugExceptionInfo" property="actionErrorClass"/></span><!--
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
		<table>
			<tr>
				<th colspan="2"><bean:message key="debug.page.header.user" bundle="GLOBAL_RESOURCES"/></th>
			</tr>
			<logic:present name="debugExceptionInfo" property="userName">
			<tr>
				<td><bean:message key="debug.page.label.user.id" bundle="GLOBAL_RESOURCES"/>:</td>
				<td><bean:write name="debugExceptionInfo" property="userName"/></td>
			</tr>
			<tr>
				<td><bean:message key="debug.page.label.user.roles" bundle="GLOBAL_RESOURCES"/>:</td>
					<logic:iterate id="roleType" name="debugExceptionInfo" property="userRoles"><!--
						--><td><span class="userRole"><bean:write name="roleType"/></span></td><!--
					--></logic:iterate>
			</tr>
			</logic:present>
			<logic:notPresent name="debugExceptionInfo" property="userName">
				<tr>
					<td>
						<bean:message key="debug.page.message.noUser" bundle="GLOBAL_RESOURCES"/>
					</td>
				</tr>
			</logic:notPresent>
			
		</table>
    </div>
    
    <div id="requestInfo">
    	<table>
			<tr>
				<th colspan="2"><bean:message key="debug.page.header.request" bundle="GLOBAL_RESOURCES"/></th>
			</tr>
			<tr>
				<td><bean:message key="debug.page.label.request.uri" bundle="GLOBAL_RESOURCES"/>:</td>
				<td><bean:write name="debugExceptionInfo" property="requestURI"/></td>
			</tr>
			<tr>
				<td><bean:message key="debug.page.label.request.url" bundle="GLOBAL_RESOURCES"/>:</td>
				<td><bean:write name="debugExceptionInfo" property="requestURL"/></td>
			</tr>
			<tr>
				<td><bean:message key="debug.page.label.request.query.full" bundle="GLOBAL_RESOURCES"/>:</td>
				<td><bean:write name="debugExceptionInfo" property="queryString"/></td>
			</tr>
			<tr>
				<td><bean:message key="debug.page.label.request.query.params" bundle="GLOBAL_RESOURCES"/>:</td>
				<td>
					<table>
						<bean:define id="queryParameters"  name="debugExceptionInfo" property = "queryParameters"/>
						<tr>
							<th><bean:message key="debug.page.header.element.key" bundle="GLOBAL_RESOURCES"/></th>
							<th><bean:message key="debug.page.header.element.value" bundle="GLOBAL_RESOURCES"/></th>
						</tr>
  			 			<logic:iterate id="element" name="queryParameters">
 			 				<tr>
 			  					<td><bean:write name="element" property="key"/></td>
  			 					<td><bean:write name="element" property="value"/></td>
  			 				</tr>
			    		</logic:iterate>
    				</table>
    			</td>
			</tr>
		</table>
    </div>
    
    
    <div id="mappingInfo">
    	<table>
			<tr>
				<th colspan="2"><bean:message key="debug.page.header.mapping" bundle="GLOBAL_RESOURCES"/></th>
			</tr>
			<logic:present name="debugExceptionInfo" property="actionMapping">
				<bean:define id="mapping" name="debugExceptionInfo" property="actionMapping"/>
				<bean:define id="module" name="mapping" property="moduleConfig"/>
				<tr>
					<td><bean:message key="debug.page.label.mapping.module.prefix" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="module" property="prefix"/></td>
				</tr>
				<tr>
					<td><bean:message key="debug.page.label.mapping.path" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="path"/></td>
				</tr>
				<logic:present name="mapping" property="type">
				<tr>
					<td><bean:message key="debug.page.label.mapping.type" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="type"/></td>
				</tr>
				</logic:present>
				<logic:present name="mapping" property="forward">
				<tr>
					<td><bean:message key="debug.page.label.mapping.forward" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="forward"/></td>
				</tr>
				</logic:present>
				<logic:present name="mapping" property="include">
				<tr>
					<td><bean:message key="debug.page.label.mapping.include" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="include"/></td>
				</tr>
				</logic:present>
				<tr>
					<td><bean:message key="debug.page.label.mapping.name" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="name"/></td>
				</tr>
				<tr>
					<td><bean:message key="debug.page.label.mapping.scope" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="scope"/></td>
				</tr>
				<tr>
					<td><bean:message key="debug.page.label.mapping.attribute" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="attribute"/></td>
				</tr>
				<tr>
					<td><bean:message key="debug.page.label.mapping.suffix" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="suffix"/></td>
				</tr>
				<tr>
					<td><bean:message key="debug.page.label.mapping.roles" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="roles"/></td>
				</tr>
				<tr>
					<td><bean:message key="debug.page.label.mapping.multipartClass" bundle="GLOBAL_RESOURCES"/>:</td>
					<td><bean:write name="mapping" property="multipartClass"/></td>
				</tr>
			</logic:present>
			<logic:notPresent name="debugExceptionInfo" property="actionMapping">
				<tr>
					<td>
						<bean:message key="debug.page.message.uncaughtException" bundle="GLOBAL_RESOURCES"/>
					</td>
				</tr>
			</logic:notPresent>
			
		</table>
    </div>
    
    <div id="requestContextInfo">
    	<bean:define id="contextEntries"  name="debugExceptionInfo" property = "requestContextEntries"/>
    	<table>
			<tr>
				<th colspan="2"><bean:message key="debug.page.header.requestContext" bundle="GLOBAL_RESOURCES"/></th>
			</tr>
			<tr>
				<th><bean:message key="debug.page.header.element.key" bundle="GLOBAL_RESOURCES"/></th>
				<th><bean:message key="debug.page.header.element.value" bundle="GLOBAL_RESOURCES"/></th>
			</tr>
   			<logic:iterate name="contextEntries" id="entry">
   				<tr>
   					<td><bean:write name="entry" property="key"/></td>
   					<td><bean:write name="entry" property="value"/></td>
   				</tr>
    		</logic:iterate>
    	</table>
    </div>
    
    <div id="sessionContextInfo">
    	<bean:define id="sessionEntries"  name="debugExceptionInfo" property = "sessionContextEntries"/>
   		<table>
			<tr>
				<th colspan="2"><bean:message key="debug.page.header.sessionContext" bundle="GLOBAL_RESOURCES"/></th>
			</tr>
			<tr>
				<th><bean:message key="debug.page.header.element.key" bundle="GLOBAL_RESOURCES"/></th>
				<th><bean:message key="debug.page.header.element.value" bundle="GLOBAL_RESOURCES"/></th>
			</tr>
   			<logic:iterate name="sessionEntries" id="entry">
   				<tr>
   					<td><bean:write name="entry" property="key"/></td>
   					<td><bean:write name="entry" property="value"/></td>
   				</tr>
    		</logic:iterate>
    	</table>
    </div>
    
	<logic:present name="debugExceptionInfo" property="extraInfo">
    <div id="extraInfo">
    	<table>
    		<th>
    			<bean:message key="debug.page.header.extra" bundle="GLOBAL_RESOURCES"/>
    		</th>
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
				<th><bean:message key="debug.page.header.stackTrace" bundle="GLOBAL_RESOURCES"/></th>
			</tr>
			<tr>
			<td>
			<bean:define id="exceptionInfoStack" name="debugExceptionInfo" property="flatExceptionStack"/>
			<logic:iterate name="exceptionInfoStack" id="exceptionInfo">
				<bean:define id="cause" name="exceptionInfo" property="cause"/>
				<bean:define id="suppressed" name="exceptionInfo" property="suppressed"/>
				<bean:define id="level" name="exceptionInfo" property="level"/>
				<bean:define id="exception" name="exceptionInfo" property="subject"/>
				<bean:define id="stackTrace" name="exception" property="stackTrace"/>
				<bean:define id="exceptionClass" name="exception" property="class"/>
				
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
							--><span class="traceExceptionClass"><bean:write name="exceptionClass" property="name"/></span><!--
							-->:<!--
						--></span><!-- 
						--><span class="traceExceptionMessage"><bean:write name="exception" property="localizedMessage"/></span><!--
					--></span><!--
				--></span>
				<br/>
				<logic:iterate name="stackTrace" id="element">
					<span class="traceLine level<bean:write name="level"/>"><!--
						--><span class="traceLineRole"><!--
							--><span class="locationText <bean:write name="role"/>Indent">at</span><!--
							-->:<!--
						--></span><!-- 
						--><span class="traceLocation"><!--
							--><bean:define id="line" name="element" property="lineNumber"/><!--
							--><bean:define id="isNative" name="element" property="nativeMethod"/><!--
							--><span class="javaLocation"><!--
								--><span class="className"><bean:write name="element" property="className"/></span><!--
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
