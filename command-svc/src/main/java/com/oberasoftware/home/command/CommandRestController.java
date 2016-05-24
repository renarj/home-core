package com.oberasoftware.home.command;

import com.oberasoftware.home.api.AutomationBus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@RestController
@RequestMapping("/command")
public class CommandRestController {
    private static final Logger LOG = getLogger(CommandRestController.class);

    @Autowired
    private AutomationBus automationBus;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody BasicRestCommand sendCommand(@RequestBody BasicRestCommand command) {
        LOG.debug("Received a command: {} dispatching to kafka", command);

        automationBus.publish(command);

        return command;
    }
}
