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

package team.idealstate.hyper.core.common.array;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.template.CollectionUtils;
import team.idealstate.hyper.core.common.template.ListUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * <p>ArrayUtils</p>
 *
 * <p>创建于 2024/3/24 1:46</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ArrayUtils {

    private static final Set<Class<?>> NATIVELY_SUPPORTED_CLASSES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            byte.class, short.class, int.class, long.class, float.class, double.class, char.class, boolean.class,
            Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Character.class, Boolean.class,
            String.class
    )));

    private static boolean isNativelySupported(@NotNull Class<?> cls) {
        return NATIVELY_SUPPORTED_CLASSES.contains(cls);
    }

    @NotNull
    @SuppressWarnings({"unchecked"})
    public static <E> E[] copyOf(E @NotNull [] source) {
        AssertUtils.notNull(source, "无效的源数组");
        return (E[]) copyOfRange((Object) source, 0, source.length);
    }

    @NotNull
    @SuppressWarnings({"unchecked"})
    public static <E> E[] copyOfRange(E @NotNull [] source, int offset) {
        AssertUtils.notNull(source, "无效的源数组");
        int len = source.length;
        AssertUtils.isTrue(offset >= 0 && offset <= len, "无效的索引");
        return (E[]) copyOfRange((Object) source, offset, len);
    }

    @NotNull
    @SuppressWarnings({"unchecked"})
    public static <E> E[] copyOfRange(E @NotNull [] source, int offset, int len) {
        AssertUtils.notNull(source, "无效的源数组");
        AssertUtils.isTrue(offset >= 0 && offset <= len && len <= source.length, "无效的索引");
        return (E[]) copyOfRange((Object) source, offset, len);
    }

    private static Object copyOfRange(Object source, int offset, int len) {
        final int length = Array.getLength(source);
        if (length == 0) {
            return source;
        }
        Class<?> componentType = source.getClass().getComponentType();
        Object copy = Array.newInstance(componentType, len - offset);
        if (isNativelySupported(componentType)) {
            //noinspection SuspiciousSystemArraycopy
            System.arraycopy(source, offset, copy, 0, len);
            return copy;
        }
        for (int i = offset; i < len; i++) {
            Object element = Array.get(source, i);
            if (element == null) {
                continue;
            }
            if (element.getClass().isArray()) {
                Array.set(copy, i, copyOfRange(element, 0, Array.getLength(element)));
            } else {
                Array.set(copy, i, element);
            }
        }
        return copy;
    }

    @NotNull
    @SafeVarargs
    public static <E> E[] merge(E @NotNull [] array1, E @NotNull [] array2, E... arrays) {
        AssertUtils.notNull(array1, "无效的数组");
        AssertUtils.notNull(array2, "无效的数组");
        List<E[]> arrayList = ListUtils.linkedListOf();
        arrayList.add(array1);
        arrayList.add(array2);
        CollectionUtils.addAll(arrayList, arrays);
        Iterator<E[]> iterator = arrayList.iterator();
        int length = 0;
        while (iterator.hasNext()) {
            E[] array = iterator.next();
            if (ArrayUtils.isEmpty(array)) {
                iterator.remove();
            } else {
                length++;
            }
        }
        if (length == 0) {
            return array1;
        }
        List<E> array = new ArrayList<>(length);
        for (E[] es : arrayList) {
            CollectionUtils.addAll(array, es);
        }
        return array.toArray(array1);
    }

    @NotNull
    public static <E> E[] emptyIfNull(E @Nullable [] array, @NotNull Class<E> componentType) {
        AssertUtils.notNull(componentType, "无效的数组元素类型");
        //noinspection unchecked
        return ObjectUtils.isNull(array) ? (E[]) Array.newInstance(componentType, 0) : array;
    }

    @NotNull
    public static <E> E[] defaultIfEmpty(E @NotNull [] array, E @NotNull [] def) {
        AssertUtils.notNull(array, "无效的数组");
        AssertUtils.notNull(def, "无效的默认值");
        return isEmpty(array) ? def : array;
    }

    @NotNull
    public static <E> E[] defaultIfNullOrEmpty(E @Nullable [] array, E @NotNull [] def) {
        AssertUtils.notNull(def, "无效的默认值");
        return isNullOrEmpty(array) ? def : array;
    }

    public static boolean isArray(@NotNull Object array) {
        AssertUtils.notNull(array, "无效的数组");
        return array.getClass().isArray();
    }

    public static boolean isNotArray(@NotNull Object array) {
        return !isArray(array);
    }

    public static boolean isNullOrArray(@Nullable Object array) {
        return ObjectUtils.isNull(array) || array.getClass().isArray();
    }

    public static boolean isNotNullOrArray(@Nullable Object array) {
        return !isNullOrArray(array);
    }

    public static boolean isEmpty(Object @NotNull [] array) {
        AssertUtils.notNull(array, "无效的数组");
        return Array.getLength(array) == 0;
    }

    public static boolean isNullOrEmpty(Object @Nullable [] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static boolean isNotEmpty(Object @NotNull [] array) {
        return !isEmpty(array);
    }

    public static boolean isNotNullOrEmpty(Object @Nullable [] array) {
        return !isNullOrEmpty(array);
    }

    public static boolean isEmpty(byte @NotNull [] array) {
        AssertUtils.notNull(array, "无效的数组");
        return Array.getLength(array) == 0;
    }

    public static boolean isNullOrEmpty(byte @Nullable [] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static boolean isNotEmpty(byte @NotNull [] array) {
        return !isEmpty(array);
    }

    public static boolean isNotNullOrEmpty(byte @Nullable [] array) {
        return !isNullOrEmpty(array);
    }

    public static boolean isEmpty(short @NotNull [] array) {
        AssertUtils.notNull(array, "无效的数组");
        return Array.getLength(array) == 0;
    }

    public static boolean isNullOrEmpty(short @Nullable [] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static boolean isNotEmpty(short @NotNull [] array) {
        return !isEmpty(array);
    }

    public static boolean isNotNullOrEmpty(short @Nullable [] array) {
        return !isNullOrEmpty(array);
    }

    public static boolean isEmpty(int @NotNull [] array) {
        AssertUtils.notNull(array, "无效的数组");
        return Array.getLength(array) == 0;
    }

    public static boolean isNullOrEmpty(int @Nullable [] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static boolean isNotEmpty(int @NotNull [] array) {
        return !isEmpty(array);
    }

    public static boolean isNotNullOrEmpty(int @Nullable [] array) {
        return !isNullOrEmpty(array);
    }

    public static boolean isEmpty(long @NotNull [] array) {
        AssertUtils.notNull(array, "无效的数组");
        return Array.getLength(array) == 0;
    }

    public static boolean isNullOrEmpty(long @Nullable [] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static boolean isNotEmpty(long @NotNull [] array) {
        return !isEmpty(array);
    }

    public static boolean isNotNullOrEmpty(long @Nullable [] array) {
        return !isNullOrEmpty(array);
    }

    public static boolean isEmpty(float @NotNull [] array) {
        AssertUtils.notNull(array, "无效的数组");
        return Array.getLength(array) == 0;
    }

    public static boolean isNullOrEmpty(float @Nullable [] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static boolean isNotEmpty(float @NotNull [] array) {
        return !isEmpty(array);
    }

    public static boolean isNotNullOrEmpty(float @Nullable [] array) {
        return !isNullOrEmpty(array);
    }

    public static boolean isEmpty(double @NotNull [] array) {
        AssertUtils.notNull(array, "无效的数组");
        return Array.getLength(array) == 0;
    }

    public static boolean isNullOrEmpty(double @Nullable [] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static boolean isNotEmpty(double @NotNull [] array) {
        return !isEmpty(array);
    }

    public static boolean isNotNullOrEmpty(double @Nullable [] array) {
        return !isNullOrEmpty(array);
    }

    public static boolean isEmpty(boolean @NotNull [] array) {
        AssertUtils.notNull(array, "无效的数组");
        return Array.getLength(array) == 0;
    }

    public static boolean isNullOrEmpty(boolean @Nullable [] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static boolean isNotEmpty(boolean @NotNull [] array) {
        return !isEmpty(array);
    }

    public static boolean isNotNullOrEmpty(boolean @Nullable [] array) {
        return !isNullOrEmpty(array);
    }

    public static boolean isEmpty(char @NotNull [] array) {
        AssertUtils.notNull(array, "无效的数组");
        return Array.getLength(array) == 0;
    }

    public static boolean isNullOrEmpty(char @Nullable [] array) {
        return ObjectUtils.isNull(array) || array.length == 0;
    }

    public static boolean isNotEmpty(char @NotNull [] array) {
        return !isEmpty(array);
    }

    public static boolean isNotNullOrEmpty(char @Nullable [] array) {
        return !isNullOrEmpty(array);
    }
}
