package entity;
import java.io.Serializable;
public class Film implements Serializable {
    private int filmId;
    private String filmName;
    private String director;
    private String genre;
    private int duration; // Thời lượng (phút)
    private String language;
    private String description;
    private String posterUrl;
    private double rating;

    // Constructor mặc định
    public Film() {}

    // Constructor đầy đủ
    public Film(int filmId, String filmName, String director, String genre,
                int duration, String language, String description, String posterUrl, double rating) {
        this.filmId = filmId;
        this.filmName = filmName;
        this.director = director;
        this.genre = genre;
        this.duration = duration;
        this.language = language;
        this.description = description;
        this.posterUrl = posterUrl;
        this.rating = rating;
    }

    // Getters and Setters
    public int getFilmId() { return filmId; }
    public void setFilmId(int filmId) { this.filmId = filmId; }

    public String getFilmName() { return filmName; }
    public void setFilmName(String filmName) { this.filmName = filmName; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    @Override
    public String toString() {
        return "Film{" +
                "filmId=" + filmId +
                ", filmName='" + filmName + '\'' +
                ", director='" + director + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", language='" + language + '\'' +
                ", rating=" + rating +
                '}';
    }
}
