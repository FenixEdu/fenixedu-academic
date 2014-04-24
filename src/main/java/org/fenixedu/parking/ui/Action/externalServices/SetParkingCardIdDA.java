package org.fenixedu.parking.ui.Action.externalServices;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.idcards.domain.CardGenerationEntry;
import org.fenixedu.idcards.domain.SantanderEntry;
import org.fenixedu.parking.ParkingConfigurationManager;
import org.fenixedu.parking.domain.ParkingParty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

@Mapping(path = "/setParkingCardId", module = "external")
public class SetParkingCardIdDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(SetParkingCardIdDA.class);

    public ActionForward setId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String password = request.getParameter("password");
        final String identificationCardCode = request.getParameter("identificationCardCode");
        final Long parkingCardID = Long.valueOf(request.getParameter("parkingCardId"));

        String message = "ko";

        try {
            message = setParkingCardId(password, identificationCardCode, parkingCardID);
        } catch (NotAuthorizedException ex) {
            message = "Not authorized";
        } catch (UserDoesNotExistException ex) {
            message = "User does not exist.";
        } catch (Throwable ex) {
            message = ex.getMessage();
            logger.error(ex.getMessage(), ex);
        } finally {
            final ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType("text/html");
            servletOutputStream.print(message);
            servletOutputStream.flush();
            response.flushBuffer();
        }

        return null;
    }

    public ActionForward setSantanderId(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String password = request.getParameter("password");
        final String identificationCardCode = request.getParameter("identificationCardCode");
        final Long parkingCardID = Long.valueOf(request.getParameter("parkingCardId"));
        final String categoryCode = request.getParameter("catCode");

        String message = "ko";

        try {
            message = runSantander(password, categoryCode, identificationCardCode, parkingCardID);
        } catch (NotAuthorizedException ex) {
            message = "Not authorized";
        } catch (UserDoesNotExistException ex) {
            message = "User does not exist.";
        } catch (Throwable ex) {
            message = ex.getMessage();
            logger.error(ex.getMessage(), ex);
        } finally {
            final ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType("text/html");
            servletOutputStream.print(message);
            servletOutputStream.flush();
            response.flushBuffer();
        }

        return null;
    }

    public static class NotAuthorizedException extends FenixServiceException {
    }

    public static class UserDoesNotExistException extends FenixServiceException {

    }

    private static final String password;
    static {
        password = ParkingConfigurationManager.getConfiguration().getParkingCardIdAdminPassword();
    }

    private static boolean isAllowed(final String password) {
        return SetParkingCardIdDA.password != null && SetParkingCardIdDA.password.equals(password);
    }

    @Atomic
    private static String setParkingCardId(final String password, final String identificationCardCode, final Long parkingCardID)
            throws FenixServiceException {
        if (isAllowed(password)) {
            CardGenerationEntry cardGenerationEntry =
                    CardGenerationEntry.readByEntityCodeAndCategoryCodeAndMemberNumber(identificationCardCode.substring(0, 13));
            if (cardGenerationEntry == null) {
                throw new UserDoesNotExistException();
            }
            if (cardGenerationEntry.getPerson().getParkingParty() == null) {
                createParkingParty(cardGenerationEntry.getPerson());
            }
            cardGenerationEntry.getPerson().getParkingParty().setCardNumber(parkingCardID);
            return cardGenerationEntry.getPerson().getIstUsername();
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Atomic
    private static String runSantander(final String password, final String categoryCode, final String identificationCardCode,
            final Long parkingCardID) throws FenixServiceException {
        if (isAllowed(password)) {
            SantanderEntry entry = SantanderEntry.readByUsernameAndCategory(identificationCardCode, categoryCode);
            if (entry == null) {
                throw new UserDoesNotExistException();
            }
            if (entry.getPerson().getParkingParty() == null) {
                createParkingParty(entry.getPerson());
            }
            entry.getPerson().getParkingParty().setCardNumber(parkingCardID);
            return entry.getPerson().getIstUsername();
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Atomic
    private static ParkingParty createParkingParty(Party party) {
        return new ParkingParty(party);
    }

}