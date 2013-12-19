/**
 * Sep 29, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class SearchPartyCarPlate {

    @Atomic
    public static List<Party> run(String nameSearch, String carPlateNumber, Long parkingCardNumber) {
        check(RolePredicates.PARKING_MANAGER_PREDICATE);
        List<Party> result = new ArrayList<Party>();
        if (!StringUtils.isEmpty(carPlateNumber) || !StringUtils.isEmpty(nameSearch) || parkingCardNumber != null) {
            Collection<ParkingParty> parkingParties = Bennu.getInstance().getParkingPartiesSet();
            for (ParkingParty parkingParty : parkingParties) {
                if (parkingParty.getParty() != null) {
                    if (satisfiedParkingCardNumber(parkingParty, parkingCardNumber)
                            && satisfiedPlateNumber(parkingParty, carPlateNumber) && satisfiedName(parkingParty, nameSearch)) {
                        result.add(parkingParty.getParty());
                    }
                }
            }
        }
        return result;
    }

    private static boolean satisfiedName(ParkingParty parkingParty, String nameSearch) {
        if (!StringUtils.isEmpty(nameSearch)) {
            String[] nameValues = StringNormalizer.normalize(nameSearch).toLowerCase().split("\\p{Space}+");
            return areNamesPresent(parkingParty.getParty().getName(), nameValues);
        }
        return true;
    }

    private static boolean satisfiedParkingCardNumber(ParkingParty parkingParty, Long parkingCardNumber) {
        if (parkingCardNumber != null) {
            return (parkingParty.getCardNumber() != null && parkingParty.getCardNumber().toString()
                    .contains(parkingCardNumber.toString())) ? true : false;
        }
        return true;
    }

    private static boolean satisfiedPlateNumber(ParkingParty parkingParty, String carPlateNumber) {
        return !StringUtils.isEmpty(carPlateNumber) ? (parkingParty.hasVehicleContainingPlateNumber(carPlateNumber.trim())) : true;
    }

    private static boolean areNamesPresent(String name, String[] searchNameParts) {
        String nameNormalized = StringNormalizer.normalize(name).toLowerCase();
        for (String searchNamePart : searchNameParts) {
            String namePart = searchNamePart;
            if (!nameNormalized.contains(namePart)) {
                return false;
            }
        }
        return true;
    }

}