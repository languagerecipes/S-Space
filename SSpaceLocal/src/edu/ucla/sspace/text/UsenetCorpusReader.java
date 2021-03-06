/*
 * Copyright 2009 Keith Stevens 
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

import edu.ucla.sspace.text.DirectoryCorpusReader.BaseFileIterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Iterator;


/**
 * A {@code DirectoryCorpusReader} for the <a
 * href="http://www.psych.ualberta.ca/~westburylab/downloads/usenetcorpus.download.html">Usenet
 * corpus</a> provided by the <a
 * href="http://www.psych.ualberta.ca/~westburylab/index.html">Westbury Lab</a>.
 * The corpus filenames are expected to remain unchanged from how they were
 * specified, i.e. have the numeric timestamp naming convention.
 *
 * @author Keith Stevens
 */
public class UsenetCorpusReader extends DirectoryCorpusReader<Document> {

    /**
     * The string delimiter that separates documents in the USENET corpus.
     */
    private static final String END_OF_DOCUMENT =
        "---END.OF.DOCUMENT---";

    /**
     * {@code true} if the reader should output UNIX timestamps for each
     * document, indicating when it was created.
     */
    private final boolean useTimestamps;

    /**
     * Creates a reader for USENET document, starting with the specified file.
     */
    public UsenetCorpusReader() {
        this(false);
    }

    /** 
     * Creates a reader for USENET document, starting with the specified file
     * and, if {@code includeTimestamps} is {@code true}, prepending the
     * creation date of each document as the first token in the document.
     */
    public UsenetCorpusReader(boolean includeTimestamps) {
        useTimestamps = includeTimestamps;
    }

    protected Iterator<Document> corpusIterator(Iterator<File> fileIter) {
        return new InnerIterator(fileIter);
    }

    public class InnerIterator extends BaseFileIterator {

        /**
         * A reader for extracting content from the usenet corpus.
         */
        private BufferedReader usenetReader;

        /**
         * The timestamp of the current document
         */
        private long curDocTimestamp;

        public InnerIterator(Iterator<File> files) {
            super(files);
        }

        /**
         * Sets up a {@code BufferedReader} to read through a single file with
         * multiple blog entries.
         */
        protected void setupCurrentDoc(File currentDocName) {
            try {
                usenetReader =
                    new BufferedReader(new FileReader(currentDocName));
                // Extract the time stamp of the current doc.  All usenet files are
                // named in the format "text.text.date.txt".  the date portion is
                // formated with YYYYMMDD followed with extra information.
                String[] parsedName = currentDocName.getName().split("\\.");
                String date = parsedName[parsedName.length - 2];
                Calendar calendar = new GregorianCalendar(
                        Integer.parseInt(date.substring(0, 4)),
                        Integer.parseInt(date.substring(4, 6)),
                        Integer.parseInt(date.substring(6, 8)));
                curDocTimestamp = calendar.getTimeInMillis();
            } catch (IOException ioe) {
                throw new IOError(ioe);
            }
        }

        /**
         * Iterates over the utterances in a file and appends the words to create a
         * new document.
         */
        protected Document advanceInDoc() {
            String line = null;
            StringBuilder content = new StringBuilder();
            // If the system is using timestamps, append it as the first token in
            // the document
            if (useTimestamps)
                content.append(curDocTimestamp).append(" ");
            try {
                // Read the the text block, possibly spanning multiple lines and
                // process it as a single document.  For consistency, strip off the
                // USENET threading formatting, e.g. >>>, from the front of each
                // line.
                while ((line = usenetReader.readLine()) != null) {
                    if (line.contains(END_OF_DOCUMENT)) 
                        return new StringDocument(cleanDoc(content.toString()));
                    else {
                        int lineStart = 0;
                        // Find the first non '>' or ' ' in the line to determine
                        // where the auto-threading formatting stops.
                        for (char c = line.charAt(lineStart); 
                             lineStart < line.length() && (c == '>' || c == ' ');
                             c = line.charAt(++lineStart))
                            ;
                        // Append the unformatted line.  Also append an extra space
                        // to avoid combining terms from multiple lines into a
                        // single token
                        content.append(line.substring(lineStart)).append(" ");
                    }
                }
            } catch (IOException ioe) {
                throw new IOError(ioe);
            }
            // There was no content left in this document.
            return null;
        }
    }
}
