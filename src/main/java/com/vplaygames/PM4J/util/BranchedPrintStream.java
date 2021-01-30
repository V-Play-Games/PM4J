/*
 * Copyright 2020 Vaibhav Nargwani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vplaygames.PM4J.util;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BranchedPrintStream extends PrintStream implements Cloneable {
    HashSet<PrintStream> printStreams;

    public BranchedPrintStream(PrintStream ps, PrintStream... rest) {
        super(ps);
        printStreams = new HashSet<>();
        printStreams.add(ps);
        printStreams.addAll(Arrays.asList(rest));
    }

    public HashSet<PrintStream> getPrintStreams() {
        return printStreams;
    }

    public BranchedPrintStream setPrintStreams(Set<PrintStream> printStreams) {
        this.printStreams = new HashSet<>(printStreams);
        return this;
    }

    @Override
    public void flush() {
        printStreams.forEach(PrintStream::flush);
    }

    @Override
    public void close() {
        printStreams.forEach(PrintStream::close);
    }

    @Override
    public void write(int b) {
        printStreams.forEach(ps -> ps.write(b));
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        printStreams.forEach(ps -> ps.write(buf, off, len));
    }

    @Override
    public void print(String s) {
        printStreams.forEach(ps -> ps.print(s));
    }

    @Override
    public void println(String x) {
        printStreams.forEach(ps -> ps.println(x));
    }

    @Override
    public BranchedPrintStream clone() {
        BranchedPrintStream clone;
        try {
            clone = (BranchedPrintStream) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        return clone.setPrintStreams(printStreams);
    }
}
