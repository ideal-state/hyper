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

package team.idealstate.hyper.common.string;

import team.idealstate.hyper.common.AssertUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>HexUtils</p>
 *
 * <p>创建于 2024/3/9 5:01</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class HexUtils {

    private static final char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    private static final Map<Character, Byte> HEX_DIGIT_TABLE = new HashMap<>(16);

    static {
        for (int i = 0; i < HEX_DIGITS.length; i++) {
            HEX_DIGIT_TABLE.put(HEX_DIGITS[i], (byte) i);
        }
    }

    public static byte[] hexToBin(String hex) {
        AssertUtils.notNull(hex, "无效的十六进制字符串");
        final int len = hex.length() / 2;
        byte[] bin = new byte[len];
        for (int i = 0; i < len; i++) {
            int j = i * 2;
            bin[i] = (byte) ((HEX_DIGIT_TABLE.get(hex.charAt(j)) << 4) + HEX_DIGIT_TABLE.get(hex.charAt(j + 1)));
        }
        return bin;
    }

    public static String binToHex(byte[] bin) {
        AssertUtils.notNull(bin, "无效的字节数组");
        StringBuilder builder = new StringBuilder();
        for (byte b : bin) {
            builder.append(HEX_DIGITS[(b & 0xf0) >> 4])
                    .append(HEX_DIGITS[b & 0x0f]);
        }
        return builder.toString();
    }
}
