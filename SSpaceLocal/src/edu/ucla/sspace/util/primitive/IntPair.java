/*
 * Copyright 2011 David Jurgens
 *
 * This file is part of the S-Space package and is covered under the terms and
 * conditions therein.
 *
 * The S-Space package is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation and distributed hereunder to you.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND NO REPRESENTATIONS OR WARRANTIES,
 * EXPRESS OR IMPLIED ARE MADE.  BY WAY OF EXAMPLE, BUT NOT LIMITATION, WE MAKE
 * NO REPRESENTATIONS OR WARRANTIES OF MERCHANT- ABILITY OR FITNESS FOR ANY
 * PARTICULAR PURPOSE OR THAT THE USE OF THE LICENSED SOFTWARE OR DOCUMENTATION
 * WILL NOT INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER
 * RIGHTS.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package edu.ucla.sspace.util.primitive;


/**
 * A utility class for holding two {@code int}s.
 */
public class IntPair {

    /**
     * The first {@code int} in the pair
     */
    public final int x;
    
    /**
     * The second {@code int} in the pair
     */
    public final int y;

    /**
     * Creates a pair out of {@code x} and {@code y}
     */
    public IntPair(int x, int y) {
	this.x = x;
	this.y = y;
    }

    /**
     * Returns {@code true} if {@code o} is a {@link Pair} and its {@code x} and
     * {@code y} elements are equal to those of this pair.  Note that equality
     * is specific to the ordering of {@code x} and {@code y}.
     */
    public boolean equals(Object o) {
	if (!(o instanceof IntPair))
	    return false;
	IntPair p = (IntPair)o;
	return x == p.x && y == p.y;
    }
    
    public int hashCode() {
        return x ^ y;
    }

    public String toString() {
	return "{" + x + ", " + y + "}";
    }
}