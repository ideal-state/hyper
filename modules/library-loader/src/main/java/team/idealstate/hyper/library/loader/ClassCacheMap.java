/*
 *    hyper-library-loader
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

package team.idealstate.hyper.library.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>ClassCacheMap</p>
 *
 * <p>创建于 2024/3/28 11:37</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ClassCacheMap extends ConcurrentHashMap<String, Class<?>> {

    private static final Logger logger = LoggerFactory.getLogger(ClassCacheMap.class);
    private final URLClassLoader ucl;
    private final ThreadLocal<Set<String>> inGet = ThreadLocal.withInitial(HashSet::new);
    private final Set<String> cannotLoadClassNames = new HashSet<>(64, 0.6F);

    ClassCacheMap(URLClassLoader ucl) {
        this.ucl = ucl;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            ucl.close();
        } catch (Throwable e) {
            logger.error("在关闭 URLClassLoader 时抛出异常", e);
        } finally {
            super.finalize();
        }
    }

    @Override
    public Class<?> get(Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        Set<String> loadingClassNames = inGet.get();
        String className = (String) key;
        if (loadingClassNames.contains(className)) {
            return null;
        }
        Class<?> cls = super.get(className);
        if (cls == null && !cannotLoadClassNames.contains(className)) {
            try {
                loadingClassNames.add(className);
                logger.trace("尝试从直接依赖内加载类 {}", className);
                cls = Class.forName(className, true, ucl);
                if (ucl.equals(cls.getClassLoader())) {
                    logger.trace("已从直接依赖内加载类 {}", className);
                }
                put(className, cls);
            } catch (Throwable e) {
                cannotLoadClassNames.add(className);
                logger.trace("从直接依赖内加载类时抛出异常", e);
            } finally {
                loadingClassNames.remove(className);
            }
        }
        return cls;
    }
}
