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

package team.idealstate.hyper.core.common.uid;

import team.idealstate.hyper.core.common.AssertUtils;

import java.util.UUID;

/**
 * <p>UUIDUtils</p>
 *
 * <p>创建于 2024/3/9 4:50</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
public abstract class UUIDUtils {

    private static final int BIN_LEN = 16;
    private static final int BIN_SUFFIX = 8;
    private static final int[] SWAP = {6, 4, 0, 2};
    private static final int[] RESTORE = {4, 6, 2, 0};

    public static UUID binToUUID(byte[] uuidBin) {
        return binToUUID(uuidBin, false);
    }

    public static UUID binToUUID(byte[] uuidBin, boolean swapFlag) {
        AssertUtils.isTrue(uuidBin != null && uuidBin.length == BIN_LEN, "无效的字节型 UUID");
        assert uuidBin != null;
        if (swapFlag) {
            byte[] uuidBinCopy = new byte[BIN_LEN];
            for (int i = 0, j = 0; j < SWAP.length; j++) {
                int k = SWAP[j];
                uuidBinCopy[i++] = uuidBin[k++];
                uuidBinCopy[i++] = uuidBin[k];
            }
            System.arraycopy(uuidBin, BIN_SUFFIX, uuidBinCopy, BIN_SUFFIX, BIN_LEN - BIN_SUFFIX);
            uuidBin = uuidBinCopy;
        }
        long mostBit = ((((long) uuidBin[0] & 0xFF) << 56)
                | (((long) uuidBin[1] & 0xFF) << 48)
                | (((long) uuidBin[2] & 0xFF) << 40)
                | (((long) uuidBin[3] & 0xFF) << 32)
                | (((long) uuidBin[4] & 0xFF) << 24)
                | (((long) uuidBin[5] & 0xFF) << 16)
                | (((long) uuidBin[6] & 0xFF) << 8)
                | (((long) uuidBin[7] & 0xFF)));
        long leastBit = ((((long) uuidBin[8] & 0xFF) << 56)
                | (((long) uuidBin[9] & 0xFF) << 48)
                | (((long) uuidBin[10] & 0xFF) << 40)
                | (((long) uuidBin[11] & 0xFF) << 32)
                | (((long) uuidBin[12] & 0xFF) << 24)
                | (((long) uuidBin[13] & 0xFF) << 16)
                | (((long) uuidBin[14] & 0xFF) << 8)
                | (((long) uuidBin[15] & 0xFF)));
        return new UUID(mostBit, leastBit);
    }

    public static byte[] uuidToBin(String uuid) {
        return uuidToBin(UUID.fromString(uuid), false);
    }

    public static byte[] uuidToBin(String uuid, boolean swapFlag) {
        return uuidToBin(UUID.fromString(uuid), swapFlag);
    }

    public static byte[] uuidToBin(UUID uuid) {
        return uuidToBin(uuid, false);
    }

    public static byte[] uuidToBin(UUID uuid, boolean swapFlag) {
        AssertUtils.notNull(uuid, "无效的 UUID");
        long mostBit = uuid.getMostSignificantBits();
        byte[] uuidBin = new byte[16];
        uuidBin[0] = (byte) ((mostBit >> 56) & 0xFF);
        uuidBin[1] = (byte) ((mostBit >> 48) & 0xFF);
        uuidBin[2] = (byte) ((mostBit >> 40) & 0xFF);
        uuidBin[3] = (byte) ((mostBit >> 32) & 0xFF);
        uuidBin[4] = (byte) ((mostBit >> 24) & 0xFF);
        uuidBin[5] = (byte) ((mostBit >> 16) & 0xFF);
        uuidBin[6] = (byte) ((mostBit >> 8) & 0xFF);
        uuidBin[7] = (byte) (mostBit & 0xFF);
        long leastBit = uuid.getLeastSignificantBits();
        uuidBin[8] = (byte) ((leastBit >> 56) & 0xFF);
        uuidBin[9] = (byte) ((leastBit >> 48) & 0xFF);
        uuidBin[10] = (byte) ((leastBit >> 40) & 0xFF);
        uuidBin[11] = (byte) ((leastBit >> 32) & 0xFF);
        uuidBin[12] = (byte) ((leastBit >> 24) & 0xFF);
        uuidBin[13] = (byte) ((leastBit >> 16) & 0xFF);
        uuidBin[14] = (byte) ((leastBit >> 8) & 0xFF);
        uuidBin[15] = (byte) (leastBit & 0xFF);
        if (swapFlag) {
            for (int i = 0, j = 0; j < RESTORE.length; j++) {
                int k = RESTORE[j];
                byte b = uuidBin[i];
                uuidBin[i++] = uuidBin[k];
                uuidBin[k++] = b;
                b = uuidBin[i];
                uuidBin[i++] = uuidBin[k];
                uuidBin[k] = b;
            }
        }
        return uuidBin;
    }
}
