package ru.pearx.libmc.common.caps.animation;

import java.util.Arrays;
import java.util.List;

/*
 * Created by mrAppleXZ on 17.02.18 17:19.
 */
public class AnimationElement
{
    private String name;
    private List<String> states;
    private int state;

    public AnimationElement(String name, String defaultState, String... availableStates)
    {
        this.name = name;
        this.states = Arrays.asList(availableStates);
        this.state = states.indexOf(defaultState);
    }

    public String getName()
    {
        return name;
    }

    public List<String> getStates()
    {
        return states;
    }

    public int getStateIndex()
    {
        return state;
    }

    public void setStateIndex(int state)
    {
        this.state = state;
    }

    public String getState()
    {
        return getStates().get(getStateIndex());
    }
}
