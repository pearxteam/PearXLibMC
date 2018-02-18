package ru.pearx.libmc.common.caps.animation;

import java.util.List;

/*
 * Created by mrAppleXZ on 21.09.17 10:51.
 */
public interface IAnimationStateManager
{
    void changeState(String name, String state);
    void changeState(int element, int state);
    AnimationElement getElement(String name);
    List<AnimationElement> getElements();
}
