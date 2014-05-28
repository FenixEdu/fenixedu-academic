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
 * Created on 4/Mar/2004
 *  
 */

package net.sourceforge.fenixedu.utilTests;

/**
 * @author Susana Fernandes
 */

public class ParseQuestionException extends Exception {

    public ParseQuestionException() {
    }

    public ParseQuestionException(final Throwable t) {
        super(t);
    }

    public ParseQuestionException(String errorMessage) {
        super(errorMessage);
    }

    public ParseQuestionException(String errorMessage, final Throwable t) {
        super(errorMessage, t);
    }

    public ParseQuestionException(String element, boolean isElement) {
        super("O elemento <" + element + "> ainda não é suportado pelo sistema.");
    }

    public ParseQuestionException(String element, String attribute) {
        super("O atributo \"" + attribute + "\" do elemento <" + element + "> ainda não é suportado pelo sistema.");
    }

    @Override
    public String toString() {
        String result = ": " + this.getMessage();
        return result;
    }
}