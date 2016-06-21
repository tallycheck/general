package com.taoswork.tallycheck.general.solution.quickinterface;

/**
 * Created by Gao Yuan on 2015/5/24.
 */
public class DataHolder<D> {
    public D data;
    public DataHolder(){
        this(null);
    }
    public DataHolder(D data){
        this.data = data;
    }

    public D put(D data) throws NullPointerException{
        D existingData = this.data;
        this.data = data;
        return existingData;
    }

    public D putDataIfNull(D data) throws NullPointerException{
        if(this.data == null){
            if(data == null){
                throw new NullPointerException();
            }
            this.data = data;
        }
        return this.data;
    }
    public boolean idDataNull(){
        return data == null;
    }
}
