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

package team.idealstate.hyper.core.common.string;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.annotation.lang.Nullable;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;

/**
 * <p>StringUtils</p>
 *
 * <p>创建于 2024/2/4 14:43</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 0.0.1
 */
public abstract class StringUtils {

    public static boolean isEmpty(@NotNull CharSequence charSequence) {
        AssertUtils.notNull(charSequence, "无效的字符序列");
        return charSequence.length() == 0;
    }

    public static boolean isNullOrEmpty(@Nullable CharSequence charSequence) {
        return ObjectUtils.isNull(charSequence) || charSequence.length() == 0;
    }

    public static boolean isNotEmpty(@NotNull CharSequence charSequence) {
        return !isEmpty(charSequence);
    }

    public static boolean isNotNullOrEmpty(@Nullable CharSequence charSequence) {
        return !isNullOrEmpty(charSequence);
    }

    public static boolean isBlank(@NotNull CharSequence charSequence) {
        AssertUtils.notNull(charSequence, "无效的字符序列");
        if (isEmpty(charSequence)) {
            return true;
        }
        final int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrBlank(@Nullable CharSequence charSequence) {
        if (isNullOrEmpty(charSequence)) {
            return true;
        }
        assert charSequence != null;
        final int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(@NotNull CharSequence charSequence) {
        AssertUtils.notNull(charSequence, "无效的字符序列");
        return !isBlank(charSequence);
    }

    public static boolean isNotNullOrBlank(@Nullable CharSequence charSequence) {
        return !isNullOrBlank(charSequence);
    }

    public static boolean isNumeric(@Nullable CharSequence charSequence) {
        if (isNullOrBlank(charSequence)) {
            return false;
        }
        assert charSequence != null;
        int length = charSequence.length();
        char c = charSequence.charAt(0);
        if (length == 1) {
            return c >= '0' && c <= '9';
        }
        if (c != '-' && (c < '0' || c > '9')) {
            return false;
        }
        c = charSequence.charAt(length - 1);
        if (c < '0' || c > '9') {
            return false;
        }
        length = length - 1;
        boolean dotAlreadyExists = false;
        for (int i = 1; i < length; i++) {
            c = charSequence.charAt(i);
            if (c == '.') {
                if (dotAlreadyExists) {
                    return false;
                }
                dotAlreadyExists = true;
                continue;
            }
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isIntegral(CharSequence charSequence) {
        if (isNullOrBlank(charSequence)) {
            return false;
        }
        assert charSequence != null;
        int length = charSequence.length();
        char c = charSequence.charAt(0);
        if (length == 1) {
            return c >= '0' && c <= '9';
        }
        if (c != '-' && (c < '0' || c > '9')) {
            return false;
        }
        c = charSequence.charAt(length - 1);
        if (c < '0' || c > '9') {
            return false;
        }
        length = length - 1;
        for (int i = 1; i < length; i++) {
            c = charSequence.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static int countMatches(@Nullable CharSequence charSequence, char matched) {
        if (isNullOrEmpty(charSequence)) {
            return 0;
        }
        assert charSequence != null;
        final int stringLen = charSequence.length();
        int count = 0;
        for (int i = 0; i < stringLen; i++) {
            char c = charSequence.charAt(i);
            if (c == matched) {
                count = count + 1;
            }
        }
        return count;
    }

    public static int countMatches(@Nullable CharSequence charSequence, @Nullable CharSequence matched) {
        if (isNullOrEmpty(charSequence) || isNullOrEmpty(matched)) {
            return 0;
        }
        assert charSequence != null;
        assert matched != null;
        final int stringLen = charSequence.length();
        final int matchedLen = matched.length();
        if (stringLen < matchedLen) {
            return 0;
        }
        int count = 0;
        int matching = 0;
        for (int i = 0; i < stringLen; i++) {
            char c = charSequence.charAt(i);
            if (c == matched.charAt(matching)) {
                matching = matching + 1;
                if (matching >= matchedLen) {
                    matching = 0;
                    count = count + 1;
                }
            } else {
                matching = 0;
            }
        }
        return count;
    }
}
