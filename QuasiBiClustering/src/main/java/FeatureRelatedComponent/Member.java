package FeatureRelatedComponent;

/**
 * Created by user on 5/31/15.
 */
public abstract class Member {
    String name;

    public Member(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
