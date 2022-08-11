/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.stream.Stream;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum ProfessionType {

    /*
     * OLD RAIDES VALUES 
     */

    UNKNOWN(true),

    PUBLIC_ADMINISTRATION_BOARD_OR_DIRECTOR_AND_BOARD_OF_COMPANIES(false),

    CIENTIFIC_AND_INTELECTUAL_PROFESSION_SPECIALIST(false),

    INTERMEDIATE_LEVEL_TECHNICALS_AND_PROFESSIONALS(false),

    ADMINISTRATIVE_STAFF_AND_SIMMILAR(false),

    SALES_AND_SERVICE_STAFF(false),

    FARMERS_AND_AGRICULTURE_AND_FISHING_QUALIFIED_WORKERS(false),

    WORKERS_CRAFTSMEN_AND_SIMMILAR(false),

    INSTALLATION_AND_MACHINE_WORKERS_AND_LINE_ASSEMBLY_WORKERS(false),

    NON_QUALIFIED_WORKERS(false),

    MILITARY_MEMBER(false),

    OTHER(false),

    /*
     * NEW RAIDES VALUES 
     */

    ARMED_FORCES_OFFICERS(true),

    ARMED_FORCES_SERGEANTS(true),

    OTHER_STAFT_OF_ARMED_FORCES(true),

    LEGISLATIVE_POWER_AND_EXECUTIVE_BODIES_REPRESENTATIVES_SENIOR_OFFICIALS_OF_PUBLIC_ADMINISTRATION_OF_SPECIAL_INTEREST_ORGANIZATIONS_ENTREPRISES_DIRECTORS_AND_MANAGERS(
            true),

    ADMINISTRATIVE_AND_COMMERCIAL_DIRECTORS(true),

    PRODUCTION_AND_SPECIALISED_SERVICES_DIRECTORS(true),

    HOTELS_FOOD_SERVICE_TRADE_AND_OTHERS_SERVICES_DIRECTORS(true),

    PHYSICAL_SCIENCES_MATHEMATICS_ENGINEERING_AND_RELATED_TECHNIQUES_SPECIALISTS(true),

    HEALTH_PROFESSIONALS(true),

    TEACHERS(true),

    FINANCE_ACCOUNTING_ADMINISTRATIVE_ORGANIZATION_PUBLIC_AND_TRADE_RELATIONS_SPECIALISTS(true),

    INFORMATION_AND_COMMUNICATIONS_TECHNOLOGY_SPECIALISTS(true),

    LEGAL_SOCIAL_ARTISTIC_AND_CULTURAL_MATTERS_SPECIALISTS(true),

    SCIENCE_AND_ENGINEERING_ASSOCIATE_PROFESSIONALS(true),

    HEALTH__TECHNICIANS_AND_ASSOCIATE_PROFESSIONALS(true),

    FINANCIAL_BUSINESS_AND_ADMINISTRATION_ASSOCIATE_PROFESSIONALS(true),

    LEGAL_SOCIAL_SPORT_CULTURAL_AND_RELATED_SERVICES_INTERMEDIATE_LEVEL_TECHNICIANS(true),

    INFORMATION_AND_COMMUNICATIONS_TECHNICIANS(true),

    OFFICE_CLERKS_GENERAL_SECRETARIES_AND_DATA_KEYBOARD_CLERKS(true),

    CUSTOMER_DIRECT_SUPPORT_STAFF(true),

    DATA_ACCOUNTING_STATISTICAL_FINANCIAL_SERVICES_AND_MATERIAL_RECORDING_OPERATORS(true),

    OTHER_CLERICAL_SUPPORT_WORKERS(true),

    PERSONAL_SERVICE_WORKERS(true),

    SALESPERSONS(true),

    PERSONAL_CARE__AND_SIMILAR_WORKERS(true),

    PROTECTIVE_AND_SAFETY_SERVICES_WORKERS(true),

    MARKET_ORIENTED_FARMERS_AND_SKILLED_AGRICULTURAL_AND_FARMING_OF_ANIMALS_WORKERS(true),

    MARKET_ORIENTED_SKILLED_FORESTRY_FISHERY_AND_HUNTING_WORKERS(true),

    SUBSISTENCE_FARMERS_FISHERS_HUNTERS_AND_GATHERERS(true),

    BUILDING_AND_RELATED_TRADES_SKILLED_WORKERS_EXCLUDING_ELECTRICIANS(true),

    METAL_MACHINERY_AND_RELATED_TRADES_SKILLED_WORKERS(true),

    PRINTING_AND_PRECISION_INSTRUMENTS_MANUFACTURING_SKILLED_WORKERS_JEWELERS_CRAFTSMAN_AND_SIMILAR_WORKERS(true),

    ELECTRICAL_AND_ELECTRONIC_TRADES_SKILLED_WORKERS(true),

    FOOD_PROCESSING_WOOD_WORKING_GARMENT_AND_OTHER_CRAFT_AND_RELATED_TRADES_WORKERS(true),

    STATIONARY_PLANT_AND_MACHINE_OPERATORS(true),

    ASSEMBLERS(true),

    DRIVERS_AND_MOBILE_PLANT_OPERATORS(true),

    CLEANERS_AND_HELPERS(true),

    AGRICULTURAL_FARMING_OF_ANIMALS_FORESTRY_AND_FISHERY_NOT_SKILLED_WORKERS(true),

    MINING_CONSTRUCTION_MANUFACTURING_AND_TRANSPORT_NOT_SKILLED_WORKERS(true),

    FOOD_PREPARATION_ASSISTANTS(true),

    STREET_VENDORS_EXCLUDING_FOOD_AND_STREET_SERVICE_WORKERS(true),

    REFUSE_WORKERS_AND_OTHER_ELEMENTARY_WORKERS(true);

    private boolean active;

    private ProfessionType(boolean active) {
        setActive(active);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return ProfessionType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return ProfessionType.class.getName() + "." + name();
    }

    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

    public static Stream<ProfessionType> getActiveValues() {
        return Stream.of(ProfessionType.values()).filter(ProfessionType::isActive);
    }

}
