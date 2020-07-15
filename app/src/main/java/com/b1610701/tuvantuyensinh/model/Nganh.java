package com.b1610701.tuvantuyensinh.model;

public class Nganh {
    private String manganh;
    private String tennganh;
    private String tohopmon;
    private int chitieu;
    private DiemChuan diemchuan;

    public Nganh(){}
    public Nganh(String maNganh, String tenNganh, String toHopMon, int chiTieu, DiemChuan diemChuan){
        this.manganh = maNganh;
        this.tennganh = tenNganh;
        this.tohopmon = toHopMon;
        this.chitieu = chiTieu;
        this.diemchuan = diemChuan;
    }
    public void setMaNganh(String maNganh){
        this.manganh = maNganh;
    }
    public void setTenNganh(String tenNganh){
        this.tennganh = tenNganh;
    }
    public void setToHopMon(String toHopMon){
        this.tohopmon = toHopMon;
    }
    public void setChiTieu(int chiTieu){
        this.chitieu = chiTieu;
    }
    public void setDiemChuan(DiemChuan diemChuan){
        this.diemchuan = diemChuan;
    }

    public String getMaNganh(){
        return this.manganh;
    }
    public String getTenNganh(){
        return this.tennganh;
    }
    public String getToHopMon(){
        return this.tohopmon;
    }
    public int getChiTieu(){
        return this.chitieu;
    }
    public DiemChuan getDiemChuan(){
        return this.diemchuan;
    }
}
