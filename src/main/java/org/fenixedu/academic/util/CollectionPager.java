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
package org.fenixedu.academic.util;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionPager<T> {

    private final Collection<T> collection;

    private final int maxElementsPerPage;

    public CollectionPager(final Collection<T> collection, final int maxElementsPerPage) {
        super();
        this.collection = collection;
        this.maxElementsPerPage = maxElementsPerPage;
    }

    /**
     * 
     * @return The original collection.
     */
    public Collection<T> getCollection() {
        return collection;
    }

    /**
     * 
     * @return The maximum number of elements that are to be displayed in each page.
     */
    public int getMaxElementsPerPage() {
        return maxElementsPerPage;
    }

    /**
     * 
     * @param pageNumber The number of page to be displayed. The page number of the first page is 1.
     * @return A collection containing the elements indexed between positions:
     *         (pageNumber - 1) * maxElementsPerPage and pageNumber * maxElementsPerPage when iterating the
     *         specified collection.
     */
    public Collection<T> getPage(final int pageNumber) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be a positive integer.");
        }

        final int startIndex = (pageNumber - 1) * maxElementsPerPage;
        final int endIndex = pageNumber * maxElementsPerPage;

        final Collection<T> page = new ArrayList<T>(maxElementsPerPage);
        int counter = 0;
        for (final T t : collection) {
            if (startIndex <= counter && counter < endIndex) {
                page.add(t);
            }
            counter++;
        }
        return page;
    }

    /**
     * 
     * @return The number of pages that can be presented based on the collection.
     */
    public int getNumberOfPages() {
        final int size = collection.size();
        final int numberOfPages = size / maxElementsPerPage;
        if (numberOfPages * maxElementsPerPage == size) {
            return numberOfPages;
        } else {
            return numberOfPages + 1;
        }
    }

}