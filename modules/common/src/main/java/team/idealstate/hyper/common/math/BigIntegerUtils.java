/*
 *    hyper-common
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

package team.idealstate.hyper.common.math;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.AssertUtils;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>BigIntegerUtils</p>
 *
 * <p>创建于 2024/1/28 13:15</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BigIntegerUtils {
    private static final Map<String, BigInteger> CACHE = new ConcurrentHashMap<>(128, 0.6F);
    public static final BigInteger ZERO = valueOf("0");

    @NotNull
    public static BigInteger valueOf(@NotNull String integer) {
        AssertUtils.isIntegral(integer, "integer 必须是一个整数");
        int length = integer.length();
        if (length > 1) {
            Integer lastOfZeroIndex = null;
            for (int i = 0; i < length; i++) {
                if (integer.charAt(i) != '0') {
                    break;
                }
                lastOfZeroIndex = i;
            }
            if (lastOfZeroIndex != null) {
                integer = integer.substring(lastOfZeroIndex + 1);
            }
        }
        return CACHE.computeIfAbsent(integer, BigInteger::new);
    }

    @NotNull
    public static BigInteger valueOf(long integer) {
        return CACHE.computeIfAbsent(String.valueOf(integer), BigInteger::new);
    }

    @NotNull
    public static BigInteger add(@NotNull String integer, @NotNull String augend) {
        AssertUtils.isIntegral(integer, "integer 必须是一个整数");
        AssertUtils.isIntegral(augend, "augend 必须是一个整数");
        BigInteger ret = valueOf(integer).add(valueOf(augend));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger add(@NotNull BigInteger integer, @NotNull BigInteger augend) {
        AssertUtils.notNull(integer, "integer 必须是一个整数");
        AssertUtils.notNull(augend, "augend 必须是一个整数");
        BigInteger ret = integer.add(augend);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger subtract(@NotNull String integer, @NotNull String subtrahend) {
        AssertUtils.isIntegral(integer, "integer 必须是一个整数");
        AssertUtils.isIntegral(subtrahend, "subtrahend 必须是一个整数");
        BigInteger ret = valueOf(integer).subtract(valueOf(subtrahend));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger subtract(@NotNull BigInteger integer, @NotNull BigInteger subtrahend) {
        AssertUtils.notNull(integer, "integer 必须是一个整数");
        AssertUtils.notNull(subtrahend, "subtrahend 必须是一个整数");
        BigInteger ret = integer.subtract(subtrahend);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger multiply(@NotNull String integer, @NotNull String multiplicand) {
        AssertUtils.isIntegral(integer, "integer 必须是一个整数");
        AssertUtils.isIntegral(multiplicand, "multiplicand 必须是一个整数");
        BigInteger ret = valueOf(integer).multiply(valueOf(multiplicand));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger multiply(@NotNull BigInteger integer, @NotNull BigInteger multiplicand) {
        AssertUtils.notNull(integer, "integer 必须是一个整数");
        AssertUtils.notNull(multiplicand, "multiplicand 必须是一个整数");
        BigInteger ret = integer.multiply(multiplicand);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger divide(@NotNull String integer, @NotNull String divisor) {
        AssertUtils.isIntegral(integer, "integer 必须是一个整数");
        AssertUtils.isIntegral(divisor, "divisor 必须是一个整数");
        BigInteger ret = valueOf(integer).divide(valueOf(divisor));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger divide(@NotNull BigInteger integer, @NotNull BigInteger divisor) {
        AssertUtils.notNull(integer, "integer 必须是一个整数");
        AssertUtils.notNull(divisor, "divisor 必须是一个整数");
        BigInteger ret = integer.divide(divisor);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger mod(@NotNull String integer, @NotNull String m) {
        AssertUtils.isIntegral(integer, "integer 必须是一个整数");
        AssertUtils.isIntegral(m, "m 必须是一个整数");
        BigInteger ret = valueOf(integer).mod(valueOf(m));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger mod(@NotNull BigInteger integer, @NotNull BigInteger m) {
        AssertUtils.notNull(integer, "integer 必须是一个整数");
        AssertUtils.notNull(m, "m 必须是一个整数");
        BigInteger ret = integer.mod(m);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger min(@NotNull String integer, @NotNull String val) {
        AssertUtils.isIntegral(integer, "integer 必须是一个整数");
        AssertUtils.isIntegral(val, "val 必须是一个整数");
        BigInteger ret = valueOf(integer).min(valueOf(val));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger min(@NotNull BigInteger integer, @NotNull BigInteger val) {
        AssertUtils.notNull(integer, "integer 必须是一个整数");
        AssertUtils.notNull(val, "val 必须是一个整数");
        BigInteger ret = integer.min(val);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger max(@NotNull String integer, @NotNull String val) {
        AssertUtils.isIntegral(integer, "integer 必须是一个整数");
        AssertUtils.isIntegral(val, "val 必须是一个整数");
        BigInteger ret = valueOf(integer).max(valueOf(val));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigInteger max(@NotNull BigInteger integer, @NotNull BigInteger val) {
        AssertUtils.notNull(integer, "integer 必须是一个整数");
        AssertUtils.notNull(val, "val 必须是一个整数");
        BigInteger ret = integer.max(val);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }
}
