
package ai.matter.agentruntime.bean.location;

/**
 * 位置
 */
public class Location {
    private String descriptionEn;
    private int corner = 1;//1左上角 2右上角 3左下角 4右下角
    private String position;

    private int cycle = 1;

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String value) {
        this.descriptionEn = value;
    }

    public int getCorner() {
        return corner;
    }

    public void setCorner(int value) {
        this.corner = value;
    }

    public String getPosition() {
        return position;
    }

    public Short getX() {
        return getPos(0);
    }

    public short getY() {
        return getPos(1);
    }

    private short getPos(int index) {
        if (position != null) {
            String[] split = position.split(",");
            if (split.length == 2) {
                return Short.parseShort(split[index]);
            } else {
                return 0;
            }
        }
        return 0;
    }

    public void setPosition(String value) {
        this.position = value;
    }
}
