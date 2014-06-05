/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.internship;

import java.io.IOException;
import java.io.OutputStream;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.internationalRelationsOffice.internship.CandidateSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.internship.InternshipCandidacyBean;
import net.sourceforge.fenixedu.domain.internship.DuplicateInternshipCandidacy;
import net.sourceforge.fenixedu.domain.internship.InternshipCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.InternationalRelationsApplication.InternRelationsInternshipApp;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

/**
 * @author Pedro Santos (pmrsa)
 */
@StrutsFunctionality(app = InternRelationsInternshipApp.class, path = "candidates", titleKey = "title.candidates")
@Mapping(path = "/internship/internshipCandidacy", module = "internationalRelatOffice")
@Forwards({ @Forward(name = "candidates", path = "/internationalRelatOffice/internship/candidacy/listCandidates.jsp"),
        @Forward(name = "view", path = "/internationalRelatOffice/internship/candidacy/viewCandidate.jsp"),
        @Forward(name = "edit", path = "/internationalRelatOffice/internship/candidacy/editCandidate.jsp"),
        @Forward(name = "delete", path = "/internationalRelatOffice/internship/candidacy/deleteCandidate.jsp") })
public class InternshipCandidacyDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(InternshipCandidacyDA.class);

    private static final String[] HEADERS = new String[] { "Nº Candidatura", "Universidade", "Nº Aluno", "Ano", "Curso", "Ramo",
            "Nome", "Sexo", "Data Nascimento", "Naturalidade", "Nacionalidade", "B.I. (ou Cartão do Cidadão)", "Arquivo",
            "Emissão", "Validade", "Passaporte", "Arquivo", "Emissão", "Validade", "Morada", "Cod. Postal", "Localidade",
            "Telefone", "Telemóvel", "e-mail", "1º Preferência", "2º Preferência", "3º Preferência", "Inglês", "Francês",
            "Espanhol", "Alemão", "Candidatura Prévia" };

    private static final String[] HEADERS_NO_UNIV = new String[] { "Nº Candidatura", "Nº Aluno", "Ano", "Curso", "Ramo", "Nome",
            "Sexo", "Data Nascimento", "Naturalidade", "Nacionalidade", "B.I. (ou Cartão do Cidadão)", "Arquivo", "Emissão",
            "Validade", "Passaporte", "Arquivo", "Emissão", "Validade", "Morada", "Cod. Postal", "Localidade", "Telefone",
            "Telemóvel", "e-mail", "1º Preferência", "2º Preferência", "3º Preferência", "Inglês", "Francês", "Espanhol",
            "Alemão", "Candidatura Prévia" };

    @EntryPoint
    public ActionForward prepareCandidates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        CandidateSearchBean search = new CandidateSearchBean();
        request.setAttribute("search", search);
        request.setAttribute("candidates", filterCandidates(search));
        return mapping.findForward("candidates");
    }

    public ActionForward sessionPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        CandidateSearchBean search = getRenderedObject("search");
        RenderUtils.invalidateViewState();
        Interval interval = search.getSession().getCandidacyInterval();
        if (search.getCutStart() == null || !interval.contains(search.getCutStart().toDateTimeAtStartOfDay())) {
            search.setCutStart(interval.getStart().toLocalDate());
        }
        if (search.getCutEnd() == null || !interval.contains(search.getCutEnd().toDateTimeAtStartOfDay())) {
            search.setCutEnd(interval.getEnd().toLocalDate());
        }
        if (interval.contains(new LocalDate().minusDays(1).toDateMidnight())) {
            search.setCutEnd(new LocalDate().minusDays(1));
        }
        request.setAttribute("search", search);
        return mapping.findForward("candidates");
    }

    public ActionForward searchCandidates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        CandidateSearchBean search = getRenderedObject("search");
        if (search.getCutStart() != null && search.getCutEnd() != null && search.getCutEnd().isBefore(search.getCutStart())) {
            addErrorMessage(request, "start", "error.internationalrelations.internship.candidacy.search.startafterend");
            return prepareCandidates(mapping, actionForm, request, response);
        }
        request.setAttribute("search", search);
        request.setAttribute("candidates", filterCandidates(search));
        return mapping.findForward("candidates");
    }

    public ActionForward candidateView(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        String oid = request.getParameter("candidacy.OID");
        request.setAttribute("candidacy", new InternshipCandidacyBean((InternshipCandidacy) FenixFramework.getDomainObject(oid)));
        return mapping.findForward("view");
    }

    public ActionForward prepareCandidacyEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        String oid = request.getParameter("candidacy.OID");
        request.setAttribute("candidacy", new InternshipCandidacyBean((InternshipCandidacy) FenixFramework.getDomainObject(oid)));
        return mapping.findForward("edit");
    }

    public ActionForward candidateEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        InternshipCandidacyBean bean = getRenderedObject();
        try {
            bean.getCandidacy().edit(bean);
        } catch (DuplicateInternshipCandidacy e) {
            addErrorMessage(request, "studentNumber", "error.internationalrelations.internship.candidacy.duplicateStudentNumber",
                    e.getNumber(), e.getUniversity());
            return mapping.findForward("edit");
        }
        return prepareCandidates(mapping, actionForm, request, response);
    }

    public ActionForward prepareCandidacyDelete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        String oid = request.getParameter("candidacy.OID");
        request.setAttribute("candidacy", new InternshipCandidacyBean((InternshipCandidacy) FenixFramework.getDomainObject(oid)));
        return mapping.findForward("delete");
    }

    public ActionForward candidateDelete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        InternshipCandidacyBean bean = getRenderedObject();
        bean.getCandidacy().delete();
        addActionMessage(request, "success.internationalrelations.internship.candidacy.delete");
        return prepareCandidates(mapping, actionForm, request, response);
    }

    public ActionForward exportToCandidatesToXls(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        CandidateSearchBean search = getRenderedObject("search");
        if (search.getCutEnd().isBefore(search.getCutStart())) {
            addErrorMessage(request, "start", "error.internationalrelations.internship.candidacy.search.startafterend");
            return prepareCandidates(mapping, actionForm, request, response);
        }
        if (search.getCutEnd().plusDays(1).toDateMidnight().isAfterNow()) {
            addErrorMessage(request, "end", "error.internationalrelations.internship.candidacy.export.todaywontwork");
            return prepareCandidates(mapping, actionForm, request, response);
        }
        Spreadsheet sheet = new Spreadsheet(search.getName());
        if (search.getUniversity() == null) {
            sheet.setHeaders(HEADERS);
        } else {
            sheet.setHeaders(HEADERS_NO_UNIV);
        }
        for (InternshipCandidacyBean bean : filterCandidates(search)) {
            Row row = sheet.addRow();
            row.setCell(bean.getCandidacy().getCandidacyCode());
            if (search.getUniversity() == null) {
                row.setCell(bean.getUniversity().getFullPresentationName());
            }
            row.setCell(bean.getStudentNumber());
            row.setCell(bean.getStudentYear().ordinal() + 1);
            row.setCell(bean.getDegree());
            row.setCell(bean.getBranch());
            row.setCell(bean.getName());
            row.setCell(bean.getGender().toLocalizedString());
            row.setCell(bean.getBirthday().toString("dd-MM-yyyy"));
            row.setCell(bean.getParishOfBirth());
            row.setCell(StringUtils.capitalize(bean.getCountryOfBirth().getCountryNationality().getPreferedContent()
                    .toLowerCase()));
            row.setCell(bean.getDocumentIdNumber());
            row.setCell(bean.getEmissionLocationOfDocumentId() != null ? bean.getEmissionLocationOfDocumentId() : null);
            row.setCell(bean.getEmissionDateOfDocumentId() != null ? bean.getEmissionDateOfDocumentId().toString("dd-MM-yyyy") : null);
            row.setCell(bean.getExpirationDateOfDocumentId() != null ? bean.getExpirationDateOfDocumentId()
                    .toString("dd-MM-yyyy") : null);
            row.setCell(bean.getPassportIdNumber() != null ? bean.getPassportIdNumber() : "");
            row.setCell(bean.getEmissionLocationOfPassport() != null ? bean.getEmissionLocationOfPassport() : "");
            row.setCell(bean.getEmissionDateOfPassport() != null ? bean.getEmissionDateOfPassport().toString("dd-MM-yyyy") : "");
            row.setCell(bean.getExpirationDateOfPassport() != null ? bean.getExpirationDateOfPassport().toString("dd-MM-yyyy") : "");
            row.setCell(bean.getStreet());
            row.setCell(bean.getAreaCode());
            row.setCell(bean.getArea());
            row.setCell(bean.getTelephone());
            row.setCell(bean.getMobilePhone());
            row.setCell(bean.getEmail());
            row.setCell(StringUtils.capitalize(bean.getFirstDestination() != null ? bean.getFirstDestination().getName()
                    .toLowerCase() : ""));
            row.setCell(StringUtils.capitalize(bean.getSecondDestination() != null ? bean.getSecondDestination().getName()
                    .toLowerCase() : ""));
            row.setCell(StringUtils.capitalize(bean.getThirdDestination() != null ? bean.getThirdDestination().getName()
                    .toLowerCase() : ""));
            row.setCell(BundleUtil.getString(Bundle.ENUMERATION, bean.getEnglish().getQualifiedKey()));
            row.setCell(BundleUtil.getString(Bundle.ENUMERATION, bean.getFrench().getQualifiedKey()));
            row.setCell(BundleUtil.getString(Bundle.ENUMERATION, bean.getSpanish().getQualifiedKey()));
            row.setCell(BundleUtil.getString(Bundle.ENUMERATION, bean.getGerman().getQualifiedKey()));
            row.setCell(bean.getPreviousCandidacy() ? "Sim" : "Não");
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + search.getName() + ".xls");

        try {
            OutputStream outputStream = response.getOutputStream();
            sheet.exportToXLSSheet(outputStream);
            outputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private SortedSet<InternshipCandidacyBean> filterCandidates(CandidateSearchBean search) {
        SortedSet<InternshipCandidacyBean> candidates = new TreeSet<InternshipCandidacyBean>();
        if (search.getSession() != null) {
            for (InternshipCandidacy candidacy : search.getSession().getInternshipCandidacySet()) {
                if (isIncluded(candidacy, search)) {
                    candidates.add(new InternshipCandidacyBean(candidacy));
                }
            }
        }
        return candidates;
    }

    private boolean isIncluded(InternshipCandidacy candidacy, CandidateSearchBean search) {
        if (search.getUniversity() != null && !candidacy.getUniversity().equals(search.getUniversity())) {
            return false;
        }
        if (search.getCutStart() != null && candidacy.getCandidacyDate().isBefore(search.getCutStart().toDateMidnight())) {
            return false;
        }
        if (search.getCutEnd() != null && !candidacy.getCandidacyDate().isBefore(search.getCutEnd().plusDays(1).toDateMidnight())) {
            return false;
        }
        return true;
    }
}
