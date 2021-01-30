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

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * This class represents JSON Array. This Class extends {@link ArrayList} so all of its functions
 * are available.
 * <br>This class implements the {@link ParsableJSONObject} interface, which means that it can be parsed
 * and stored into a {@link JSONArray} and can be converted into a JSON String.
 * The object can be set to an "initialized" state using {@link #initialized()} method.
 * See {@link #initialized()} for more details.
 *
 * @author Vaibhav Nargwani
 * @since 1.0.0
 * @see ParsableJSONObject
 * @see #initialized()
 */
public class JSONArray<E extends ParsableJSONObject<E>> extends ArrayList<E> implements ParsableJSONObject<JSONArray<E>> {
    private final E dummyElement;
    private boolean initialized;

    /**
     * Constructs a new JSONArray and fills in an {@code element} {@code count} times.
     *
     * @param count   No. of elements to be filled in.
     * @param element The element to be filled in.
     * @throws NullPointerException if {@code element} was null.
     */
    public JSONArray(int count, E element) {
        initialized = false;
        dummyElement = Objects.requireNonNull(element);
        while (count-- > 0)
            this.add(element);
    }

    /**
     * Sets the object to an "initialized" state indicating that
     * the values stored in this object can no longer be set, removed, added
     * or changed the order using:-
     * <ul>
     *     <li>{@linkplain #set(int, ParsableJSONObject)}</li>
     *     <li>{@linkplain #add(ParsableJSONObject)}</li>
     *     <li>{@linkplain #add(int, ParsableJSONObject)}</li>
     *     <li>{@linkplain #remove(int)}</li>
     *     <li>{@linkplain #remove(Object)}</li>
     *     <li>{@linkplain #clear()}</li>
     *     <li>{@linkplain #addAll(Collection)}</li>
     *     <li>{@linkplain #addAll(int, Collection)}</li>
     *     <li>{@linkplain #removeAll(Collection)}</li>
     *     <li>{@linkplain #retainAll(Collection)}</li>
     *     <li>{@linkplain #removeIf(Predicate)}</li>
     *     <li>{@linkplain #replaceAll(UnaryOperator)}</li>
     *     <li>{@linkplain #sort(Comparator)}</li>
     * </ul>
     * If any of the aforementioned methods are called after
     * this method is called, then {@linkplain IllegalStateException IllegalStateException} is thrown.
     *
     * @return This instance. Useful for chaining and assigning.
     */
    public JSONArray<E> initialized() {
        this.initialized = true;
        return this;
    }

    /**
     * Parses the given {@link org.json.simple.JSONArray} to a JSON Array of the same type
     * as the <code>element</code>.
     *
     * @param ja      The {@link org.json.simple.JSONArray} to be parsed.
     * @param <E>     The type parameter of the JSON Array to be returned.
     * @param element The type of elements for the resulting JSONArray to have
     * @return The JSONArray object.
     * @throws ParseException       if the JSON Object was unable to be parsed.
     * @throws NullPointerException if there were null value(s) were passed.
     */
    public static <E extends ParsableJSONObject<E>>
    JSONArray<E> parse(org.json.simple.JSONArray ja, E element) {
        JSONArray<E> jsonArray = new JSONArray<>(0, element);
        if (ja == null) return jsonArray;
        for (Object object : ja) {
            jsonArray.add(element.parseFromJSON(object.toString()));
        }
        return jsonArray;
    }

    /**
     * Parses the given <code>String</code> to a JSON Array of the same type
     * as the <code>element</code>.
     *
     * @param json    The <code>String</code> to be parsed.
     * @param element The type of element th resulting JSONArray should have.
     * @param <E>     The type parameter of the JSON Array to be returned.
     * @return The JSONArray object parsed from the JSON String.
     * @throws com.vplaygames.PM4J.exceptions.ParseException if the JSON Object was unable to be parsed.
     * @throws NullPointerException                          if there were null value(s) were passed.
     */
    public static <E extends ParsableJSONObject<E>>
    JSONArray<E> parse(String json, E element) {
        return parse(MiscUtil.parseJSONArray(json), element);
    }

    @Override
    public String getAsJSON() {
        StringJoiner tor = new StringJoiner(",", "[", "]");
        for (E e : this) {
            tor.add(e.getAsJSON());
        }
        return tor.toString();
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
    public String toString() {
        return getAsJSON();
    }

    @Override
    public E set(int index, E element) {
        checkAccess();
        return super.set(index, element);
    }

    @Override
    public boolean add(E e) {
        checkAccess();
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        checkAccess();
        super.add(index, element);
    }

    @Override
    public E remove(int index) {
        checkAccess();
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        checkAccess();
        return super.remove(o);
    }

    @Override
    public void clear() {
        checkAccess();
        super.clear();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        checkAccess();
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkAccess();
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        checkAccess();
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        checkAccess();
        return super.retainAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        checkAccess();
        return super.removeIf(filter);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        checkAccess();
        super.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        checkAccess();
        super.sort(c);
    }

    private void checkAccess() {
        if (initialized) throw new IllegalStateException();
    }
}
