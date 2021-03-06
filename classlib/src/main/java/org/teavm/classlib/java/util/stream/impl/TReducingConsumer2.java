/*
 *  Copyright 2017 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.classlib.java.util.stream.impl;

import java.util.function.BiFunction;
import java.util.function.Predicate;

class TReducingConsumer2<T, U> implements Predicate<T> {
    private BiFunction<U, ? super T, U> accumulator;
    U result;

    TReducingConsumer2(BiFunction<U, ? super T, U> accumulator, U result) {
        this.accumulator = accumulator;
        this.result = result;
    }

    @Override
    public boolean test(T t) {
        result = accumulator.apply(result, t);
        return true;
    }
}
