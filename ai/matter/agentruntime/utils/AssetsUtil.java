package ai.matter.agentruntime.utils;

import ai.matter.agentruntime.bean.location.MouseLocations;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 获取assets数据工具类
 */
public class AssetsUtil {

    private static final String LOCATION_PATH = "location/";
    private static final String JSON_EXTENSION = ".json";

    private static final HashMap<String, MouseLocations> mouseLocationsMap = new HashMap<>();

    /**
     * 获取位置bean
     */
    public static MouseLocations getMouseLocations(String phoneMode, String version) {
        if (mouseLocationsMap.containsKey(version)) {
            return mouseLocationsMap.get(version);
        }
        String string = loadJSONFromAsset(LOCATION_PATH + version + "/" + phoneMode + JSON_EXTENSION);
        MouseLocations mouseLocations = GsonUtils.fromJson(string, MouseLocations.class);
        if (mouseLocations != null) {
            mouseLocationsMap.put(version, mouseLocations);
        }
        return mouseLocations;
    }

    /**
     * 加载assets数据
     *
     * @param fileName 文件名
     */
    private static String loadJSONFromAsset(String fileName) {
        String json = "";
        try {
            InputStream is = Utils.getApp().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
        return json;
    }
}