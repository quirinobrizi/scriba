/**
 * Scriba is a software library that aims to analyse REST interface and
 * produce machine readable documentation.
 * <p/>
 * Copyright (C) 2015  Quirino Brizi (quirino.brizi@gmail.com)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package codesketch.scriba.analyser.domain.model;

/**
 * Identifies the URI path that a resource class or class method will serve
 * requests for.
 *
 * @author quirino.brizi
 * @since 28 Jan 2015
 *
 */
public class Path {

    private String value;

    /**
     * Create a new Path instance.
     *
     * @param value
     *            the path value.
     */
    public Path(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
