package com.oberasoftware.home.command;

import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.core.model.BasicCommandImpl;
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
    private LocalEventBus eventBus;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody
    BasicCommandImpl sendCommand(@RequestBody BasicCommandImpl command) {
        LOG.info("Received a command: {} dispatching to eventbus", command);

        eventBus.publish(command);

        return command;
    }
}
