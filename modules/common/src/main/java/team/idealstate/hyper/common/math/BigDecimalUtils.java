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

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>BigDecimalUtils</p>
 *
 * <p>创建于 2024/3/23 17:31</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BigDecimalUtils {
    private static final Map<String, BigDecimal> CACHE = new ConcurrentHashMap<>(128, 0.6F);
    public static final BigDecimal ZERO = valueOf("0.0");

    @NotNull
    public static BigDecimal valueOf(@NotNull String number) {
        AssertUtils.isNumeric(number, "number 必须是一个数字");
        int length = number.length();
        if (length > 1) {
            Integer lastOfZeroIndex = null;
            for (int i = 0; i < length; i++) {
                if (number.charAt(i) != '0') {
                    break;
                }
                lastOfZeroIndex = i;
            }
            if (lastOfZeroIndex != null) {
                number = number.substring(lastOfZeroIndex + 1);
            }
        }
        return CACHE.computeIfAbsent(number, BigDecimal::new);
    }

    @NotNull
    public static BigDecimal valueOf(long number) {
        return CACHE.computeIfAbsent(String.valueOf(number), BigDecimal::new);
    }

    @NotNull
    public static BigDecimal add(@NotNull String number, @NotNull String augend) {
        AssertUtils.isNumeric(number, "number 必须是一个数字");
        AssertUtils.isNumeric(augend, "augend 必须是一个数字");
        BigDecimal ret = valueOf(number).add(valueOf(augend));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal add(@NotNull BigDecimal number, @NotNull BigDecimal augend) {
        AssertUtils.notNull(number, "number 必须是一个数字");
        AssertUtils.notNull(augend, "augend 必须是一个数字");
        BigDecimal ret = number.add(augend);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal subtract(@NotNull String number, @NotNull String subtrahend) {
        AssertUtils.isNumeric(number, "number 必须是一个数字");
        AssertUtils.isNumeric(subtrahend, "subtrahend 必须是一个数字");
        BigDecimal ret = valueOf(number).subtract(valueOf(subtrahend));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal subtract(@NotNull BigDecimal number, @NotNull BigDecimal subtrahend) {
        AssertUtils.notNull(number, "number 必须是一个数字");
        AssertUtils.notNull(subtrahend, "subtrahend 必须是一个数字");
        BigDecimal ret = number.subtract(subtrahend);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal multiply(@NotNull String number, @NotNull String multiplicand) {
        AssertUtils.isNumeric(number, "number 必须是一个数字");
        AssertUtils.isNumeric(multiplicand, "multiplicand 必须是一个数字");
        BigDecimal ret = valueOf(number).multiply(valueOf(multiplicand));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal multiply(@NotNull BigDecimal number, @NotNull BigDecimal multiplicand) {
        AssertUtils.notNull(number, "number 必须是一个数字");
        AssertUtils.notNull(multiplicand, "multiplicand 必须是一个数字");
        BigDecimal ret = number.multiply(multiplicand);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal divide(@NotNull String number, @NotNull String divisor) {
        AssertUtils.isNumeric(number, "number 必须是一个数字");
        AssertUtils.isNumeric(divisor, "divisor 必须是一个数字");
        BigDecimal ret = valueOf(number).divide(valueOf(divisor));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal divide(@NotNull BigDecimal number, @NotNull BigDecimal divisor) {
        AssertUtils.notNull(number, "number 必须是一个数字");
        AssertUtils.notNull(divisor, "divisor 必须是一个数字");
        BigDecimal ret = number.divide(divisor);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal mod(@NotNull String number, @NotNull String m) {
        AssertUtils.isNumeric(number, "number 必须是一个数字");
        AssertUtils.isNumeric(m, "m 必须是一个数字");
        BigDecimal ret = valueOf(number).divideAndRemainder(valueOf(m))[1];
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal mod(@NotNull BigDecimal number, @NotNull BigDecimal m) {
        AssertUtils.notNull(number, "number 必须是一个数字");
        AssertUtils.notNull(m, "m 必须是一个数字");
        BigDecimal ret = number.divideAndRemainder(m)[1];
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal min(@NotNull String number, @NotNull String val) {
        AssertUtils.isNumeric(number, "number 必须是一个数字");
        AssertUtils.isNumeric(val, "val 必须是一个数字");
        BigDecimal ret = valueOf(number).min(valueOf(val));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal min(@NotNull BigDecimal number, @NotNull BigDecimal val) {
        AssertUtils.notNull(number, "number 必须是一个数字");
        AssertUtils.notNull(val, "val 必须是一个数字");
        BigDecimal ret = number.min(val);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal max(@NotNull String number, @NotNull String val) {
        AssertUtils.isNumeric(number, "number 必须是一个数字");
        AssertUtils.isNumeric(val, "val 必须是一个数字");
        BigDecimal ret = valueOf(number).max(valueOf(val));
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }

    @NotNull
    public static BigDecimal max(@NotNull BigDecimal number, @NotNull BigDecimal val) {
        AssertUtils.notNull(number, "number 必须是一个数字");
        AssertUtils.notNull(val, "val 必须是一个数字");
        BigDecimal ret = number.max(val);
        CACHE.putIfAbsent(ret.toString(), ret);
        return ret;
    }
}
