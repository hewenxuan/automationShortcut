package ai.matter.agentruntime.core;

import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_EXT_BLUETOOTH_CONNECT_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_SUCCESS;

import android.text.TextUtils;

import ai.matter.agentruntime.bean.AutomationShortcutConfigBean;
import ai.matter.agentruntime.bean.IMessageAutomationConfigBean;
import ai.matter.agentruntime.bean.IMessageAutomationUpdateBean;
import ai.matter.agentruntime.bean.MqttMessageBean;
import ai.matter.agentruntime.bean.ResponseResult;
import ai.matter.agentruntime.bean.data.IMessageAutomationConfig;
import ai.matter.agentruntime.bean.data.IMessageAutomationUpdateContact;
import ai.matter.agentruntime.manager.ExtKeyboardMouseManager;
import ai.matter.agentruntime.manager.IMessageAutomationManager;
import ai.matter.agentruntime.manager.KeyboardMouseManager;
import ai.matter.agentruntime.manager.LogServiceManager;
import ai.matter.agentruntime.manager.MqttConnectManager;
import ai.matter.agentruntime.manager.VoiceInjectionManager;
import ai.matter.agentruntime.utils.GsonUtils;
import ai.matter.agentruntime.utils.LogUtils;
import ai.matter.agentruntime.utils.ShortcutUtil;
import ai.matter.agentruntime.utils.ThreadUtils;
import ai.matter.lightrpc.RpcServiceHelper;

/**
 * IMessage自动化消息管理核心
 */
public class IMessageAutomationRpaCore {
    private static final String TAG = "Shortcut IMessageAutomationRpaCore ";

    /**
     * 安装IMessage自动化
     */
    public static void installIMessageAutomation(MqttMessageBean mqttMessageBean, RpcServiceHelper.RpcCallback rpcCallback) {
        ThreadUtils.getCpuPool().execute(() -> {
            //解析数据
            String traceId = mqttMessageBean.getTraceId();
            try {
                MqttConnectManager.getInstance().sendAsyncMessageToMqttResultPart(rpcCallback, new ResponseResult(RESPONSE_CODE_SUCCESS, "installIMessageAutomation start config"));//给服务端发消息，代表开始执行了
                LogServiceManager.getInstance().log(traceId, "installIMessageAutomation start config" + GsonUtils.toJson(mqttMessageBean));
                IMessageAutomationConfigBean iMessageAutomationUpdateBean = GsonUtils.fromJson(GsonUtils.toJson(mqttMessageBean.getParams()), IMessageAutomationConfigBean.class);
                if (iMessageAutomationUpdateBean == null) {
                    LogUtils.e(TAG, "installIMessageAutomation，iMessageAutomationUpdateBean is null");
                    LogServiceManager.getInstance().log(traceId, "installIMessageAutomation，iMessageAutomationUpdateBean is null");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "获取自动化快捷指令配置iMessageAutomationUpdateBean失败"));
                    return;
                }
                IMessageAutomationConfig iMessageAutomationConfig = iMessageAutomationUpdateBean.getIMessageAutomationConfig();
                if (iMessageAutomationConfig == null) {
                    LogUtils.e(TAG, "installIMessageAutomation，iMessageAutomationConfig is null");
                    LogServiceManager.getInstance().log(traceId, "installIMessageAutomation，iMessageAutomationConfig is null");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "获取自动化快捷指令配置iMessageAutomationUpdateBean失败"));
                    return;
                }

                boolean connect = ExtKeyboardMouseManager.getInstance().connect();
                if (connect) {
                    //开启旁白
                    ResponseResult  openNarrationResult= VoiceInjectionManager.openNarration(traceId);//打开旁白
                    if (openNarrationResult.getCode() != RESPONSE_CODE_SUCCESS) {
                        LogUtils.e(TAG, "installIMessageAutomation，openNarration failed");
                        MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, openNarrationResult);
                        return;
                    }
                    ResponseResult responseResult = IMessageAutomationManager.installIMessageAutomation(iMessageAutomationConfig, traceId);
                    LogUtils.d(TAG, "installIMessageAutomation，responseResult：" + responseResult);
//                    ShortcutUtil.returnToOneActionWebApp(isWebApp, webAppLink, traceId);//回到oneAction/webApp
                    //关闭旁白
                    VoiceInjectionManager.closeNarration(traceId);
                    //关闭蓝牙
                    ExtKeyboardMouseManager.getInstance().disconnect();
                    LogServiceManager.getInstance().log(traceId, "installIMessageAutomation，responseResult：" + responseResult);
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, responseResult);
                } else {
                    LogUtils.e("installIMessageAutomation，bluetooth connect fail");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_EXT_BLUETOOTH_CONNECT_FAIL, "蓝牙连接失败"));
                    LogServiceManager.getInstance().log(traceId, "installIMessageAutomation，bluetooth connect fail");
                }
            } catch (Exception e) {
                LogUtils.e("installIMessageAutomation，exception：" + e.getMessage());
                MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation执行异常：" + e.getMessage()));
                LogServiceManager.getInstance().log(traceId, "installIMessageAutomation，exception：" + e.getMessage());
            }
        });
    }

    /**
     * IMessage自动化更新联系人
     */
    public static void updateIMessageAutomationConstant(MqttMessageBean mqttMessageBean, RpcServiceHelper.RpcCallback rpcCallback) {
        ThreadUtils.getCpuPool().execute(() -> {
            String traceId = mqttMessageBean.getTraceId();
            try {
                //解析数据
                LogServiceManager.getInstance().log(traceId, "updateIMessageAutomationConstant start config" + GsonUtils.toJson(mqttMessageBean));
                IMessageAutomationUpdateBean iMessageAutomationUpdateBean = GsonUtils.fromJson(GsonUtils.toJson(mqttMessageBean.getParams()), IMessageAutomationUpdateBean.class);
                if (iMessageAutomationUpdateBean == null) {
                    LogUtils.e(TAG, "updateIMessageAutomationConstant，iMessageAutomationUpdateBean is null");
                    LogServiceManager.getInstance().log(traceId, "updateIMessageAutomationConstant，iMessageAutomationUpdateBean is null");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "获取更新IMessage自动化联系人bean失败"));
                    return;
                }
                IMessageAutomationUpdateContact iMessageAutomationUpdate = iMessageAutomationUpdateBean.getIMessageAutomationUpdate();
                if (iMessageAutomationUpdate == null) {
                    LogUtils.e(TAG, "updateIMessageAutomationConstant，iMessageAutomationUpdate is null");
                    LogServiceManager.getInstance().log(traceId, "updateIMessageAutomationConstant，iMessageAutomationUpdate is null");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "获取更新IMessage自动化联系人bean失败"));
                    return;
                }

                //连接蓝牙
                boolean connect = ExtKeyboardMouseManager.getInstance().connect();
                if (connect) {
                    //开启旁白
                    ResponseResult  openNarrationResult= VoiceInjectionManager.openNarration(traceId);//打开旁白
                    if (openNarrationResult.getCode() != RESPONSE_CODE_SUCCESS) {
                        LogUtils.e(TAG, "updateIMessageAutomationConstant，openNarration failed");
                        MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, openNarrationResult);
                        return;
                    }
                    ResponseResult responseResult = IMessageAutomationManager.updateIMessageAutomationConstant(iMessageAutomationUpdate, traceId);
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, responseResult);
                    LogUtils.d(TAG, "updateIMessageAutomationConstant，responseResult：" + responseResult);
                    LogServiceManager.getInstance().log(traceId, "updateIMessageAutomationConstant，responseResult：" + responseResult);
                    //关闭旁白
                    VoiceInjectionManager.closeNarration(traceId);
                    //关闭蓝牙
                    ExtKeyboardMouseManager.getInstance().disconnect();
                } else {
                    LogUtils.e("updateIMessageAutomationConstant，bluetooth connect fail");
                    LogServiceManager.getInstance().log(traceId, "updateIMessageAutomationConstant，bluetooth connect fail");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_EXT_BLUETOOTH_CONNECT_FAIL, "蓝牙连接失败"));
                }
            } catch (Exception e) {
                LogUtils.e("updateIMessageAutomationConstant，exception：" + e.getMessage());
                LogServiceManager.getInstance().log(traceId, "updateIMessageAutomationConstant，exception：" + e.getMessage());
                MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "updateIMessageAutomationConstant执行异常：" + e.getMessage()));
            }
        });
    }
}
