/**
 * Scriba is a software library that aims to analyse REST interface and 
 * produce machine readable documentation.
 *
 * Copyright (C) 2015  Quirino Brizi (quirino.brizi@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package codesketch.scriba.analyser.domain.model.constraint;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * A constraint for parameters annotated whit numeric restriction validators like {@link Max}, {@link Min}, etc.
 *
 * @author quirino.brizi
 * @since 22 Dec 2015
 *
 */
public class NumericConstraint implements Constraint {

    private String value;
    private String type;

    public NumericConstraint(String value, String type) {
        this.value = value;
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see codesketch.scriba.analyser.domain.model.constraint.Constraint#constraint()
     */
    @Override
    public String constraint() {
        return this.value;
    }

    @Override
    public String type() {
        return this.type;
    }

}
