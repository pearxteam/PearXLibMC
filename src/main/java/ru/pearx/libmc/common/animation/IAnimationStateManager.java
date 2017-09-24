package ru.pearx.libmc.common.animation;

import java.util.List;

/*
 * Created by mrAppleXZ on 21.09.17 10:51.
 */
public interface IAnimationStateManager
{
    void changeState(String state);
    String getState();
    List<String> getAvailableStates();
}
