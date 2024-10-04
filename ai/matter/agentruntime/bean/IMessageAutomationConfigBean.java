package ai.matter.agentruntime.bean;

import ai.matter.agentruntime.bean.data.IMessageAutomationConfig;

/**
 * 配置Imessage自动化bean
 */
public class IMessageAutomationConfigBean {
    private String webAppLink;
    private String phoneModel;
    private String os;
    private IMessageAutomationConfig iMessageAutomationConfig;
    private String resolution;
    private boolean isWebApp;
    private String versionCode;

    public IMessageAutomationConfig getIMessageAutomationConfig() {
        return iMessageAutomationConfig;
    }

    public void setIMessageAutomationConfig(IMessageAutomationConfig iMessageAutomationConfig) {
        this.iMessageAutomationConfig = iMessageAutomationConfig;
    }

    public String getWebAppLink() {
        return webAppLink;
    }

    public void setWebAppLink(String value) {
        this.webAppLink = value;
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


    public String getResolution() {
        return resolution;
    }

    public void setResolution(String value) {
        this.resolution = value;
    }

    public boolean isWebApp() {
        return isWebApp;
    }

    public void setIsWebApp(boolean value) {
        this.isWebApp = value;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String value) {
        this.versionCode = value;
    }
}

