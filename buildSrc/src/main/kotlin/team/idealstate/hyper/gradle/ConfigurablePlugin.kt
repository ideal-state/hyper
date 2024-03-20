package team.idealstate.hyper.gradle

/**
 * <p>ConfigurablePlugin</p>
 *
 * <p>创建于 2024/3/20 23:33</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
abstract class ConfigurablePlugin<C>(
    private val configSupport: ConfigurationSupport,
    private val configPath: String,
    private val configType: Class<C>
) : Plugin() {

    val config: C by lazy { configSupport.build().readValue(asset(configPath), configType) }
}