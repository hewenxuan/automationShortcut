package ai.matter.agentruntime.bean.data;

import ai.matter.agentruntime.bean.GroupKeyBean;

/**
 * 快捷指令
 */
public class Shortcut {
    private String iCloudLink = "";//快捷指令iCloud链接
    private String shortcutName = "";//快捷指令名称

    private long installLoadWaitTime = 6000;//安装加载快捷指令等待时间（快捷指令里面）
    private long webpageResponseWaitTime = 6000;//网页响应等待时间（浏览器里）
    private String shortcutKey = "";//快捷组合键

    private boolean isInstalled;//是否已经安装

    private boolean isSetKeyed;//是否已经设置过快捷键
    private boolean isNeedSetKeys;//是否需要设置快捷键

    private boolean isSetShortcutId;//是否设置过快捷指令id

    private boolean isNeedSetShortcutId;//是否设置过快捷指令id
//    private GroupKeyBean groupKeys;//快捷组合键

    private String shortcutId ;//快捷指令id（服务端用）
    private String version;//版本号（服务端用）

    public String getShortcutId() {
        return shortcutId;
    }

    public void setShortcutId(String shortcutId) {
        this.shortcutId = shortcutId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isNeedSetKeys() {
        return isNeedSetKeys;
    }

    public void setNeedSetKeys(boolean needSetKeys) {
        isNeedSetKeys = needSetKeys;
    }


    public boolean isSetShortcutId() {
        return isSetShortcutId;
    }

    public void setSetShortcutId(boolean setShortcutId) {
        isSetShortcutId = setShortcutId;
    }

    public boolean isNeedSetShortcutId() {
        return isNeedSetShortcutId;
    }

    public void setNeedSetShortcutId(boolean needSetShortcutId) {
        isNeedSetShortcutId = needSetShortcutId;
    }

    public long getWebpageResponseWaitTime() {
        return webpageResponseWaitTime;
    }

    public void setWebpageResponseWaitTime(long webpageResponseWaitTime) {
        this.webpageResponseWaitTime = webpageResponseWaitTime;
    }

    public long getInstallLoadWaitTime() {
        return installLoadWaitTime;
    }

    public void setInstallLoadWaitTime(long installLoadWaitTime) {
        this.installLoadWaitTime = installLoadWaitTime;
    }

    public boolean isSetKeyed() {
        return isSetKeyed;
    }

    public void setKeyed(boolean setKeyed) {
        isSetKeyed = setKeyed;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

    public String getICloudLink() {
        return iCloudLink;
    }

    public void setICloudLink(String value) {
        this.iCloudLink = value;
    }

    public String getShortcutName() {
        return shortcutName.toLowerCase();
    }

    public void setShortcutName(String value) {
        this.shortcutName = value;
    }

    public String getShortcutKey() {
        return shortcutKey;
    }

    public void setShortcutKey(String value) {
        this.shortcutKey = value;
    }

    @Override
    public String toString() {
        return "Shortcut{" +
                "iCloudLink='" + iCloudLink + '\'' +
                ", shortcutName='" + shortcutName + '\'' +
                ", isInstalled=" + isInstalled +
                ", isSetKeyed=" + isSetKeyed +
                ", isSetShortcutId=" + isSetShortcutId +
                ", isNeedSetShortcutId=" + isNeedSetShortcutId +
                '}';
    }
}
