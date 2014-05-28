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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="message.inquiries.title" bundle="INQUIRIES_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<div id="container">
			<div id="dotist_id">
				<img alt="<%=org.fenixedu.bennu.portal.domain.PortalConfiguration.getInstance().getApplicationTitle().getContent() %>"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1><bean:message key="message.inquiries.title" bundle="INQUIRIES_RESOURCES"/></h1>
				<div class="mtop1">
					<!--<bean:message key="" bundle="INQUIRIES_RESOURCES"/>-->
					<p>
						Caro(a) aluno(a)
						<br/><br/>
						Está a decorrer a fase de recolha dos Inquéritos aos Alunos relativa aos QUC  do 2º semestre de 2010/2011. Muitos alunos já procederam ao seu preenchimento. No entanto, devido a um erro técnico, verificou-se uma falha na informação recolhida. Torna-se pois necessário proceder à repetição da recolha de alguns dos inquéritos.
						<br/><br/>
						Ciente do incómodo causado, venho por este meio apelar para o preenchimento dos inquéritos, mesmo naqueles casos em que já terão sido preenchidos anteriormente. Dado que as respostas são anónimas, como é garantido no regulamento dos QUC, não é possível a correcção individual do inquérito a preencher.
						<br/><br/>
						Com as melhores saudações académicas,
						<br/><br/>
						Conselho Pedagógico	
					</p>
				</div>
			</div>
			<br />

			<div align="center">
				<table>
					<tr>
						<td>
							<form method="post" action="<%= request.getContextPath() %>/respondToInquiriesQuestion.do">
								<html:hidden property="method" value="respondNow"/>
								<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.now" property="ok">
									<bean:message key="button.inquiries.respond.now" />
								</html:submit>
							</form>
						</td>
						<td>
							<form method="post" action="<%= request.getContextPath() %>/respondToInquiriesQuestion.do">
								<html:hidden property="method" value="registerStudentResponseRespondLater"/>
								<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.later" property="ok">
									<bean:message key="button.inquiries.respond.later" />
								</html:submit>
							</form>
						</td>
					</tr>
				</table>
			</div>

		</div>
	</body>
</html:html>
