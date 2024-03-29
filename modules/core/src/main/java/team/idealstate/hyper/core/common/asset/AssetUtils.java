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

package team.idealstate.hyper.core.common.asset;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.core.common.AssertUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * <p>AssetUtils</p>
 *
 * <p>创建于 2024/2/9 16:22</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AssetUtils {

    @NotNull
    public static String asset(@NotNull Class<?> sourceClass, @NotNull String assetPath) {
        AssertUtils.notNull(sourceClass, "无效的来源类型");
        AssertUtils.notBlank(assetPath, "无效的资源路径");
        return "/assets/" + getName(sourceClass) + assetPath;
    }

    private static String getName(Class<?> sourceClass) {
        String name = null;
        URI uri;
        try {
            uri = sourceClass.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try (JarFile jarFile = new JarFile(new File(uri))) {
            Manifest manifest = jarFile.getManifest();
            if (manifest != null) {
                Attributes mainAttributes = manifest.getMainAttributes();
                if (mainAttributes != null) {
                    name = mainAttributes.getValue("Hyper-Name");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AssertUtils.notBlank(name, "无效的工件名称");
        return name;
    }
}
