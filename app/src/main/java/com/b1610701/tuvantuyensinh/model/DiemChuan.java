package com.b1610701.tuvantuyensinh.model;

public class DiemChuan {
    private float _1;
    private float _2;
    private float _3;
    public DiemChuan(){}
    public DiemChuan(float _1, float _2, float _3){
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }
    public void set_1(float _1){
        this._1 = _1;
    }

    public void set_2(float _2){
        this._2 = _2;
    }
    public void set_3(float _3){
        this._3 = _3;
    }
    public float get_1(){
        return this._1;
    }
    public float get_2(){
        return this._2;
    }
    public float get_3(){
        return this._3;
    }
}
