package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderCardInformation;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderEntry;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "person", path = "/identificationCard", parameter = "method")
@Forwards(value = { @Forward(name = "show.card.information", path = "/person/identificationCard/showCardInformation.jsp") })
public class IdentificationCardDA extends FenixDispatchAction {

    public ActionForward prepare(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final Person person = AccessControl.getPerson();
        final String cardProdutionState = getCardProdutionState(person);

        request.setAttribute("person", person);
        request.setAttribute("state", cardProdutionState);
        return mapping.findForward("show.card.information");
    }

    private String getCardProdutionState(Person person) {
        /*verify first step - information sent to SIBS*/
        Set<SantanderEntry> entries = person.getSantanderEntriesSet();
        SantanderEntry lastEntry = null;
        for (SantanderEntry entry : entries) {
            if (entry.getSantanderBatch().getSent() == null) {
                continue;
            }
            if (lastEntry == null || entry.getSantanderBatch().getSent().isAfter(lastEntry.getSantanderBatch().getSent())) {
                lastEntry = entry;
            }
        }
        Set<SantanderCardInformation> cards = person.getSantanderCardsInformationSet();
        if (lastEntry == null) {
            return BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                    "label.identification.card.production.state.one");
        }
        if (cards.isEmpty()) {
            return BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                    "label.identification.card.production.state.two", new String[] { lastEntry.getSantanderBatch().getSent()
                            .toString("dd/MM/yyyy") });
        }
        /*verify second step - SIBS emitted the card*/
        SantanderCardInformation lastCard = null;
        for (SantanderCardInformation card : cards) {
            DateTime prod_card_date = SantanderCardInformation.getProductionDateTime(card.getDchpRegisteLine());
            if (lastCard == null
                    || prod_card_date.isAfter(SantanderCardInformation.getProductionDateTime(lastCard.getDchpRegisteLine()))) {
                lastCard = card;
            }
        }
        DateTime prod_card_date = SantanderCardInformation.getProductionDateTime(lastCard.getDchpRegisteLine());
        return BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                "label.identification.card.production.state.three", new String[] { prod_card_date.toString("dd/MM/yyyy"), });
    }

}
