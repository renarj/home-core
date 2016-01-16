package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.util.UtilConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@Configuration
@Import(UtilConfiguration.class)
@ComponentScan
public class MQTTConfiguration implements SpringExtension {
}
