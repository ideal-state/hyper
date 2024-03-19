package team.idealstate.hyper.gradle.hyper.lang.entity

import team.idealstate.hyper.gradle.hyper.lang.entity.java_lang.File
import team.idealstate.hyper.gradle.hyper.lang.entity.java_lang.Language

/**
 * <p>JvmLanguage</p>
 *
 * <p>创建于 2024/3/19 19:59</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
data class JavaLang(
    val language: Language,
    val file: File
)