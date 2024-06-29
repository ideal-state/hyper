/*
 *    hyper-context
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

package team.idealstate.hyper.context.impl.bean.scope;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.common.template.MapUtils;
import team.idealstate.hyper.common.template.SetUtils;
import team.idealstate.hyper.context.api.bean.exception.BeanContextException;
import team.idealstate.hyper.context.api.bean.scope.BeanScope;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>StdPrototypeBeanScope</p>
 *
 * <p>创建于 2024/6/28 下午8:55</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdPrototypeBeanScope implements BeanScope {

    private static final Logger logger = LogManager.getLogger(StdPrototypeBeanScope.class);

    private static final long DEFAULT_TIMEOUT = 3L;

    private final Map<String, Object> prototypes = MapUtils.linkedMapOf();
    private final Map<String, Object> earlyPrototypes = MapUtils.linkedMapOf();
    private final Lock lock = new ReentrantLock();

    private void checkName(@NotNull String name) {
        if (prototypes.containsKey(name)) {
            throw new BeanContextException("已存在具有相同名称 '" + name + "' 的 Bean 对象");
        }
    }

    private void checkState(@NotNull Object object) {
        if (!ObjectUtils.isCloneable(object)) {
            throw new BeanContextException("Bean 对象原型必须实现 Cloneable 接口和 clone 方法");
        }
    }

    @Override
    public void addEarlyBean(@NotNull String name, @NotNull Object object) {
        AssertUtils.notNullOrBlank(name, "无效的 Bean 名称");
        AssertUtils.notNull(object, "无效的 Bean 对象");
        checkName(name);
        checkState(object);
        try {
            if (!lock.tryLock(DEFAULT_TIMEOUT, TimeUnit.SECONDS)) {
                logger.error("系统繁忙，注册操作已超时");
                return;
            }
            checkName(name);
            try {
                earlyPrototypes.put(name, object);
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("已取消注册操作，线程已中断");
        }
    }

    @Override
    public void addBean(@NotNull String name, @NotNull Object object) {
        AssertUtils.notNullOrBlank(name, "无效的 Bean 名称");
        AssertUtils.notNull(object, "无效的 Bean 对象");
        checkName(name);
        checkState(object);
        try {
            if (!lock.tryLock(DEFAULT_TIMEOUT, TimeUnit.SECONDS)) {
                logger.error("系统繁忙，注册操作已超时");
                return;
            }
            checkName(name);
            try {
                earlyPrototypes.remove(name);
                prototypes.put(name, object);
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("已取消注册操作，线程已中断");
        }
    }

    @Nullable
    @Override
    public <T> T getBean(@NotNull String name, @NotNull Class<T> type) {
        AssertUtils.notNullOrBlank(name, "无效的 Bean 名称");
        AssertUtils.notNull(type, "无效的 Bean 类型");
        Object prototypeObject = prototypes.get(name);
        if (prototypeObject == null) {
            prototypeObject = earlyPrototypes.get(name);
        }
        if (ObjectUtils.isNull(prototypeObject)) {
            return null;
        }
        if (!type.isAssignableFrom(prototypeObject.getClass())) {
            return null;
        }
        return type.cast(ObjectUtils.copyOf(prototypeObject));
    }

    @Nullable
    @Override
    public Object getBeanByName(@NotNull String name) {
        AssertUtils.notNullOrBlank(name, "无效的 Bean 名称");
        Object prototypeObject = prototypes.get(name);
        if (prototypeObject == null) {
            prototypeObject = earlyPrototypes.get(name);
        }
        if (ObjectUtils.isNull(prototypeObject)) {
            return null;
        }
        return ObjectUtils.copyOf(prototypeObject);
    }

    @Nullable
    @Override
    public <T> T getBeanByType(@NotNull Class<T> type) {
        AssertUtils.notNull(type, "无效的 Bean 类型");
        for (Object prototypeObject : prototypes.values()) {
            if (type.isAssignableFrom(prototypeObject.getClass())) {
                return type.cast(ObjectUtils.copyOf(prototypeObject));
            }
        }
        for (Object prototypeObject : earlyPrototypes.values()) {
            if (type.isAssignableFrom(prototypeObject.getClass())) {
                return type.cast(ObjectUtils.copyOf(prototypeObject));
            }
        }
        return null;
    }

    @NotNull
    @Override
    public <T> List<T> getBeansByType(@NotNull Class<T> type) {
        AssertUtils.notNull(type, "无效的 Bean 类型");
        Set<T> prototypeObjects = SetUtils.linkedSetOf();
        for (Object prototypeObject : prototypes.values()) {
            if (type.isAssignableFrom(prototypeObject.getClass())) {
                prototypeObjects.add(type.cast(ObjectUtils.copyOf(prototypeObject)));
            }
        }
        for (Object prototypeObject : earlyPrototypes.values()) {
            if (type.isAssignableFrom(prototypeObject.getClass())) {
                prototypeObjects.add(type.cast(ObjectUtils.copyOf(prototypeObject)));
            }
        }
        if (CollectionUtils.isEmpty(prototypeObjects)) {
            return ListUtils.emptyList();
        }
        return ListUtils.linkedListOf(prototypeObjects);
    }
}
