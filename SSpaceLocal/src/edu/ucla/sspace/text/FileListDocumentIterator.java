/*
 * Copyright 2009 David Jurgens
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

package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Iterator;
import java.util.Queue;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An iterator implementation that returns {@link Document} instances given a
 * file that contains list of files.
 *
 * <p>
 *
 * This class is thread-safe.
 */
public class FileListDocumentIterator implements Iterator<Document> {

    /**
     * The files in the list that have yet to be returned as {@code Document}
     * instances
     */
    private final Queue<String> filesToProcess;
    
    /**
     * Creates an {@code Iterator} over the files listed in the provided file.
     *
     * @code fileListName a file containing a list of file names with one per
     *       line
     *
     * @throws IOException if any error occurs when reading {@code fileListName}
     */
    public FileListDocumentIterator(String fileListName) throws IOException {
	
	filesToProcess = new ConcurrentLinkedQueue<String>();
	
	// read in all the files we have to process
	BufferedReader br = new BufferedReader(new FileReader(fileListName));
	for (String line = null; (line = br.readLine()) != null; )
	    filesToProcess.offer(line.trim());	    

	br.close();
    }

    /**
     * Returns {@code true} if there are more documents to return.
     */
    public boolean hasNext() {
	return !filesToProcess.isEmpty();
    }
    
    /**
     * Returns the next document from the list.
     */
    public Document next() {
	String fileName = filesToProcess.poll();
	if (fileName == null) 
	    return null;
	try {
	    return new FileDocument(fileName);
	} catch (IOException ioe) {
	    return null;
	}
    }	
    
    /**
     * Throws an {@link UnsupportedOperationException} if called.
     */
    public void remove() {
	throw new UnsupportedOperationException(
	    "removing documents is not supported");
    }
}
