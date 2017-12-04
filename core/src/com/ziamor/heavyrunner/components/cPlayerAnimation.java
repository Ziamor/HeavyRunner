package com.ziamor.heavyrunner.components;

import com.artemis.Component;

public class cPlayerAnimation extends Component {
    public enum AnimState{
        WALK,
        JUMP
    }

    public AnimState state = AnimState.WALK;
}
