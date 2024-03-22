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

import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication

/**
 * <p>HyperPublishingExtension</p>
 *
 * <p>创建于 2024/3/21 11:41</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */

val PublicationContainer.hyperPublication: MavenPublication
    get() = named("hyper", MavenPublication::class.java).get()