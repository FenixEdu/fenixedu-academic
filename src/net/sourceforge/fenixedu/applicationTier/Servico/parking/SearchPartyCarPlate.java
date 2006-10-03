/**
 * Sep 29, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.util.StringNormalizer;

import org.apache.commons.lang.StringUtils;

/**
 * @author Ricardo Rodrigues
 *
 */

public class SearchPartyCarPlate extends Service {

    public List<Party> run(String nameSearch, String carPlateNumber) {
        List<Party> result = new ArrayList<Party>();

        if (!StringUtils.isEmpty(carPlateNumber)) {
            List<ParkingParty> parkingParties = rootDomainObject.getParkingParties();
            for (ParkingParty parkingParty : parkingParties) {
                if (parkingParty.hasCarContainingPlateNumber(carPlateNumber.trim())) {
                    if (StringUtils.isEmpty(nameSearch)) {
                        result.add(parkingParty.getParty());
                    } else {
                        String[] nameValues = StringNormalizer.normalize(nameSearch).toLowerCase()
                                .split("\\p{Space}+");
                        if (areNamesPresent(parkingParty.getParty().getName(), nameValues)) {
                            result.add(parkingParty.getParty());
                        }
                    }
                }
            }
        } else if (!StringUtils.isEmpty(nameSearch)) {
            List<Party> parties = rootDomainObject.getPartys();
            String[] nameValues = StringNormalizer.normalize(nameSearch).toLowerCase().split(
                    "\\p{Space}+");
            for (Party party : parties) {
                if (areNamesPresent(party.getName(), nameValues)) {
                    if (!StringUtils.isEmpty(carPlateNumber)) {
                        if (party.getParkingParty() != null
                                && party.getParkingParty().hasCarContainingPlateNumber(carPlateNumber)) {
                            result.add(party);
                        }
                    } else {
                        result.add(party);
                    }
                }
            }
        }
        return result;
    }

    private boolean areNamesPresent(String name, String[] searchNameParts) {
        String nameNormalized = StringNormalizer.normalize(name).toLowerCase();
        for (int i = 0; i < searchNameParts.length; i++) {
            String namePart = searchNameParts[i];
            if (!nameNormalized.contains(namePart)) {
                return false;
            }
        }
        return true;
    }

}
