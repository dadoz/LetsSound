package com.example.davide.letssound.helpers;

/**
 * Created by davide on 20/12/15.
 */
public class SoundTrackStatus {
    public static final int INVALID_POSITION = -1;

    private enum StatusEnum {
        IDLE, PLAY, SELECT, DOWNLOAD
    }
    private StatusEnum currentStatus;
    private static SoundTrackStatus instance;
    private int currentPosition = -1;
    /**
     * 
     * @return SoundTrackStatus
     */
    public static SoundTrackStatus getInstance() {
        return instance == null ?
                instance = new SoundTrackStatus(StatusEnum.IDLE) :
                instance;
    }

    /**
     * 
     * @param currentStatus
     */
    private SoundTrackStatus(StatusEnum currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * 
     * @return StatusEnum
     */
    public StatusEnum getCurrentStatus() {
        return currentStatus;
    }

    /**
     * 
     * @param currentStatus
     */
    public void setCurrentStatus(StatusEnum currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * 
     */
    public void setPlayStatus() {
        this.currentStatus = StatusEnum.PLAY;

    }

    /**
     * 
     */
    public void setSelectStatus() {
        this.currentStatus = StatusEnum.SELECT;

    }

    /**
     * 
     */
    public void setIdleStatus() {
        this.currentStatus = StatusEnum.IDLE;
    }
    /**
     *
     */
    public void setDownloadStatus() {
        this.currentStatus = StatusEnum.DOWNLOAD;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isPlayStatus() {
        return this.currentStatus == StatusEnum.PLAY;

    }

    /**
     * 
     * @return boolean
     */
    public boolean isSelectStatus() {
        return this.currentStatus == StatusEnum.SELECT;

    }

    /**
     * 
     * @return boolean
     */
    public boolean isIdleStatus() {
        return this.currentStatus == StatusEnum.IDLE;
    }
    /**
     *
     * @return boolean
     */
    public boolean isDownloadStatus() {
        return this.currentStatus == StatusEnum.DOWNLOAD;
    }

    /**
     *
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     *
     * @param currentPosition
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     *
     * @param pos
     * @return
     */
    public boolean isValidPosition(int pos) {
        return pos != INVALID_POSITION;
    }
}
