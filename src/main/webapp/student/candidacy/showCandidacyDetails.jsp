<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="org.fenixedu.academic.domain.candidacy.CandidacyOperationType"%><html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html:html xhtml="true">
    <head>
        <title>
            <bean:message  key="label.candidacy.candidacyDetails" bundle="CANDIDATE_RESOURCES"/>
        </title>

        <link href="${pageContext.request.contextPath}/themes/<%= org.fenixedu.bennu.portal.domain.PortalConfiguration.getInstance().getTheme() %>/css/style.css" rel="stylesheet" type="text/css" />

        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
            .container {
                background-color: #fefefe;
                padding: 30px;
                border-radius: 10px;
                margin-top: 50px;
                max-width: 800px;
            }
            .title {
                border-bottom: 1px solid #eee;
                padding-bottom: 5px;
                font-size: 25px;
                min-height: 35px;
            }
            dd {
            	margin-bottom: 5px;
            }
        </style>
    </head>
    <body>
    
        <div class="container">
            <div class="title row">
                <div class="col-sm-3 text-right col-sm-push-9">
                    <img src="${pageContext.request.contextPath}/api/bennu-portal/configuration/logo"/>
                </div>
                <div class="col-sm-9 col-sm-pull-3">
                	Inscrição concluída
                </div>
            </div>
            <br/><br/>
            <div id="txt">
				<logic:messagesPresent message="true">
					<ul class="nobullet list6">
						<html:messages id="messages" message="true" bundle="CANDIDATE_RESOURCES">
							<li><span class="error0"><bean:write name="messages" /></span></li>
						</html:messages>
					</ul>
				</logic:messagesPresent>

				<dl class="dl-horizontal">
					<dt>${fr:message('resources.CandidateResources', 'label.org.fenixedu.academic.domain.Person.name')}</dt>
					<dd><c:out value="${candidacy.person.name}"/></dd>
					<dt>${fr:message('resources.CandidateResources', 'label.studentNumber')}</dt>
					<dd><c:out value="${candidacy.person.student.number}"/></dd>
					<dt>${fr:message('resources.CandidateResources', 'label.username')}</dt>
					<dd><c:out value="${candidacy.person.username}"/></dd>
					<dt>${fr:message('resources.CandidateResources', 'org.fenixedu.academic.domain.candidacy.StudentCandidacy.executionDegree.degreeCurricularPlan.degree.name')}</dt>
					<dd><c:out value="${candidacy.executionDegree.degreeName}"/></dd>
					<dt>${fr:message('resources.CandidateResources', 'org.fenixedu.academic.domain.candidacy.Candidacy.activeCandidacySituation.candidacySituationType')}</dt>
					<dd>${fr:message('resources.EnumerationResources', candidacy.activeCandidacySituation.candidacySituationType.qualifiedName)}</dd>
				</dl>

				<br />

				<logic:equal name="candidacy" property="activeCandidacySituation.candidacySituationType" value="REGISTERED">
					<h3>${fr:message('resources.CandidateResources', 'label.candidacy.congratulations')}!</h3>
					<p class="lead">
						${fr:message('resources.CandidateResources', 'label.candidacy.process.concluded')}
					</p>
				</logic:equal>
			</div>
		</div>
	</body>
</html:html>
