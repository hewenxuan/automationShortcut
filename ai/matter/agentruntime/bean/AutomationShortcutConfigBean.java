package ai.matter.agentruntime.bean;

import ai.matter.agentruntime.bean.data.IMessageAutomationConfig;
import ai.matter.agentruntime.bean.data.Shortcut;

import java.util.List;

/**
 * 自动化配置bean
 */
public class AutomationShortcutConfigBean {
    private String shortcutFolderName;
    private String webAppLink;
    private String shortcutNameTag;
    private String password;
    private String phoneModel;
    private String os;
    private List<Shortcut> shortcuts;
    private String resolution;
    private boolean isWebApp;
    private String mac;
    private String versionCode;

    private String uniqueId;

    private int fullKeyboardControlAutoHideTime = 15;//全键盘控制自动隐藏时间

    private ProgressManager progressManager;
    private IMessageAutomationConfig iMessageAutomationConfig;

    public int getFullKeyboardControlAutoHideTime() {
        return fullKeyboardControlAutoHideTime;
    }

    public void setFullKeyboardControlAutoHideTime(int fullKeyboardControlAutoHideTime) {
        this.fullKeyboardControlAutoHideTime = fullKeyboardControlAutoHideTime;
    }

    public ProgressManager getProgressManager() {
        return progressManager;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setProgressManager(ProgressManager progressManager) {
        this.progressManager = progressManager;
    }

    public String getShortcutFolderName() {
        return shortcutFolderName;
    }

    public void setShortcutFolderName(String value) {
        this.shortcutFolderName = value;
    }

    public String getWebAppLink() {
        return webAppLink;
    }

    public void setWebAppLink(String value) {
        this.webAppLink = value;
    }

    public String getShortcutNameTag() {
        return shortcutNameTag;
    }

    public void setShortcutNameTag(String value) {
        this.shortcutNameTag = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String value) {
        this.phoneModel = value;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String value) {
        this.os = value;
    }

    public List<Shortcut> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(List<Shortcut> value) {
        this.shortcuts = value;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String value) {
        this.resolution = value;
    }

    public boolean IsWebApp() {
        return isWebApp;
    }

    public void setIsWebApp(boolean value) {
        this.isWebApp = value;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String value) {
        this.mac = value;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String value) {
        this.versionCode = value;
    }

    public IMessageAutomationConfig getIMessageAutomation() {
        return iMessageAutomationConfig;
    }

    public void setIMessageAutomation(IMessageAutomationConfig value) {
        this.iMessageAutomationConfig = value;
    }

    public static class ProgressManager {
        private boolean isOpenAssistiveTouch;//是否已打开辅助触控功能
        private boolean isOpenFullKeyboardControl;//是否已打开全键盘控制功能
        private boolean isSetAssistiveTouchConfig;//是否已打开辅助控制配置
        private boolean isCreateShortcutFolder;//是否已经创建好快捷指令文件夹
        private boolean isMoveShortcut;//是否已经移动过快捷指令到文件夹

        public boolean isMoveShortcut() {
            return isMoveShortcut;
        }

        public void setMoveShortcut(boolean moveShortcut) {
            isMoveShortcut = moveShortcut;
        }

        public boolean isOpenAssistiveTouch() {
            return isOpenAssistiveTouch;
        }

        public void setOpenAssistiveTouch(boolean openAssistiveTouch) {
            isOpenAssistiveTouch = openAssistiveTouch;
        }

        public boolean isOpenFullKeyboardControl() {
            return isOpenFullKeyboardControl;
        }

        public void setIsOpenFullKeyboardControl(boolean isOpenFullKeyboardControl) {
            this.isOpenFullKeyboardControl = isOpenFullKeyboardControl;
        }

        public boolean isSetAssistiveTouchConfig() {
            return isSetAssistiveTouchConfig;
        }

        public void setSetAssistiveTouchConfig(boolean setAssistiveTouchConfig) {
            isSetAssistiveTouchConfig = setAssistiveTouchConfig;
        }

        public boolean isCreateShortcutFolder() {
            return isCreateShortcutFolder;
        }

        public void setCreateShortcutFolder(boolean createShortcutFolder) {
            isCreateShortcutFolder = createShortcutFolder;
        }
    }
}


