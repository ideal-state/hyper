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

import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

/**
 * <p>AnnotatedClass</p>
 *
 * <p>创建于 2024/6/26 上午9:36</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
@MyAnnotation
public class AnnotatedClassTest {

    @Test
    public void test() {
        try {
            Field out = System.class.getDeclaredField("out");
            out.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.set(out, out.getModifiers() & ~Modifier.FINAL);
            out.set(null, new PrintStream(System.out) {
                @Override
                public void println(String x) {
                    try {
                        UUID.fromString(x);
                    } catch (Exception e) {
                        super.println(x);
                        return;
                    }
                    super.println(x);
                    try {
                        throw new RuntimeException("test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
