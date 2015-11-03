/**
 * Copyright (c) 2015 nextlets.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * The Software shall be used for Good, not Evil.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.nextlets.erc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The primitive implementation of stop watch.
 *
 * @author Y.Sh.
 */
public class ERCStopWatch {

    private String name;
    private long start, stop, suspend, delta;
    private boolean stopped, suspended;

    private List<String>names = new ArrayList<String>();
    private Map<String,ERCStopWatch>watches = new HashMap<String,ERCStopWatch>();

    /**
     * Constructs stop watch.
     * @param name - stop watch name.
     */
    public ERCStopWatch(String name) {
        this.name = name;
        this.start = this.stop = System.currentTimeMillis();
        this.suspend = this.delta = 0L;
        this.stopped = this.suspended = false;
    }

    /**
     * Initializes and starts stop watch.
     * If you called start, you must call stop with the same point name!
     * @param pointName - stop watch point name
     */
    public void start(String pointName) {
        if (!watches.containsKey(pointName)) {
            names.add(pointName);
        }
        watches.put(pointName, new ERCStopWatch(pointName));
    }

    /**
     * Stops stop watch.
     * @param pointName - stop watch point name
     */
    public void stop(String pointName) {
        ERCStopWatch sw = watches.get(pointName);
        if (sw != null) {
            if (sw.suspended) {
                resume(pointName);
            }
            sw.stop = System.currentTimeMillis();
            sw.stopped = true;
        }
    }

    /**
     * Suspends stop watch.
     * If you called suspend, you must call resume with the same point name!
     * @param pointName - stop watch point name
     */
    public void suspend(String pointName) {
        ERCStopWatch sw = watches.get(pointName);
        if (sw != null) {
            sw.suspend = System.currentTimeMillis();
            sw.suspended = true;
        }
    }

    /**
     * Resumes stop watch.
     * @param pointName - stop watch point name
     */
    public void resume(String pointName) {
        ERCStopWatch sw = watches.get(pointName);
        if (sw != null && sw.suspended) {
            sw.delta = System.currentTimeMillis() - sw.suspend;
            sw.suspend = 0L;
            sw.suspended = false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        int errors = 0;
        boolean first = true;
        long tt = 0;
        for (String name : names) {
            ERCStopWatch sw = watches.get(name);
            if (!first) {
                sb.append(", ");
            }
            if (!sw.stopped) {
                sb.append(sw.name).append(": >>NOT STOPPED<<");
                errors++;
            } else
            if (sw.suspended) {
               sb.append(sw.name).append(": >>NOT RESUMED<<");
               errors++;
            } else {
                long wt = sw.stop - sw.start - sw.delta;
                tt += wt;
                sb.append(sw.name).append(": ").append(wt).append("ms");
            }
            first = false;
        }
        sb.append(").");
        StringBuilder sb1 = new StringBuilder("StopWatch: ");
        sb1.append(name).append(", total time: ").append(tt).append("ms ");
        if (errors > 0) {
            sb1.append(">>CONTAINS ").append(errors).append(" ERROR").append(errors == 1 ? "<< " : "S<< ") ;
        }
        sb1.append(sb.toString()).toString();
        return sb1.toString();
    }

}
