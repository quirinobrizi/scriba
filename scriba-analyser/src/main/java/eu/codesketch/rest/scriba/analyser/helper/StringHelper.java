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
package eu.codesketch.rest.scriba.analyser.helper;

import java.util.Collection;

/**
 * simple utility class for dealing with strings
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public abstract class StringHelper {

    private StringHelper() {
    }

    public static String join(Collection<String> collection, String delimiter) {
        StringBuilder builder = new StringBuilder();
        for (String element : collection) {
            builder.append(element).append(delimiter);
        }
        return builder.substring(0, builder.length() - 1).toString();
    }
}
