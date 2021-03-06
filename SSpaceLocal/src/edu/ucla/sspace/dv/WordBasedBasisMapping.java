/*
 * Copyright 2010 David Jurgens 
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

package edu.ucla.sspace.dv;

import edu.ucla.sspace.basis.AbstractBasisMapping;

import edu.ucla.sspace.dependency.DependencyPath;


/**
 * A {@link BasisMapping} implementation where each word corresponds to a unique
 * dimension regardless of how it is grammatically related.  For example "clock"
 * occuring with the "{@code SBJ}" relation will have the same dimension as
 * "clock" occurring with the "{@code OBJ}" relation.
 *
 * @author David Jurgens
 */
public class WordBasedBasisMapping
    extends AbstractBasisMapping<DependencyPath, String> 
    implements DependencyPathBasisMapping {

    private static final long serialVersionUID = 1L;

    /**
     * Returns the dimension number corresponding to the term at the end of the
     * provided path.
     *
     * @param path a path whose end represents a semantic connection
     *
     * @return the dimension for the occurrence of the last word in the path
     */
    public int getDimension(DependencyPath path) {       
        return getDimensionInternal(path.last().word());
    }
}
