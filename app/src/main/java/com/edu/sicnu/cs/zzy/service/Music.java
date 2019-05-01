package com.edu.sicnu.cs.zzy.service;

public class Music {
    private String musicName;
    private String musicPath;

    public String getMusicName() {
        return musicName;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public Music(String musicName, String musicPath) {
        this.musicName = musicName;
        this.musicPath = musicPath;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }
}
