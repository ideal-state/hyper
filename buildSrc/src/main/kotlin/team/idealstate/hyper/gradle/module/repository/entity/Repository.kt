package team.idealstate.hyper.gradle.module.repository.entity

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

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
    val url: String,
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