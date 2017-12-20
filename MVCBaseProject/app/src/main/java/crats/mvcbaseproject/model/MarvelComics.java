package crats.mvcbaseproject.model;

/**
 * Created by arsh on 2017-12-15.
 */

public class MarvelComics {
    private String nameOfComic;
    private String resUri;

    public MarvelComics(String nameOfComic, String resUri) {
        this.nameOfComic = nameOfComic;
        this.resUri = resUri;
    }

    public String getname() {
        return this.nameOfComic;
    }

    public String getres() {
        return this.resUri;
    }

}
