package com.mfr.system.entity;

public class Counter
{
    private int count;

    public Counter(int count)
    {
        this.count = count;
    }
    
    public Counter() {
    	this.count = 0;
    }


    public int get()
    {
    	count++;
        return count;
    }

//    public String toString()
//    {
//        return ""+count;
//    }
}