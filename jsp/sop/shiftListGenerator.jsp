<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.Action.sop.ClassShiftManagerDispatchAction" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

		<br>
        <span class="error"><html:errors/></span>
        
        
		<logic:present name="<%= SessionConstants.CLASS_INFO_SHIFT_LIST_KEY %>" >
			<bean:define id="list" name="<%= SessionConstants.CLASS_INFO_SHIFT_LIST_KEY %>" />
		</logic:present>
		<logic:present name="<%= SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY %>" >
			<bean:define id="available" value="true"/>
			<bean:define id="list" name="<%= SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY %>" />
		</logic:present>

		<logic:present name="available">						
			<h4>
				<bean:message key="title.shifts.available"/>
			</h4>
			
		</logic:present>
		<logic:notPresent name="available">						
			<h4>
				<bean:message key="title.shifts.inserted"/>
			</h4>
		
			<ul>
				<li>
				    <html:link  page="/chooseExecutionCourse.do">
		    			<bean:message key="label.addShift"/>
				    </html:link>
			    </li>
			</ul>
		</logic:notPresent>
    


		<logic:present name="list">
			<table>
						<tr>
								<th>
									<bean:message key="property.name"/>
								</th>
								<th>
									<bean:message key="property.type"/>
								</th>
								<th>
									<bean:message key="property.turno.capacity"/>
								</th>
								<th>
								<logic:present name="available">						
										<bean:message key="label.add"/>
								</logic:present>
								<logic:notPresent name="available">						
										<bean:message key="label.delete"/>
								</logic:notPresent>
								</th>
							</tr>
			
				<logic:iterate id="shiftView" name="list" indexId="shiftIndex">
							
							<tr>
								<td>
									<jsp:getProperty name="shiftView" property="nome"/>
								</td>
								<td>
									<jsp:getProperty name="shiftView" property="tipo"/>
								</td>
								<td>
									<jsp:getProperty name="shiftView" property="lotacao"/>
								</td>
								<logic:present name="available">
									<td>
										<html:link paramId="shiftIndex" paramName="shiftIndex" href="ClassShiftManagerDA.do?method=addClassShift">
											<bean:message key="label.add"/>
										</html:link>
									</td>
								</logic:present>
								<logic:notPresent name="available">						
									<td>
										<html:link paramId="shiftIndex" paramName="shiftIndex" href="ClassShiftManagerDA.do?method=removeClassShift">
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
		

		