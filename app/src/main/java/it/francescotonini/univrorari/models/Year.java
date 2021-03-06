/*
 * The MIT License
 *
 * Copyright (c) 2017-2019 Francesco Tonini - francescotonini.me
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package it.francescotonini.univrorari.models;

import java.util.List;

/**
 * Represents a year of a university {@link Course} (e.g. third year)
 */
public class Year {
    /**
     * Gets the name of this {@link Year}
     * @return name of this {@link Year}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this {@link Year}
     * @param name name of this {@link Year}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the id of this {@link Year}
     * @return id of this {@link Year}
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of this {@link Year}
     * @param id id of this {@link Year}
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets a list of {@link Teaching}
     * @return a list of {@link Teaching}
     */
    public List<Teaching> getTeachings() {
        return teachings;
    }

    /**
     * Sets a list of {@link Teaching}
     * @param teachings a list of {@link Teaching}
     */
    public void setTeachings(List<Teaching> teachings) {
        this.teachings = teachings;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Year year = (Year)obj;
            boolean value = year.getId().hashCode() == this.getId().hashCode();
            return value;
        }
        catch (Exception e) {
            return false;
        }
    }

    private String name;
    private String id;
    private List<Teaching> teachings;
}
