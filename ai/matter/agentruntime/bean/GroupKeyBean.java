package ai.matter.agentruntime.bean;

/**
 * 组合键bean类
 */
public class GroupKeyBean {
    boolean isShift;
    boolean isCommand;
    boolean isCtrl;
    boolean isAlt;
    char key;

    public boolean isShift() {
        return isShift;
    }

    public void setShift(boolean shift) {
        isShift = shift;
    }

    public boolean isCommand() {
        return isCommand;
    }

    public void setCommand(boolean command) {
        isCommand = command;
    }

    public boolean isCtrl() {
        return isCtrl;
    }

    public void setCtrl(boolean ctrl) {
        isCtrl = ctrl;
    }

    public boolean isAlt() {
        return isAlt;
    }

    public void setAlt(boolean alt) {
        isAlt = alt;
    }

    public char getKey() {
        return key;
    }

    public void setKey(char key) {
        this.key = key;
    }
}
