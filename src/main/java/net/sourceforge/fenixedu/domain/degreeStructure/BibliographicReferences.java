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
 * Created on Jan 24, 2006
 */
package net.sourceforge.fenixedu.domain.degreeStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

public class BibliographicReferences implements Serializable {
    private List<BibliographicReference> bibliographicReferencesList;

    public SortedSet<BibliographicReference> getBibliographicReferencesSortedByOrder() {
        final SortedSet<BibliographicReference> bibliographicReferences = new TreeSet<BibliographicReference>();
        bibliographicReferences.addAll(getBibliographicReferencesList());
        return bibliographicReferences;
    }

    public List<BibliographicReference> getBibliographicReferencesList() {
        if (this.bibliographicReferencesList == null) {
            this.bibliographicReferencesList = new ArrayList<BibliographicReference>();
        }
        return this.bibliographicReferencesList;
    }

    public int getBibliographicReferencesListCount() {
        return getBibliographicReferencesList().size();
    }

    public BibliographicReference getBibliographicReference(int oid) {
        return getBibliographicReferencesList().get(oid);
    }

    public void createBibliographicReference(String year, String title, String authors, String reference, String url,
            BibliographicReferenceType type) {
        getBibliographicReferencesList().add(
                new BibliographicReference(year, title, authors, reference, url, type, getBibliographicReferencesListCount()));
    }

    public void createBibliographicReference(String year, String title, String authors, String reference, String url,
            BibliographicReferenceType type, int order) {
        getBibliographicReferencesList().add(new BibliographicReference(year, title, authors, reference, url, type, order));
    }

    public void editBibliographicReference(int oid, String year, String title, String authors, String reference, String url,
            BibliographicReferenceType type) {
        getBibliographicReferencesList().get(oid).edit(year, title, authors, reference, url, type);
    }

    public void deleteBibliographicReference(int oid) {
        removeBibliographicReference(oid);
        reOrderBibliographicReferences();
    }

    public void switchBibliographicReferencePosition(int oldPosition, int newPosition) {
        try {
            if (validPositions(oldPosition, newPosition)) {
                final BibliographicReference bibliographicReference = getBibliographicReference(oldPosition);
                removeBibliographicReference(oldPosition);
                getBibliographicReferencesList().add(newPosition, bibliographicReference);
                reOrderBibliographicReferences();
            }
        } catch (IndexOutOfBoundsException e) {
            throw new DomainException("bibliographicReferences.invalid.reference.positions");
        }
    }

    private boolean validPositions(int oldPosition, int newPosition) {
        if (oldPosition == newPosition || newPosition < 0 || newPosition == getBibliographicReferencesListCount()) {
            return false;
        }
        return true;
    }

    private BibliographicReference removeBibliographicReference(int oid) {
        return getBibliographicReferencesList().remove(oid);
    }

    private void reOrderBibliographicReferences() {
        for (int i = 0; i < getBibliographicReferencesListCount(); i++) {
            getBibliographicReference(i).setOrder(i);
        }
    }

    public static class BibliographicReference implements Comparable<BibliographicReference>, Serializable {
        private String year;

        private String title;

        private String authors;

        private String reference;

        private String url;

        private BibliographicReferenceType type;

        private int order;

        public BibliographicReference(String year, String title, String authors, String reference, String url,
                BibliographicReferenceType type, int order) {
            if (!StringUtils.isEmpty(title) && !StringUtils.isEmpty(authors)) {
                setInformation(year, title, authors, reference, url, type, order);
            }
        }

        public void edit(String year, String title, String authors, String reference, String url, BibliographicReferenceType type) {
            if (!StringUtils.isEmpty(title) && !StringUtils.isEmpty(authors)) {
                setInformation(year, title, authors, reference, url, type, getOrder());
            }
        }

        private void setInformation(String year, String title, String authors, String reference, String url,
                BibliographicReferenceType type, int order) {
            setYear(year);
            setTitle(title);
            setAuthors(authors);
            setReference(reference);
            setUrl(url);
            setType(type);
            setOrder(order);
        }

        public String getAuthors() {
            return authors;
        }

        public void setAuthors(String authors) {
            this.authors = authors;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public BibliographicReferenceType getType() {
            return type;
        }

        public void setType(BibliographicReferenceType type) {
            this.type = type;
        }

        public String getUrl() {
            return url == null || url.length() == 0 || url.equalsIgnoreCase("http://") ? null : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        @Override
        public int compareTo(BibliographicReference bibliographicReference) {
            return getOrder() - bibliographicReference.getOrder();
        }

        public boolean isMain() {
            return getType() == BibliographicReferenceType.MAIN;
        }

        public boolean isSecondary() {
            return getType() == BibliographicReferenceType.SECONDARY;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();

            result.append(year).append(" || ");
            result.append(title).append(" || ");
            result.append(authors).append(" || ");
            result.append(reference).append(" || ");
            result.append(url).append("\n");

            return result.toString();
        }
    }

    public enum BibliographicReferenceType {
        MAIN, SECONDARY;

        public String getName() {
            return name();
        }
    }

    public List<BibliographicReference> getMainBibliographicReferences() {
        List<BibliographicReference> result = new ArrayList<BibliographicReference>();

        for (BibliographicReference bibliographicReference : getBibliographicReferencesList()) {
            if (bibliographicReference.isMain()) {
                result.add(bibliographicReference);
            }
        }

        return result;
    }

    public List<BibliographicReference> getSecondaryBibliographicReferences() {
        List<BibliographicReference> result = new ArrayList<BibliographicReference>();

        for (BibliographicReference bibliographicReference : getBibliographicReferencesList()) {
            if (bibliographicReference.isSecondary()) {
                result.add(bibliographicReference);
            }
        }

        return result;
    }

}
