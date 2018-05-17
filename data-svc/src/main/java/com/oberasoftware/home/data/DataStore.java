package com.oberasoftware.home.data;



import com.oberasoftware.robo.api.model.Controller;
import com.oberasoftware.robo.api.model.Item;

import java.util.List;

/**
 * @author renarj
 */
public interface DataStore {
    void register(Controller controller);

    void register(Item item);

    List<Controller> findController(String controllerId);
}
