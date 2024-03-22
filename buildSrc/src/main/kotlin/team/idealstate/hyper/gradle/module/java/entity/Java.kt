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

package team.idealstate.hyper.gradle.module.java.entity

/**
 * <p>Java</p>
 *
 * <p>创建于 2024/3/19 19:59</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
data class Java(
    val language: Language,
    val file: File
)