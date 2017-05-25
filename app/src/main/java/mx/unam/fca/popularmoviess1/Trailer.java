package mx.unam.fca.popularmoviess1;

/**
 * Created by Hugoro
 */

public class Trailer {
    String id;
    String key;
    String name;

    public Trailer(String id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
