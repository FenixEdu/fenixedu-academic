<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.Action.sop.ClassShiftManagerDispatchAction" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	<html:errors />
		<logic:present name="<%= ClassShiftManagerDispatchAction.AVAILABLE_LIST %>" scope="request">						
			<h4>
				<bean:message key="title.shifts.available"/>
			</h4>
			
		</logic:present>
		<logic:notPresent name="<%= ClassShiftManagerDispatchAction.AVAILABLE_LIST %>" scope="request">						
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
    
		<logic:present name="<%= ClassShiftManagerDispatchAction.SHIFT_LIST_ATT %>" scope="request">
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
								<logic:present name="<%= ClassShiftManagerDispatchAction.AVAILABLE_LIST %>" scope="request">						
										<bean:message key="label.add"/>
								</logic:present>
								<logic:notPresent name="<%= ClassShiftManagerDispatchAction.AVAILABLE_LIST %>" scope="request">						
										<bean:message key="label.delete"/>
								</logic:notPresent>
								</th>
							</tr>
			
				<logic:iterate id="shiftView" name="<%= ClassShiftManagerDispatchAction.SHIFT_LIST_ATT %>" scope="request">
							
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
								<logic:present name="<%= ClassShiftManagerDispatchAction.AVAILABLE_LIST %>" scope="request">						
									<td>
										<html:link paramId="shiftName" paramName="shiftView" paramProperty="nome"  href="ClassShiftManagerDA.do?method=addClassShift">
											<bean:message key="label.add"/>
										</html:link>
									</td>
								</logic:present>
								<logic:notPresent name="<%= ClassShiftManagerDispatchAction.AVAILABLE_LIST %>" scope="request">						
									<td>
										<html:link paramId="shiftName" paramName="shiftView" paramProperty="nome" href="ClassShiftManagerDA.do?method=removeClassShift">
											<bean:message key="label.delete"/>
										</html:link>
									</td>
								</logic:notPresent>
							</tr>
				</logic:iterate>
			</table>
		</logic:present>
		<logic:notPresent name="<%= ClassShiftManagerDispatchAction.SHIFT_LIST_ATT %>" scope="request">
			<table>
				<tr>
					<td>
						<font color="red">
							<logic:present name="<%= ClassShiftManagerDispatchAction.AVAILABLE_LIST %>" scope="request">						
								<bean:message key="error.shifts.class.not.available"/>
							</logic:present>
							<logic:notPresent name="<%= ClassShiftManagerDispatchAction.AVAILABLE_LIST %>" scope="request">						
								<bean:message key="error.shifts.class.not.associated"/>
							</logic:notPresent>
						</font>
					</td>
				</tr>
			</table>
		</logic:notPresent>
		

		