package hr.algebra.concertcrew.serialization;

import hr.algebra.concertcrew.entity.Concert;

import java.io.Serializable;


public class ConcertSnapshot implements Serializable {


    private static final long serialVersionUID = 1L;

    private final String mainArtist;
    private final String tourName;
    private final String venue;
    private final String city;
    private final String country;
    private final String dateAttended;
    private final int rating;
    private final int songsPlayed;

    public ConcertSnapshot(String mainArtist, String tourName, String venue, String city,
                           String country, String dateAttended, int rating, int songsPlayed) {
        this.mainArtist = mainArtist;
        this.tourName = tourName;
        this.venue = venue;
        this.city = city;
        this.country = country;
        this.dateAttended = dateAttended;
        this.rating = rating;
        this.songsPlayed = songsPlayed;
    }

    public static ConcertSnapshot from(Concert c) {
        return new ConcertSnapshot(
            c.getMainArtist(),
            c.getTourName(),
            c.getVenue(),
            c.getCity(),
            c.getCountry(),
            c.getDateAttended() == null ? null : c.getDateAttended().toString(),
            c.getRating() == null ? 0 : c.getRating(),
            c.getSongsPlayed() == null ? 0 : c.getSongsPlayed()
        );
    }

    public String getMainArtist()  { return mainArtist; }
    public String getTourName()    { return tourName; }
    public String getVenue()       { return venue; }
    public String getCity()        { return city; }
    public String getCountry()     { return country; }
    public String getDateAttended(){ return dateAttended; }
    public int getRating()         { return rating; }
    public int getSongsPlayed()    { return songsPlayed; }

    @Override
    public String toString() {
        return "ConcertSnapshot{mainArtist='" + mainArtist + "', tourName='" + tourName
            + "', venue='" + venue + "', city='" + city + "', country='" + country
            + "', dateAttended='" + dateAttended + "', rating=" + rating
            + ", songsPlayed=" + songsPlayed + '}';
    }
}
