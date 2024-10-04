package ai.matter.agentruntime.manager;

import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ai.matter.agentruntime.executor.CrdRpaExecutor;
import ai.matter.agentruntime.utils.Utils;
import ai.matter.bthid.KeyboardHelper;

/**
 * 键鼠操作管理类
 */
public class KeyboardMouseManager {
    private static final String TAG = "Shortcut KeyboardMouseManager ";

    private static class SingletonHolder {
        private static final KeyboardMouseManager INSTANCE = new KeyboardMouseManager();
    }

    public static KeyboardMouseManager getInstance() {
        return KeyboardMouseManager.SingletonHolder.INSTANCE;
    }

    // 连接状态标志
    private boolean isConnect = false;
    // 连接中状态标志
    private boolean isConnecting = false;
    // 超时时间，单位为秒
    private static final long TIMEOUT = 5;

    // 连接方法，同步返回连接结果，连接中时不允许再次连接
    public synchronized boolean connect(String targetMac) {
        // 如果正在连接中，直接返回false
        if (isConnecting) {
            Log.d(TAG, "Bluetooth connection in progress...");
            return false;
        }
        if (isConnect()) {
            Log.d(TAG, "Bluetooth is already connected, no need to connect again");
            return true;
        }

        // 设置为正在连接中
        isConnecting = true;
        // 创建CountDownLatch实例，用于同步等待连接结果
        CountDownLatch latch = new CountDownLatch(1);

        // 调用连接方法
        getCrdRpaExecutor().connect(Utils.getApp(), targetMac, new CrdRpaExecutor.ConnectionStateChangeListener() {
            @Override
            public void onConnecting() {
                Log.d(TAG, "Bluetooth connecting...");
            }

            @Override
            public void onConnected(String name, String address) {
                Log.d(TAG, "Bluetooth connection successful");
                // 连接成功，设置连接状态为true，连接中状态为false
                isConnect = true;
                isConnecting = false;
                // 计数减1
                latch.countDown();
            }

            @Override
            public void onDisConnected() {
                Log.e(TAG, "Bluetooth has been disconnected");
                // 连接断开，设置连接状态为false，连接中状态为false
                isConnect = false;
                isConnecting = false;
                // 计数减1
                latch.countDown();
            }
        });

        try {
            // 等待连接结果，如果超时则设置连接状态为false，连接中状态为false
            boolean success = latch.await(TIMEOUT, TimeUnit.SECONDS);
            if (!success) {
                isConnect = false;
                isConnecting = false;
            }
//            latch.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "蓝牙连接异常；" + e.getMessage());
            e.printStackTrace();
        }

        // 返回连接状态
        return isConnect;
    }

    // 断开连接方法
    public void disconnect() {
        getCrdRpaExecutor().disconnect();
        // 设置连接状态为false
        isConnect = false;
    }

    public CrdRpaExecutor getCrdRpaExecutor() {
        return CrdRpaExecutor.getInstance();
    }

    /**
     * 是否已连接
     */
    public boolean isConnect() {
        return getCrdRpaExecutor().isConnect();
    }

    /**
     * 重置鼠标位置
     */
    public void resetMousePosition() {
        Log.d(TAG, "reset");
        getCrdRpaExecutor().fastMoveToOriginPoint();
    }

    /**
     * 单击
     */
    public void leftClick() {
        Log.d(TAG, "leftClick");
        getCrdRpaExecutor().leftClick();
    }

    /**
     * 类似右键单击
     */
    public void rightClick() {
        Log.d(TAG, "rightClick");
        enterKeyCombinationTriple(KeyboardHelper.Modifier.NONE, KeyboardHelper.Key.TAB, 'M');
    }

    /**
     * 长按
     */
    public void leftClickLongPress() {
        Log.d(TAG, "leftClickLongPress");
        getCrdRpaExecutor().leftClickLongPress();
    }

    /**
     * 左键双击
     */
    public void leftDoubleClick() {
        Log.d(TAG, "leftClick");
        getCrdRpaExecutor().leftDoubleClick();
    }

    /**
     * 移动到指定坐标位置
     *
     * @param x 屏幕x轴坐标
     * @param y 屏幕y轴坐标
     */
    public void tap(int x, int y) {
        Log.d(TAG, "tap x: " + x + ", y: " + y);
        getCrdRpaExecutor().tap(x, y);
    }

    /**
     * 锁屏
     */
    public void lockIos() {
        Log.d(TAG, "lock");
        getCrdRpaExecutor().lockIos();
    }

    /**
     * 锁屏
     */
    public void lockAndroid() {
        Log.d(TAG, "lockAndroid");
        getCrdRpaExecutor().lockAndroid();
    }

    /**
     * 解锁
     *
     * @param pwd 密码
     */
    public void unlockIOS(String pwd) {
        Log.d(TAG, "unlock: " + pwd);
        getCrdRpaExecutor().unlockIOS(pwd);
    }

    /**
     * 解锁
     *
     * @param pwd 密码
     */
    public void unlockAndroid(String pwd) {
        Log.d(TAG, "unlock: " + pwd);
        getCrdRpaExecutor().unlockAndroid(pwd);
    }


    /**
     * 单键
     */
    public void enterKeyCombination(int key) {
        Log.d(TAG, "enterKeyCombination: " + key);
        enterKeyCombination(KeyboardHelper.Modifier.NONE, key);
    }

    /**
     * 单键
     */
    public void enterKeyCombination(char key) {
        Log.d(TAG, "enterKeyCombination: " + key);
        getCrdRpaExecutor().enterKeyCombination(KeyboardHelper.Modifier.NONE, key);
    }


    /**
     * 单键 普通的组合键如快捷指令的组合键
     *
     * @param modifier
     * @param key
     */
    public void enterKeyCombination(int modifier, char key) {
        Log.d(TAG, "enterKeyCombination: " + modifier + ", " + key);
        getCrdRpaExecutor().enterKeyCombination(modifier, key);
    }


    /**
     * 回车
     *
     * @param modifier
     * @param key
     */
    public void enterKeyCombination(int modifier, int key) {
        Log.d(TAG, "enterKeyCombination: " + modifier + ", " + key);
        getCrdRpaExecutor().enterKeyCombination(modifier, key);
    }

    /**
     * 按下某个键再按下另一个键
     *
     * @param key  第一个键
     * @param key2 第二个键
     */
    public void pressModifierThenKey(int key, char key2) {
        Log.d(TAG, "pressModifierThenKey: " + ", " + key + ", " + key2);
        getCrdRpaExecutor().pressModifierThenKey(key, key2);
    }

    /**
     * 输入文字
     */
    public void inputString(String input) {
        Log.d(TAG, "inputString: " + input);
        getCrdRpaExecutor().inputString(input);
    }

    /**
     * 快捷指令的组合键，适用于控制旁白模式
     *
     * @param modifier
     * @param key
     */
    public void enterKeyCombinationSpecial(int modifier, char key) {
        Log.d(TAG, "enterKeyCombinationSpecial: " + modifier + ", " + key);
        getCrdRpaExecutor().enterKeyCombinationSpecial(modifier, key);
    }

    /**
     * 组合键 播音这种
     *
     * @param modifier
     * @param key
     * @param key2
     */
    public void enterKeyCombinationTriple(int modifier, int key, char key2) {
        Log.d(TAG, "enterKeyCombinationTriple: " + modifier + ", " + key + ", " + key2);
        getCrdRpaExecutor().enterKeyCombinationTriple(modifier, key, key2);
    }

    /**
     * 滚动 负数向上滑动，正数向下滑动
     */
    public void wheelMove(int size) {
        Log.d(TAG, "wheelMove: " + size);
        getCrdRpaExecutor().wheelMove(size);
    }

    /**
     * 移动到指定位置并点击
     *
     * @param x
     * @param y
     */
    public void moveAndClick(int x, int y, long sleepTime) {
        tap(x, y);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        leftClick();
    }

    /**
     * 移动到指定位置并双进击
     *
     * @param x
     * @param y
     */
    public void moveAndDoubleClick(int x, int y, long sleepTime) {
        tap(x, y);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        leftDoubleClick();
    }

    /**
     * 移动到指定位置并长按
     *
     * @param x
     * @param y
     */
    public void moveAndClickLongPress(int x, int y, long sleepTime) {
        tap(x, y);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        leftClickLongPress();
    }

    /**
     * 按下电源键
     */
    public void pressPowerKey() {
        Log.d(TAG, "pressPowerKey");
        enterKeyCombination(KeyboardHelper.Modifier.LEFT_GUI, 'H');
    }

    /**
     * 切换输入法
     */
    public void switchInputMethod() {
        Log.d(TAG, "switchInputMethod");
        enterKeyCombination(KeyboardHelper.Modifier.RIGHT_CTRL, KeyboardHelper.Key.SPACE);
    }

    /**
     * 切换输入法
     */
    public void openSearchPage() {
        Log.d(TAG, "openSearch");
        enterKeyCombination(KeyboardHelper.Modifier.RIGHT_GUI, KeyboardHelper.Key.SPACE);
    }


    /**
     * 按下返回键
     */
    public void pressBackKey() {
        Log.d(TAG, "pressBackKey");
        pressModifierThenKey(KeyboardHelper.Key.TAB, 'B');
    }

    /**
     * 粘贴
     */
    public void stickup() {
        Log.d(TAG, "stickup");
        enterKeyCombination(KeyboardHelper.Modifier.LEFT_GUI, 'V');
    }

    /**
     * 按下上键
     */
    public void pressUpKey() {
        Log.d(TAG, "pressUpKey");
        enterKeyCombination(KeyboardHelper.Key.UP);
    }

    /**
     * 按下下键
     */
    public void pressDownKey() {
        Log.d(TAG, "pressDownKey");
        enterKeyCombination(KeyboardHelper.Key.DOWN);
    }

    /**
     * 按下左键
     */
    public void pressLeftKey() {
        Log.d(TAG, "pressLeftKey");
        enterKeyCombination(KeyboardHelper.Key.LEFT);
    }

    /**
     * 按下右键
     */
    public void pressRightKey() {
        Log.d(TAG, "pressRightKey");
        enterKeyCombination(KeyboardHelper.Key.RIGHT);
    }

    /**
     * 空格键激活
     */
    public void pressActivationSpaceKey() {
        Log.d(TAG, "pressActivationSpaceKey");
        enterKeyCombination(KeyboardHelper.Key.SPACE);
    }

    /**
     * 按下回车按钮
     */
    public void pressEnterKey() {
        Log.d(TAG, "pressEnterKey");
        enterKeyCombination(KeyboardHelper.Key.ENTER);
    }



}
