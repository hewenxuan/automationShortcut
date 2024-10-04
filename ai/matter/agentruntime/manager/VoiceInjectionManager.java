package ai.matter.agentruntime.manager;

import static ai.matter.agentruntime.constant.AsideKeywordsConstant.ALERT;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_10000;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.VOICE;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_SUCCESS;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_1000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_1500;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_2000;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ai.matter.agentruntime.bean.ResponseResult;
import ai.matter.agentruntime.executor.ExtCrdRpaExecutor;
import ai.matter.agentruntime.utils.LogUtils;
import ai.matter.agentruntime.utils.Utils;
import ai.matter.extbt.player.AudioPlayer;
import ai.matter.extbt.profile.Constants;

/**
 * 语音注入管理
 */
public class VoiceInjectionManager {
    private static final String TAG = "Shortcut VoiceInjectionManager ";

    private static AudioFocusRequest audioFocusRequest;

    private static final String AUDIO_OPEN_TALK_BACK_PATH = "audio/open_talk_back.wav";//打开旁白
    private static final String AUDIO_CLOSE_TALK_BACK_PATH = "audio/close_talk_back.wav";//关闭旁白
    private static final String AUDIO_OPEN_ACCESSIBILITY_TOUCH_PATH = "audio/open_accessibility_touch.wav";//打开辅助触控功能
    private static final String AUDIO_OPEN_ACCESSIBILITY_PAGE_PATH = "audio/open_accessibility_page.wav";//打开辅助功能页面
    private static final String AUDIO_OPEN_SETTING_PATH = "audio/open_setting.wav";//打开设置页面
    private static final String AUDIO_OPEN_ACCESSIBILITY_TOUCH_SETTING_PAGE_PATH = "audio/open_accessibility_touch_setting_page.wav";//打开辅助触控设置页面
    private static final String AUDIO_OPEN_SHORTCUT_PAGE_PATH = "audio/open_shortcut_page.wav";//打开快捷指令页面
    private static final String AUDIO_OPEN_BROWSER_PAGE_PATH = "audio/open_browser_page.wav";//打开浏览器页面
    private static final String AUDIO_OPEN_ONE_ACTION_PAGE_PATH = "audio/open_one_action_page.wav";//打开oneAction页面 todo
    private static final String AUDIO_OPEN_HOT_SPOT_PATH = "audio/open_hotspot.wav";//打开热点 pixel
    private static final String AUDIO_CLOSE_HOT_SPOT_PATH = "audio/close_hotspot.wav";//关闭热点

    private static final String AUDIO_TURN_ON_HOT_SPOT_PATH = "audio/turn_on_hotspot.wav";//打开热点
    private static final String AUDIO_TURN_OFF_HOT_SPOT_PATH = "audio/turn_off_hotspot.wav";//关闭热点


    private static boolean isFirstOpenNarration = false;//首次打开旁白

    /**
     * 打开旁白（语音）
     */
    public static ResponseResult openNarration(String traceId) throws InterruptedException {
        ExtCrdRpaExecutor.getInstance().connectA2dp();
        //验证siri旁白是否打开，如果第一次，要验证siri是否打开，
        if (isFirstOpenNarration) {
            LogUtils.d(TAG, "openNarration isFirstOpenNarration is true，start verify Siri open");
            String voiceTextWithTimeout = VoiceAsideVerificationManager.getInstance().getVoiceTextWithTimeout(traceId, TIME_10000, () -> startVoiceRecognizeHfp(AUDIO_OPEN_TALK_BACK_PATH));
            LogUtils.d(TAG, "openNarration voiceTextWithTimeout: " + voiceTextWithTimeout);
            if (TextUtils.isEmpty(voiceTextWithTimeout)) {
                LogUtils.e(TAG, "openNarration voiceTextWithTimeout is empty，openNarration fail");
                return new ResponseResult(RESPONSE_CODE_FAIL, "语音注入打开旁白失败，siri打开失败");
            } else if (voiceTextWithTimeout.contains(ALERT) && voiceTextWithTimeout.contains(VOICE)) {
                LogUtils.e(TAG, "openNarration fail");
                return new ResponseResult(RESPONSE_CODE_FAIL, "语音注入打开旁白失败");
            } else {//其他语音代表成功
                isFirstOpenNarration = false;
            }
        } else {
            LogUtils.d(TAG, "openNarration isFirstOpenNarration is false");
            startVoiceRecognizeHfp(AUDIO_OPEN_TALK_BACK_PATH);
        }
        LogUtils.d(TAG, "openNarration success");
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开旁白完成");
    }

    /**
     * 关闭旁白（语音）
     */
    public static ResponseResult closeNarration(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_CLOSE_TALK_BACK_PATH);
        ExtCrdRpaExecutor.getInstance().disconnectA2dp();
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入关闭旁白完成");
    }

    /**
     * 打开辅助触控功能
     */
    public static ResponseResult openAccessibilityTouch(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_OPEN_ACCESSIBILITY_TOUCH_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开辅助触控功能完成");
    }


    /**
     * 打开辅助功能页面 Accessibility
     */
    public static ResponseResult openAccessibility(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_OPEN_ACCESSIBILITY_PAGE_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开辅助功能页面完成");
    }

    /**
     * 打开设置页面 Settings
     */
    public static ResponseResult openSettings(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_OPEN_SETTING_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开设置页面完成");
    }

    /**
     * 打开辅助触控设置页面
     */
    public static ResponseResult openAssistiveTouchSettings(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_OPEN_ACCESSIBILITY_TOUCH_SETTING_PAGE_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开辅助触控设置页面完成");
    }

    /**
     * 打开快捷指令页面
     */
    public static ResponseResult openShortcut(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_OPEN_SHORTCUT_PAGE_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开快捷指令页面完成");
    }

    /**
     * 打开iPhone浏览器
     */
    public static ResponseResult openBrowser(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_OPEN_BROWSER_PAGE_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开浏览器完成");
    }

    /**
     * 打开oneAction应用
     */
    public static ResponseResult openOneActionApp(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_OPEN_ONE_ACTION_PAGE_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开oneAction应用完成");
    }

    /**
     * 打开热点
     */
    public static ResponseResult openHotSpot(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_TURN_ON_HOT_SPOT_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开热点完成");
    }

    /**
     * 关闭热点
     */
    public static ResponseResult closeHotSpot(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_TURN_OFF_HOT_SPOT_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入关闭热点完成");
    }

    /**
     * 打开热点 pixel
     */
    public static ResponseResult openHotSpotPixel(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_OPEN_HOT_SPOT_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入打开热点完成");
    }

    /**
     * 关闭热点 pixel
     */
    public static ResponseResult closeHotSpotPixel(String traceId) throws InterruptedException {
        startVoiceRecognizeHfp(AUDIO_CLOSE_HOT_SPOT_PATH);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "语音注入关闭热点完成");
    }

    /**
     * 开始语音识别
     */
    private static void startVoiceRecognizeHfp(String path) throws InterruptedException {
        ExtCrdRpaExecutor.getInstance().connectHfp();
        Thread.sleep(SLEEP_TIME_1000);
        ExtCrdRpaExecutor.getInstance().startVoiceRecognizeHfp();
        Thread.sleep(SLEEP_TIME_2000);
        inject(path);
    }

    /**
     * 申请音频焦点
     */
    private static void requestFocus() {
        Log.d(TAG, "requestFocus.");
        AudioManager audioManager = (AudioManager) Utils.getApp().getSystemService(Context.AUDIO_SERVICE);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true) // 设置是否允许延迟获取焦点
                .setWillPauseWhenDucked(true) // 设置是否在失去焦点时暂停
                .setOnAudioFocusChangeListener(focusChange -> {
                    Log.d(TAG, "onAudioFocusChange: $focusChange");
                })
                .build();
        int result = audioManager.requestAudioFocus(audioFocusRequest);
        Log.d(TAG, "requestFocus result: " + result);
    }

    /**
     * 放弃音频焦点
     */
    private static void abandonFocus() {
        Log.d(TAG, "abandonFocus.");
        AudioManager audioManager = (AudioManager) Utils.getApp().getSystemService(Context.AUDIO_SERVICE);
        if (audioFocusRequest != null) {
            int result = audioManager.abandonAudioFocusRequest(audioFocusRequest);
            Log.d(TAG, "abandonAudioFocusRequest result: " + result);
        }
    }

    private static void inject(String path) {
        Log.d(TAG, "inject: " + path);
        requestFocus();
//        AudioManager audioManager = (AudioManager) Utils.getApp().getSystemService(Context.AUDIO_SERVICE);
//        AudioHelper.Companion.startBluetoothSco(audioManager);
        // 创建一个CountDownLatch实例
        CountDownLatch latch = new CountDownLatch(1);
        AudioPlayer.getInstance().play(Utils.getApp(), path, new AudioPlayer.OnPlaybackListener() {
            @Override
            public void onMarkerReached(AudioTrack track) {
                Log.d(TAG, "onMarkerReached");
                abandonFocus();
//                AudioHelper.Companion.stopBluetoothSco(audioManager);
                try {
                    Thread.sleep(SLEEP_TIME_2000);
                    ExtCrdRpaExecutor.getInstance().stopVoiceRecognizeHfp();//关闭语音助手
                    Thread.sleep(SLEEP_TIME_1000);
                    ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//防止焦点乱跑
                } catch (InterruptedException e) {
                }
                latch.countDown();
            }

            @Override
            public void onPeriodicNotification(AudioTrack track, int progress) {

            }
        });
        try {
            latch.await(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(TAG, "inject exception:", e);
        }
    }


}
