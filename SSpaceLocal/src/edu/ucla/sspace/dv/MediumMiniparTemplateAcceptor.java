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

import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyTreeNode;

import edu.ucla.sspace.text.IteratorFactory;

import java.util.HashSet;
import java.util.Set;


/**      
 * A {@code DependencyPathAcceptor} that accepts the minimum set of path
 * templates specified by <a
 * href="http://www.nlpado.de/~sebastian/pub/papers/cl07_pado.pdf">Padó and
 * Lapata (2007)</a>. This acceptor is designed to
 * be used with the <a
 * link="http://webdocs.cs.ualberta.ca/~lindek/minipar.htm">Minipar</a> parser
 * and its associated part of speech tag set.
 *
 * @see MinimumMiniparTemplateAcceptor
 * @see MaximumMiniparTemplateAcceptor
 */
public class MediumMiniparTemplateAcceptor implements DependencyPathAcceptor {

    static final Set<String> MEDIUM_TEMPLATES = new HashSet<String>();

    static {
        MEDIUM_TEMPLATES.add("A:mod:N,N:lex-mod:(null)");
        MEDIUM_TEMPLATES.add("A:mod:N,N:nn:N");
        MEDIUM_TEMPLATES.add("A:subj:N,N:lex-mod:(null)");
        MEDIUM_TEMPLATES.add("A:subj:N,N:nn:N");
        MEDIUM_TEMPLATES.add("N:conj:N,N:lex-mod:(null)");
        MEDIUM_TEMPLATES.add("N:conj:N,N:nn:N");
        MEDIUM_TEMPLATES.add("N:gen:N,N:lex-mod:(null)");
        MEDIUM_TEMPLATES.add("N:gen:N,N:nn:N");
        MEDIUM_TEMPLATES.add("N:nn:N,N:conj:N");
        MEDIUM_TEMPLATES.add("N:nn:N,N:conj:N,N:nn:N");
        MEDIUM_TEMPLATES.add("N:nn:N,N:gen:N");
        MEDIUM_TEMPLATES.add("N:nn:N,N:gen:N,N:nn:N");
        MEDIUM_TEMPLATES.add("N:nn:N,N:mod:A");
        MEDIUM_TEMPLATES.add("N:nn:N,N:mod:Pred");
        MEDIUM_TEMPLATES.add("N:nn:N,N:obj:V");
        MEDIUM_TEMPLATES.add("N:nn:N,N:subj:A");
        MEDIUM_TEMPLATES.add("N:nn:N,N:subj:V");
        MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:conj:N");
        MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:conj:N,N:lex-mod:(null)");
        MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:gen:N");
        MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:gen:N,N:lex-mod:(null)");
        MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:mod:A");
        MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:mod:Pred");
        MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:obj:V");
        MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:subj:A");
        MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:subj:V");
        MEDIUM_TEMPLATES.add("Prep:mod:N,N:lex-mod:(null)");
        MEDIUM_TEMPLATES.add("Prep:mod:N,N:nn:N");
        MEDIUM_TEMPLATES.add("V:obj:N,N:lex-mod:(null)");
        MEDIUM_TEMPLATES.add("V:obj:N,N:nn:N");
        MEDIUM_TEMPLATES.add("V:subj:N,N:lex-mod:(null)");
        MEDIUM_TEMPLATES.add("V:subj:N,N:nn:N");
    }
    
    /**
     * Creates the acceptor with its standard templates
     */
    public MediumMiniparTemplateAcceptor() { }
   
    /**
     * Returns {@code true} if the path matches one of the predefined templates
     *
     * @param path a dependency path
     *
     * @return {@code true} if the path matches a template
     */
    public boolean accepts(DependencyPath path) {
        return acceptsInternal(path);
    }

    /**
     * A package-private method that checks whether the path matches any of the
     * predefined templates.  This method is provided so other template classes
     * have access to the accept logic used by this class.
     *
     * @param path a dependency path
     *
     * @return {@code true} if the path matches a template
     */
    static boolean acceptsInternal(DependencyPath path) {
        // First check whether the minimum template acceptor would allow this
        // path
        if (MinimumMiniparTemplateAcceptor.acceptsInternal(path))
            return true;

        // Filter out paths that can't match the template due to length
        if (path.length() > 3)
            return false;

        int pathLength = path.length();

        // The medium set of templates contains "null" matches which are wild
        // cards against any part of speech.  We handle these by generating
        // three possible patterns that could represent the provided path.  If
        // any of these patterns are found in the medium set, the path is valid.
        StringBuilder nullStart = new StringBuilder(pathLength * 16);
        StringBuilder nullEnd = new StringBuilder(pathLength * 16);
        StringBuilder noNulls = new StringBuilder(pathLength * 16);

        // Iterate over each pair in the path and create the pattern string that
        // represents this path.  The pattern string is pos:rel:pos[,...] .
        DependencyTreeNode first = path.first();
        for (int i = 1; i < pathLength; ++i) {
            DependencyTreeNode second = path.getNode(i);
            // Check that the nodes weren't filtered out.  If so reject the path
            // even if the part of speech and relation text may have matched a
            // template.
            if (first.word().equals(IteratorFactory.EMPTY_TOKEN))
                return false;

            // Get the relation between the two nodes
            String rel = path.getRelation(i - 1);
            String firstPos = first.pos();
            String secPos = second.pos();
            
            nullStart.append((i == 0) ? "(null)" : firstPos);
            nullStart.append(":").append(rel).append(":").append(secPos);

            nullEnd.append(firstPos).append(":").append(rel).append(":");
            nullEnd.append((i + 1 == pathLength) ? "(null)" : secPos);

            noNulls.append(firstPos).append(":").append(rel)
                .append(":").append(secPos);

            // Check whether more elements existing, and if so, add the ','
            if (i + 1 < pathLength) {
                nullStart.append(",");
                nullEnd.append(",");
                noNulls.append(",");
            }

            // Last, shift over the node
            first = second;
        }
        
        // Extra case for the last token in the path
        if (first.word().equals(IteratorFactory.EMPTY_TOKEN))
            return false;

        return MEDIUM_TEMPLATES.contains(noNulls.toString())
            || MEDIUM_TEMPLATES.contains(nullStart.toString())
            || MEDIUM_TEMPLATES.contains(nullEnd.toString());
    }

    /**
     * {@inheritDoc}
     */
    public int maxPathLength() {
        return 4;
    }
}