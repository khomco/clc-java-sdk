/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.core.function;

import java.util.function.Predicate;

/**
 * @author Ilya Drabenia
 */
public class ConstPredicate<T> implements Predicate<T> {
    private final boolean defaultValue;

    public ConstPredicate(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean test(T t) {
        return defaultValue;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public static <T> ConstPredicate<T> cast(Predicate<T> predicate) {
        return (ConstPredicate<T>) predicate;
    }
}
