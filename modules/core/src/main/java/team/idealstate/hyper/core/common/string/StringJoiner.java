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

import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.core.common.AssertUtils;

/**
 * <p>StringJoiner</p>
 *
 * <p>创建于 2024/3/27 6:16</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class StringJoiner {

    private static final int DEFAULT_INITIAL_CAPACITY = 64;
    private final String delimiter;
    private final boolean delimiterIsEmpty;
    private final String prefix;
    private final boolean prefixIsEmpty;
    private final String suffix;
    private final boolean suffixIsEmpty;
    private final StringBuilder builder;

    public StringJoiner(@NotNull CharSequence delimiter) {
        this(delimiter, "", "");
    }

    public StringJoiner(@NotNull CharSequence delimiter, @NotNull CharSequence prefix, @NotNull CharSequence suffix) {
        this(DEFAULT_INITIAL_CAPACITY, delimiter, prefix, suffix);
    }

    public StringJoiner(int initialCapacity, @NotNull CharSequence delimiter) {
        this(initialCapacity, delimiter, "", "");
    }

    public StringJoiner(int initialCapacity, @NotNull CharSequence delimiter, @NotNull CharSequence prefix, @NotNull CharSequence suffix) {
        AssertUtils.isTrue(initialCapacity > 0, "无效的初始容量");
        AssertUtils.notNull(delimiter, "无效的分隔符");
        AssertUtils.notNull(prefix, "无效的前缀");
        AssertUtils.notNull(suffix, "无效的后缀");
        this.delimiter = delimiter.toString();
        this.delimiterIsEmpty = StringUtils.isEmpty(delimiter);
        this.prefix = prefix.toString();
        this.prefixIsEmpty = StringUtils.isEmpty(prefix);
        this.suffix = suffix.toString();
        this.suffixIsEmpty = StringUtils.isEmpty(suffix);
        this.builder = new StringBuilder(initialCapacity);
    }

    @NotNull
    public StringJoiner append(@NotNull StringBuffer stringBuffer) {
        AssertUtils.notNull(stringBuffer, "无效的字符串缓冲区");
        appendDelimiter();
        appendPrefix();
        builder.append(stringBuffer);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(@NotNull String string) {
        AssertUtils.notNull(string, "无效的字符串");
        appendDelimiter();
        appendPrefix();
        builder.append(string);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(int i) {
        AssertUtils.notNull(i, "无效的整数");
        appendDelimiter();
        appendPrefix();
        builder.append(i);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(long l) {
        AssertUtils.notNull(l, "无效的长整数");
        appendDelimiter();
        appendPrefix();
        builder.append(l);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(float f) {
        AssertUtils.notNull(f, "无效的浮点数");
        appendDelimiter();
        appendPrefix();
        builder.append(f);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(double d) {
        AssertUtils.notNull(d, "无效的双精度浮点数");
        appendDelimiter();
        appendPrefix();
        builder.append(d);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(boolean b) {
        AssertUtils.notNull(b, "无效的布尔值");
        appendDelimiter();
        appendPrefix();
        builder.append(b);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(char c) {
        AssertUtils.notNull(c, "无效的字符");
        appendDelimiter();
        appendPrefix();
        builder.append(c);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(char[] chars) {
        AssertUtils.notNull(chars, "无效的字符");
        appendDelimiter();
        appendPrefix();
        builder.append(chars);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(char[] chars, int offset) {
        AssertUtils.notNull(chars, "无效的字符");
        return append(chars, offset, chars.length);
    }

    @NotNull
    public StringJoiner append(char[] chars, int offset, int len) {
        AssertUtils.notNull(chars, "无效的字符");
        AssertUtils.isTrue(offset >= 0 && offset <= len && len <= chars.length, "无效的索引");
        appendDelimiter();
        appendPrefix();
        builder.append(chars, offset, len);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(@NotNull CharSequence charSequence) {
        AssertUtils.notNull(charSequence, "无效的字符序列");
        appendDelimiter();
        appendPrefix();
        builder.append(charSequence);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(@NotNull CharSequence charSequence, int start) {
        AssertUtils.notNull(charSequence, "无效的字符序列");
        return append(charSequence, start, charSequence.length());
    }

    @NotNull
    public StringJoiner append(@NotNull CharSequence charSequence, int start, int end) {
        AssertUtils.notNull(charSequence, "无效的字符序列");
        AssertUtils.isTrue(start >= 0 && start <= end && end <= charSequence.length(), "无效的索引");
        appendDelimiter();
        appendPrefix();
        builder.append(charSequence, start, end);
        appendSuffix();
        return this;
    }

    @NotNull
    public StringJoiner append(@NotNull Object object) {
        AssertUtils.notNull(object, "无效的对象");
        appendDelimiter();
        appendPrefix();
        builder.append(object);
        appendSuffix();
        return this;
    }

    protected void appendDelimiter() {
        if (builder.length() > 0 && !delimiterIsEmpty) {
            builder.append(delimiter);
        }
    }

    protected void appendPrefix() {
        if (!prefixIsEmpty) {
            builder.append(prefix);
        }
    }

    protected void appendSuffix() {
        if (!suffixIsEmpty) {
            builder.append(suffix);
        }
    }

    @NotNull
    public String getDelimiter() {
        return delimiter;
    }

    @NotNull
    public String getPrefix() {
        return prefix;
    }

    @NotNull
    public String getSuffix() {
        return suffix;
    }

    @NotNull
    @Override
    public String toString() {
        return builder.toString();
    }
}
