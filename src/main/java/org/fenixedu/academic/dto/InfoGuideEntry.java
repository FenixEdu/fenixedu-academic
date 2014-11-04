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
/*
 * Created on 21/Mar/2003
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideEntry;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGuideEntry extends InfoObject {

    protected GraduationType graduationType;

    protected DocumentType documentType;

    protected String description;

    protected Double price;

    protected Integer quantity;

    protected InfoGuide infoGuide;

    public InfoGuideEntry() {
    }

    public InfoGuideEntry(GraduationType graduationType, DocumentType documentType, String description, Double price,
            Integer quantity, InfoGuide infoGuide) {
        this.description = description;
        this.documentType = documentType;
        this.graduationType = graduationType;
        this.infoGuide = infoGuide;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoGuideEntry) {
            InfoGuideEntry infoGuideEntry = (InfoGuideEntry) obj;

            resultado =
                    getInfoGuide().equals(infoGuideEntry.getInfoGuide())
                            && getGraduationType().equals(infoGuideEntry.getGraduationType())
                            && getDocumentType().equals(infoGuideEntry.getDocumentType())
                            && getDescription().equals(infoGuideEntry.getDescription());
        }
        return resultado;
    }

    @Override
    public String toString() {
        String result = "[GUIDE ENTRY";
        result += ", description=" + description;
        result += ", infoGuide=" + infoGuide.getExternalId();
        result += ", documentType=" + documentType;
        result += ", graduationType=" + graduationType;
        result += ", price=" + price;
        result += ", quantity=" + quantity;
        result += "]";
        return result;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public DocumentType getDocumentType() {
        return documentType;
    }

    /**
     * @return
     */
    public GraduationType getGraduationType() {
        return graduationType;
    }

    /**
     * @return
     */
    public InfoGuide getInfoGuide() {
        return infoGuide;
    }

    /**
     * @return
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @return
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param type
     */
    public void setDocumentType(DocumentType type) {
        documentType = type;
    }

    /**
     * @param type
     */
    public void setGraduationType(GraduationType type) {
        graduationType = type;
    }

    /**
     * @param guide
     */
    public void setInfoGuide(InfoGuide guide) {
        infoGuide = guide;
    }

    /**
     * @param double1
     */
    public void setPrice(Double double1) {
        price = double1;
    }

    /**
     * @param integer
     */
    public void setQuantity(Integer integer) {
        quantity = integer;
    }

    public void copyFromDomain(GuideEntry guideEntry) {
        super.copyFromDomain(guideEntry);
        if (guideEntry != null) {
            setDescription(guideEntry.getDescription());
            setDocumentType(guideEntry.getDocumentType());
            setGraduationType(guideEntry.getGraduationType());
            setPrice(guideEntry.getPrice());
            setQuantity(guideEntry.getQuantity());
        }
    }

    public static InfoGuideEntry newInfoFromDomain(GuideEntry guideEntry) {
        InfoGuideEntry infoGuideEntry = null;
        if (guideEntry != null) {
            infoGuideEntry = new InfoGuideEntry();
            infoGuideEntry.copyFromDomain(guideEntry);
        }
        return infoGuideEntry;
    }

    public void copyToDomain(InfoGuideEntry infoGuideEntry, GuideEntry guideEntry) {
        guideEntry.setDescription(infoGuideEntry.getDescription());
        guideEntry.setDocumentType(infoGuideEntry.getDocumentType());
        guideEntry.setGraduationType(infoGuideEntry.getGraduationType());
        guideEntry.setPrice(infoGuideEntry.getPrice());
        guideEntry.setQuantity(infoGuideEntry.getQuantity());
    }

}
