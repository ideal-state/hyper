/*
 *    hyper-core
 *    Copyright [2024] [ideal-state]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package team.idealstate.hyper.core.common;

import team.idealstate.hyper.core.common.array.ArrayUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.string.StringUtils;
import team.idealstate.hyper.core.common.template.CollectionUtils;
import team.idealstate.hyper.core.common.template.MapUtils;

import java.util.Collection;
import java.util.Map;

/**
 * <p>AssertUtils</p>
 *
 * <p>创建于 2024/2/4 14:43</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes"})
public abstract class AssertUtils {

    public static void isTrue(Boolean expression, String message) {
        if (!Boolean.TRUE.equals(expression)) {
            if (ObjectUtils.isNull(message)) {
                throw new AssertionException();
            }
            throw new AssertionException(message);
        }
    }

    public static void isFalse(Boolean expression, String message) {
        isTrue(!Boolean.TRUE.equals(expression), message);
    }

    public static void isNull(Object object, String message) {
        isTrue(ObjectUtils.isNull(object), message);
    }

    public static void notNull(Object object, String message) {
        isTrue(ObjectUtils.isNotNull(object), message);
    }

    public static void isEmpty(Object[] array, String message) {
        isTrue(ArrayUtils.isEmpty(array), message);
    }

    public static void isNullOrEmpty(Object[] array, String message) {
        isTrue(ArrayUtils.isNullOrEmpty(array), message);
    }

    public static void notEmpty(Object[] array, String message) {
        isTrue(ArrayUtils.isNotEmpty(array), message);
    }

    public static void notNullOrEmpty(Object[] array, String message) {
        isTrue(ArrayUtils.isNotNullOrEmpty(array), message);
    }

    public static void isEmpty(byte[] array, String message) {
        isTrue(ArrayUtils.isEmpty(array), message);
    }

    public static void isNullOrEmpty(byte[] array, String message) {
        isTrue(ArrayUtils.isNullOrEmpty(array), message);
    }

    public static void notEmpty(byte[] array, String message) {
        isTrue(ArrayUtils.isNotEmpty(array), message);
    }

    public static void notNullOrEmpty(byte[] array, String message) {
        isTrue(ArrayUtils.isNotNullOrEmpty(array), message);
    }

    public static void isEmpty(short[] array, String message) {
        isTrue(ArrayUtils.isEmpty(array), message);
    }

    public static void isNullOrEmpty(short[] array, String message) {
        isTrue(ArrayUtils.isNullOrEmpty(array), message);
    }

    public static void notEmpty(short[] array, String message) {
        isTrue(ArrayUtils.isNotEmpty(array), message);
    }

    public static void notNullOrEmpty(short[] array, String message) {
        isTrue(ArrayUtils.isNotNullOrEmpty(array), message);
    }

    public static void isEmpty(int[] array, String message) {
        isTrue(ArrayUtils.isEmpty(array), message);
    }

    public static void isNullOrEmpty(int[] array, String message) {
        isTrue(ArrayUtils.isNullOrEmpty(array), message);
    }

    public static void notEmpty(int[] array, String message) {
        isTrue(ArrayUtils.isNotEmpty(array), message);
    }

    public static void notNullOrEmpty(int[] array, String message) {
        isTrue(ArrayUtils.isNotNullOrEmpty(array), message);
    }

    public static void isEmpty(long[] array, String message) {
        isTrue(ArrayUtils.isEmpty(array), message);
    }

    public static void isNullOrEmpty(long[] array, String message) {
        isTrue(ArrayUtils.isNullOrEmpty(array), message);
    }

    public static void notEmpty(long[] array, String message) {
        isTrue(ArrayUtils.isNotEmpty(array), message);
    }

    public static void notNullOrEmpty(long[] array, String message) {
        isTrue(ArrayUtils.isNotNullOrEmpty(array), message);
    }

    public static void isEmpty(float[] array, String message) {
        isTrue(ArrayUtils.isEmpty(array), message);
    }

    public static void isNullOrEmpty(float[] array, String message) {
        isTrue(ArrayUtils.isNullOrEmpty(array), message);
    }

    public static void notEmpty(float[] array, String message) {
        isTrue(ArrayUtils.isNotEmpty(array), message);
    }

    public static void notNullOrEmpty(float[] array, String message) {
        isTrue(ArrayUtils.isNotNullOrEmpty(array), message);
    }

    public static void isEmpty(double[] array, String message) {
        isTrue(ArrayUtils.isEmpty(array), message);
    }

    public static void isNullOrEmpty(double[] array, String message) {
        isTrue(ArrayUtils.isNullOrEmpty(array), message);
    }

    public static void notEmpty(double[] array, String message) {
        isTrue(ArrayUtils.isNotEmpty(array), message);
    }

    public static void notNullOrEmpty(double[] array, String message) {
        isTrue(ArrayUtils.isNotNullOrEmpty(array), message);
    }

    public static void isEmpty(boolean[] array, String message) {
        isTrue(ArrayUtils.isEmpty(array), message);
    }

    public static void isNullOrEmpty(boolean[] array, String message) {
        isTrue(ArrayUtils.isNullOrEmpty(array), message);
    }

    public static void notEmpty(boolean[] array, String message) {
        isTrue(ArrayUtils.isNotEmpty(array), message);
    }

    public static void notNullOrEmpty(boolean[] array, String message) {
        isTrue(ArrayUtils.isNotNullOrEmpty(array), message);
    }

    public static void isEmpty(char[] array, String message) {
        isTrue(ArrayUtils.isEmpty(array), message);
    }

    public static void isNullOrEmpty(char[] array, String message) {
        isTrue(ArrayUtils.isNullOrEmpty(array), message);
    }

    public static void notEmpty(char[] array, String message) {
        isTrue(ArrayUtils.isNotEmpty(array), message);
    }

    public static void notNullOrEmpty(char[] array, String message) {
        isTrue(ArrayUtils.isNotNullOrEmpty(array), message);
    }

    public static void isEmpty(Collection collection, String message) {
        isTrue(CollectionUtils.isEmpty(collection), message);
    }

    public static void isNullOrEmpty(Collection collection, String message) {
        isTrue(CollectionUtils.isNullOrEmpty(collection), message);
    }

    public static void notEmpty(Collection collection, String message) {
        isTrue(CollectionUtils.isNotEmpty(collection), message);
    }

    public static void notNullOrEmpty(Collection collection, String message) {
        isTrue(CollectionUtils.isNotNullOrEmpty(collection), message);
    }

    public static void isEmpty(Map map, String message) {
        isTrue(MapUtils.isEmpty(map), message);
    }

    public static void isNullOrEmpty(Map map, String message) {
        isTrue(MapUtils.isNullOrEmpty(map), message);
    }

    public static void notEmpty(Map map, String message) {
        isTrue(MapUtils.isNotEmpty(map), message);
    }

    public static void notNullOrEmpty(Map map, String message) {
        isTrue(MapUtils.isNotNullOrEmpty(map), message);
    }

    public static void isBlank(CharSequence string, String message) {
        isTrue(StringUtils.isBlank(string), message);
    }

    public static void isNullOrBlank(CharSequence string, String message) {
        isTrue(StringUtils.isNullOrBlank(string), message);
    }

    public static void notBlank(CharSequence string, String message) {
        isTrue(StringUtils.isNotBlank(string), message);
    }

    public static void notNullOrBlank(CharSequence string, String message) {
        isTrue(StringUtils.isNotNullOrBlank(string), message);
    }

    public static void isEmpty(CharSequence string, String message) {
        isTrue(StringUtils.isEmpty(string), message);
    }

    public static void isNullOrEmpty(CharSequence string, String message) {
        isTrue(StringUtils.isNullOrEmpty(string), message);
    }

    public static void notEmpty(CharSequence string, String message) {
        isTrue(StringUtils.isNotEmpty(string), message);
    }

    public static void notNullOrEmpty(CharSequence string, String message) {
        isTrue(StringUtils.isNotNullOrEmpty(string), message);
    }

    public static void isArray(Object array, String message) {
        isTrue(ArrayUtils.isArray(array), message);
    }

    public static void isNotArray(Object array, String message) {
        isTrue(ArrayUtils.isNotArray(array), message);
    }

    public static void isNullOrArray(Object array, String message) {
        isTrue(ArrayUtils.isNullOrArray(array), message);
    }

    public static void isNotNullOrArray(Object array, String message) {
        isTrue(ArrayUtils.isNotNullOrArray(array), message);
    }

    public static void isNumeric(CharSequence string, String message) {
        isTrue(StringUtils.isNumeric(string), message);
    }

    public static void isIntegral(CharSequence string, String message) {
        isTrue(StringUtils.isIntegral(string), message);
    }
}
