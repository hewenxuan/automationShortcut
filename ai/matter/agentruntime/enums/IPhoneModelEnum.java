package ai.matter.agentruntime.enums;

public enum IPhoneModelEnum {
    IPHONE_MODEL_12("iphone12"),
    IPHONE_MODEL_12_PRO("iphone12 pro"),
    IPHONE_MODEL_12_PRO_MAX("iphone12 pro max"),

    IPHONE_MODEL_13("iphone13"),
    IPHONE_MODEL_13_PRO("iphone13 pro"),
    IPHONE_MODEL_13_PRO_MAX("iphone13 pro max"),

    IPHONE_MODEL_14("iphone14"),
    IPHONE_MODEL_14_PLUS("iphone14 plus"),
    IPHONE_MODEL_14_PRO("iphone14 pro"),
    IPHONE_MODEL_14_PRO_MAX("iphone14 pro max"),

    IPHONE_MODEL_15("iphone15"),
    IPHONE_MODEL_15_PLUS("iphone15 plus"),
    IPHONE_MODEL_15_PRO("iphone15 pro"),
    IPHONE_MODEL_15_PRO_MAX("iphone15 pro max"),

    IPHONE_MODEL_16("iphone16"),
    IPHONE_MODEL_16_PLUS("iphone16 plus"),
    IPHONE_MODEL_16_PRO("iphone16 pro"),
    IPHONE_MODEL_16_PRO_MAX("iphone16 pro max");
    private final String modelName;

    IPhoneModelEnum(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return this.modelName;
    }
}
