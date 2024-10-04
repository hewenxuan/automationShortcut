package ai.matter.agentruntime.bean;

/**
 * 语音识别返回数据
 */
public class VoiceTextBean {
    private String msg;
    private long code;
    private Data data;

    public String getMsg() { return msg; }
    public void setMsg(String value) { this.msg = value; }

    public long getCode() { return code; }
    public void setCode(long value) { this.code = value; }

    public Data getData() { return data; }
    public void setData(Data value) { this.data = value; }

    @Override
    public String toString() {
        return "VoiceTextBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }

    public class Data {
        private String traceid;
        private String text;

        public String getTraceid() { return traceid; }
        public void setTraceid(String value) { this.traceid = value; }

        public String getText() { return text; }
        public void setText(String value) { this.text = value; }

        @Override
        public String toString() {
            return "Data{" +
                    "traceid='" + traceid + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }
}


