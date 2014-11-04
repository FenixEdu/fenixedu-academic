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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews;

public class Inar {
    private Integer enrolled;
    private Integer frequenting;
    private Integer notEvaluated;
    private Integer approved;
    private Integer flunked;

    public Integer getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    public void incEnrolled() {
        this.enrolled++;
    }

    public Integer getFrequenting() {
        return frequenting;
    }

    public void setFrequenting(Integer frequenting) {
        this.frequenting = frequenting;
    }

    public void incFrequenting() {
        this.frequenting++;
    }

    public Integer getNotEvaluated() {
        return notEvaluated;
    }

    public void setNotEvaluated(Integer notEvaluated) {
        this.notEvaluated = notEvaluated;
    }

    public void incNotEvaluated() {
        this.notEvaluated++;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public void incApproved() {
        this.approved++;
    }

    public Integer getFlunked() {
        return flunked;
    }

    public void setFlunked(Integer flunked) {
        this.flunked = flunked;
    }

    public void incFlunked() {
        this.flunked++;
    }

    public Inar() {
        this.enrolled = new Integer(0);
        this.frequenting = new Integer(0);
        this.approved = new Integer(0);
        this.notEvaluated = new Integer(0);
        this.flunked = new Integer(0);
    }

    public boolean evaluate() {
        return (enrolled == frequenting + notEvaluated + approved + flunked) ? true : false;
    }

    public boolean getChecksum() {
        return this.evaluate();
    }

    public boolean getAB50Heuristic() {
        double _enrolled = enrolled * 1.0;
        double _approved = approved * 1.0;
        double ratio = _approved / _enrolled;
        return (ratio < 0.5);
    }

    public boolean getFO30Heuristic() {
        double _enrolled = enrolled * 1.0;
        double _flunked = flunked * 1.0;
        double ratio = _flunked / _enrolled;
        return (ratio >= 0.3);
    }

    public int[] exportAsArray() {
        int[] result = new int[5];
        result[0] = frequenting;
        result[1] = approved;
        result[2] = notEvaluated;
        result[3] = flunked;
        result[4] = enrolled;

        return result;
    }
}