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
package com.vplaygames.PM4J;

import com.vplaygames.PM4J.util.BranchedPrintStream;

import java.util.Objects;

public class Settings implements Cloneable {
    private boolean logPolicy;
    private Connection connection;
    private BranchedPrintStream logOutputStream;

    public Settings() {
        logPolicy = true;
        connection = new Connection();
        logOutputStream = new BranchedPrintStream(System.out);
    }

    // Getters
    public boolean getLogPolicy() {
        return logPolicy;
    }

    public Connection getConnection() {
        return connection;
    }

    public BranchedPrintStream getLogOutputStream() {
        return logOutputStream;
    }

    // Setters
    public Settings setLogPolicy(boolean logPolicy) {
        this.logPolicy = logPolicy;
        return this;
    }

    public Settings setConnection(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "Connection can't be null!");
        return this;
    }

    public Settings setLogOutputStream(BranchedPrintStream logOutputStream) {
        this.logOutputStream = Objects.requireNonNull(logOutputStream, "logOutputStream can't be null!");
        return this;
    }

    public Settings copyFrom(Settings other) {
        Objects.requireNonNull(other);
        logPolicy = other.logPolicy;
        connection = new Connection(other.connection.client, other.connection.useHTTPS);
        logOutputStream = other.logOutputStream.clone();
        return this;
    }
}
