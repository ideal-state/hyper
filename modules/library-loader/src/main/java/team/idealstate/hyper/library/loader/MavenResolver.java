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

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.supplier.RepositorySystemSupplier;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transfer.TransferResource;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>MavenResolver</p>
 *
 * <p>创建于 2024/2/15 20:50</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
abstract class MavenResolver {

    private static final Logger logger = LoggerFactory.getLogger(MavenResolver.class);
    private static final RepositorySystem SYSTEM;
    private static final DefaultRepositorySystemSession SESSION;
    private static volatile List<RemoteRepository> repositories = null;

    static {
        RepositorySystemSupplier supplier = new RepositorySystemSupplier();
        SYSTEM = supplier.get();
        SESSION = MavenRepositorySystemUtils.newSession();
    }

    static void initialize(File localRepositories, ConfigurationSection config) {
        SESSION.setChecksumPolicy("fail");
        SESSION.setLocalRepositoryManager(SYSTEM.newLocalRepositoryManager(SESSION,
                new LocalRepository(localRepositories)));
        SESSION.setTransferListener(new TransferLog());
        SESSION.setReadOnly();
        Set<RemoteRepository> remoteRepositories;
        if (config != null) {
            remoteRepositories = new LinkedHashSet<>(config.getKeys(false).size() + 1);
            for (String key : config.getKeys(false)) {
                ConfigurationSection section = config.getConfigurationSection(key);
                if (section == null) {
                    continue;
                }
                String url = section.getString("url");
                if (url == null) {
                    continue;
                }
                remoteRepositories.add(new RemoteRepository.Builder(key, "default", url).build());
            }
        } else {
            remoteRepositories = new LinkedHashSet<>(1);
        }
        remoteRepositories.add(new RemoteRepository.Builder(
                "central", "default", "https://repo1.maven.org/maven2/").build());
        repositories = SYSTEM.newResolutionRepositories(SESSION, new ArrayList<>(remoteRepositories));
    }

    public static List<Artifact> resolve(Collection<String> dependencyIds) {
        if (!(dependencyIds instanceof Set)) {
            dependencyIds = new LinkedHashSet<>(dependencyIds);
        }
        List<Dependency> dependencies = new ArrayList<>();
        for (String dependencyId : dependencyIds) {
            DefaultArtifact defaultArtifact = new DefaultArtifact(dependencyId);
            Dependency dependency = new Dependency(defaultArtifact, JavaScopes.RUNTIME);
            dependencies.add(dependency);
        }

        DependencyResult dependencyResult;
        try {
            dependencyResult = SYSTEM.resolveDependencies(
                    SESSION,
                    new DependencyRequest(
                            new CollectRequest((Dependency) null, dependencies, repositories),
                            null
                    )
            );
        } catch (DependencyResolutionException e) {
            throw new RuntimeException("无法解析依赖项", e);
        }
        return dependencyResult.getArtifactResults()
                .stream()
                .map(ArtifactResult::getArtifact)
                .collect(Collectors.toList());
    }

    private static class TransferLog extends AbstractTransferListener {
        @Override
        public void transferStarted(TransferEvent event) throws TransferCancelledException {
            TransferResource resource = event.getResource();
            logger.info("开始下载 {}",
                    resource.getRepositoryUrl() + resource.getResourceName());
        }

        @Override
        public void transferSucceeded(TransferEvent event) {
            TransferResource resource = event.getResource();
            logger.info("下载完成 {}",
                    resource.getRepositoryUrl() + resource.getResourceName());
        }

        @Override
        public void transferFailed(TransferEvent event) {
            TransferResource resource = event.getResource();
            logger.error("下载失败 {}",
                    resource.getRepositoryUrl() + resource.getResourceName());
        }
    }
}
