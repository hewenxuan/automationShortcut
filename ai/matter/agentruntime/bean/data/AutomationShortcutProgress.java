package ai.matter.agentruntime.bean.data;

import ai.matter.agentruntime.bean.AutomationShortcutConfigBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动化配置进度管理bean
 */
public class AutomationShortcutProgress {

    private AutomationShortcutConfigBean.ProgressManager progressManager;

    private List<Shortcut> shortcuts = new ArrayList<>();

    public AutomationShortcutConfigBean.ProgressManager getProgressManager() {
        return progressManager;
    }

    public void setProgressManager(AutomationShortcutConfigBean.ProgressManager progressManager) {
        this.progressManager = progressManager;
    }

    public List<Shortcut> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(List<Shortcut> shortcuts) {
        this.shortcuts = shortcuts;
    }
}


