package fr.univ_amu.iut.beans;

import fr.univ_amu.iut.Exceptions.NoMapFoundException;

import java.util.ArrayList;

public class Map {

    private int idMap;
    private String name;
    private boolean checkpoint;
    private boolean rest;
    private String text;
    private int idWeapon;
    private int idArmor;
    private int idEnnemi;
    private int golds;
    private int FCE;
    private int AGI;
    private int INT;
    private int END;
    private int CHARI;
    private String testStat;
    private String choix1;
    private String choix2;
    private String choix3;
    private String choix4;
    private int map1;
    private int map2;
    private int map3;
    private int map4;

    private static ArrayList<Map> allMaps = new ArrayList<>();

    public static Map findMapById(int idMap) throws NoMapFoundException {
        for (Map map : Map.getAllMaps()) {
            if (map.getIdMap() == idMap) {
                return map;
            }
        }
        throw new NoMapFoundException();
    }

    public static ArrayList<Map> getAllMaps() {
        return allMaps;
    }

    public static void setAllMaps(ArrayList<Map> allMaps) {
        Map.allMaps = allMaps;
    }

    public int getIdMap() {
        return idMap;
    }

    public void setIdMap(int idMap) {
        this.idMap = idMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(boolean checkpoint) {
        this.checkpoint = checkpoint;
    }

    public boolean isRest() {
        return rest;
    }

    public void setRest(boolean rest) {
        this.rest = rest;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIdWeapon() {
        return idWeapon;
    }

    public void setIdWeapon(int idWeapon) {
        this.idWeapon = idWeapon;
    }

    public int getIdArmor() {
        return idArmor;
    }

    public void setIdArmor(int idArmor) {
        this.idArmor = idArmor;
    }

    public int getIdEnnemi() {
        return idEnnemi;
    }

    public void setIdEnnemi(int idEnnemi) {
        this.idEnnemi = idEnnemi;
    }

    public int getGolds() {
        return golds;
    }

    public void setGolds(int golds) {
        this.golds = golds;
    }

    public String getTestStat() {
        return testStat;
    }

    public void setTestStat(String testStat) {
        this.testStat = testStat;
    }

    public int getFCE() {
        return FCE;
    }

    public void setFCE(int FCE) {
        this.FCE = FCE;
    }

    public int getAGI() {
        return AGI;
    }

    public void setAGI(int AGI) {
        this.AGI = AGI;
    }

    public int getINT() {
        return INT;
    }

    public void setINT(int INT) {
        this.INT = INT;
    }

    public int getEND() {
        return END;
    }

    public void setEND(int END) {
        this.END = END;
    }

    public int getCHARI() {
        return CHARI;
    }

    public void setCHARI(int CHARI) {
        this.CHARI = CHARI;
    }

    public String getChoix1() {
        return choix1;
    }

    public void setChoix1(String choix1) {
        this.choix1 = choix1;
    }

    public String getChoix2() {
        return choix2;
    }

    public void setChoix2(String choix2) {
        this.choix2 = choix2;
    }

    public String getChoix3() {
        return choix3;
    }

    public void setChoix3(String choix3) {
        this.choix3 = choix3;
    }

    public String getChoix4() {
        return choix4;
    }

    public void setChoix4(String choix4) {
        this.choix4 = choix4;
    }

    public int getMap1() {
        return map1;
    }

    public void setMap1(int map1) {
        this.map1 = map1;
    }

    public int getMap2() {
        return map2;
    }

    public void setMap2(int map2) {
        this.map2 = map2;
    }

    public int getMap3() {
        return map3;
    }

    public void setMap3(int map3) {
        this.map3 = map3;
    }

    public int getMap4() {
        return map4;
    }

    public void setMap4(int map4) {
        this.map4 = map4;
    }


}
