<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.ClassShiftManagerDispatchAction" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br>
<span class="error">
	<html:errors/>
</span>
<logic:present name="<%= SessionConstants.CLASS_INFO_SHIFT_LIST_KEY %>" >
	<bean:define id="list" name="<%= SessionConstants.CLASS_INFO_SHIFT_LIST_KEY %>" />
</logic:present>
<logic:present name="<%= SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY %>" >
	<bean:define id="available" value="true"/>
	<bean:define id="list" name="<%= SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY %>" />
</logic:present>
<logic:present name="available">
	<h2>
		<bean:message key="title.shifts.available"/>
	</h2>
</logic:present>
<logic:notPresent name="available">
	<h2>
		<bean:message key="title.shifts.inserted"/>
	</h2>
	<ul>
		<li>
		   		<html:link page="<%= "/chooseExecutionCourse.do?"
					+ SessionConstants.CLASS_VIEW_OID
				  	+ "="
				  	+ pageContext.findAttribute("classOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_COURSE_OID
				  	+ "="
				  	+ pageContext.findAttribute("executionCourseOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_PERIOD_OID
				  	+ "="
				  	+ pageContext.findAttribute("executionPeriodOID")
				  	+ "&amp;"
				  	+ SessionConstants.CURRICULAR_YEAR_OID
					+ "="
				  	+ pageContext.findAttribute("curricularYearOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_DEGREE_OID
				  	+ "="
					+ pageContext.findAttribute("executionDegreeOID") %>">
				<bean:message key="label.addShift"/>
			</html:link>

		</li>
	</ul>
</logic:notPresent>
<logic:present name="list">
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="property.name"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="property.type"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="property.lessons"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="property.turno.capacity"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="property.shift.ocupation"/>
			</td>
            <td class="listClasses-header">
            	<bean:message key="property.shift.percentage"/>
            </td>
			<td class="listClasses-header">
				<logic:present name="available">
					<bean:message key="label.add"/>
				</logic:present>
				<logic:notPresent name="available">
					<bean:message key="label.delete"/>
				</logic:notPresent>
			</td>
		</tr>
		<logic:iterate id="shiftView" name="list" indexId="shiftIndex">
			<tr>
				<td class="listClasses">
					<jsp:getProperty name="shiftView" property="nome"/>
				</td>
				<td class="listClasses">
					<jsp:getProperty name="shiftView" property="tipo"/>
				</td>
				<td class="listClasses">
					<logic:empty name="shiftView" property="lessons">
						&nbsp;
					</logic:empty>
					<logic:notEmpty name="shiftView" property="lessons">
						<jsp:getProperty name="shiftView" property="lessons"/>
					</logic:notEmpty>
				</td>
				<td class="listClasses">
					<jsp:getProperty name="shiftView" property="lotacao"/>
				</td>
				<td class="listClasses">
					<jsp:getProperty name="shiftView" property="ocupation"/>
				</td>
              	<td class="listClasses">
              		<jsp:getProperty name="shiftView" property="percentage"/> %
              	</td>
				<logic:present name="available">
					<td class="listClasses">
		   		<html:link page="<%= "/ClassShiftManagerDA.do?method=addClassShift&amp;shiftIndex="
		   			+ pageContext.findAttribute("shiftIndex")
		   			+ "&amp;"
					+ SessionConstants.CLASS_VIEW_OID
				  	+ "="
				  	+ pageContext.findAttribute("classOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_COURSE_OID
				  	+ "="
				  	+ pageContext.findAttribute("executionCourseOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_PERIOD_OID
				  	+ "="
				  	+ pageContext.findAttribute("executionPeriodOID")
				  	+ "&amp;"
				  	+ SessionConstants.CURRICULAR_YEAR_OID
					+ "="
				  	+ pageContext.findAttribute("curricularYearOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_DEGREE_OID
				  	+ "="
					+ pageContext.findAttribute("executionDegreeOID") %>">
							<bean:message key="label.add"/>
						</html:link>
					</td>
				</logic:present>
				<logic:notPresent name="available">
					<td class="listClasses">

		   		<html:link page="<%= "/ClassShiftManagerDA.do?method=removeClassShift&amp;shiftIndex="
		   			+ pageContext.findAttribute("shiftIndex")
		   			+ "&amp;"
					+ SessionConstants.CLASS_VIEW_OID
				  	+ "="
				  	+ pageContext.findAttribute("classOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_COURSE_OID
				  	+ "="
				  	+ pageContext.findAttribute("executionCourseOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_PERIOD_OID
				  	+ "="
				  	+ pageContext.findAttribute("executionPeriodOID")
				  	+ "&amp;"
				  	+ SessionConstants.CURRICULAR_YEAR_OID
					+ "="
				  	+ pageContext.findAttribute("curricularYearOID")
				  	+ "&amp;"
					+ SessionConstants.EXECUTION_DEGREE_OID
				  	+ "="
					+ pageContext.findAttribute("executionDegreeOID") %>">
							<bean:message key="label.delete"/>
						</html:link>
					</td>
				</logic:notPresent>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
<logic:notPresent name="list">
	<table>
		<tr>
			<td>
				<span class="error">
					<logic:present name="available">
						<bean:message key="error.shifts.class.not.available"/>
					</logic:present>
					<logic:notPresent name="available">
						<bean:message key="error.shifts.class.not.associated"/>
					</logic:notPresent>
				</span>
			</td>
		</tr>
	</table>
</logic:notPresent>