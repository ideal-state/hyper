/*
 *    hyper-gradle-plugin
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

package team.idealstate.hyper.gradle.module.repository.entity

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.net.URI

/**
 * <p>Repository</p>
 *
 * <p>创建于 2024/3/20 17:01</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
data class Repository(
    @JsonDeserialize(using = Type.Deserializer::class)
    val type: Type,
    val name: String,
    val url: URI,
    val username: String?,
    val password: String?,
) {
    enum class Type {
        MAVEN,
        IVY;

        internal object Deserializer : StdDeserializer<Type>(Type::class.java) {
            private const val serialVersionUID: Long = 4371258286182194375L

            override fun deserialize(parser: JsonParser, context: DeserializationContext): Type {
                return valueOf(parser.text.uppercase())
            }
        }
    }
}