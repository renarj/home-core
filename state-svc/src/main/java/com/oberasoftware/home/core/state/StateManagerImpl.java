package com.oberasoftware.home.core.state;

import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.home.api.impl.events.devices.StateUpdateEvent;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Renze de Vries
 */
@Component
public class StateManagerImpl implements StateManager {
    @Autowired
    private StateDAO stateDAO;

    @Autowired
    private LocalEventBus eventBus;

    @Override
    public State setState(String controllerId, String itemId, String label, Value value) {
        State state = stateDAO.setState(controllerId, itemId, label, value);
        eventBus.publish(new StateUpdateEvent(state, label));
        return state;
    }

    @Override
    public State getState(String controllerId, String itemId) {
        return stateDAO.getState(controllerId, itemId);
    }

    @Override
    public List<State> getStates(String controllerId) {
        return stateDAO.getStates(controllerId);
    }
}
