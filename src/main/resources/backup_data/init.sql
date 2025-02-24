CREATE DATABASE mupl_music;
USE mupl_music;

CREATE TABLE mupl_artist (
                             artist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             description TEXT,
                             gender VARCHAR(50),
                             country VARCHAR(100),
                             birthday DATE,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE mupl_album (
                            album_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            artist_id BIGINT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            released_at TIMESTAMP,
                            FOREIGN KEY (artist_id) REFERENCES mupl_artist(artist_id) ON DELETE CASCADE
);

CREATE TABLE mupl_song (
                           song_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           album_id BIGINT,
                           image_path TEXT,
                           released_at DATE,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (album_id) REFERENCES mupl_album(album_id) ON DELETE SET NULL
);

CREATE TABLE mupl_genre (
                            genre_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE mupl_artists_songs (
                                    artist_id BIGINT,
                                    song_id BIGINT,
                                    PRIMARY KEY (artist_id, song_id),
                                    FOREIGN KEY (artist_id) REFERENCES mupl_artist(artist_id) ON DELETE CASCADE,
                                    FOREIGN KEY (song_id) REFERENCES mupl_song(song_id) ON DELETE CASCADE
);

CREATE TABLE mupl_genres_songs (
                                   genre_id BIGINT,
                                   song_id BIGINT,
                                   PRIMARY KEY (genre_id, song_id),
                                   FOREIGN KEY (genre_id) REFERENCES mupl_genre(genre_id) ON DELETE CASCADE,
                                   FOREIGN KEY (song_id) REFERENCES mupl_song(song_id) ON DELETE CASCADE
);

CREATE TABLE mupl_lyric (
                            lyric_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            song_quantity_id BIGINT,
                            marked_at INT
);

CREATE TABLE mupl_song_quantity (
                                    song_quantity_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    song_quantity VARCHAR(50) NOT NULL,
                                    song_path TEXT NOT NULL,
                                    song_id BIGINT,
                                    duration INT NOT NULL,
                                    FOREIGN KEY (song_id) REFERENCES mupl_song(song_id) ON DELETE CASCADE
);
