package ai.matter.agentruntime.manager;


import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ai.matter.agentruntime.utils.LogUtils;
import ai.matter.bridgesdk.BridgeManager;
import ai.matter.bridgesdk.OnAsrResultListener;
import ai.matter.bridgesdk.OnVerifyTalkBackListener;

/**
 * 语音旁白验证管理
 */
public class VoiceAsideVerificationManager {
    private static final String TAG = "Shortcut VoiceAsideVerificationManager ";
    private volatile boolean isRecording = false;

    private VoiceAsideVerificationManager() {
//        ExtCrdRpaExecutor.getInstance().setIctA2dpStatusListener(new IctA2dpPlayStatusListener() {
//
//            @Override
//            public void onIctA2dpPlayStop() {
//                Log.d(TAG, "onIctA2dpPlayStop:isRecording " + isRecording);
//                try {
//                   Thread.sleep(SLEEP_TIME_1000);//有可能录音提前结束，延迟1s结束
//                    if (isRecording) {
//                        BridgeManager.getInstance().stopRecord();
//                        isRecording = false;
//                    }
//                } catch (InterruptedException e) {
//                    Log.e(TAG, "onIctA2dpPlayStop: ", e);
//                }
//            }
//
//            @Override
//            public void onIctA2dpPlayStart() {
//                Log.d(TAG, "onIctA2dpPlayStart");
//            }
//        });
    }

    private static class SingletonHolder {
        private static final VoiceAsideVerificationManager INSTANCE = new VoiceAsideVerificationManager();
    }

    public static VoiceAsideVerificationManager getInstance() {
        return VoiceAsideVerificationManager.SingletonHolder.INSTANCE;
    }


    /**
     * 更新热词
     */
    public void updateHotWord(String hotWord) {
        Log.d(TAG, "updateHotWord : " + hotWord);
        BridgeManager.getInstance().updateHotWord(hotWord);
    }

    /**
     * 旁白验证
     *
     * @param message 验证信息
     */
    public boolean voiceAsideVerification(String traceId, IStartPlayAsideVoice iStartPlayAsideVoice, String message) {
        final boolean[] verifyResult = {false};
        try {
            updateHotWord(message);
            CountDownLatch latch = new CountDownLatch(1);
//            ExtCrdRpaExecutor.getInstance().connectA2dp();

//            Thread.sleep(SLEEP_TIME_1000);
            isRecording = true;
            BridgeManager.getInstance().verifyTalkBack(traceId, message, new OnVerifyTalkBackListener() {
                @Override
                public void onVerifyStart() {
                    if (iStartPlayAsideVoice != null) {
                        try {
                            iStartPlayAsideVoice.startPlayVoice();
                        } catch (InterruptedException e) {
                            Log.e(TAG, "voiceAsideVerification startPlayVoice: ", e);
                        }
                    }
                }

                @Override
                public void onVerifySucceed(String result) {
                    Log.d(TAG, " v: " + result);
                    verifyResult[0] = true;
                    latch.countDown();
                }

                @Override
                public void onVerifyFailed(String result) {
                    Log.e(TAG, "onVerifyFailed: " + result);
                    verifyResult[0] = false;
                    latch.countDown();
                }
            });
            latch.await(15, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "voiceAsideVerification: ", e);
            if (isRecording) {
                BridgeManager.getInstance().stopRecord();
                isRecording = false;
            }
        }
//        ExtCrdRpaExecutor.getInstance().disconnectA2dp();
        return verifyResult[0];
    }

    /**
     * @param timeout 录音超时时间，单位: 毫秒
     */
    public boolean voiceAsideVerificationWithTimeout(String traceId,
                                                     long timeout,
                                                     IStartPlayAsideVoice iStartPlayAsideVoice,
                                                     String message) {
        final boolean[] verifyResult = {false};
        try {
            updateHotWord(message);
            CountDownLatch latch = new CountDownLatch(1);
//            ExtCrdRpaExecutor.getInstance().connectA2dp();

//            Thread.sleep(SLEEP_TIME_1000);
            isRecording = true;
            BridgeManager.getInstance().verifyTalkBackWithTimeout(traceId, message, timeout, new OnVerifyTalkBackListener() {
                @Override
                public void onVerifyStart() {
                    if (iStartPlayAsideVoice != null) {
                        try {
                            iStartPlayAsideVoice.startPlayVoice();
                        } catch (InterruptedException e) {
                            Log.e(TAG, "voiceAsideVerification startPlayVoice: ", e);
                        }
                    }
                }

                @Override
                public void onVerifySucceed(String result) {
                    LogUtils.d(TAG, "voiceAsideVerificationWithTimeout onVerifySucceed: " + result);
                    verifyResult[0] = true;
                    latch.countDown();
                }

                @Override
                public void onVerifyFailed(String result) {
                    LogUtils.e(TAG, "voiceAsideVerificationWithTimeout onVerifyFailed: " + result);
                    verifyResult[0] = false;
                    latch.countDown();
                }
            });
            latch.await();
        } catch (Exception e) {
            Log.e(TAG, "voiceAsideVerification: ", e);
            if (isRecording) {
                BridgeManager.getInstance().stopRecord();
                isRecording = false;
            }
        }
//        ExtCrdRpaExecutor.getInstance().disconnectA2dp();
        return verifyResult[0];
    }

    /**
     * 获取旁白文本
     */
    public String getVoiceText(String traceId, IStartPlayAsideVoice iStartPlayAsideVoice) {
        StringBuilder voiceText = new StringBuilder();
        try {
            CountDownLatch latch = new CountDownLatch(1);
//            ExtCrdRpaExecutor.getInstance().connectA2dp();

//            Thread.sleep(SLEEP_TIME_1000);
            isRecording = true;
            BridgeManager.getInstance().getTalkBackText(traceId, new OnAsrResultListener() {
                @Override
                public void onRecognizingResult(@Nullable String s) {

                }

                @Override
                public void onRecognizedResult(@Nullable String s) {

                }

                @Override
                public void onFinalResult(@Nullable String result) {
                    LogUtils.d(TAG, "onSucceed: " + result);
                    if (!TextUtils.isEmpty(result)) {
                        voiceText.append(result.toLowerCase());
                    } else {
                        voiceText.append(result);
                    }
                    latch.countDown();
                }

                @Override
                public void onStart() {
                    Log.d(TAG, "onStart");
                }

                @Override
                public void onFailed(@Nullable String result) {
                    LogUtils.e(TAG, "onFailed: " + result);
                    voiceText.append("error: ").append(result);
                    latch.countDown();
                }
            });
            if (iStartPlayAsideVoice != null) {
                iStartPlayAsideVoice.startPlayVoice();
            }
            latch.await();
        } catch (Exception e) {
            Log.e(TAG, "getVoiceText: ", e);
            if (isRecording) {
                BridgeManager.getInstance().stopRecord();
                isRecording = false;
            }
        }
//        ExtCrdRpaExecutor.getInstance().disconnectA2dp();
        return voiceText.toString();
    }

    /**
     * 获取旁白文本, 带超时
     */
    public String getVoiceTextWithTimeout(String traceId, long timeout, IStartPlayAsideVoice iStartPlayAsideVoice) {
        Log.d(TAG, "getVoiceTextWithTimeout: " + timeout);
        StringBuilder voiceText = new StringBuilder();
        try {
            CountDownLatch latch = new CountDownLatch(1);
//            ExtCrdRpaExecutor.getInstance().connectA2dp();

//            Thread.sleep(SLEEP_TIME_1000);
            isRecording = true;
            BridgeManager.getInstance().getTalkBackTextWithTimeout(traceId, timeout, new OnAsrResultListener() {
                @Override
                public void onRecognizingResult(@Nullable String s) {

                }

                @Override
                public void onRecognizedResult(@Nullable String s) {

                }

                @Override
                public void onFinalResult(@Nullable String result) {
                    LogUtils.d(TAG, "getVoiceTextWithTimeout onSucceed: " + result);
                    if (!TextUtils.isEmpty(result)) {
                        voiceText.append(result.toLowerCase());
                    } else {
                        voiceText.append(result);
                    }
                    latch.countDown();
                }

                @Override
                public void onStart() {
                    Log.d(TAG, "onStart");
                }

                @Override
                public void onFailed(@Nullable String result) {
                    LogUtils.e(TAG, "getVoiceTextWithTimeout onFailed: " + result);
                    voiceText.append("error: ").append(result);
                    latch.countDown();
                }
            });
            if (iStartPlayAsideVoice != null) {
                iStartPlayAsideVoice.startPlayVoice();
            }
            latch.await();
        } catch (Exception e) {
            Log.e(TAG, "getVoiceText: ", e);
            if (isRecording) {
                BridgeManager.getInstance().stopRecord();
                isRecording = false;
            }
        }
//        ExtCrdRpaExecutor.getInstance().disconnectA2dp();
        return voiceText.toString();
    }

    /**
     * 开始播放旁白语音的接口
     */
    public interface IStartPlayAsideVoice {
        void startPlayVoice() throws InterruptedException;
    }
}
