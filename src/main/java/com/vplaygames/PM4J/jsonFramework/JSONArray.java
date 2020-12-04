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
package com.vplaygames.PM4J.jsonFramework;

import com.vplaygames.PM4J.exceptions.ParseException;
import com.vplaygames.PM4J.util.MiscUtil;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class JSONArray<E extends ParsableJSONObject<E>> extends ArrayList<E> implements ParsableJSONObject<JSONArray<E>> {
    private final E dummyElement;
    private boolean initialized;

    public JSONArray(int count, E element) {
        initialized = false;
        dummyElement = element;
        while (count-- > 0)
            this.add(element);
    }

    public JSONArray<E> initialized() {
        this.initialized = true;
        return this;
    }

    @Override
    public String getAsJSON() {
        StringJoiner tor = new StringJoiner(",");
        for (E e : this) {
            tor.add(e.getAsJSON());
        }
        return "[" + tor.toString() + "]";
    }

    @Override
    public JSONArray<E> parseFromJSON(String JSON) throws ParseException {
        try {
            return parse((org.json.simple.JSONArray) new JSONParser().parse(JSON), dummyElement);
        } catch (org.json.simple.parser.ParseException e) {
            throw new ParseException();
        }
    }

    @Override
    public void validate() {
        this.forEach(ParsableJSONObject::validate);
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    public static <E extends ParsableJSONObject<E>>
    JSONArray<E> parse(org.json.simple.JSONArray ja, E element) {
        JSONArray<E> jsonArray = new JSONArray<>(0, element);
        for (Object object : ja) {
            jsonArray.add(element.parseFromJSON(object.toString()));
        }
        return jsonArray;
    }

    public static <E extends ParsableJSONObject<E>>
    JSONArray<E> parse(String json, E element) {
        return parse(MiscUtil.parseJSONArray(json), element);
    }

    @Override
    public E set(int index, E element) {
        if (initialized) throw new IllegalStateException();
        return super.set(index,element);
    }

    @Override
    public boolean add(E e) {
        if (initialized) throw new IllegalStateException();
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        if (initialized) throw new IllegalStateException();
        super.add(index,element);
    }

    @Override
    public E remove(int index) {
        if (initialized) throw new IllegalStateException();
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        if (initialized) throw new IllegalStateException();
        return super.remove(o);
    }

    @Override
    public void clear() {
        if (initialized) throw new IllegalStateException();
        super.clear();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (initialized) throw new IllegalStateException();
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (initialized) throw new IllegalStateException();
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (initialized) throw new IllegalStateException();
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (initialized) throw new IllegalStateException();
        return super.retainAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        if (initialized) throw new IllegalStateException();
        return super.removeIf(filter);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        if (initialized) throw new IllegalStateException();
        super.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        if (initialized) throw new IllegalStateException();
        super.sort(c);
    }
}