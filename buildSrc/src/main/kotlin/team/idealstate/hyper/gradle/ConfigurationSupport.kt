package team.idealstate.hyper.gradle

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.toml.TomlMapper

/**
 * <p>ConfigurationSupport</p>
 *
 * <p>创建于 2024/3/20 23:53</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
enum class ConfigurationSupport(
    val build: () -> ObjectMapper
) {
    JSON({
        ObjectMapper().apply {
            registerModule(
                com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build()
            )
        }
    }),
    TOML({
        TomlMapper.builder()
            .addModule(
                com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build()
            )
            .build()
    });
}