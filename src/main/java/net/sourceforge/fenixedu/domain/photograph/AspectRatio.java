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
package net.sourceforge.fenixedu.domain.photograph;

public enum AspectRatio {
    ª1_by_1(1, 1), ª3_by_4(3, 4), ª9_by_10(9, 10);

    private int xRatio;
    private int yRatio;

    private AspectRatio(final int xRatio, final int yRatio) {
        this.xRatio = xRatio;
        this.yRatio = yRatio;
    }

    public int getXRatio() {
        return xRatio;
    }

    public int getYRatio() {
        return yRatio;
    }
}
