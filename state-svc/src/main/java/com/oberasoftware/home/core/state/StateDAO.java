package com.oberasoftware.home.core.state;


import com.oberasoftware.robo.api.model.State;
import com.oberasoftware.robo.api.model.Value;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface StateDAO {
    State setState(String controllerId, String itemId, String label, Value value);

    State getState(String controllerId, String itemId);

    List<State> getStates(String controllerId);
}
