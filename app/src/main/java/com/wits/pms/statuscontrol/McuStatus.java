package com.wits.pms.statuscontrol;

import com.google.gson.Gson;
import com.wits.ksw.media.utlis.Constants;
import java.util.ArrayList;
import java.util.List;

public class McuStatus {
    public static final int ANDROID_MODE = 1;
    public static final int CAR_MODE = 2;
    public static final int TYPE_MCU_STATUS = 5;
    public CarData carData = new CarData();
    public String mcuVerison;
    public int systemMode;

    public static final class MediaType {
        public static final int SRC_ALL_APP = 13;
        public static final int SRC_AUX = 6;
        public static final int SRC_BT = 3;
        public static final int SRC_BT_MUSIC = 4;
        public static final int SRC_CAR = 0;
        public static final int SRC_DTV = 9;
        public static final int SRC_DVD = 8;
        public static final int SRC_DVD_YUV = 12;
        public static final int SRC_DVR = 5;
        public static final int SRC_IPOD = 11;
        public static final int SRC_MUSIC = 1;
        public static final int SRC_PHONELINK = 7;
        public static final int SRC_RADIO = 10;
        public static final int SRC_VIDEO = 2;
    }

    public McuStatus() {
    }

    public McuStatus(int i, String str) {
        this.systemMode = i;
        this.mcuVerison = str;
    }

    public static McuStatus getStatusFromJson(String str) {
        return (McuStatus) new Gson().fromJson(str, McuStatus.class);
    }

    public List<String> compare(McuStatus mcuStatus) {
        ArrayList arrayList = new ArrayList();
        if (this.systemMode != mcuStatus.systemMode) {
            arrayList.add(Constants.SYSTEM_STATUS_SYSTEMMODE);
        }
        if (this.carData.safetyBelt != mcuStatus.carData.safetyBelt) {
            arrayList.add("safetyBelt");
        }
        if (this.carData.handbrake != mcuStatus.carData.handbrake) {
            arrayList.add("handbrake");
        }
        if (this.carData.oilUnitType != mcuStatus.carData.oilUnitType) {
            arrayList.add("oilUnitType");
        }
        if (this.carData.temperatureUnitType != mcuStatus.carData.temperatureUnitType) {
            arrayList.add("temperatureUnitType");
        }
        if (this.carData.distanceUnitType != mcuStatus.carData.distanceUnitType) {
            arrayList.add("distanceUnitType");
        }
        if (this.carData.airTemperature != mcuStatus.carData.airTemperature) {
            arrayList.add("airTemperature");
        }
        if (this.carData.oilSum != mcuStatus.carData.oilSum) {
            arrayList.add("oilSum");
        }
        if (this.carData.engineTurnS != mcuStatus.carData.engineTurnS) {
            arrayList.add("engineTurnS");
        }
        if (this.carData.speed != mcuStatus.carData.speed) {
            arrayList.add("speed");
        }
        if (this.carData.averSpeed != mcuStatus.carData.averSpeed) {
            arrayList.add("averSpeed");
        }
        if (this.carData.oilWear != mcuStatus.carData.oilWear) {
            arrayList.add("oilWear");
        }
        if (this.carData.mileage != mcuStatus.carData.mileage) {
            arrayList.add("mileage");
        }
        if (this.carData.carDoor != mcuStatus.carData.carDoor) {
            arrayList.add("carDoor");
        }
        return arrayList;
    }

    public class CarData {
        public static final int AHEAD_COVER = 8;
        public static final int BACK_COVER = 4;
        public static final int LEFT_AHEAD = 16;
        public static final int LEFT_BACK = 64;
        public static final int RIGHT_AHEAD = 32;
        public static final int RIGHT_BACK = 128;
        public float airTemperature;
        public float averSpeed;
        public int carDoor;
        public int distanceUnitType;
        public int engineTurnS;
        public boolean handbrake;
        public int mileage;
        public int oilSum;
        public int oilUnitType;
        public float oilWear;
        public boolean safetyBelt;
        public int speed;
        public int temperatureUnitType;

        public CarData() {
        }

        public boolean isOpen(int i) {
            return (i & this.carDoor) != 0;
        }

        public int getCarDoor() {
            return this.carDoor;
        }

        public void setCarDoor(int i) {
            this.carDoor = i;
        }

        public boolean isHandbrake() {
            return this.handbrake;
        }

        public void setHandbrake(boolean z) {
            this.handbrake = z;
        }

        public boolean isSafetyBelt() {
            return this.safetyBelt;
        }

        public void setSafetyBelt(boolean z) {
            this.safetyBelt = z;
        }

        public int getMileage() {
            return this.mileage;
        }

        public void setMileage(int i) {
            this.mileage = i;
        }

        public float getOilWear() {
            return this.oilWear;
        }

        public void setOilWear(float f) {
            this.oilWear = f;
        }

        public int getOilSum() {
            return this.oilSum;
        }

        public void setOilSum(int i) {
            this.oilSum = i;
        }

        public float getAverSpeed() {
            return this.averSpeed;
        }

        public void setAverSpeed(float f) {
            this.averSpeed = f;
        }

        public int getSpeed() {
            return this.speed;
        }

        public void setSpeed(int i) {
            this.speed = i;
        }

        public int getEngineTurnS() {
            return this.engineTurnS;
        }

        public void setEngineTurnS(int i) {
            this.engineTurnS = i;
        }

        public float getAirTemperature() {
            return this.airTemperature;
        }

        public void setAirTemperature(float f) {
            this.airTemperature = f;
        }

        public int getDistanceUnitType() {
            return this.distanceUnitType;
        }

        public void setDistanceUnitType(int i) {
            this.distanceUnitType = i;
        }

        public int getTemperatureUnitType() {
            return this.temperatureUnitType;
        }

        public void setTemperatureUnitType(int i) {
            this.temperatureUnitType = i;
        }

        public int getOilUnitType() {
            return this.oilUnitType;
        }

        public void setOilUnitType(int i) {
            this.oilUnitType = i;
        }
    }

    public int getSystemMode() {
        return this.systemMode;
    }

    public void setSystemMode(int i) {
        this.systemMode = i;
    }

    public String getMcuVerison() {
        return this.mcuVerison;
    }

    public void setMcuVerison(String str) {
        this.mcuVerison = str;
    }

    public CarData getCarData() {
        return this.carData;
    }

    public void setCarData(CarData carData2) {
        this.carData = carData2;
    }
}
