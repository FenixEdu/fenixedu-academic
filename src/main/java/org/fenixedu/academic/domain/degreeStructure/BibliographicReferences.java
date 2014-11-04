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
/*
 * Created on Jan 24, 2006
 */
package org.fenixedu.academic.domain.degreeStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.exceptions.DomainException;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public class BibliographicReferences implements Serializable {
    private static final long serialVersionUID = 1489616281219374804L;

    private final List<BibliographicReference> bibliographicReferences;

    public BibliographicReferences() {
        this.bibliographicReferences = ImmutableList.of();
    }

    public BibliographicReferences(List<BibliographicReference> refs) {
        this.bibliographicReferences = ImmutableList.copyOf(refs);
    }

    public SortedSet<BibliographicReference> getBibliographicReferencesSortedByOrder() {
        return new TreeSet<BibliographicReference>(bibliographicReferences);
    }

    public List<BibliographicReference> getBibliographicReferencesList() {
        return bibliographicReferences;
    }

    public int getBibliographicReferencesListCount() {
        return bibliographicReferences.size();
    }

    public BibliographicReference getBibliographicReference(int oid) {
        return bibliographicReferences.get(oid);
    }

    public BibliographicReferences with(String year, String title, String authors, String reference, String url,
            BibliographicReferenceType type) {
        List<BibliographicReference> refs = new ArrayList<BibliographicReference>(bibliographicReferences);
        refs.add(new BibliographicReference(year, title, authors, reference, url, type, refs.size()));
        return new BibliographicReferences(refs);
    }

    public BibliographicReferences with(String year, String title, String authors, String reference, String url,
            BibliographicReferenceType type, int order) {
        List<BibliographicReference> refs = new ArrayList<BibliographicReference>(bibliographicReferences);
        refs.add(order, new BibliographicReference(year, title, authors, reference, url, type, order));
        return new BibliographicReferences(refs);
    }

    public BibliographicReferences replacing(int index, String year, String title, String authors, String reference, String url,
            BibliographicReferenceType type) {
        List<BibliographicReference> refs = new ArrayList<BibliographicReference>(bibliographicReferences);
        refs.set(index, new BibliographicReference(year, title, authors, reference, url, type, index));
        return new BibliographicReferences(refs);
    }

    public BibliographicReferences without(int index) {
        List<BibliographicReference> refs = new ArrayList<BibliographicReference>(bibliographicReferences);
        refs.remove(index);
        reOrderBibliographicReferences(refs);
        return new BibliographicReferences(refs);
    }

    public BibliographicReferences movingBibliographicReference(int oldPosition, int newPosition) {
        try {
            if (validPositions(oldPosition, newPosition)) {
                final BibliographicReference bibliographicReference = getBibliographicReference(oldPosition);
                List<BibliographicReference> refs = new ArrayList<BibliographicReference>(bibliographicReferences);
                refs.remove(oldPosition);
                refs.add(newPosition, bibliographicReference);
                reOrderBibliographicReferences(refs);
                return new BibliographicReferences(refs);
            } else {
                return this;
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

    private static void reOrderBibliographicReferences(List<BibliographicReference> refs) {
        for (int i = 0; i < refs.size(); i++) {
            refs.set(i, refs.get(i).withOrder(i));
        }
    }

    public static class BibliographicReference implements Comparable<BibliographicReference>, Serializable {
        private static final long serialVersionUID = 7998864914358693747L;

        private final String year;

        private final String title;

        private final String authors;

        private final String reference;

        private final String url;

        private final BibliographicReferenceType type;

        private final int order;

        public BibliographicReference(String year, String title, String authors, String reference, String url,
                BibliographicReferenceType type, int order) {
            if (Strings.isNullOrEmpty(title) || Strings.isNullOrEmpty(authors)) {
                throw new IllegalArgumentException("Title and author cannot be empty!");
            }
            this.year = year;
            this.title = title;
            this.authors = authors;
            this.reference = reference;
            this.url = url;
            this.type = type;
            this.order = order;
        }

        public BibliographicReference withOrder(int newOrder) {
            return new BibliographicReference(year, title, authors, reference, url, type, newOrder);
        }

        public String getAuthors() {
            return authors;
        }

        public String getReference() {
            return reference;
        }

        public String getTitle() {
            return title;
        }

        public BibliographicReferenceType getType() {
            return type;
        }

        public String getUrl() {
            return url == null || url.length() == 0 || url.equalsIgnoreCase("http://") ? null : url;
        }

        public String getYear() {
            return year;
        }

        public int getOrder() {
            return order;
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
        return bibliographicReferences.stream().filter(BibliographicReference::isMain).collect(Collectors.toList());
    }

    public List<BibliographicReference> getSecondaryBibliographicReferences() {
        return bibliographicReferences.stream().filter(BibliographicReference::isSecondary).collect(Collectors.toList());
    }

}
