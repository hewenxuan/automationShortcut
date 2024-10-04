package ai.matter.agentruntime.core;

import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_EXCEPTION_ERROR;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_FAIL;

import android.text.TextUtils;

import ai.matter.agentruntime.bean.ExecuteShortcutBean;
import ai.matter.agentruntime.bean.MqttMessageBean;
import ai.matter.agentruntime.bean.ResponseResult;
import ai.matter.agentruntime.executor.ExtCrdRpaExecutor;
import ai.matter.agentruntime.manager.LogServiceManager;
import ai.matter.agentruntime.manager.MqttConnectManager;
import ai.matter.agentruntime.manager.ShortcutManager;
import ai.matter.agentruntime.utils.GsonUtils;
import ai.matter.agentruntime.utils.LogUtils;
import ai.matter.agentruntime.utils.ThreadUtils;
import ai.matter.lightrpc.RpcServiceHelper;

/**
 * 快捷指令管理核心
 */
public class ShortcutManagerRpaCore {
    private static final String TAG = "Shortcut ShortcutManagerRpaCore ";

    /**
     * 安装快捷指令
     */
    public static void installShortcut() {
        // TODO: 实现安装快捷指令的逻辑

    }

    /**
     * 设置快捷指令快捷键
     */
    public static void setShortcutKey() {
        // TODO: 实现设置快捷指令快捷键的逻辑

    }

    /**
     * 执行快捷指令
     */
    public static void executeShortcut(MqttMessageBean mqttMessageBean, RpcServiceHelper.RpcCallback rpcCallback) {
        ThreadUtils.getCpuPool().execute(() -> {
            String traceId = mqttMessageBean.getTraceId();
            try {
                ExecuteShortcutBean executeShortcutBean = GsonUtils.fromJson(GsonUtils.toJson(mqttMessageBean.getParams()), ExecuteShortcutBean.class);
                LogServiceManager.getInstance().log(traceId, "executeShortcut start config" + GsonUtils.toJson(mqttMessageBean));
                if (executeShortcutBean == null) {
                    LogUtils.e(TAG, "executeShortcut，executeShortcutBean is null");
                    LogServiceManager.getInstance().log(traceId, "executeShortcut，executeShortcutBean is null");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "get executeShortcutBean is null"));
                    return;
                }
                LogUtils.d(TAG, "executeShortcut，start executeShortcut", executeShortcutBean);
                ResponseResult responseResult = ShortcutManager.executeShortcut(executeShortcutBean, traceId);
                LogUtils.d(TAG, "executeShortcut，responseResult：" + responseResult);
                LogServiceManager.getInstance().log(traceId, "executeShortcut，responseResult：" + responseResult);
                MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, responseResult);
                ExtCrdRpaExecutor.getInstance().disconnectHid();
            } catch (Exception e) {
                LogUtils.e("executeShortcut，exception：" + e.getMessage());
                MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_EXCEPTION_ERROR, "executeShortcut执行异常：" + e.getMessage()));
                LogServiceManager.getInstance().log(traceId, "executeShortcut，exception：" + e.getMessage());
            }
        });
    }

    /**
     * 删除快捷指令
     */
    public static void deleteShortcut(String shortcutId) {
        // TODO: 实现删除快捷指令的逻辑

    }

    /**
     * 更新快捷指令
     */
    public static void updateShortcut(String shortcutId) {
        // TODO: 实现更新快捷指令的逻辑

    }

    /**
     * 创建快捷指令文件夹
     */
    public static void createShortcutFolder(String folderName) {
        // TODO: 实现创建快捷指令文件夹的逻辑

    }

    /**
     * 快捷指令归类（移动快捷指令到文件夹）
     */
    public static void classifyShortcut(String shortcutId, String folderName) {
        // TODO: 实现快捷指令归类的逻辑

    }
}
