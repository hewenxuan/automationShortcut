package ai.matter.agentruntime.enums;

import ai.matter.bthid.KeyboardHelper;

public enum KeyEnum {
    COMMAND(KeyboardHelper.Modifier.LEFT_GUI),
    WIN(KeyboardHelper.Modifier.LEFT_GUI),
    SHIFT(KeyboardHelper.Modifier.LEFT_SHIFT),
    CTRL(KeyboardHelper.Modifier.LEFT_CTRL),
    ALT(KeyboardHelper.Modifier.LEFT_ALT),
    ENTER(KeyboardHelper.Key.ENTER),
    ESCAPE(KeyboardHelper.Key.ESCAPE),
    BACKSPACE(KeyboardHelper.Key.BACKSPACE),
    TAB(KeyboardHelper.Key.TAB),
    SPACE(KeyboardHelper.Key.SPACE),
    CAPS(KeyboardHelper.Key.CAPS),
    RIGHT(KeyboardHelper.Key.RIGHT),
    LEFT(KeyboardHelper.Key.LEFT),
    DOWN(KeyboardHelper.Key.DOWN),
    UP(KeyboardHelper.Key.UP);

    private final int keyCode;

    KeyEnum(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
